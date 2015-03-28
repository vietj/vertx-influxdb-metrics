package io.vertx.metrics.influxdb.impl;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.spi.metrics.HttpServerMetrics;
import io.vertx.metrics.influxdb.InfluxDBOptions;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class HttpServerMetricsImpl extends HttpMetricsImpl implements HttpServerMetrics<JsonArray, Void> {

  public HttpServerMetricsImpl(InfluxDBOptions options, Vertx vertx) {
    super(options, vertx, options.getHttpServerRequestsSerie(), options.getHttpServerTcpSerie());
  }

  @Override
  public HttpServerMetricsImpl schedule() {
    return (HttpServerMetricsImpl) super.schedule();
  }

  @Override
  public JsonArray requestBegin(HttpServerRequest request) {
    return createRequestMetric(request.localAddress(), request.remoteAddress(), request.method(), request.uri());
  }

  @Override
  public void responseEnd(JsonArray requestMetric, HttpServerResponse response) {
    reportRequestMetric(requestMetric, response.getStatusCode());
  }
}
