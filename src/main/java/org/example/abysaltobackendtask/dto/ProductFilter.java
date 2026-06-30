package org.example.abysaltobackendtask.dto;

public record ProductFilter (
        String category,
        Double minPrice,
        Double maxPrice,
        String search
){
}
