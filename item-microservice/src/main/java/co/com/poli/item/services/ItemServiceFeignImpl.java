package co.com.poli.item.services;

import co.com.poli.item.clients.ProductFeignClient;
import co.com.poli.item.models.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("item-service-feign-client")
@RequiredArgsConstructor
public class ItemServiceFeignImpl implements ItemService {

    private final ProductFeignClient productFeignClient;
    @Override
    public List<Item> findAll() {
        System.out.println("Feign Client");
        return productFeignClient.findAll().stream()
                .map(product -> new Item(product, 1))
                .collect(Collectors.toList());
    }

    @Override
    public Item findById(Long id, Integer quantity) {
        System.out.println("Feign Client");
        return new Item(productFeignClient.findById(id), quantity);
    }
}
