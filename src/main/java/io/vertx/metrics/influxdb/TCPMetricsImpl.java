package io.vertx.metrics.influxdb;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.SocketAddress;
import io.vertx.core.spi.metrics.TCPMetrics;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TCPMetricsImpl extends ScheduledMetrics implements TCPMetrics<Void> {

  private final String tcpSerieName;
  private final AtomicLong bytesWritten = new AtomicLong();
  private final AtomicLong bytesRead = new AtomicLong();
  private final AtomicLong connectionCount = new AtomicLong();

  public TCPMetricsImpl(Vertx vertx, String tcpSerieName) {
    super(vertx);
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
}
