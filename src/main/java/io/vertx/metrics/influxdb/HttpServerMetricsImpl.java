package io.vertx.metrics.influxdb;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.SocketAddress;
import io.vertx.core.spi.metrics.HttpServerMetrics;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class HttpServerMetricsImpl implements HttpServerMetrics<JsonArray, Void> {

  private final AtomicReference<ConcurrentLinkedDeque<JsonArray>> requestMeters = new AtomicReference<>();
  private final Vertx vertx;
  private final HttpClient client;
  private volatile boolean closed;
  private final AtomicLong bytesWritten = new AtomicLong();
  private final AtomicLong bytesRead = new AtomicLong();
  private final AtomicLong connectionCount = new AtomicLong();

  public HttpServerMetricsImpl(Vertx vertx) {
    this.vertx = vertx;
    client = vertx.createHttpClient(new HttpClientOptions().
        setDefaultHost("localhost").
        setDefaultPort(8086));
    requestMeters.set(new ConcurrentLinkedDeque<>());
    schedule();
  }

  private void schedule() {
    vertx.setTimer(1000, id -> {
      ConcurrentLinkedDeque<JsonArray> timers = requestMeters.getAndSet(new ConcurrentLinkedDeque<>());
      JsonObject requestSerie = new JsonObject().
          put("name", "http_requests").
          put("columns", new JsonArray().add("time").add("method").add("uri").add("duration"));
      JsonArray points = new JsonArray();
      for (JsonArray point = timers.poll();point != null;point = timers.poll()) {
        points.add(point);
      }
      requestSerie.put("points", points);
      JsonArray payload = new JsonArray();
      payload.add(requestSerie);
      String s = payload.encode();
      Buffer buffer = Buffer.buffer(s);
      HttpClientRequest req = client.post("/db/vertx/series?u=root&p=root&time_precision=ms", response -> {
        if (response.statusCode() != 200) {
          System.out.println("response " + response.statusCode());
          response.bodyHandler(body -> {
            System.out.println(body.toString());
          });
        }
        if (!closed) {
          schedule();
        } else {
          client.close();
        }
      });
      req.putHeader("Content-Length", "" + buffer.length());
      req.putHeader("Content-Type", "application/json");
      req.exceptionHandler(err -> {
        System.out.println("Could not send metrics");
        err.printStackTrace();
      });
      req.write(buffer);
      req.end();
    });
  }

  @Override
  public JsonArray requestBegin(HttpServerRequest request) {
    JsonArray point = new JsonArray();
    point.add(System.currentTimeMillis());
    point.add(request.method().toString());
    point.add(request.uri());
    return point;
  }

  @Override
  public void responseEnd(JsonArray point, HttpServerRequest request, HttpServerResponse response) {
    point.add(System.currentTimeMillis() - point.getLong(0));
    requestMeters.get().add(point);
  }

  @Override
  public Void connected(SocketAddress remoteAddress) {
    connectionCount.incrementAndGet();
    return null;
  }

  @Override
  public void disconnected(Void socketMetric, SocketAddress remoteAddress) {
    connectionCount.decrementAndGet();
  }

  @Override
  public void bytesRead(Void socketMetric, SocketAddress remoteAddress, long numberOfBytes) {
    bytesRead.addAndGet(numberOfBytes);
  }

  @Override
  public void bytesWritten(Void socketMetric, SocketAddress remoteAddress, long numberOfBytes) {
    bytesWritten.addAndGet(numberOfBytes);
  }

  @Override
  public void exceptionOccurred(Void socketMetric, SocketAddress remoteAddress, Throwable t) {
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public void close() {
    closed = true;
  }
}
