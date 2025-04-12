package com.kns.shipshop.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kns.shipshop.persistence.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
	
}
