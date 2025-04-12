package com.kns.shipshop.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kns.shipshop.persistence.entity.Orders;
import com.kns.shipshop.persistence.entity.User;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
	
	List<Orders> findByUser(User user);
	
}
