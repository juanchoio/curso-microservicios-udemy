package co.com.poli.item.controllers;

import co.com.poli.item.models.Item;
import co.com.poli.item.models.Product;
import co.com.poli.item.services.ItemService;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    @Qualifier(value = "item-service-feign-client")
    private ItemService itemService;

    @GetMapping
    public List<Item> findAll(
            @RequestParam(name = "nombre") String nombre,
            @RequestHeader(name = "token-request") String token) {
        System.out.println("RequestParam: {nombre} " + nombre);
        System.out.println("RequestHeader: {token-request} " + token);
        return itemService.findAll();
    }


    @HystrixCommand(fallbackMethod = "metodoAlternativo")
    @GetMapping("/{id}/quantity/{quantity}")
    public Item findById(@PathVariable Long id, @PathVariable Integer quantity){
        return itemService.findById(id, quantity);
    }

    public Item metodoAlternativo(Long id, Integer quantity){
        Item item = new Item();
        Product product = new Product();
        item.setQuantity(quantity);
        product.setId(id);
        product.setName("Camara Sony");
        product.setPrice(500.00);
        item.setProduct(product);
        return item;
    }
}
