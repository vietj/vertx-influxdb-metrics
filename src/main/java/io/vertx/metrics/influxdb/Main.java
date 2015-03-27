package io.vertx.metrics.influxdb;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.metrics.MetricsOptions;

import java.util.Random;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Main {

  public static void main(String[] args) throws Exception {

    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(new MetricsOptions().setEnabled(true)));

    Random serviceTime = new Random();
    HttpServer server = vertx.createHttpServer(new HttpServerOptions().setPort(8080));
    server.requestHandler(req -> {
      vertx.setTimer(1 + serviceTime.nextInt(1000), id -> {
        req.response().end();
      });
    });
    server.listen(ar -> {
      if (ar.succeeded()) {
        System.out.println("Server started");
        startInject(vertx);
      } else {
        System.out.println("Could not start");
        ar.cause().printStackTrace();
      }
    });
  }

  private static void startInject(Vertx vertx) {
    HttpClient client = vertx.createHttpClient(new HttpClientOptions().setDefaultPort(8080));
    Random random = new Random();
    vertx.setPeriodic(5, id -> {
      client.get("/index.html", response -> {

      }).end();
    });
  }
}
