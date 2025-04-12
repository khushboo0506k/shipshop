package com.kns.shipshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.kns.shipshop.persistence.entity.Category;
import com.kns.shipshop.persistence.entity.Product;
import com.kns.shipshop.service.CategoryService;
import com.kns.shipshop.service.ProductService;
import com.kns.shipshop.utility.Utility;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

	@GetMapping("/dashboard")
	public ModelAndView adminHome() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/admin/admin_home");
		return modelAndView;
	}

	@GetMapping("/categories")
	public ModelAndView getcategory() {
		ModelAndView modelAndView = new ModelAndView("categories");
		List<Category> categories = this.categoryService.getCategories();
		modelAndView.addObject("categories", categories);
		return modelAndView;
	}

	@RequestMapping(value = "/categories", method = RequestMethod.POST)
	public String addCategory(@RequestParam("categoryname") String category_name) {
		System.out.println(category_name);

		Category category = this.categoryService.addCategory(category_name);
		if (category.getName().equals(category_name)) {
			return "redirect:categories";
		} else {
			return "redirect:categories";
		}
	}

	@GetMapping("/categories/delete")
	public ModelAndView removeCategoryDb(@RequestParam("id") int id) {
		this.categoryService.deleteCategory(id);
		ModelAndView mView = new ModelAndView("forward:/categories");
		return mView;
	}

	@GetMapping("/categories/update")
	public String updateCategory(@RequestParam("categoryid") int id,
			@RequestParam("categoryname") String categoryname) {
		Category category = this.categoryService.updateCategory(id, categoryname);
		return "redirect:/admin/categories";
	}

	@GetMapping("/products")
	public ModelAndView getproduct() {
		ModelAndView mView = new ModelAndView("products");

		List<Product> products = this.productService.getAllProducts();

		if (products.isEmpty()) {
			mView.addObject("msg", "No products are available");
		} else {
			mView.addObject("products", products);
		}
		return mView;
	}

	@GetMapping("/products/add")
	public ModelAndView addProduct() {
		ModelAndView mView = new ModelAndView("productsAdd");
		List<Category> categories = this.categoryService.getCategories();
		mView.addObject("categories", categories);
		return mView;
	}

	@RequestMapping(value = "/products/add", method = RequestMethod.POST)
	public String addProduct(@RequestParam("name") String name, @RequestParam("categoryid") int categoryId,
			@RequestParam("price") int price, @RequestParam("description") String description,
			@RequestParam("productImage") String productImage) {
		System.out.println(categoryId);
		Category category = this.categoryService.getCategory(categoryId);
		Product product = new Product();
		product.setProductName(name);
		product.setCategory(category);
		product.setDescription(description);
		product.setMrp(price);
		this.productService.addProduct(product);
		return "redirect:/admin/products";
	}

	@GetMapping("/products/update/{id}")
	public ModelAndView updateproduct(@PathVariable("id") int id) {

		ModelAndView mView = new ModelAndView("productsUpdate");
		Product product = this.productService.getProduct(id);
		List<Category> categories = this.categoryService.getCategories();

		mView.addObject("categories", categories);
		mView.addObject("product", product);
		return mView;
	}

	@RequestMapping(value = "/products/update/{id}", method = RequestMethod.POST)
	public String updateProduct(@PathVariable("id") int id, @RequestParam("name") String name,
			@RequestParam("categoryid") int categoryId, @RequestParam("price") int price,
			@RequestParam("weight") int weight, @RequestParam("quantity") int quantity,
			@RequestParam("description") String description, @RequestParam("productImage") String productImage) {

//		this.productService.updateProduct();
		return "redirect:/admin/products";
	}

	@GetMapping("/products/delete")
	public String removeProduct(@RequestParam("id") int id) {
		this.productService.deleteProduct(id);
		return "redirect:/admin/products";
	}

	@PostMapping("/products")
	public String postproduct() {
		return "redirect:/admin/categories";
	}
	
}
