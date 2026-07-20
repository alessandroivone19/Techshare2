package com.generation.techshare.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.generation.techshare.dto.CategoryDto;
import com.generation.techshare.exception.ServiceException;
import com.generation.techshare.mapper.CategoryMapper;
import com.generation.techshare.model.Category;
import com.generation.techshare.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    // Trova una categoria per ID
    public CategoryDto findById(Integer id) throws ServiceException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Categoria non trovata con ID: " + id));
        return categoryMapper.toDTO(category);
    }

    // Trova tutte le categorie
    public List<CategoryDto> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toDtos(categories);
    }

    // Crea una nuova categoria
    public CategoryDto insert(CategoryDto categoryDto) throws ServiceException {
        try{
            Category category = categoryMapper.toEntity(categoryDto);
            Category savedCategory = categoryRepository.save(category);
            return categoryMapper.toDTO(savedCategory);
        }catch(Exception e){
            throw new ServiceException("non è stato possibile salvare la categoria");
        }
    }

    // Aggiorna una categoria
    public CategoryDto update(Integer id, CategoryDto categoryDto) throws ServiceException {
        try{ 
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new ServiceException("Categoria non trovata con ID: " + id));
    
            category.setName(categoryDto.getName());
            category.setDescription(categoryDto.getDescription());
            
            Category updatedCategory = categoryRepository.save(category);
            return categoryMapper.toDTO(updatedCategory);
        }catch(Exception e){
            throw new ServiceException("non è stato possibile aggiornare la categoria");
        }
    }

    // Elimina una categoria
    public void delete(Integer id) throws ServiceException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Categoria non trovata con ID: " + id));
        categoryRepository.delete(category);
    }

}