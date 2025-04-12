package com.kns.shipshop.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kns.shipshop.persistence.entity.Category;
import com.kns.shipshop.persistence.entity.Product;
import com.kns.shipshop.persistence.entity.User;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

	List<Product> findByCategory(Category category);
	List<Product> findByProductName(String productName);
	List<Product> findByProductNameContainingIgnoreCase(String searchStr);
	List<Product> findByCategoryAndProductNameContainingIgnoreCase(Category category, String searchStr);
	
	List<Product> findFirst10ByOrderByInsertTimestampDesc();
	
	List<Product> findFirst10ByCategoryOrderByInsertTimestampDesc(Category category);
	List<Product> findByUser(User user);
	
}
