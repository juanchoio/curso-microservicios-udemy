package co.com.poli.item.controllers;

import co.com.poli.item.models.Item;
import co.com.poli.item.models.Product;
import co.com.poli.item.services.ItemService;

//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RefreshScope
@RestController
@RequestMapping("/items")
public class ItemController {

    private final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private Environment env;

    @Value("${configuracion.texto}")
    private String textoConfigurationServerConfig;

    @Autowired
    private CircuitBreakerFactory cbFactory;

    @Autowired
    @Qualifier(value = "item-service-rest-template")
    private ItemService itemService;

    @GetMapping("/obtenerConfiguracion")
    public ResponseEntity<?> obtenerConfiguracion(@Value("${server.port}") String port){
        Map<String, String> json = new HashMap<>();
        json.put("textoConfigServer", textoConfigurationServerConfig);
        json.put("puertoConfigServer", port);
        logger.info("textoConfigServer {}", textoConfigurationServerConfig);
        logger.info("puertoConfigServer {}", port);

        if (env.getActiveProfiles().length > 0 && env.getActiveProfiles()[0].equals("dev")) {
            json.put("autor.nombre", env.getProperty("configuracion.autor.nombre"));
            json.put("autor.email", env.getProperty("configuracion.autor.email"));
        }

        return new ResponseEntity<Map<String, String>>(json, HttpStatus.OK);
    }

    @GetMapping
    public List<Item> findAll(
            @RequestParam(name = "nombre", required = false) String nombre,
            @RequestHeader(name = "token-request", required = false) String token) {
        System.out.println("RequestParam: {nombre} " + nombre);
        System.out.println("RequestHeader: {token-request} " + token);
        return itemService.findAll();
    }


    //@HystrixCommand(fallbackMethod = "metodoAlternativo")
    @GetMapping("/{id}/quantity/{quantity}")
    public Item findById(@PathVariable Long id, @PathVariable Integer quantity) {
        return cbFactory.create("item")
                .run(
                        () -> itemService.findById(id, quantity),
                        err -> metodoAlternativo(id, quantity, err)
                );
    }

    //usando anotaciones solo se aplica la configuracion via archivo de config
    @CircuitBreaker(name = "item", fallbackMethod = "metodoAlternativo")
    @GetMapping("/annotation/{id}/quantity/{quantity}")
    public Item findById2(@PathVariable Long id, @PathVariable Integer quantity) {
        return itemService.findById(id, quantity);
    }

    @CircuitBreaker(name = "item", fallbackMethod = "metodoAlternativo2")
    @TimeLimiter(name = "item")
    @GetMapping("/annotation2/{id}/quantity/{quantity}")
    public CompletableFuture<Item> findById3(@PathVariable Long id, @PathVariable Integer quantity) {
        return CompletableFuture.supplyAsync(() -> itemService.findById(id, quantity)) ;
    }

    public Item metodoAlternativo(Long id, Integer quantity,Throwable e){
        logger.error(e.getMessage());
        Item item = new Item();
        Product product = new Product();
        item.setQuantity(quantity);
        product.setId(id);
        product.setName("Camara Sony");
        product.setPrice(500.00);
        item.setProduct(product);
        return item;
    }

    public CompletableFuture<Item> metodoAlternativo2(Long id, Integer quantity,Throwable e){
        logger.error(e.getMessage());
        Item item = new Item();
        Product product = new Product();
        item.setQuantity(quantity);
        product.setId(id);
        product.setName("Camara Kodak");
        product.setPrice(500.00);
        item.setProduct(product);
        return CompletableFuture.supplyAsync(() -> item);
    }
}
