package co.com.poli.product.models.services;

import co.com.poli.product.models.entities.Product;

import java.util.List;

public interface ProductService {

    Product findById(Long id);
    List<Product> findAll();
    Product save(Product product);
}
