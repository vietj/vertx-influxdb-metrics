package io.vertx.metrics.influxdb;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.ReplyFailure;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.metrics.EventBusMetrics;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class EventBusMetricsImpl implements EventBusMetrics {

  private final Vertx vertx;
  private final HttpClient client;
  private final Deque<JsonArray> sentPoints = new ConcurrentLinkedDeque<>();

  public EventBusMetricsImpl(Vertx vertx) {
    this.vertx = vertx;
    this.client = vertx.createHttpClient(new HttpClientOptions().
        setDefaultHost("localhost").
        setDefaultPort(8086));
  }

  private void schedule() {
    vertx.setTimer(1000, id -> {
      JsonObject sendPointsSerie = new JsonObject().
          put("name", "eventbus_sent").
          put("columns", new JsonArray().add("address").add("publish").add("local").add("remote"));
      JsonArray sendPointsA = new JsonArray();
      for (JsonArray point = sentPoints.poll();point != null;point = sentPoints.poll()) {
        sendPointsA.add(point);
      }
      sendPointsSerie.put("points", sendPointsA);
      JsonArray payload = new JsonArray();
      payload.add(sendPointsSerie);
      String s = sendPointsSerie.encode();
      Buffer buffer = Buffer.buffer(s);
      HttpClientRequest req = client.post("/db/vertx/series?u=root&p=root");
      req.write(buffer);
      schedule();
    });
  }

  @Override
  public Object handlerRegistered(String address, boolean replyHandler) {
    return null;
  }

  @Override
  public void handlerUnregistered(Object handler) {
  }

  @Override
  public void beginHandleMessage(Object handler, boolean local) {
  }

  @Override
  public void endHandleMessage(Object handler, Throwable failure) {
  }

  @Override
  public void messageSent(String address, boolean publish, boolean local, boolean remote) {
    sentPoints.add(new JsonArray().add(address).add(publish).add(local).add(remote));
  }

  @Override
  public void messageReceived(String address, boolean publish, boolean local, int handlers) {
/*
    Serie serie = new Serie.Builder("message_received")
        .columns("address", "publish", "local", "handlers")
        .values(address, publish, local, handlers)
        .build();
    influxDB.writeUdp(8088, serie);
*/
  }

  @Override
  public void replyFailure(String address, ReplyFailure failure) {
  }

  @Override
  public void messageWritten(String address, int size) {
  }

  @Override
  public void messageRead(String address, int size) {
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public void close() {
  }
}
