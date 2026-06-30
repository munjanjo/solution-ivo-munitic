package org.example.abysaltobackendtask.service;

import lombok.RequiredArgsConstructor;
import org.example.abysaltobackendtask.dto.ProductDetailDto;
import org.example.abysaltobackendtask.dto.ProductListItemDto;
import org.example.abysaltobackendtask.model.Product;
import org.example.abysaltobackendtask.source.ProductSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private static final int SHORT_DESCRIPTION_MAX_LENGTH = 100;
    private final ProductSource productSource;

    public List<ProductListItemDto> getProducts(){
        return productSource.getAllProducts().stream().map(this::toListItem).toList();
    }
    public Optional<ProductDetailDto> getProductById(long id){
        return productSource.getProductById(id).map(this::toDetail);
    }
    private ProductListItemDto toListItem(Product p) {
        return new ProductListItemDto(
                p.id(),
                p.title(),
                p.price(),
                p.thumbnail(),
                shorten(p.description())
        );
    }
    private ProductDetailDto toDetail(Product p) {
        return new ProductDetailDto(
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
    private String shorten(String description) {
        if (description == null) {
            return null;
        }
        if (description.length() <= SHORT_DESCRIPTION_MAX_LENGTH) {
            return description;
        }
        /// nisan siguran ako treba desc sve skupa imati 100 znakova ako da onda se ili moze maknuti ovaj moj dodatak
        /// ili samo max length smajniti za jos 3 znaka
        return description.substring(0, SHORT_DESCRIPTION_MAX_LENGTH) + "...";
    }
}
