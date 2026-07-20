package com.generation.techshare.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.generation.techshare.dto.CategoryDto;
import com.generation.techshare.model.Category;



@Mapper(componentModel = "spring")
public interface CategoryMapper {

    // Category Entity -> CategoryDTO
    CategoryDto toDTO(Category category);

    // CategoryDTO -> Category Entity
    
    Category toEntity(CategoryDto categoryDto);

    List<CategoryDto> toDtos (List<Category> categories);

    List<Category> toEntity (List<CategoryDto> dtos);
    

}