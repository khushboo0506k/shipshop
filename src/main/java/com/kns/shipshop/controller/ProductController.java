package com.kns.shipshop.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.kns.shipshop.persistence.entity.Category;
import com.kns.shipshop.persistence.entity.Product;
import com.kns.shipshop.persistence.repository.CategoryRepository;
import com.kns.shipshop.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@GetMapping(value = "/all")
	public List<Product> getAll() {
		return productService.getAllProducts();
	}
	
	@GetMapping(value = "/new")
	public List<Product> getNewProducts() {
		return productService.getNewProducts();
	}
	
	@GetMapping(value = "/category/{id}/top")
	public List<Product> getProductsByCategory(@PathVariable String id) {
		Category category = categoryRepository.findById(Integer.parseInt(id)).orElse(null);
		return productService.getTopProductsByCategory(category);
	}
	
	
	@GetMapping(value = "/get")
	public Product get(@RequestParam("id") int id) {
		return productService.getProduct(id);
	}
	
	@PostMapping(value = "/add")
	public Product persist(@RequestBody final Product product) {
		return productService.addProduct(product);
	}
	
	@DeleteMapping(value = "/delete")
	public boolean delete(@PathVariable int id) {
		return productService.deleteProduct(id);
	}
	
	@PutMapping(value = "/put/{id}")
	public Product put(@PathVariable int id, @RequestBody Product product) {
		return productService.updateProduct(id, product);
	}
	
	@GetMapping(value = "/category/{id}")
	public ModelAndView persist(@PathVariable String id) {
		ModelAndView modelAndView = new ModelAndView("product/product_category");
		Category category = categoryRepository.findById(Integer.parseInt(id)).orElse(null);
		modelAndView.addObject("products", productService.getProductsByCategory(category));
		modelAndView.addObject("category", category);
		return modelAndView;
	}
	
	@GetMapping(value = "/search/{category}")
	public ModelAndView searchProduct(@PathVariable int category, @RequestParam String searchTerm) {
		ModelAndView modelAndView = new ModelAndView("product/product_search");
		
		modelAndView.addObject("searchStr", searchTerm);
		modelAndView.addObject("products", productService.searchProduct(searchTerm, categoryRepository.findById(category).orElse(null)));
		
		return modelAndView;
	}
	
	@GetMapping(value = "/{id}")
	public ModelAndView getProductPage(@PathVariable String id) {
		ModelAndView modelAndView = new ModelAndView("product/product_info");
		modelAndView.addObject("product", productService.getProduct(Integer.parseInt(id)));
		return modelAndView;
	}

}
