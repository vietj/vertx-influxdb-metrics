package io.vertx.metrics.influxdb;

import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.datagram.DatagramSocketOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.SocketAddress;
import io.vertx.core.spi.metrics.DatagramSocketMetrics;
import io.vertx.core.spi.metrics.EventBusMetrics;
import io.vertx.core.spi.metrics.HttpClientMetrics;
import io.vertx.core.spi.metrics.HttpServerMetrics;
import io.vertx.core.spi.metrics.TCPMetrics;
import io.vertx.core.spi.metrics.VertxMetrics;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class VertxMetricsImpl implements VertxMetrics {

  private final Vertx vertx;

  public VertxMetricsImpl() {
  vertx = Vertx.vertx();
  }

  @Override
  public boolean isMetricsEnabled() {
    return true;
  }

  @Override
  public void verticleDeployed(Verticle verticle) {
  }

  @Override
  public void verticleUndeployed(Verticle verticle) {
  }

  @Override
  public void timerCreated(long id) {
  }

  @Override
  public void timerEnded(long id, boolean cancelled) {
  }

  @Override
  public EventBusMetrics createMetrics(EventBus eventBus) {
    return new EventBusMetricsImpl(vertx).schedule();
  }

  @Override
  public HttpServerMetrics<?, ?> createMetrics(HttpServer server, SocketAddress localAddress, HttpServerOptions options) {
    return new HttpServerMetricsImpl(vertx).schedule();
  }

  @Override
  public HttpClientMetrics<?, ?> createMetrics(HttpClient client, HttpClientOptions options) {
    return new HttpClientMetricsImpl(vertx).schedule();
  }

  @Override
  public TCPMetrics<?> createMetrics(NetServer server, SocketAddress localAddress, NetServerOptions options) {
    return new TCPMetricsImpl(vertx, "tcp_server").schedule();
  }

  @Override
  public TCPMetrics<?> createMetrics(NetClient client, NetClientOptions options) {
    return new TCPMetricsImpl(vertx, "tcp_client").schedule();
  }

  @Override
  public DatagramSocketMetrics createMetrics(DatagramSocket socket, DatagramSocketOptions options) {
    return new DatagramSocketMetricsImpl();
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public void close() {
  }
}
