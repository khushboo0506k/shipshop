package com.kns.shipshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kns.shipshop.persistence.entity.Brand;
import com.kns.shipshop.persistence.repository.BrandRepository;
import com.kns.shipshop.service.BrandService;

@Service
public class BrandServiceImpl implements BrandService {

	@Autowired
	private BrandRepository brandRepository;
	
	@Override
	public Brand findByName(String name) {
		Brand brand = brandRepository.findByName(name);
		if(brand == null) {
			brand = new Brand();
			brand.setName(name);
			brandRepository.saveAndFlush(brand);
		}
		return brand;
	}
	
	

}
