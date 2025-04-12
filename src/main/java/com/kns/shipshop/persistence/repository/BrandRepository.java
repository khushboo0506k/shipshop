package com.kns.shipshop.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kns.shipshop.persistence.entity.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
	
	Brand findByName(String name);

}
