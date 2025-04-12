package com.kns.shipshop.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kns.shipshop.persistence.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	public Optional<User> findByUsername(String username);
	
	public Optional<User> findUserByEmail(String email);
	
	public Optional<List<User>> findFirst10ByUsernameContainingIgnoreCase(String keyword);
	
}
