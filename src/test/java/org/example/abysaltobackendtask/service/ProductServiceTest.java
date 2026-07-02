package org.example.abysaltobackendtask.service;

import org.example.abysaltobackendtask.dto.ProductFilter;
import org.example.abysaltobackendtask.dto.ProductListItemDto;
import org.example.abysaltobackendtask.model.Product;
import org.example.abysaltobackendtask.source.ProductSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductSource productSource;
    @InjectMocks
    private ProductService productService;
    private Product product(long id, String title, String category, double price, String description){
        return new Product(id, title, description, category, price,
                0.0, 0.0, 0, "brand", "thumb.jpg", List.of());
    }
    @Test
    void shortensDescTo100Characters(){
        String longDesc = "a".repeat(150);
        when(productSource.getAllProducts()).thenReturn(
                List.of(product(1,"title","category",10.0,longDesc))
        );

        List<ProductListItemDto> result = productService.getProducts(new ProductFilter(
                null,null,null,null));

        assertThat(result).hasSize(1);
        //100+"..."=103
        assertThat(result.get(0).shortDescription()).hasSize(103);
        assertThat(result.get(0).shortDescription()).endsWith("...");

    }
    @Test
    void keepsShortDescriptionUnchanged() {

        when(productSource.getAllProducts()).thenReturn(
                List.of(product(1, "Test", "beauty", 10.0, "kratak opis"))
        );

        List<ProductListItemDto> result = productService.getProducts(
                new ProductFilter(null, null, null, null));

        assertThat(result.get(0).shortDescription()).isEqualTo("kratak opis");
    }

    @Test
    void filtersByCategory() {
        when(productSource.getAllProducts()).thenReturn(List.of(
                product(1, "Mascara", "beauty", 10.0, "opis"),
                product(2, "Laptop", "electronics", 999.0, "opis")
        ));

        List<ProductListItemDto> result = productService.getProducts(
                new ProductFilter("beauty", null, null, null));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Mascara");
    }
    @Test
    void filtersByPriceRange() {
        when(productSource.getAllProducts()).thenReturn(List.of(
                product(1, "Jeftino", "beauty", 5.0, "opis"),
                product(2, "Srednje", "beauty", 15.0, "opis"),
                product(3, "Skupo", "beauty", 100.0, "opis")
        ));

        List<ProductListItemDto> result = productService.getProducts(
                new ProductFilter(null, 10.0, 50.0, null));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Srednje");
    }

    @Test
    void searchByNameIsCaseInsensitive() {
        when(productSource.getAllProducts()).thenReturn(List.of(
                product(1, "Essence Mascara", "beauty", 10.0, "opis"),
                product(2, "Red Lipstick", "beauty", 12.0, "opis")
        ));

        List<ProductListItemDto> result = productService.getProducts(
                new ProductFilter(null, null, null, "MASCARA"));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Essence Mascara");
    }

    @Test
    void emptyFilterReturnsAllProducts() {
        when(productSource.getAllProducts()).thenReturn(List.of(
                product(1, "A", "beauty", 10.0, "opis"),
                product(2, "B", "electronics", 20.0, "opis")
        ));

        List<ProductListItemDto> result = productService.getProducts(
                new ProductFilter(null, null, null, null));

        assertThat(result).hasSize(2);
    }
}




