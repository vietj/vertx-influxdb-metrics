package io.vertx.metrics.influxdb.impl;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.SocketAddress;
import io.vertx.core.spi.metrics.TCPMetrics;
import io.vertx.metrics.influxdb.InfluxDBOptions;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public abstract class HttpMetricsImpl extends TCPMetricsImpl implements TCPMetrics<SocketMetric> {

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
                add("duration").add("bytes_read").add("bytes_written")).
            put("points", requestPoints)
    );
  }

  @Override
  public SocketMetric connected(SocketAddress remoteAddress) {
    super.connected(remoteAddress);
    return new SocketMetric();
  }

  @Override
  public void bytesRead(SocketMetric socketMetric, SocketAddress remoteAddress, long numberOfBytes) {
    super.bytesRead(socketMetric, remoteAddress, numberOfBytes);
    socketMetric.bytesRead += numberOfBytes;
  }

  @Override
  public void bytesWritten(SocketMetric socketMetric, SocketAddress remoteAddress, long numberOfBytes) {
    super.bytesWritten(socketMetric, remoteAddress, numberOfBytes);
    socketMetric.bytesWritten += numberOfBytes;
  }

  protected void reportRequestMetric(RequestMetric requestMetric, int status) {
    JsonArray point = new JsonArray();
    long end = System.currentTimeMillis();
    point.add(end);
    point.add(requestMetric.localAddress.host());
    point.add(requestMetric.localAddress.port());
    point.add(requestMetric.remoteAddress.host());
    point.add(requestMetric.remoteAddress.port());
    point.add(requestMetric.method.toString());
    point.add(requestMetric.uri);
    point.add(status);
    point.add(end - requestMetric.start);
    point.add(requestMetric.socketMetric.bytesRead);
    point.add(requestMetric.socketMetric.bytesWritten);
    requests.add(point);
  }
}
