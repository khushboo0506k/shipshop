package com.kns.shipshop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kns.shipshop.persistence.entity.Category;
import com.kns.shipshop.persistence.entity.Product;
import com.kns.shipshop.persistence.entity.User;
import com.kns.shipshop.persistence.repository.ProductRepository;
import com.kns.shipshop.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Override
	public List<Product> getAllProducts(){
		return productRepository.findAll();
	}
	
	@Override
	public List<Product> getNewProducts() {
		return productRepository.findFirst10ByOrderByInsertTimestampDesc();
	}
	
	@Override
	public List<Product> getProductsByCategory(Category category) {
		return productRepository.findByCategory(category);
	}
	
	@Override
	public List<Product> getTopProductsByCategory(Category category) {
		return productRepository.findFirst10ByCategoryOrderByInsertTimestampDesc(category);
	}
	
	@Override
	public Product getProduct(Integer id) {
		return productRepository.findById(id).get();
	}
	
	@Override
	public Product addProduct(Product product) {
		return productRepository.saveAndFlush(product);
	}
	
	@Override
	public Product getProduct(int id) {
		return productRepository.findById(id).orElse(null);
	}

	@Override
	public Product updateProduct(int id, Product product){
		if (productRepository.existsById(id)) {
			deleteProduct(id);
			return addProduct(product);
		}
		return null;
	}
	
	@Override
	public boolean deleteProduct(int id) {
		productRepository.deleteById(id);
		return true;
	}

	@Override
	public List<Product> searchProduct(String searchStr, Category category) {
		if(category == null)
			return productRepository.findByProductNameContainingIgnoreCase(searchStr);
		else
			return productRepository.findByCategoryAndProductNameContainingIgnoreCase(category, searchStr);
	}

	@Override
	public List<Product> getAllProducts(User user) {
		return productRepository.findByUser(user);
	}

}
