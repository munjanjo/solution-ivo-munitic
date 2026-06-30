package org.example.abysaltobackendtask.source;
import org.example.abysaltobackendtask.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductSource {

    List<Product> getAllProducts();

    Optional<Product> getProductById(long id);
}