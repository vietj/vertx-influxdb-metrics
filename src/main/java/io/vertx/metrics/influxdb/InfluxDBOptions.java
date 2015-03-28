package io.vertx.metrics.influxdb;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.core.metrics.MetricsOptions;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject
public class InfluxDBOptions extends MetricsOptions {

  public static final String DEFAULT_HOST = "localhost";
  public static final int DEFAULT_PORT = 8086;
  public static final String DEFAULT_USERNAME = "root";
  public static final String DEFAULT_PASSWORD = "root";
  public static final String DEFAULT_DB = "vertx";
  public static final String DEFAULT_EVENT_BUS_MESSAGE_SENT_SERIE = "eventbus_sent"; 
  public static final String DEFAULT_EVENT_BUS_MESSAGE_RECEIVED_SERIE = "eventbus_received"; 
  public static final String DEFAULT_EVENT_BUS_TCP_SERIE = "eventbus_tcp"; 
  public static final String DEFAULT_HTTP_CLIENT_REQUESTS_SERIE = "http_client_requests"; 
  public static final String DEFAULT_HTTP_CLIENT_TCP_SERIE = "http_client_tcp"; 
  public static final String DEFAULT_HTTP_SERVER_REQUESTS_SERIE = "http_server_requests"; 
  public static final String DEFAULT_HTTP_SERVER_TCP_SERIE = "http_server_tcp"; 
  public static final String DEFAULT_TCP_CLIENT_SERIE = "tcp_client"; 
  public static final String DEFAULT_TCP_SERVER_SERIE = "tcp_server"; 

  private String host;
  private int port;
  private String username;
  private String password;
  private String db;
  private String eventBusMessageSentSerie;
  private String eventBusMessageReceivedSerie;
  private String eventBusTcpSerie;
  private String httpClientRequestsSerie;
  private String httpClientTcpSerie;
  private String httpServerRequestsSerie;
  private String httpServerTcpSerie;
  private String tcpClientSerie;
  private String tcpServerSerie;

  public InfluxDBOptions() {
    host = DEFAULT_HOST;
    port = DEFAULT_PORT;
    username = DEFAULT_USERNAME;
    password = DEFAULT_PASSWORD;
    db = DEFAULT_DB;
    eventBusMessageSentSerie = DEFAULT_EVENT_BUS_MESSAGE_SENT_SERIE;
    eventBusMessageReceivedSerie = DEFAULT_EVENT_BUS_MESSAGE_RECEIVED_SERIE;
    eventBusTcpSerie = DEFAULT_EVENT_BUS_TCP_SERIE;
    httpClientRequestsSerie = DEFAULT_HTTP_CLIENT_REQUESTS_SERIE;
    httpClientTcpSerie = DEFAULT_HTTP_CLIENT_TCP_SERIE;
    httpServerRequestsSerie = DEFAULT_HTTP_SERVER_REQUESTS_SERIE;
    httpServerTcpSerie = DEFAULT_HTTP_SERVER_TCP_SERIE;
    tcpClientSerie = DEFAULT_TCP_CLIENT_SERIE;
    tcpServerSerie = DEFAULT_TCP_SERVER_SERIE;
  }

  public InfluxDBOptions(InfluxDBOptions other) {
    super(other);
    host = other.host;
    port = other.port;
    username = other.username;
    password = other.password;
    db = other.db;
    eventBusMessageSentSerie = other.eventBusMessageSentSerie;
    eventBusMessageReceivedSerie = other.eventBusMessageReceivedSerie;
    eventBusTcpSerie = other.eventBusTcpSerie;
    httpClientRequestsSerie = other.httpClientRequestsSerie;
    httpClientTcpSerie = other.httpClientTcpSerie;
    httpServerRequestsSerie = other.httpServerRequestsSerie;
    httpServerTcpSerie = other.httpServerTcpSerie;
    tcpClientSerie = other.tcpClientSerie;
    tcpServerSerie = other.tcpServerSerie;
  }

  public InfluxDBOptions(JsonObject json) {
    super(json);
    host = json.getString("host", DEFAULT_HOST);
    port = json.getInteger("port", DEFAULT_PORT);
    username = json.getString("username", DEFAULT_USERNAME);
    password = json.getString("password", DEFAULT_PASSWORD);
    db = json.getString("db", DEFAULT_DB);
    eventBusMessageSentSerie = json.getString("eventBusMessageSentSerie", DEFAULT_EVENT_BUS_MESSAGE_SENT_SERIE);
    eventBusMessageReceivedSerie = json.getString("eventBusMessageReceivedSerie", DEFAULT_EVENT_BUS_MESSAGE_RECEIVED_SERIE);
    eventBusTcpSerie = json.getString("eventBusTcpSerie", DEFAULT_EVENT_BUS_TCP_SERIE);
    httpClientRequestsSerie = json.getString("httpClientRequestsSerie", DEFAULT_HTTP_CLIENT_REQUESTS_SERIE);
    httpClientTcpSerie = json.getString("httpClientTcpSerie", DEFAULT_HTTP_CLIENT_TCP_SERIE);
    httpServerRequestsSerie = json.getString("httpServerRequestsSerie", DEFAULT_HTTP_SERVER_REQUESTS_SERIE);
    httpServerTcpSerie = json.getString("httpServerTcpSerie", DEFAULT_HTTP_SERVER_TCP_SERIE);
    tcpClientSerie = json.getString("tcpClientSerie", DEFAULT_TCP_CLIENT_SERIE);
    tcpServerSerie = json.getString("tcpServerSerie", DEFAULT_TCP_SERVER_SERIE);
  }

  public String getHost() {
    return host;
  }

  public InfluxDBOptions setHost(String host) {
    this.host = host;
    return this;
  }

  public int getPort() {
    return port;
  }

  public InfluxDBOptions setPort(int port) {
    this.port = port;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public InfluxDBOptions setUsername(String username) {
    this.username = username;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public InfluxDBOptions setPassword(String password) {
    this.password = password;
    return this;
  }

  public String getDB() {
    return db;
  }

  public InfluxDBOptions setDB(String db) {
    this.db = db;
    return this;
  }

  public String getEventBusMessageSentSerie() {
    return eventBusMessageSentSerie;
  }

  public InfluxDBOptions setEventBusMessageSentSerie(String eventBusMessageSentSerie) {
    this.eventBusMessageSentSerie = eventBusMessageSentSerie;
    return this;
  }

  public String getEventBusMessageReceivedSerie() {
    return eventBusMessageReceivedSerie;
  }

  public InfluxDBOptions setEventBusMessageReceivedSerie(String eventBusMessageReceivedSerie) {
    this.eventBusMessageReceivedSerie = eventBusMessageReceivedSerie;
    return this;
  }

  public String getEventBusTcpSerie() {
    return eventBusTcpSerie;
  }

  public InfluxDBOptions setEventBusTcpSerie(String eventBusTcpSerie) {
    this.eventBusTcpSerie = eventBusTcpSerie;
    return this;
  }

  public String getHttpClientRequestsSerie() {
    return httpClientRequestsSerie;
  }

  public InfluxDBOptions setHttpClientRequestsSerie(String httpClientRequestsSerie) {
    this.httpClientRequestsSerie = httpClientRequestsSerie;
    return this;
  }

  public String getHttpClientTcpSerie() {
    return httpClientTcpSerie;
  }

  public InfluxDBOptions setHttpClientTcpSerie(String httpClientTcpSerie) {
    this.httpClientTcpSerie = httpClientTcpSerie;
    return this;
  }

  public String getHttpServerRequestsSerie() {
    return httpServerRequestsSerie;
  }

  public InfluxDBOptions setHttpServerRequestsSerie(String httpServerRequestsSerie) {
    this.httpServerRequestsSerie = httpServerRequestsSerie;
    return this;
  }

  public String getHttpServerTcpSerie() {
    return httpServerTcpSerie;
  }

  public InfluxDBOptions setHttpServerTcpSerie(String httpServerTcpSerie) {
    this.httpServerTcpSerie = httpServerTcpSerie;
    return this;
  }

  public String getTcpClientSerie() {
    return tcpClientSerie;
  }

  public InfluxDBOptions setTcpClientSerie(String tcpClientSerie) {
    this.tcpClientSerie = tcpClientSerie;
    return this;
  }

  public String getTcpServerSerie() {
    return tcpServerSerie;
  }

  public InfluxDBOptions setTcpServerSerie(String tcpServerSerie) {
    this.tcpServerSerie = tcpServerSerie;
    return this;
  }

  public InfluxDBOptions prefixSeriesWith(String prefix) {
    eventBusMessageSentSerie = prefix + eventBusMessageSentSerie;
    eventBusMessageReceivedSerie = prefix + eventBusMessageReceivedSerie;
    eventBusTcpSerie = prefix + eventBusTcpSerie;
    httpClientRequestsSerie = prefix + httpClientRequestsSerie;
    httpClientTcpSerie = prefix + httpClientTcpSerie;
    httpServerRequestsSerie = prefix + httpServerRequestsSerie;
    httpServerTcpSerie = prefix + httpServerTcpSerie;
    tcpClientSerie = prefix + tcpClientSerie;
    tcpServerSerie = prefix + tcpServerSerie;
    return this;
  }

  public InfluxDBOptions suffixSeriesWith(String suffix) {
    eventBusMessageSentSerie = eventBusMessageSentSerie + suffix;
    eventBusMessageReceivedSerie = eventBusMessageReceivedSerie + suffix;
    eventBusTcpSerie = eventBusTcpSerie + suffix;
    httpClientRequestsSerie = httpClientRequestsSerie + suffix;
    httpClientTcpSerie = httpClientTcpSerie + suffix;
    httpServerRequestsSerie = httpServerRequestsSerie + suffix;
    httpServerTcpSerie = httpServerTcpSerie + suffix;
    tcpClientSerie = tcpClientSerie + suffix;
    tcpServerSerie = tcpServerSerie + suffix;
    return this;
  }

  @Override
  public InfluxDBOptions setEnabled(boolean enable) {
    return (InfluxDBOptions) super.setEnabled(enable);
  }
}
