package io.vertx.metrics.influxdb;

import io.vertx.core.net.SocketAddress;
import io.vertx.core.spi.metrics.TCPMetrics;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TCPMetricsImpl implements TCPMetrics {

  @Override
  public Object connected(SocketAddress remoteAddress) {
    return null;
  }

  @Override
  public void disconnected(Object socketMetric, SocketAddress remoteAddress) {

  }

  @Override
  public void bytesRead(Object socketMetric, SocketAddress remoteAddress, long numberOfBytes) {

  }

  @Override
  public void bytesWritten(Object socketMetric, SocketAddress remoteAddress, long numberOfBytes) {

  }

  @Override
  public void exceptionOccurred(Object socketMetric, SocketAddress remoteAddress, Throwable t) {

  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public void close() {

  }
}
