package org.example.abysaltobackendtask.dto;
public record ProductListItemDto(
        Long id,
        String name,
        Double price,
        String image,
        String shortDescription) {

}
