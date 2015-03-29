package io.vertx.metrics.influxdb.impl;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.spi.metrics.Metrics;
import io.vertx.metrics.influxdb.InfluxDBOptions;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public abstract class ScheduledMetrics implements Metrics {

  private volatile boolean closed;
  protected final Vertx vertx;
  protected final InfluxDBOptions options;
  private final HttpClient client;
  private final String requestURI;

  public ScheduledMetrics(InfluxDBOptions options, Vertx vertx) {
    this.requestURI = "/db/" + options.getDB() + "/series?time_precision=ms&u=" + options.getUsername() + "&p=" + options.getPassword();
    this.vertx = vertx;
    this.options = options;
    this.client = vertx.createHttpClient(new HttpClientOptions().
        setDefaultHost(options.getHost()).
        setDefaultPort(options.getPort()));
  }

  protected void collectSeries(JsonArray series) {
  }

  public ScheduledMetrics schedule() {
    vertx.setTimer(1000, id -> {
      JsonArray series = new JsonArray();
      collectSeries(series);
      String s = series.encode();
      Buffer buffer = Buffer.buffer(s);
      HttpClientRequest req = client.post(requestURI, response -> {
        if (response.statusCode() != 200) {
          System.out.println("response " + response.statusCode());
          response.bodyHandler(msg -> {
            System.out.println(msg.toString());
          });
        }
        if (!closed) {
          schedule();
        } else {
          client.close();
        }
      });
      req.putHeader("Content-Length", "" + buffer.length());
      req.putHeader("Content-Type", "application/json");
      req.exceptionHandler(err -> {
        System.out.println("Could not send metrics");
        err.printStackTrace();
      });
      req.write(buffer);
      req.end();
    });
    return this;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public void close() {
    closed = true;
  }
}
