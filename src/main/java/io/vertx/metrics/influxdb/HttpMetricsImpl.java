package io.vertx.metrics.influxdb;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.SocketAddress;
import io.vertx.core.spi.metrics.TCPMetrics;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public abstract class HttpMetricsImpl implements TCPMetrics<Void> {

  private final AtomicReference<ConcurrentLinkedDeque<JsonArray>> requests = new AtomicReference<>();
  private final Vertx vertx;
  private final HttpClient client;
  private final String requestSerieName;
  private final String tcpSerieName;
  private volatile boolean closed;
  private final AtomicLong bytesWritten = new AtomicLong();
  private final AtomicLong bytesRead = new AtomicLong();
  private final AtomicLong connectionCount = new AtomicLong();

  public HttpMetricsImpl(Vertx vertx, String requestSerieName, String tcpSerieName) {
    this.vertx = vertx;
    this.requestSerieName = requestSerieName;
    this.tcpSerieName = tcpSerieName;
    client = vertx.createHttpClient(new HttpClientOptions().
        setDefaultHost("localhost").
        setDefaultPort(8086));
    requests.set(new ConcurrentLinkedDeque<>());
    schedule();
  }

  private void schedule() {
    vertx.setTimer(1000, id -> {

      //
      ConcurrentLinkedDeque<JsonArray> requests = this.requests.getAndSet(new ConcurrentLinkedDeque<>());
      JsonObject requestSerie = new JsonObject().
          put("name", requestSerieName).
          put("columns", new JsonArray().add("time").add("method").add("uri").add("duration"));
      JsonArray requestPoints = new JsonArray();
      for (JsonArray requestPoint = requests.poll();requestPoint != null;requestPoint = requests.poll()) {
        requestPoints.add(requestPoint);
      }
      requestSerie.put("points", requestPoints);

      //
      JsonObject httpTcp = new JsonObject().
          put("name", tcpSerieName).
          put("columns", new JsonArray().add("bytes_read").add("bytes_written").add("connection_count")).
          put("points", new JsonArray().add(new JsonArray().add(bytesRead.getAndSet(0)).add(bytesWritten.getAndSet(0)).add(connectionCount.getAndSet(0))));

      //
      JsonArray body = new JsonArray();
      body.add(requestSerie);
      body.add(httpTcp);
      String s = body.encode();
      Buffer buffer = Buffer.buffer(s);
      HttpClientRequest req = client.post("/db/vertx/series?u=root&p=root&time_precision=ms", response -> {
        if (response.statusCode() != 200) {
          System.out.println("response " + response.statusCode());
          response.bodyHandler(msg -> {
            System.out.println(msg.toString());
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

  protected JsonArray createRequestMetric(HttpMethod method, String uri) {
    JsonArray point = new JsonArray();
    point.add(System.currentTimeMillis());
    point.add(method.toString());
    point.add(uri);
    return point;
  }

  protected void reportRequestMetric(JsonArray point) {
    point.add(System.currentTimeMillis() - point.getLong(0));
    requests.get().add(point);
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
