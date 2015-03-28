package io.vertx.metrics.influxdb;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.SocketAddress;
import io.vertx.core.spi.metrics.TCPMetrics;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public abstract class HttpMetricsImpl extends TCPMetricsImpl implements TCPMetrics<Void> {

  private final AtomicReference<ConcurrentLinkedDeque<JsonArray>> requests = new AtomicReference<>();
  private final String requestSerieName;

  public HttpMetricsImpl(Vertx vertx, String requestSerieName, String tcpSerieName) {
    super(vertx, tcpSerieName);
    this.requestSerieName = requestSerieName;
    requests.set(new ConcurrentLinkedDeque<>());
    schedule();
  }

  @Override
  protected void collectSeries(JsonArray series) {
    super.collectSeries(series);
    ConcurrentLinkedDeque<JsonArray> requests = this.requests.getAndSet(new ConcurrentLinkedDeque<>());
    JsonObject requestSerie = new JsonObject().
        put("name", requestSerieName).
        put("columns", new JsonArray().add("time").add("method").add("uri").add("duration"));
    JsonArray requestPoints = new JsonArray();
    for (JsonArray requestPoint = requests.poll();requestPoint != null;requestPoint = requests.poll()) {
      requestPoints.add(requestPoint);
    }
    requestSerie.put("points", requestPoints);
    series.add(requestSerie);
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
  public void exceptionOccurred(Void socketMetric, SocketAddress remoteAddress, Throwable t) {
  }
}
