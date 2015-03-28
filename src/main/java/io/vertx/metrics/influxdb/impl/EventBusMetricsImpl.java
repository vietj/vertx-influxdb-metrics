package io.vertx.metrics.influxdb.impl;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.ReplyFailure;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.metrics.EventBusMetrics;
import io.vertx.metrics.influxdb.InfluxDBOptions;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class EventBusMetricsImpl extends ScheduledMetrics implements EventBusMetrics {

  private final Deque<JsonArray> sent = new ConcurrentLinkedDeque<>();
  private final Deque<JsonArray> received = new ConcurrentLinkedDeque<>();
  private final AtomicReference<ConcurrentMap<String, AtomicLong>> messageBytesWritten = new AtomicReference<>();
  private final AtomicReference<ConcurrentMap<String, AtomicLong>> messageBytesRead = new AtomicReference<>();

  public EventBusMetricsImpl(InfluxDBOptions options, Vertx vertx) {
    super(options, vertx);
    messageBytesRead.set(new ConcurrentHashMap<>());
    messageBytesWritten.set(new ConcurrentHashMap<>());
  }

  @Override
  public EventBusMetricsImpl schedule() {
    return (EventBusMetricsImpl) super.schedule();
  }

  @Override
  protected void collectSeries(JsonArray series) {
    super.collectSeries(series);
    JsonArray sendPoints = new JsonArray();
    for (int size = sent.size(); size > 0;size--) {
      sendPoints.add(sent.pop());
    }
    JsonArray receivedPoints = new JsonArray();
    for (int size = received.size(); size > 0;size--) {
      receivedPoints.add(received.pop());
    }
    JsonArray messageBytesPoints = new JsonArray();
    ConcurrentMap<String, AtomicLong> bytesReadMetrics = messageBytesRead.getAndSet(new ConcurrentHashMap<>());
    ConcurrentMap<String, AtomicLong> bytesWrittenMetrics = messageBytesWritten.getAndSet(new ConcurrentHashMap<>());
    for (Map.Entry<String, AtomicLong> bytesReadMetric : bytesReadMetrics.entrySet()) {
      AtomicLong bytesWrittenMetric = bytesWrittenMetrics.remove(bytesReadMetric.getKey());
      messageBytesPoints.add(new JsonArray().add(bytesReadMetric.getKey()).add(bytesReadMetric.getValue().get()).add(bytesWrittenMetric != null ? bytesWrittenMetric.get() : 0));
    }
    series.add(new JsonObject().
        put("name", "eventbus_sent").
        put("columns", new JsonArray().add("time").add("address").add("publish").add("local").add("remote")).
        put("points", sendPoints)
    );
    series.add(new JsonObject().
        put("name", "eventbus_received").
        put("columns", new JsonArray().add("time").add("address").add("publish").add("local").add("handlers")).
        put("points", receivedPoints)
    );
    series.add(new JsonObject().
        put("name", "eventbus_messages_bytes").
        put("columns", new JsonArray().add("address").add("bytes_read").add("bytes_written")).
        put("points", messageBytesPoints)
    );
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
    sent.add(new JsonArray().add(System.currentTimeMillis()).add(address).add(publish).add(local).add(remote));
  }

  @Override
  public void messageReceived(String address, boolean publish, boolean local, int handlers) {
    received.add(new JsonArray().add(System.currentTimeMillis()).add(address).add(publish).add(local).add(handlers));
  }

  @Override
  public void replyFailure(String address, ReplyFailure failure) {
  }

  @Override
  public void messageWritten(String address, int size) {
    messageBytesWritten.get().computeIfAbsent(address, key -> new AtomicLong()).addAndGet(size);
  }

  @Override
  public void messageRead(String address, int size) {
    messageBytesRead.get().computeIfAbsent(address, key -> new AtomicLong()).addAndGet(size);
  }
}
