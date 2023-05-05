package co.com.poli.gatewaymicroservice.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
//import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class GlobalFilterExample implements GlobalFilter, Ordered {

    private final Logger logger = LoggerFactory.getLogger(GlobalFilterExample.class);

    /*exchange podemos acceder al request y el response y manipular*/
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("Ejecutanto filtro pre");
        //Modificando el request en el filtro pre
        exchange.getRequest().mutate().headers(h -> h.add("token", "123456"));
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            logger.info("Ejecutando filtro post");

            //obtenemos las cabeceras del request
            Optional.ofNullable(
                    exchange.getRequest().getHeaders().getFirst("token")
            ).ifPresent((valor) -> {
                //pasamos el token pero en la respuesta
                exchange.getResponse().getHeaders().add("token", valor);
            });

            exchange.getResponse().getCookies().add("color", ResponseCookie.from("color", "rojo").build());
            //exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
        }));//continua con la ejecucion de nuestro flujo
    }


    //da una precedencia en el orden de ejecucion de los filtros
    @Override
    public int getOrder() {
        return 10;
    }
}
