$(document).ready(function(){

	$(document).on("click", ".add-to-wishlist", function(e){
		let productId = $(this).closest('.product').prop('id');
		addToWishlist(productId, function(){
			getWishlists();
		});
		
	})
	
	$(document).on("click", ".add-to-cart-btn", function(e){
		let productId = $(this).closest('.product').prop('id');
		addToCart(productId, function(){
			getCartItems();
		});
	})
	
	$(document).on("click", ".product-img", function(e){
		let productId = $(this).parents(".product").prop('id');
		window.location.href = '/product/' + productId;
	})
	
	$(document).on("click", ".checkout-btn", function(e){
		verifyAddressAndPlaceOrder(function(rtnMsg){
			console.log(rtnMsg);
			if(rtnMsg) {
				window.location.href = '/user/checkout/confirm/' + rtnMsg.orderId;
			}
		})
			
	})
	
	$(document).on("click", ".address-btn", function(e){
		window.location.href = '/user/checkout';
	})
	
	$(document).on("click", ".search-btn", function(e){
		e.preventDefault()
		let category = $(".category").val();
		let searchInput = $(".search-input").val();
		window.location.href = '/product/search/' + category + '?searchTerm=' + searchInput;
	})
	
	$(document).on("click", "#cartItemsList .itemContainer-base-closeIcon", function(e){
		e.preventDefault()
		console.log('clicked cart delete')
		let productId = $(this).parents(".itemContainer-base-itemMargin").prop('id');
		console.log(productId)
		removeFromCart(productId, function(data){
			console.log(data);
			if(data){
				window.location.reload();
			}
		})
		
	})
	
	$(document).on("click", ".index-wishListContainer .itemcard-removeIcon", function(e){
		e.preventDefault()
		console.log('clicked wishlist delete');
		let productId = $(this).parents(".itemcard-itemCard").prop('id');
		console.log(productId)
		removeFromWishlist(productId, function(data){
			console.log(data);
			if(data){
				window.location.reload();
			}
		})
	})
	
	getAllCategory();
	getWishlists();
	getCartItems();
})

function addToWishlist(productId, callBack) {
	$.ajax({
		url: "/user/addToWishlist",
		type: "POST",
		data: JSON.stringify({
			productCode : productId
		}),
		contentType: "application/json; charset=utf-8",
		success: function(data){
			
			if(data == "Already Added") {
				doAlert("error", data);
				return callBack(data);
			}
			
			doAlert("success", "Added to Wishlist");
			return callBack(data);
		},
		error: function(jqXHR, exception){
			console.log('Error occurred ' + exception);
			return callBack(data);
		}
	}).always(function(data){
		
	})
}

function addToCart(productId, callBack) {
	$.ajax({
		url: "/user/addToCart",
		type: "POST",
		data: JSON.stringify({
			productCode : productId
		}),
		contentType: "application/json; charset=utf-8",
		success: function(data){
			console.log('added to cart')
			if(data == "UNAUTHORIZED") {
				doAlert("error", "Unauthorized! Login Again");
				return callBack(data);
			}
			
			doAlert("success", "Added to Wishlist");
			return callBack(data);
		},
		error: function(jqXHR, exception){
			console.log('Error occurred ' + exception);
			return callBack(data);
		}
	}).always(function(data){
		
	})
}

function getAllCategory() {
	$.ajax({
		url: "/category/all",
		type: "GET",
		success: function(data){
			$(data).each(function(){
				$("select.category").append('<option value="' + this.id +'">' + this.name + '</option>')
			})
			
		},
		error: function(jqXHR, exception){
			console.log('Error occurred ' + exception);
		}
	}).always(function(data){
		
	})
}

function getWishlists() {
	$.ajax({
		url: "/user/wishlist",
		type: "POST",
		contentType: "application/json; charset=utf-8",
		success: function(data){
			if(data.length > 0) {
				$(".wishlist-qty").text(data.length);
				$(".wishlist-qty").removeClass('hidden');
			}
		},
		error: function(jqXHR, exception){
			console.log('Error occurred ' + exception);
		}
	}).always(function(data){
		
	})
}


function getCartItems() {
	$.ajax({
		url: "/user/cart",
		type: "POST",
		contentType: "application/json; charset=utf-8",
		success: function(data){
			if(data == 'UNAUTHORIZED')
				return;
			if(data.length > 0) {
				$(".cart-qty").text(data.length);
				$(".cart-qty").removeClass('hidden');
				$(data).each(function(){
					$('.cart-list').empty();
					$('.cart-list').append(createCartItems(this));
				});
				$('.carts > .cart-summary').empty();
				$('.carts > .cart-summary').append(createCartSummary(data));
			}
		},
		error: function(jqXHR, exception){
			console.log('Error occurred ' + exception);
		}
	}).always(function(data){
		
	})
}

function removeFromCart(productId, callBack) {
	$.ajax({
		url: "/user/removeCartItem",
		type: "POST",
		data: JSON.stringify({
			productCode : productId
		}),
		contentType: "application/json; charset=utf-8",
		success: function(data){
			console.log('added to cart')
			if(data == "UNAUTHORIZED") {
				doAlert("error", "Unauthorized! Login Again");
				return callBack(data);
			}
			
			doAlert("success", "Added to Wishlist");
			return callBack(data);
		},
		error: function(jqXHR, exception){
			console.log('Error occurred ' + exception);
			return callBack(data);
		}
	}).always(function(data){
		
	})
}

function removeFromWishlist(productId, callBack) {
	$.ajax({
		url: "/user/removeWishlistItem",
		type: "POST",
		data: JSON.stringify({
			productCode : productId
		}),
		contentType: "application/json; charset=utf-8",
		success: function(data){
			console.log('removed from wishlist')
			if(data == "UNAUTHORIZED") {
				doAlert("error", "Unauthorized! Login Again");
				return callBack(data);
			}
			
			doAlert("success", "Added to Wishlist");
			return callBack(data);
		},
		error: function(jqXHR, exception){
			console.log('Error occurred ' + exception);
			return callBack(data);
		}
	}).always(function(data){
		
	})
}

function createCartItems(cart){
	let cartList = '<div class="product-widget"><div class="product-img">'
					+ '<img src="' + '/img/no-image.jpg' + '" alt="">'
					+ '</div><div class="product-body">'
					+ '<h3 class="product-name">'
					+ '<a href="/product/' + cart.product.productCode + '">' + cart.product.productName + '</a>'
					+ '</h3><h4 class="product-price"><span class="qty">' + cart.quantity + 'x</span>₹'
					+ (cart.product.mrp - (cart.product.mrp*cart.product.discount/100)) * cart.quantity + '</h4></div>'
					+ '<button class="delete"><i class="fa fa-close"></i></button></div>'
	
	return cartList;
}

function createCartSummary(cart){
	let cartSummary = '<small>' + cart.length + ' Item' + (cart.length > 1 ? '(s)' : '') + ' selected</small>'
						+ '<h5>SUBTOTAL: ₹' + getTotal(cart) + '</h5>'
	
	return cartSummary;
}

function getTotal(cart) {
	let total = 0;
	$(cart).each(function(){
		total += (this.product.mrp - (this.product.mrp*this.product.discount/100)) * this.quantity;
	})
	return total;
}

function verifyAddressAndPlaceOrder(callBack) {
	let shippingDetails = $(".shipping-details");
	let fullName = $(shippingDetails).find('#fullName').val();
	let address1 = $(shippingDetails).find('#address1').val();
	let address2 = $(shippingDetails).find('#address2').val();
	let landmark = $(shippingDetails).find('#landmark').val();
	let city = $(shippingDetails).find('#city').val();
	let state = $(shippingDetails).find('#state').val();
	let country = $(shippingDetails).find('#country').val();
	let pinCode = $(shippingDetails).find('#pinCode').val();
	let mobile = $(shippingDetails).find('#mobile').val();
	
	///addAddress(fullName, address1, address2, landmark, city, state, country, pinCode, mobile, callBack);
	placeOrder(fullName, address1, address2, landmark, city, state, country, pinCode, mobile, callBack);
}

function addAddress(fullName, address1, address2, landmark, city,
			state, country, pinCode, mobile, callBack) {
	
	$.ajax({
		url: "/user/addAddress",
		type: "POST",
		data: JSON.stringify({
			fullName : fullName,
			addressLine1 : address1,
			addressLine2 : address2,
			landmark : landmark,
			city : city,
			state : state,
			country : country,
			pinCode : pinCode,
			mobileNumber : mobile
		}),
		dataType: "json",
		contentType: "application/json; charset=utf-8",
		success: function(data){
			console.log(data)
			if(data == "UNAUTHORIZED") {
				doAlert("error", "Unauthorized! Login Again");
				return callBack(false);
			}
		},
		error: function(jqXHR, exception){
			console.log('Error occurred ' + exception);
		}
	}).always(function(data){
		
	})
	
	return callBack(false);
}

function placeOrder(fullName, address1, address2, landmark, city, state, country, pinCode, mobile, callBack) {
	$.ajax({
		url: "/user/placeOrder",
		type: "POST",
		data: JSON.stringify({
			fullName : fullName,
			addressLine1 : address1,
			addressLine2 : address2,
			landmark : landmark,
			city : city,
			state : state,
			country : country,
			pinCode : pinCode,
			mobileNumber : mobile
		}),
		dataType: "json",
		contentType: "application/json; charset=utf-8",
		success: function(data){
			console.log(data)
			if(data == "UNAUTHORIZED") {
				doAlert("error", "Unauthorized! Login Again");
				return callBack(false);
			}
			
			callBack(data)
		},
		error: function(jqXHR, exception){
			console.log('Error occurred ' + exception);
		}
	}).always(function(data){
		
	})
	
	return callBack(false);

}

