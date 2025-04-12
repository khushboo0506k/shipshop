package com.kns.shipshop.service;

import com.kns.shipshop.persistence.entity.Brand;

public interface BrandService {
	
	public Brand findByName(String name);
	
}
