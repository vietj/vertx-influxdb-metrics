package io.vertx.metrics.influxdb;

import io.vertx.core.http.HttpMethod;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class HttpRequestMetric {

  final HttpMethod method;
  final String uri;
  final long start;
  final long end;

  public HttpRequestMetric(HttpMethod method, String uri, long start, long end) {
    this.method = method;
    this.uri = uri;
    this.start = start;
    this.end = end;
  }
}
