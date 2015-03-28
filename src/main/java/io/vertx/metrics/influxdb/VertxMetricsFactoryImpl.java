package io.vertx.metrics.influxdb;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.metrics.MetricsOptions;
import io.vertx.core.spi.VertxMetricsFactory;
import io.vertx.core.spi.metrics.VertxMetrics;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class VertxMetricsFactoryImpl implements VertxMetricsFactory {

  @Override
  public VertxMetrics metrics(Vertx vertx, VertxOptions options) {
    MetricsOptions baseOptions = options.getMetricsOptions();
    InfluxDBOptions metricsOptions;
    if (baseOptions instanceof InfluxDBOptions) {
      metricsOptions = (InfluxDBOptions) baseOptions;
    } else {
      metricsOptions = new InfluxDBOptions(baseOptions.toJson());
    }
    return new VertxMetricsImpl(metricsOptions);
  }
}
