package io.vertx.metrics.influxdb;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.SocketAddress;
import io.vertx.core.spi.metrics.TCPMetrics;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public abstract class HttpMetricsImpl extends TCPMetricsImpl implements TCPMetrics<Void> {

  private final ConcurrentLinkedDeque<JsonArray> requests = new ConcurrentLinkedDeque<>();
  private final String requestSerieName;

  public HttpMetricsImpl(InfluxDBOptions options, Vertx vertx, String requestSerieName, String tcpSerieName) {
    super(options, vertx, tcpSerieName);
    this.requestSerieName = requestSerieName;
  }

  @Override
  protected void collectSeries(JsonArray series) {
    super.collectSeries(series);
    JsonArray requestPoints = new JsonArray();
    for (int size = requests.size();size > 0;size--) {
      requestPoints.add(requests.pop());
    }
    series.add(new JsonObject().
            put("name", requestSerieName).
            put("columns", new JsonArray().
                add("time").
                add("local_host").add("local_port").
                add("remote_host").add("remote_port").
                add("method").add("uri").add("status").
                add("duration")).
            put("points", requestPoints)
    );
  }

  protected JsonArray createRequestMetric(SocketAddress localAddress, SocketAddress remoteAddress, HttpMethod method, String uri) {
    JsonArray point = new JsonArray();
    point.add(System.currentTimeMillis());
    point.add(localAddress.host());
    point.add(localAddress.port());
    point.add(remoteAddress.host());
    point.add(remoteAddress.port());
    point.add(method.toString());
    point.add(uri);
    return point;
  }

  protected void reportRequestMetric(JsonArray point, int status) {
    requests.add(point.add(status).add(System.currentTimeMillis() - point.getLong(0)));
  }

  @Override
  public void exceptionOccurred(Void socketMetric, SocketAddress remoteAddress, Throwable t) {
  }
}
