package org.example.abysaltobackendtask.dto;
import java.util.List;

public record ProductDetailDto(
        Long id,
        String name,
        String description,
        String category,
        Double price,
        Double discountPercentage,
        Double rating,
        Integer stock,
        String brand,
        String image,
        List<String> images
) {
}
