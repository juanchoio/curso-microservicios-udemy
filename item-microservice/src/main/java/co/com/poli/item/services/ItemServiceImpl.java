package co.com.poli.item.services;

import co.com.poli.item.models.Item;
import co.com.poli.item.models.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("item-service-rest-template")
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{

    private final RestTemplate restTemplate;
    @Override
    public List<Item> findAll() {
        System.out.println("RestTemplate Client");
        List<Product> products = Arrays.asList(restTemplate.getForObject("http://product-microservice/products", Product[].class));

        return products.stream().map(product -> new Item(product, 1)).collect(Collectors.toList());
    }

    @Override
    public Item findById(Long id, Integer quantity) {
        System.out.println("RestTemplate Client");
        Map<String, String> pathVariables = new HashMap<>();
        pathVariables.put("id", id.toString());
        Product product = restTemplate.getForObject("http://product-microservice/products/{id}", Product.class, pathVariables);

        return new Item(product, quantity);
    }
}
