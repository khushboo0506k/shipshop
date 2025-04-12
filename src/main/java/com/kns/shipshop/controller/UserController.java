package com.kns.shipshop.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.kns.shipshop.exception.EmailAlreadyExistsException;
import com.kns.shipshop.exception.InvalidEmailException;
import com.kns.shipshop.exception.InvalidPasswordException;
import com.kns.shipshop.exception.UserAlreadyExistsException;
import com.kns.shipshop.persistence.entity.Address;
import com.kns.shipshop.persistence.entity.Cart;
import com.kns.shipshop.persistence.entity.Items;
import com.kns.shipshop.persistence.entity.Orders;
import com.kns.shipshop.persistence.entity.Product;
import com.kns.shipshop.persistence.entity.User;
import com.kns.shipshop.persistence.entity.Wishlist;
import com.kns.shipshop.persistence.repository.ItemsRepository;
import com.kns.shipshop.persistence.repository.ProductRepository;
import com.kns.shipshop.persistence.repository.WishlistRepository;
import com.kns.shipshop.service.CartService;
import com.kns.shipshop.service.UserService;
import com.kns.shipshop.user.UserDetailsImpl;
import com.kns.shipshop.utility.Utility;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private WishlistRepository wishlistRepository;

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	private CartService cartService;

	@Value("${LOG_DIR}")
	private String logsDir;

	@PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registerUserAccount(@RequestBody @Valid User user, HttpServletRequest request) {
		try {
			LOGGER.debug("registerUserAccount :: user :: {}", user);
			return new ResponseEntity<>(new Gson().toJson(userService.register(user, request)), HttpStatus.OK);
		} catch (UserAlreadyExistsException e) {
			LOGGER.error("Exception at registerUserAccount");
			return new ResponseEntity<>(new Gson().toJson("Email already exists"), HttpStatus.OK);
		} catch (EmailAlreadyExistsException e) {
			LOGGER.error("Exception at registerUserAccount");
			return new ResponseEntity<>(new Gson().toJson("Email already exists"), HttpStatus.OK);
		} catch (ConstraintViolationException cvex) {
			return new ResponseEntity<>(new Gson().toJson(cvex.getConstraintViolations().stream()
					.map(ConstraintViolation::getPropertyPath).map(Path::toString).collect(Collectors.toList())),
					HttpStatus.OK);
		} catch (InvalidEmailException ex) {
			LOGGER.error("Exception at registerUserAccount :: Invalid Email");
			return new ResponseEntity<>(new Gson().toJson("Invalid Email"), HttpStatus.OK);
		} catch (InvalidPasswordException ex) {
			LOGGER.error("Exception at registerUserAccount :: Invalid password");
			return new ResponseEntity<>(new Gson().toJson("Invalid Password"), HttpStatus.OK);
		}
	}

	@PostMapping(value = "/register/seller", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registerSellerAccount(@RequestBody @Valid User user, HttpServletRequest request) {
		try {
			user.setRole("ROLE_SELLER");
			LOGGER.debug("registerUserAccount :: seller :: {}", user);
			return new ResponseEntity<>(new Gson().toJson(userService.register(user, request)), HttpStatus.OK);
		} catch (UserAlreadyExistsException e) {
			LOGGER.error("Exception at registerUserAccount");
			return new ResponseEntity<>(new Gson().toJson("Email already exists"), HttpStatus.OK);
		} catch (EmailAlreadyExistsException e) {
			LOGGER.error("Exception at registerUserAccount");
			return new ResponseEntity<>(new Gson().toJson("Email already exists"), HttpStatus.OK);
		} catch (ConstraintViolationException cvex) {
			return new ResponseEntity<>(new Gson().toJson(cvex.getConstraintViolations().stream()
					.map(ConstraintViolation::getPropertyPath).map(Path::toString).collect(Collectors.toList())),
					HttpStatus.OK);
		} catch (InvalidEmailException ex) {
			LOGGER.error("Exception at registerUserAccount :: Invalid Email");
			return new ResponseEntity<>(new Gson().toJson("Invalid Email"), HttpStatus.OK);
		} catch (InvalidPasswordException ex) {
			LOGGER.error("Exception at registerUserAccount :: Invalid password");
			return new ResponseEntity<>(new Gson().toJson("Invalid Password"), HttpStatus.OK);
		}
	}

	@GetMapping("/verify/{code}")
	public ModelAndView verifyUser(@PathVariable String code) {
		ModelAndView modelAndView = new ModelAndView();
		if (userService.verify(code)) {
			modelAndView.setViewName("user/verify_success");
		} else {
			modelAndView.setViewName("user/verify_fail");
		}

		return modelAndView;
	}

	@GetMapping("/account")
	public ModelAndView getAccountHomePage(Authentication authentication) {

		ModelAndView modelAndView = new ModelAndView();
		if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
			String email = ((UserDetailsImpl) authentication.getPrincipal()).getEmail();
			modelAndView.addObject("email", email);
		}
		modelAndView.setViewName("/user/account-home");
		return modelAndView;
	}

	@GetMapping("/orders")
	public ModelAndView getOrdersPage(Authentication authentication) {
		ModelAndView modelAndView = new ModelAndView();
		if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
			String email = ((UserDetailsImpl) authentication.getPrincipal()).getEmail();
			List<Orders> orders = userService.getOrders(email);
			modelAndView.addObject("orderItems", orders);
//			modelAndView.addObject("quantity", orders.stream()
//					.map(o-> o.getOrderItems()
//							.stream().reduce((x,y)-> x+y))
//					.reduce(null);
		}
		modelAndView.setViewName("/user/my_orders");
		return modelAndView;
	}

	@GetMapping("/profile")
	public ModelAndView getProfilePage(Authentication authentication) {

		ModelAndView modelAndView = new ModelAndView();
		if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
			String email = ((UserDetailsImpl) authentication.getPrincipal()).getEmail();
			modelAndView.addObject("email", email);
		}
		modelAndView.setViewName("/user/my_profile");
		return modelAndView;
	}

	@GetMapping("/wishlist")
	public ModelAndView getWishlistPage(Authentication authentication) {

		ModelAndView modelAndView = new ModelAndView();
		if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
			String email = ((UserDetailsImpl) authentication.getPrincipal()).getEmail();
			modelAndView.addObject("email", email);
			List<Wishlist> wishlistItems = userService.getWishlistItems(email);
			modelAndView.addObject("wishlistItems", wishlistItems);
			modelAndView.addObject("count", wishlistItems.size());
		}
		modelAndView.setViewName("user/my_wishlist");
		return modelAndView;
	}

	@PostMapping("/wishlist")
	public ResponseEntity<?> wishlistItems(Authentication authentication) {
		String email = "";
		if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
			email = ((UserDetailsImpl) authentication.getPrincipal()).getEmail();
		}

		return new ResponseEntity<>(userService.getWishlistItems(email), HttpStatus.OK);
	}

	@PostMapping("/addToWishlist")
	public ResponseEntity<?> addToWishlist(Authentication authentication, @RequestBody Map<String, String> attributeMap) {
		if (!Utility.isAuthenticated(authentication))
			return ResponseEntity.badRequest().body("UNAUTHORIZED");
		
		String productCode = attributeMap.get("productCode");
		Product product = productRepository.findById(Integer.parseInt(productCode)).orElse(null);
		
		User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
		
		if(wishlistRepository.findByUserAndProduct(user, product) != null)
			return ResponseEntity.ok().body("ALREADY_ADDED");
		
		Wishlist wishlist = new Wishlist();
		wishlist.setProduct(product);
		wishlist.setUser(user);
		wishlistRepository.saveAndFlush(wishlist);

		return ResponseEntity.ok(wishlist.getProduct().getProductName());
	}

	@PostMapping("/removeWishlistItem")
	public ResponseEntity<?> removeWishlistItem(Authentication authentication,
			@RequestBody Map<String, String> attributeMap) {
		if (!Utility.isAuthenticated(authentication))
			return ResponseEntity.badRequest().body("UNAUTHORIZED");
		
		String productId = attributeMap.get("productCode");
		User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
		
		return ResponseEntity.ok(userService.removeWishlistItem(user, productRepository.findById(Integer.parseInt(productId)).orElse(null)));
	}

	@PostMapping("/cart")
	public ResponseEntity<?> cartItems(Authentication authentication) {
		String email = "";
		if (!Utility.isAuthenticated(authentication))
			return ResponseEntity.badRequest().body("UNAUTHORIZED");

		email = ((UserDetailsImpl) authentication.getPrincipal()).getEmail();

		return ResponseEntity.ok().body(userService.getCartItems(email));
	}

	@GetMapping("/cart")
	public ModelAndView getCartPage(Authentication authentication) {

		ModelAndView modelAndView = new ModelAndView();
		if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
			String email = ((UserDetailsImpl) authentication.getPrincipal()).getEmail();
			modelAndView.addObject("email", email);
			Set<Items> cartItems = userService.getCartItems(email);
			modelAndView.addObject("cartItems", cartItems);
			if(cartItems.size() > 0) {
				modelAndView.addObject("count", cartItems.size());
				modelAndView.addObject("totalPrice",
						cartItems.stream().map(i -> (int) i.getProduct().getMrp()).reduce((x, y) -> x + y).get());
				modelAndView.addObject("discountedPrice",
						cartItems.stream()
								.map(i -> (int) (i.getProduct().getMrp()
										- (i.getProduct().getMrp() * i.getProduct().getDiscount() / 100)))
								.reduce((x, y) -> x + y).get());
			}
		}
		modelAndView.setViewName("user/my_cart");
		return modelAndView;
	}

	@PostMapping("/addToCart")
	public String addToCart(Authentication authentication, @RequestBody Map<String, String> attributeMap) {
		if (!Utility.isAuthenticated(authentication))
			return "UNAUTHORIZED";

		User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

		Cart cart = cartService.findByUser(user);
		String productCode = attributeMap.get("productCode");
		Product product = productRepository.findById(Integer.parseInt(productCode)).orElse(null);
		Items cartItem = new Items(product, 1);

		if (cart == null) {
			cart = new Cart(user, cartItem);
		} else if (cart.getItems().stream().anyMatch(c -> c.getProduct().equals(product))) {
			cart.getItems().stream().filter(cp -> cp.getProduct().equals(product))
					.forEach(cp -> cp.setQuantity(cp.getQuantity() + 1));
		} else {
			cart.getItems().add(cartItem);
		}

		cartService.addToCart(cart);
		cartItem.setCart(cart);
		itemsRepository.saveAndFlush(cartItem);

		return "ADDED_TO_CART";
	}

	// TODO:
	@PostMapping("/removeCartItem")
	public ResponseEntity<?> removeCartItem(Authentication authentication, @RequestBody Map<String, String> attributeMap) {
		if (!Utility.isAuthenticated(authentication))
			return ResponseEntity.badRequest().body("UNAUTHORIZED");

		User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

		String productId = attributeMap.get("productCode");
		
		return ResponseEntity.ok(userService.removeCartItem(user, productRepository.findById(Integer.parseInt(productId)).orElse(null)));
	}
	
	@GetMapping("/checkout")
	public ModelAndView getAddressPage(Authentication authentication) {

		ModelAndView modelAndView = new ModelAndView();
		if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
			String email = ((UserDetailsImpl) authentication.getPrincipal()).getEmail();
			modelAndView.addObject("email", email);
			Set<Items> cartItems = userService.getCartItems(email);
			modelAndView.addObject("cartItems", cartItems);
			if(cartItems.size() > 0) {
				modelAndView.addObject("count", cartItems.size());
				modelAndView.addObject("totalPrice",
						cartItems.stream().map(i -> (int) i.getProduct().getMrp()).reduce((x, y) -> x + y).get());
				modelAndView.addObject("discountedPrice",
						cartItems.stream()
								.map(i -> (int) (i.getProduct().getMrp()
										- (i.getProduct().getMrp() * i.getProduct().getDiscount() / 100)))
								.reduce((x, y) -> x + y).get());
			}
		}
		modelAndView.setViewName("user/checkout");
		return modelAndView;
	}

	@PostMapping("/addAddress")
	public ResponseEntity<?> addAddress(Authentication authentication, @RequestBody Map<String, String> attributeMap) {
		Address address = null;
		LOGGER.info("address:: " + attributeMap);
		String fullName = attributeMap.get("fullName");
		String addressLine1 = attributeMap.get("addressLine1");
		String addressLine2 = attributeMap.get("addressLine2");
		String landmark = attributeMap.get("landmark");
		String city = attributeMap.get("city");
		String state = attributeMap.get("state");
		String country = attributeMap.get("country");
		Integer pinCode = Integer.parseInt(attributeMap.get("pinCode"));
		Long mobileNumber = Long.parseLong(attributeMap.get("mobileNumber"));
		
		if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
			User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
			address = new Address(fullName, addressLine1, addressLine2, landmark, city, state, country, pinCode, mobileNumber, user);
		}

		return ResponseEntity.ok(userService.addAddress(address));
	}
	
	
	@PostMapping("/placeOrder")
	public ResponseEntity<?> placeOrder(Authentication authentication, @RequestBody Map<String, String> attributeMap) {
		LOGGER.info("address:: " + attributeMap);
		User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
		return ResponseEntity.ok(userService.placeOrder(attributeMap, user));
	}

//	@GetMapping("/checkout")
//	public ModelAndView getCheckoutPage(Authentication authentication) {
//
//		ModelAndView modelAndView = new ModelAndView();
//		if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
//			String email = ((UserDetailsImpl) authentication.getPrincipal()).getEmail();
//			modelAndView.addObject("email", email);
//			Set<Items> cartItems = userService.getCartItems(email);
//			modelAndView.addObject("cartItems", cartItems);
//			modelAndView.addObject("count", cartItems.size());
//			modelAndView.addObject("totalPrice",
//					cartItems.stream().map(i -> (int) i.getProduct().getMrp()).reduce((x, y) -> x + y).get());
//			modelAndView.addObject("discountedPrice",
//					cartItems.stream()
//							.map(i -> (int) (i.getProduct().getMrp()
//									- (i.getProduct().getMrp() * i.getProduct().getDiscount() / 100)))
//							.reduce((x, y) -> x + y).get());
//		}
//		modelAndView.setViewName("user/checkout");
//		return modelAndView;
//	}

	@GetMapping("/checkout/confirm/{orderId}")
	public ModelAndView getCheckoutConfirmPage(Authentication authentication, @PathVariable Long orderId) {

		ModelAndView modelAndView = new ModelAndView();
		if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
			String email = ((UserDetailsImpl) authentication.getPrincipal()).getEmail();
			modelAndView.addObject("email", email);
			Orders order = userService.getOrder(orderId);
			modelAndView.addObject("order", order);
		}
		modelAndView.setViewName("user/checkout_confirm");
		return modelAndView;
	}

	@GetMapping("/getLogs")
	public ResponseEntity<?> getLogs() {

		return ResponseEntity.ok(Utility.listFilesUsingJavaIO(logsDir).stream()
				.filter(f -> f.endsWith(".log") || f.endsWith(".html")).collect(Collectors.toSet()));
	}

	@GetMapping("/viewLog/{fileName:.+}")
	public ResponseEntity<Resource> viewLog(@PathVariable String fileName, HttpServletRequest request) {
		// Load file as Resource
		try {
			java.nio.file.Path filePath = Paths.get(logsDir).toAbsolutePath().normalize().resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());

			String contentType = null;
			try {
				contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
			} catch (IOException ex) {
				LOGGER.info("Could not determine file type.");
			}

			// Fallback to the default content type if type could not be determined
			if (contentType == null) {
				contentType = "application/octet-stream";
			}

			return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
					.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
					.body(resource);
		} catch (MalformedURLException e) {
			LOGGER.error("Error in getLogs :: fileName :: {} ", fileName, e);
		}

		return ResponseEntity.notFound().build();
	}

}
