package co.com.poli.gatewaymicroservice.filters.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class PersonalizedGatewayFilterFactory extends AbstractGatewayFilterFactory<PersonalizedGatewayFilterFactory.Configuration> {

    private final Logger logger = LoggerFactory.getLogger(PersonalizedGatewayFilterFactory.class);

    public PersonalizedGatewayFilterFactory() {
        super(Configuration.class);
    }

    @Override
    public GatewayFilter apply(Configuration config) {

        return (exchange, chain) -> {
            //pre
            logger.info("Ejecutando pre gateway filter factory: " + config.mensaje);

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                //post
                Optional.ofNullable(config.cookieValor).ifPresent(cookie -> {
                    exchange.getResponse().addCookie(ResponseCookie.from(config.cookieName, config.cookieValor).build());
                });
                logger.info("Ejecutando post gateway filter factory: " +config.mensaje);
            }));
        };
    }

    @Override
    public String name() {
        return "PersonalizedCookie";
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("mensaje", "cookieName", "cookieValor");
    }

    public static class Configuration {

        private String mensaje;
        private String cookieValor;
        private String cookieName;

        public String getMensaje() {
            return mensaje;
        }

        public String getCookieValor() {
            return cookieValor;
        }

        public String getCookieName() {
            return cookieName;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }

        public void setCookieValor(String cookieValor) {
            this.cookieValor = cookieValor;
        }

        public void setCookieName(String cookieName) {
            this.cookieName = cookieName;
        }
    }



}


