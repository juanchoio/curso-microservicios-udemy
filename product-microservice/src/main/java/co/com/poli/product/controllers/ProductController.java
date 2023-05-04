package co.com.poli.product.controllers;

import co.com.poli.product.models.entities.Product;
import co.com.poli.product.models.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final Environment environment;

    private final ProductService productService;
    @Value("${server.port}")
    private Integer port;

    @GetMapping
    public List<Product> findAll(){
        return productService.findAll().stream()
                .map(product -> {
                    product.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
                    //product.setPort(port);
                    return product;
                }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable Long id) throws InterruptedException {
        if(id.equals(10L)) throw new IllegalStateException("Producto no encontrado");
        if(id.equals(7L)) TimeUnit.SECONDS.sleep(5L);
        Product product = productService.findById(id);
        product.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
/*        boolean ok = false;
        if(!ok){
            throw new RuntimeException("No se pudo cargar el producto!");
        }*/
/*        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/
        return productService.findById(id);
    }

    @PostMapping
    public Product save(@RequestBody Product product){
        return productService.save(product);
    }


}
