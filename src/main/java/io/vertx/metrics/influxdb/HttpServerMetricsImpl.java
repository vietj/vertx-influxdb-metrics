package io.vertx.metrics.influxdb;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.spi.metrics.HttpServerMetrics;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class HttpServerMetricsImpl extends HttpMetricsImpl implements HttpServerMetrics<JsonArray, Void> {

  public HttpServerMetricsImpl(Vertx vertx) {
    super(vertx, "http_server_requests", "tcp_http_server");
  }

  @Override
  public HttpServerMetricsImpl schedule() {
    return (HttpServerMetricsImpl) super.schedule();
  }

  @Override
  public JsonArray requestBegin(HttpServerRequest request) {
    return createRequestMetric(request.method(), request.uri());
  }

  @Override
  public void responseEnd(JsonArray requestMetric, HttpServerResponse response) {
    reportRequestMetric(requestMetric, response.getStatusCode());
  }
}
