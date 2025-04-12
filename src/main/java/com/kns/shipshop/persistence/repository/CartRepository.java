package com.kns.shipshop.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kns.shipshop.persistence.entity.Cart;
import com.kns.shipshop.persistence.entity.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

	public Cart findByUser(User user);
}
