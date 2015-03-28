package io.vertx.metrics.influxdb;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.spi.metrics.HttpClientMetrics;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class HttpClientMetricsImpl extends HttpMetricsImpl implements HttpClientMetrics<JsonArray, Void> {

  public HttpClientMetricsImpl(Vertx vertx) {
    super(vertx, "http_client_requests", "tcp_http_client");
  }

  @Override
  public JsonArray requestBegin(HttpClientRequest request) {
    return createRequestMetric(request.method(), request.uri());
  }

  @Override
  public void responseEnd(JsonArray requestMetric, HttpClientResponse response) {
    reportRequestMetric(requestMetric);
  }
}
