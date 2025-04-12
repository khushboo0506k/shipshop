package com.kns.shipshop.service;

import java.util.List;

import com.kns.shipshop.persistence.entity.Category;

public interface CategoryService {
	
	public Category addCategory(String name);
	
	public List<Category> getCategories();
	
	public Boolean deleteCategory(int id);
	
	public Category updateCategory(int id,String name);

	public Category getCategory(int id);
}
