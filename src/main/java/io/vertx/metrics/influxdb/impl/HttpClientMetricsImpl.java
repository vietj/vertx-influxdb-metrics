package io.vertx.metrics.influxdb.impl;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.net.SocketAddress;
import io.vertx.core.spi.metrics.HttpClientMetrics;
import io.vertx.metrics.influxdb.InfluxDBOptions;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class HttpClientMetricsImpl extends HttpMetricsImpl implements HttpClientMetrics<RequestMetric, SocketMetric> {

  public HttpClientMetricsImpl(InfluxDBOptions options, Vertx vertx) {
    super(options, vertx, options.getHttpClientRequestsSerie(), options.getHttpClientTcpSerie());
  }

  @Override
  public HttpClientMetricsImpl schedule() {
    return (HttpClientMetricsImpl) super.schedule();
  }

  @Override
  public RequestMetric requestBegin(SocketMetric socketMetric, SocketAddress localAddress, SocketAddress remoteAddress, HttpClientRequest request) {
    socketMetric.bytesRead = 0;
    socketMetric.bytesWritten = 0;
    return new RequestMetric(socketMetric, localAddress, remoteAddress, request.method(), request.uri());
  }

  @Override
  public void responseEnd(RequestMetric requestMetric, HttpClientResponse response) {
    reportRequestMetric(requestMetric, response.statusCode());
  }
}
