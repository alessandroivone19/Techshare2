package com.generation.techshare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.techshare.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
