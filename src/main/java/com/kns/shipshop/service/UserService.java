package com.kns.shipshop.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kns.shipshop.exception.InvalidPasswordException;
import com.kns.shipshop.exception.UserAlreadyExistsException;
import com.kns.shipshop.persistence.entity.Address;
import com.kns.shipshop.persistence.entity.Items;
import com.kns.shipshop.persistence.entity.Orders;
import com.kns.shipshop.persistence.entity.Product;
import com.kns.shipshop.persistence.entity.User;
import com.kns.shipshop.persistence.entity.Wishlist;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

	String register(User user, HttpServletRequest request) throws UserAlreadyExistsException;

	User udpateUser(User user);

	List<User> getAllUsers();

	Page<User> findPaginatedUser(Pageable pageable, boolean refresh);

	boolean changePassword(User user) throws InvalidPasswordException;

	List<User> searchUserByUsernameKeyword(String keyword);

	User getUser(String email);

	boolean verify(String verificationCode);

	void sendVerificationEmail(User user, String email, String hostName);

	String validateEmail(String email);

	User refund(String username, double amount, String code);

	String changePassword(String currentPassword, String newPassword, Long userId);

	String validateUsername(String username);

	Set<Items> getCartItems(String email);

	List<Wishlist> getWishlistItems(String email);
	
	Address addAddress(Address address);

	Orders placeOrder(Map<String, String> attributeMap, User user);

	List<Orders> getOrders(String email);

	Orders getOrder(Long orderId);

	boolean removeCartItem(User user, Product product);

	boolean removeWishlistItem(User user, Product productId);

	List<Product> getProducts(User user);

}
