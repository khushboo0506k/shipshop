package com.kns.shipshop.service;

import java.util.List;

import com.kns.shipshop.persistence.entity.Category;
import com.kns.shipshop.persistence.entity.Product;
import com.kns.shipshop.persistence.entity.User;

public interface ProductService {
	
	public List<Product> getAllProducts();
	
	public List<Product> getNewProducts();
	
	public List<Product> getProductsByCategory(Category category);
	
	public List<Product> getTopProductsByCategory(Category category);
	
	public Product getProduct(Integer id);
	
	public Product addProduct(Product product);
	
	public Product getProduct(int id);

	public Product updateProduct(int id,Product product);
	
	public boolean deleteProduct(int id);
	
	public List<Product> searchProduct(String searchStr, Category category);

	public List<Product> getAllProducts(User user);
	
}
