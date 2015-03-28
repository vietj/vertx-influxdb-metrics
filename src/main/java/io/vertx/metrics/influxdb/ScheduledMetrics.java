package io.vertx.metrics.influxdb;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.spi.metrics.Metrics;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public abstract class ScheduledMetrics implements Metrics {

  private volatile boolean closed;
  protected final Vertx vertx;
  private final HttpClient client;

  public ScheduledMetrics(Vertx vertx) {
    this.vertx = vertx;
    this.client = vertx.createHttpClient(new HttpClientOptions().
        setDefaultHost("localhost").
        setDefaultPort(8086));
  }

  protected void collectSeries(JsonArray series) {

  }

  public void schedule() {
    vertx.setTimer(1000, id -> {
      JsonArray series = new JsonArray();
      collectSeries(series);
      String s = series.encode();
      Buffer buffer = Buffer.buffer(s);
      HttpClientRequest req = client.post("/db/vertx/series?u=root&p=root&time_precision=ms", response -> {
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
