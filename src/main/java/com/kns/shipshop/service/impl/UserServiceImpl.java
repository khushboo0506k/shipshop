package com.kns.shipshop.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kns.shipshop.constants.CommonConstants;
import com.kns.shipshop.exception.InvalidEmailException;
import com.kns.shipshop.exception.InvalidPasswordException;
import com.kns.shipshop.exception.InvalidUsernameException;
import com.kns.shipshop.exception.UserAlreadyExistsException;
import com.kns.shipshop.mail.CustomMailSender;
import com.kns.shipshop.persistence.entity.Address;
import com.kns.shipshop.persistence.entity.Cart;
import com.kns.shipshop.persistence.entity.Items;
import com.kns.shipshop.persistence.entity.OrderItems;
import com.kns.shipshop.persistence.entity.Orders;
import com.kns.shipshop.persistence.entity.Product;
import com.kns.shipshop.persistence.entity.User;
import com.kns.shipshop.persistence.entity.VerificationToken;
import com.kns.shipshop.persistence.entity.Wishlist;
import com.kns.shipshop.persistence.repository.AddressRepository;
import com.kns.shipshop.persistence.repository.OrderItemsRepository;
import com.kns.shipshop.persistence.repository.OrdersRepository;
import com.kns.shipshop.persistence.repository.UserRepository;
import com.kns.shipshop.persistence.repository.VerificationTokenRepository;
import com.kns.shipshop.persistence.repository.WishlistRepository;
import com.kns.shipshop.service.CartService;
import com.kns.shipshop.service.ProductService;
import com.kns.shipshop.service.UserService;
import com.kns.shipshop.utility.Utility;

import jakarta.servlet.http.HttpServletRequest;
import net.bytebuddy.utility.RandomString;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CustomMailSender mailSender;

	@Autowired
	protected AuthenticationManager authenticationManager;

	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	@Autowired
	private CartService cartService;

	@Autowired
	private WishlistRepository wishlistRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private OrdersRepository ordersRepository;
	
	@Autowired
	private OrderItemsRepository orderItemsRepository;
	
	@Autowired
	private ProductService productService;

	private final List<User> usersList = new ArrayList<>();

	@Override
	public String register(User user, HttpServletRequest request)
			throws UserAlreadyExistsException, InvalidUsernameException, InvalidPasswordException {
		if (user.getEmail() == null) {
			throw new InvalidEmailException("Invalid Email");
		}

		if (!user.getPassword().matches(CommonConstants.PASSWORD_REGEX)) {
			throw new InvalidPasswordException("Invalid password");
		}

		if (userRepo.findUserByEmail(user.getEmail()).orElse(null) != null) {
			LOGGER.info("User with email: {} already exists", user.getEmail());
			throw new UserAlreadyExistsException("User with email: " + user.getEmail() + " already exists");
		}

		if (Utility.isValidPwd(user.getPassword()))
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		else
			user.setPassword(null);

		userRepo.save(user);

//		authWithAuthManager(request, user.getEmail(), tempPass);

		sendVerificationEmail(user, user.getEmail(), Utility.getHostName(request));

		return "Success";
	}

	public void authWithAuthManager(HttpServletRequest request, String email, String password) {
		LOGGER.debug("Inside authWithAuthManager");
		LOGGER.debug("request: {}", request);
		try {
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
			authToken.setDetails(new WebAuthenticationDetails(request));

			Authentication authentication = authenticationManager.authenticate(authToken);

			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (Exception e) {
			LOGGER.error("Error occurred in authWithAuthManager: {}", e.getMessage(), e);
		}
	}

	@Override
	public void sendVerificationEmail(User user, String email, String hostName) {
		String randomCode = RandomString.make(64);

		VerificationToken token = new VerificationToken(randomCode, user);
		token.setEmail(email);
		verificationTokenRepository.save(token);

		String subject = "Please verify your email!";
		String content = "Dear [[name]],\n\n" + "Please click the link below to verify your email:\n\n" + "[[URL]] \n\n"
				+ "Thank you,\n" + "ShipShop.";
		content = content.replace("[[name]]", user.getFirstName());
		String verifyURL = "http://" + hostName + "/user/verify/" + randomCode;
		content = content.replace("[[URL]]", verifyURL);

		mailSender.sendVerificationMail(email, subject, content);
	}

	@Override
	public boolean verify(String verificationCode) {
		final VerificationToken token = verificationTokenRepository.findByToken(verificationCode);
		if (token == null) {
			return false;
		}
		LOGGER.debug("Token :: {}", token);
		final User user = token.getUser();
		final Calendar cal = Calendar.getInstance();
		if ((token.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			verificationTokenRepository.delete(token);
			return false;
		}
		LOGGER.debug("User :: {}", user);
		user.setEnabled(true);
		userRepo.save(user);

		verificationTokenRepository.delete(token);
		return true;
	}

	@Override
	public User udpateUser(User user) {

		return userRepo.saveAndFlush(user);
	}

	@Override
	public boolean changePassword(User user) throws InvalidPasswordException {
		if (!user.getPassword().matches(CommonConstants.PASSWORD_REGEX))
			return false;

		User foundUser = userRepo.findByUsername(user.getUsername()).orElse(null);
		if (foundUser == null)
			return false;

		if (Utility.isValidPwd(user.getPassword()))
			foundUser.setPassword(passwordEncoder.encode(user.getPassword()));
		else
			return false;

		userRepo.saveAndFlush(foundUser);
		return true;
	}

	@Override
	public String changePassword(String currentPassword, String newPassword, Long userId) {

		if (currentPassword.equals(newPassword))
			return "New Password is same as current password";

		String validity = Utility.checkValidPassword(newPassword);

		if (!validity.equalsIgnoreCase("VALID"))
			return validity;

		User user = userRepo.getReferenceById(userId);

		if (!(Utility.checkValidCharacters(currentPassword)
				&& passwordEncoder.matches(currentPassword, user.getPassword())))
			return "Current Password is not valid";

		user.setPassword(passwordEncoder.encode(newPassword));
		userRepo.saveAndFlush(user);

		return "SUCCESS";
	}

	@Override
	public String validateUsername(String username) {
		return (userRepo.findByUsername(username).orElse(null) == null) ? "FAILED" : "SUCCESS";
	}

	@Override
	public List<User> getAllUsers() {
		return userRepo.findAll();
	}

	public void initUserList(boolean refresh) {
		if (usersList.isEmpty()) {
			usersList.addAll(userRepo.findAll());
		} else if (refresh) {
			usersList.clear();
			usersList.addAll(userRepo.findAll());
		}
	}

	@Override
	public Page<User> findPaginatedUser(Pageable pageable, boolean refresh) {
		initUserList(refresh);
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		List<User> list = usersList.stream().skip(startItem).limit(pageSize).collect(Collectors.toList());

		Page<User> userPage = new PageImpl<User>(list, PageRequest.of(currentPage, pageSize), usersList.size());

		return userPage;
	}

	@Override
	public List<User> searchUserByUsernameKeyword(String keyword) {

		Optional<List<User>> result = userRepo.findFirst10ByUsernameContainingIgnoreCase(keyword);

		return result.orElse(null);
	}

	@Override
	public User getUser(String email) {

		return userRepo.findUserByEmail(email).orElse(null);
	}

	@Override
	public String validateEmail(String email) {

		return userRepo.findUserByEmail(email) != null ? "EXISTS" : "NOT_EXISTS";
	}

	@Override
	public User refund(String username, double amount, String code) {
		User user = null;
//		try {
//			int i = userRepo.addBalance(amount, username);
//			if(i == 1) {
//				user = userRepo.findByUsername(username).orElse(null);
//				user.setBalance(user.getBalance());
//			}
//		} catch (Exception ex) {
//			LOGGER.error("Couln't update user : " + ex);
//			return null;
//		}
		return user;
	}

	@Override
	public Set<Items> getCartItems(String email) {
		User user = userRepo.findUserByEmail(email).orElse(null);
		Cart cart = cartService.findByUser(user);
		return cart == null ? new HashSet<>() : cart.getItems();
	}

	@Override
	public List<Wishlist> getWishlistItems(String email) {
		return wishlistRepository.findByUser(userRepo.findUserByEmail(email).orElse(null));
	}

	@Override
	public Address addAddress(Address address) {

		return addressRepository.saveAndFlush(address);
	}

	@Override
	public Orders placeOrder(Map<String, String> attributeMap, User user) {
		String fullName = attributeMap.get("fullName");
		String addressLine1 = attributeMap.get("addressLine1");
		String addressLine2 = attributeMap.get("addressLine2");
		String landmark = attributeMap.get("landmark");
		String city = attributeMap.get("city");
		String state = attributeMap.get("state");
		String country = attributeMap.get("country");
		Integer pinCode = Integer.parseInt(attributeMap.get("pinCode"));
		Long mobileNumber = Long.parseLong(attributeMap.get("mobileNumber"));

		Address address = new Address(fullName, addressLine1, addressLine2, landmark, city, state, country, pinCode,
				mobileNumber, user);
		addAddress(address);
		Orders order = new Orders(user, address, null, null, false);
		Cart cart = cartService.findByUser(user);
		Set<OrderItems> orderItems = cart.getItems().stream().map(item -> new OrderItems(item.getProduct(), order, item.getQuantity()))
				.collect(Collectors.toSet());
		
		order.setOrderItems(orderItems);
		order.setAmount(orderItems.stream()
				.map(i -> (i.getProduct().getMrp() - (i.getProduct().getMrp() * i.getProduct().getDiscount()/100))*i.getQuantity())
				.reduce((x,y) -> x+y).get());
		
		ordersRepository.saveAndFlush(order);
		orderItemsRepository.saveAllAndFlush(orderItems);
		cartService.removeCart(cart);
		
		return order;
	}
	
	@Override
	public List<Orders> getOrders(String email) {
		User user = userRepo.findUserByEmail(email).orElse(null);
		return ordersRepository.findByUser(user);
	}

	@Override
	public Orders getOrder(Long orderId) {
		return ordersRepository.findById(orderId).orElse(null);
	}

	@Override
	public boolean removeCartItem(User user, Product product) {
		Cart cart = cartService.findByUser(user);
		cart.getItems().stream().filter(i -> product.equals(i.getProduct())).forEach(i -> {
			if (i.getQuantity() > 1) {
				i.setQuantity(i.getQuantity() - 1);
				cartService.updateCartItem(i);
			} else {
				cartService.removeCartItem(i);
			}
		});
		return true;
	}

	@Override
	public boolean removeWishlistItem(User user, Product product) {
		wishlistRepository.deleteByProductAndUser(product, user);
		return true;
	}

	@Override
	public List<Product> getProducts(User user) {
		
		return productService.getAllProducts(user);
	}
}
