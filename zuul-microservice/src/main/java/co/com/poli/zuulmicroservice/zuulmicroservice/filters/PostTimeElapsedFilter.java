package co.com.poli.zuulmicroservice.zuulmicroservice.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class PostTimeElapsedFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(PostTimeElapsedFilter.class);
    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    /*Aca podemos hacer una validacion cualquiera
    * si es true ejecuta el filtro run() method*/
    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        log.info("Entrando a post filter");
        Long tiempoInicio = (Long) request.getAttribute("tiempoInicio");
        Long tiempoFinal = System.currentTimeMillis();


       log.info(String.format(
               "Tiempo Transcurrido en segundos %s",
                ((tiempoFinal.doubleValue()-tiempoInicio.doubleValue())/1000.00)
        ));
        log.info(String.format(
                "Tiempo Transcurrido en milisegundos %s",
                (tiempoFinal.doubleValue()-tiempoInicio.doubleValue())
        ));
        return null;
    }
}
