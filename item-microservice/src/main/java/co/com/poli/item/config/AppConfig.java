package co.com.poli.item.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class AppConfig {

    @Bean("restTemplate")
    @LoadBalanced
    public RestTemplate registerRestTemplate(){
        return new RestTemplate();
    }

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
        //Configuracion para cualquier corto circuito
        //idCircuitBreaker es el nombre del cortoCircuito
        //como solo hay uno es el que se ejecuta
        return resilience4JCircuitBreakerFactory ->
                resilience4JCircuitBreakerFactory.configureDefault(
                        idCircuitBreaker -> {
                            return new Resilience4JConfigBuilder(idCircuitBreaker)
                                    .circuitBreakerConfig(CircuitBreakerConfig.custom()
                                            .slidingWindowSize(10)
                                            .failureRateThreshold(50)
                                            .waitDurationInOpenState(Duration.ofSeconds(10L))
                                            .permittedNumberOfCallsInHalfOpenState(5)
                                            .slowCallRateThreshold(50)
                                            .slowCallDurationThreshold(Duration.ofSeconds(2L))
                                            .build())
                                    .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(3L)).build())
                                    .build();
                }
        );
    }
}

