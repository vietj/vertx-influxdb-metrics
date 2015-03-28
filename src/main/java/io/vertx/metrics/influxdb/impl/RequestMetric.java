package io.vertx.metrics.influxdb.impl;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.net.SocketAddress;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class RequestMetric {

  final SocketMetric socketMetric;
  final SocketAddress localAddress;
  final SocketAddress remoteAddress;
  final HttpMethod method;
  final String uri;
  final long start;

  public RequestMetric(SocketMetric socketMetric, SocketAddress localAddress, SocketAddress remoteAddress, HttpMethod method, String uri) {
    this.socketMetric = socketMetric;
    this.localAddress = localAddress;
    this.remoteAddress = remoteAddress;
    this.method = method;
    this.uri = uri;
    this.start = System.currentTimeMillis();
  }
}
