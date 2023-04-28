package co.com.poli.item.services;

import co.com.poli.item.models.Item;

import java.util.List;

public interface ItemService {

    List<Item> findAll();
    Item findById(Long id, Integer quantity);

}
