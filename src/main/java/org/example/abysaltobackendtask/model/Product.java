package org.example.abysaltobackendtask.model;

import java.util.List;

public record Product(
        Long id,
        String title,
        String description,
        String category,
        Double price,
        Double discountPercentage,
        Double rating,
        Integer stock,
        String brand,
        String thumbnail,
        List<String> images
) {
}