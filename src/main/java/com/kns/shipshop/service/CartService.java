package com.kns.shipshop.service;

import org.springframework.stereotype.Service;

import com.kns.shipshop.persistence.entity.Cart;
import com.kns.shipshop.persistence.entity.Items;
import com.kns.shipshop.persistence.entity.User;

@Service
public interface CartService {
	
	public Cart findByUser(User user);

	public Cart addToCart(Cart cart);

	boolean removeCart(Cart cart);

	boolean removeCartItem(Items item);

	public void updateCartItem(Items item);

}
