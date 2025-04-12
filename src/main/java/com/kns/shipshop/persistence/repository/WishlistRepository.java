package com.kns.shipshop.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kns.shipshop.persistence.entity.Product;
import com.kns.shipshop.persistence.entity.User;
import com.kns.shipshop.persistence.entity.Wishlist;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
	
	List<Wishlist> findByUser(User user);
	
	void deleteByProductAndUser(Product product, User user);

	Wishlist findByUserAndProduct(User user, Product product);

}
