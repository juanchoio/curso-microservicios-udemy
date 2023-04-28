package co.com.poli.item.clients;


import co.com.poli.item.models.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "product-microservice")
public interface ProductFeignClient {

    @GetMapping("/products")
    List<Product> findAll();

    @GetMapping("/products/{id}")
    Product findById(@PathVariable Long id);


}
