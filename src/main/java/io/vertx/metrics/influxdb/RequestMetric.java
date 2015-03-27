package io.vertx.metrics.influxdb;

import io.vertx.core.http.HttpMethod;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class RequestMetric {

  final HttpMethod method;
  final String uri;
  final long time;

  public RequestMetric(HttpMethod method, String uri, long time) {
    this.method = method;
    this.uri = uri;
    this.time = time;
  }
}
