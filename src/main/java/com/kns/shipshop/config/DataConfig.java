package com.kns.shipshop.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kns.shipshop.persistence.entity.Category;
import com.kns.shipshop.persistence.entity.Product;
import com.kns.shipshop.persistence.repository.CategoryRepository;
import com.kns.shipshop.persistence.repository.ProductRepository;
import com.kns.shipshop.service.BrandService;

@Configuration
public class DataConfig {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private BrandService brandService;
	
	@Bean
	public String insertRecords() {
		System.out.println("__ Inserting Records __");
		insertCategories();
		insertProducts();
		
		System.out.println("__ Installed Services __");
		return "success";
	}
	
	private void insertCategories() {
		System.out.println("__ Inserting Categories __");
		String fileName = "D:\\categories.txt";
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			Set<Category> categories = stream.map(s -> s.split(";")).map(arr -> new Category(Integer.parseInt(arr[0].trim()), arr[1].trim()))
					.collect(Collectors.toSet());
			categoryRepository.saveAllAndFlush(categories);
			System.out.println("__ Inserted " + categories.size() + " Categories __");
		} catch (IOException e) {
			System.out.println("Error occurred :: " + e.getMessage());
		}
	}
	
	private void insertProducts() {
		System.out.println("__ Inserting Products __");
		String fileName = "D:\\products.txt";
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			Set<Product> products = stream.map(s -> s.split(";"))
					.map(arr -> new Product(Integer.parseInt(arr[0].trim()), arr[1].trim(), Double.parseDouble(arr[2].trim()), 
							Double.parseDouble(arr[3].trim()), categoryRepository.findById(Integer.parseInt(arr[4].trim())).orElse(null), 
							arr[5].trim(), brandService.findByName(arr[6].trim())))
					.collect(Collectors.toSet());
			productRepository.saveAllAndFlush(products);
			System.out.println("__ Inserted " + products.size() + " Products __");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error occurred :: " + e.getMessage());
		}
	}

}
