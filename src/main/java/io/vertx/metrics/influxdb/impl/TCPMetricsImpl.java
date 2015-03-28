package io.vertx.metrics.influxdb.impl;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.SocketAddress;
import io.vertx.core.spi.metrics.TCPMetrics;
import io.vertx.metrics.influxdb.InfluxDBOptions;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TCPMetricsImpl extends ScheduledMetrics implements TCPMetrics<SocketMetric> {

  private final String tcpSerieName;
  private final AtomicLong bytesWritten = new AtomicLong();
  private final AtomicLong bytesRead = new AtomicLong();
  private final AtomicLong connectionCount = new AtomicLong();

  public TCPMetricsImpl(InfluxDBOptions options, Vertx vertx, String tcpSerieName) {
    super(options, vertx);
    this.tcpSerieName = tcpSerieName;
  }

  @Override
  public TCPMetricsImpl schedule() {
    return (TCPMetricsImpl) super.schedule();
  }

  @Override
  protected void collectSeries(JsonArray series) {
    super.collectSeries(series);
    JsonObject httpTcp = new JsonObject().
        put("name", tcpSerieName).
        put("columns", new JsonArray().add("bytes_read").add("bytes_written").add("connection_count")).
        put("points", new JsonArray().add(new JsonArray().add(bytesRead.getAndSet(0)).add(bytesWritten.getAndSet(0)).add(connectionCount.getAndSet(0))));
    series.add(httpTcp);
  }

  @Override
  public SocketMetric connected(SocketAddress remoteAddress) {
    connectionCount.incrementAndGet();
    return null;
  }

  @Override
  public void disconnected(SocketMetric socketMetric, SocketAddress remoteAddress) {
    connectionCount.decrementAndGet();
  }

  @Override
  public void bytesRead(SocketMetric socketMetric, SocketAddress remoteAddress, long numberOfBytes) {
    bytesRead.addAndGet(numberOfBytes);
  }

  @Override
  public void bytesWritten(SocketMetric socketMetric, SocketAddress remoteAddress, long numberOfBytes) {
    bytesWritten.addAndGet(numberOfBytes);
  }

  @Override
  public void exceptionOccurred(SocketMetric socketMetric, SocketAddress remoteAddress, Throwable t) {
  }
}
