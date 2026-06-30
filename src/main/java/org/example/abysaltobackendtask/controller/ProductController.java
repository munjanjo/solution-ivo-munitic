package org.example.abysaltobackendtask.controller;

import lombok.RequiredArgsConstructor;
import org.example.abysaltobackendtask.dto.ProductDetailDto;
import org.example.abysaltobackendtask.dto.ProductFilter;
import org.example.abysaltobackendtask.dto.ProductListItemDto;
import org.example.abysaltobackendtask.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductListItemDto> getProducts(@RequestParam(required = false) String category,
                                                @RequestParam(required = false) Double minPrice,
                                                @RequestParam(required = false) Double maxPrice,
                                                @RequestParam(required = false) String search) {
        ProductFilter filter = new ProductFilter(category,minPrice,maxPrice,search);
        return productService.getProducts(filter);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailDto> getProductById(@PathVariable long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}