package org.example.abysaltobackendtask.source.dummyjson;

import lombok.RequiredArgsConstructor;
import org.example.abysaltobackendtask.dto.dummyjson.DummyJsonProduct;
import org.example.abysaltobackendtask.dto.dummyjson.DummyJsonProductListResponse;
import org.example.abysaltobackendtask.model.Product;
import org.example.abysaltobackendtask.source.ProductSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DummyJsonProductSource implements ProductSource {
    private final RestClient restClient;

@Override
    public List<Product> getAllProducts(){
    DummyJsonProductListResponse response= restClient.get()
            .uri("/products?limit=0")
            .retrieve()
            .body(DummyJsonProductListResponse.class);
    if(response==null || response.products()== null){
        return List.of();
    }
    return response.products().stream()
            .map(this::toProduct)
            .toList();

}
@Override
public Optional<Product> getProductById(long id){
    try{
        DummyJsonProduct product = restClient.get()
                .uri("/products/{id}",id)
                .retrieve()
                .body(DummyJsonProduct.class);
        return Optional.ofNullable(product).map(this::toProduct);
    }catch (HttpClientErrorException e){
        return Optional.empty();
    }
}
    private Product toProduct(DummyJsonProduct p) {
        return new Product(
                p.id(),
                p.title(),
                p.description(),
                p.category(),
                p.price(),
                p.discountPercentage(),
                p.rating(),
                p.stock(),
                p.brand(),
                p.thumbnail(),
                p.images()
        );
    }
}
