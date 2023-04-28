package co.com.poli.zuulmicroservice.zuulmicroservice.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class PreTimeElapsedFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(PreTimeElapsedFilter.class);
    @Override
    public String filterType() {
        return "pre";
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
        log.info(String.format(
                "%s request enrutado a %s",
                request.getMethod(),
                request.getRequestURL().toString()
        ));
        request.setAttribute("tiempoInicio", System.currentTimeMillis());
        return null;
    }
}
