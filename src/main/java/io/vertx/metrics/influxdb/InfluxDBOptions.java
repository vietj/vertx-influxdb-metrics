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

  private String host;
  private int port;
  private String username;
  private String password;
  private String db;

  public InfluxDBOptions() {
    host = DEFAULT_HOST;
    port = DEFAULT_PORT;
    username = DEFAULT_USERNAME;
    password = DEFAULT_PASSWORD;
    db = DEFAULT_DB;
  }

  public InfluxDBOptions(InfluxDBOptions other) {
    super(other);
    host = other.host;
    port = other.port;
    username = other.username;
    password = other.password;
    db = other.db;
  }

  public InfluxDBOptions(JsonObject json) {
    super(json);
    host = json.getString("host", DEFAULT_HOST);
    port = json.getInteger("port", DEFAULT_PORT);
    username = json.getString("username", DEFAULT_USERNAME);
    password = json.getString("password", DEFAULT_PASSWORD);
    db = json.getString("db", DEFAULT_DB);
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

  @Override
  public InfluxDBOptions setEnabled(boolean enable) {
    return (InfluxDBOptions) super.setEnabled(enable);
  }
}
