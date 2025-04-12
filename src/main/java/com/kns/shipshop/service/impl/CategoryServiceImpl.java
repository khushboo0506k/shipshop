package com.kns.shipshop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kns.shipshop.persistence.entity.Category;
import com.kns.shipshop.persistence.repository.CategoryRepository;
import com.kns.shipshop.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;
	
	public Category addCategory(String name) {
		Category category = new Category();
		category.setName(name);
		categoryRepository.saveAndFlush(category);
		return category;
	}
	
	public List<Category> getCategories(){
		return categoryRepository.findAll();
	}
	
	public Boolean deleteCategory(int id) {
		categoryRepository.deleteById(id);
		return true;
	}
	
	public Category updateCategory(int id, String name) {
		Category category = categoryRepository.findById(id).orElse(null);
		if(category == null)
			return null;
		category.setName(name);
		categoryRepository.saveAndFlush(category);
		return category;
	}

	public Category getCategory(int id) {
		return this.categoryRepository.findById(id).orElse(null);
	}
}
