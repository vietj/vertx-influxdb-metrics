package io.vertx.metrics.influxdb;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
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

    Random random = new Random();

    Vertx.clusteredVertx(new VertxOptions().
        setClustered(true).
        setMetricsOptions(new InfluxDBOptions().setEnabled(true)), ar -> {
      if (ar.succeeded()) {
        Vertx vertx = ar.result();
        vertx.eventBus().consumer("the_address", msg -> {
          vertx.setTimer(1 + random.nextInt(1000), id -> {
            if (random.nextInt(100) > 10) {
              msg.reply(Buffer.buffer(new byte[1 + random.nextInt(2000)]));
            } else {
              msg.fail(500, "an error occured");
            }
          });
        });
      } else {
        ar.cause().printStackTrace();
      }
    });

    Vertx.clusteredVertx(new VertxOptions().
        setClustered(true).
        setMetricsOptions(new InfluxDBOptions().setEnabled(true)), ar -> {
      if (ar.succeeded()) {
        Vertx vertx = ar.result();
        HttpServer server = vertx.createHttpServer(new HttpServerOptions().setPort(8080));
        server.requestHandler(req -> {
          vertx.eventBus().<Buffer>send("the_address", "ping", reply -> {
            if (reply.succeeded()) {
              Buffer buffer = reply.result().body();
              req.response().putHeader("Content-Length", "" + buffer.length()).write(buffer).end();
            } else {
              req.response().setStatusCode(500).end();
            }
          });
        });
        server.listen(ar2 -> {
          if (ar2.succeeded()) {
            System.out.println("Server started");
            startInject(Vertx.vertx(new VertxOptions().setMetricsOptions(new InfluxDBOptions().setEnabled(true))));
          } else {
            System.out.println("Could not start");
            ar.cause().printStackTrace();
          }
        });
      } else {
        ar.cause().printStackTrace();
      }
    });
  }

  private static void startInject(Vertx vertx) {
    HttpClient client = vertx.createHttpClient(new HttpClientOptions().setDefaultPort(8080));
    Random random = new Random();
    Buffer body = Buffer.buffer(new byte[1 + random.nextInt(2000)]);
    vertx.setPeriodic(5, id -> {
      client.get("/index.html", response -> {
      }).putHeader("Content-Length", "" + body.length()).write(body).end();
    });
  }
}
