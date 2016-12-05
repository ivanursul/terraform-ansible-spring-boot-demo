package org.ivanursul.terraform.ansible;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.graphite.GraphiteReporter.Builder;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.codahale.metrics.servlets.AdminServlet;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableMetrics
@ConditionalOnProperty(name = "metrics.enabled", havingValue = "true")
public class MonitoringConfiguration extends MetricsConfigurerAdapter {

    @Value("${graphite.host}")
    private String graphiteHost;

    @Value("${graphite.port}")
    private int graphitePort;

    @Value("${graphite.amount.of.time.between.polls}")
    private long graphiteAmountOfTimeBetweenPolls;

    @Autowired
    private MetricRegistry metricRegistry;

    @Autowired
    private HealthCheckRegistry healthCheckRegistry;

    @Autowired
    private Graphite graphite;

    @PostConstruct
    public void init() {
        configureReporters(metricRegistry);
    }

    @Bean
    public MetricsServletContextListener metricsServletContextListener(MetricRegistry metricRegistry, HealthCheckRegistry healthCheckRegistry) {
        return new MetricsServletContextListener(metricRegistry, healthCheckRegistry);
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        return new ServletRegistrationBean(new AdminServlet(),"/dropwizard/*");
    }

    @Bean
    public Graphite graphite() {
        return new Graphite(
                new InetSocketAddress(graphiteHost, graphitePort)
        );
    }

    @Bean
    @ConditionalOnProperty(name = { "graphite.enabled", "metrics.enabled"}, havingValue = "true")
    public GraphiteReporter graphiteReporter(Graphite graphite) {
        GraphiteReporter graphiteReporter = getGraphiteReporterBuilder(metricRegistry).build(graphite);
        registerReporter(graphiteReporter);
        graphiteReporter.start(graphiteAmountOfTimeBetweenPolls, TimeUnit.MILLISECONDS);

        return graphiteReporter;
    }

    @Override
    public void configureReporters(MetricRegistry metricRegistry) {
        registerReporter(JmxReporter.forRegistry(metricRegistry).build()).start();
    }

    private Builder getGraphiteReporterBuilder(MetricRegistry metricRegistry) {
        metricRegistry.register("gc", new GarbageCollectorMetricSet());
        metricRegistry.register("memory", new MemoryUsageGaugeSet());
        metricRegistry.register("threads", new ThreadStatesGaugeSet());
        return GraphiteReporter.forRegistry(metricRegistry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .filter(MetricFilter.ALL);
    }

}