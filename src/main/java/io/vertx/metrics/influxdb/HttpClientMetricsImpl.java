package io.vertx.metrics.influxdb;

import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.net.SocketAddress;
import io.vertx.core.spi.metrics.HttpClientMetrics;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class HttpClientMetricsImpl implements HttpClientMetrics {

  @Override
  public Object requestBegin(HttpClientRequest request) {
    return null;
  }

  @Override
  public void responseEnd(Object requestMetric, HttpClientRequest request, HttpClientResponse response) {
  }

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
