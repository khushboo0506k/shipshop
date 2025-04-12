package com.kns.shipshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kns.shipshop.persistence.entity.Cart;
import com.kns.shipshop.persistence.entity.Items;
import com.kns.shipshop.persistence.entity.User;
import com.kns.shipshop.persistence.repository.CartRepository;
import com.kns.shipshop.persistence.repository.ItemsRepository;
import com.kns.shipshop.service.CartService;

@Service
public class CartServiceImpl implements CartService {
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired ItemsRepository itemsRepository;

	@Override
	public Cart findByUser(User user) {
		return cartRepository.findByUser(user);
	}

	@Override
	public Cart addToCart(Cart cart) {
		return cartRepository.saveAndFlush(cart);
	}
	
	@Override
	public boolean removeCart(Cart cart) {
		itemsRepository.deleteAll(cart.getItems());
		cartRepository.delete(cart);
		return true;
	}
	
	@Override
	public boolean removeCartItem(Items item) {
		itemsRepository.delete(item);
		return true;
	}

	@Override
	public void updateCartItem(Items item) {
		itemsRepository.saveAndFlush(item);
	}

}
