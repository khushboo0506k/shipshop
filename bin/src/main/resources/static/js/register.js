$(document).ready(function() {

	$(document).on("click", "#show-password", function(){
		$(this).is(":checked") ? $("#password, #repeatPassword").attr("type", "text") : $("#password, #repeatPassword").attr("type", "password");
	})
	
	$(document).on("keyup", "#repeatPassword", function(event){
		if($(this).val().length > $("#password").val().length){
			$(".error-password").text("Password does not match");
		} else {
			$(".error-password").empty();
		}
	
		if($(this).val() != $("#password").val().substring(0, $(this).val().length)){
			$("#repeatPassword").addClass("is-invalid");
		} else {
			$("#repeatPassword").removeClass("is-invalid");
		}
			
	})
	
	
	$(document).on("click", ".register-btn", function(e){
		e.preventDefault();
		clearInvalid();
		let email = $("#email").val();
		let password = $("#password").val();
		let firstName = $("#first-name").val();
		let lastName = $("#last-name").val();
		
		let repeatPassword = $("#repeatPassword").val();
		
		if(email.length < 8) {
			doAlert('error', 'Please Enter Valid Email');
			$("#email").focus();
			$("#email").addClass("is-invalid");
			return;
		}
		
		if(password.length == 0){
			doAlert('error', 'Please Enter Password');
			$("#password").focus();
			$("#password").addClass("is-invalid");
			return;
		}
		
		if(password != repeatPassword) {
			$("#repeatPassword").addClass("is-invalid");
			doAlert('error', 'Passwords does not match');
			$("#repeatPassword").focus();
			$("#repeatPassword").addClass("is-invalid");
			return;
		}
		
		doRegister(email, password, firstName, lastName);
	})
	
	
	$(document).on("click", ".seller-register-btn", function(e){
		e.preventDefault();
		clearInvalid();
		let email = $("#email").val();
		let password = $("#password").val();
		let firstName = $("#first-name").val();
		let lastName = $("#last-name").val();
		
		let repeatPassword = $("#repeatPassword").val();
		
		if(email.length < 8) {
			doAlert('error', 'Please Enter Valid Email');
			$("#email").focus();
			$("#email").addClass("is-invalid");
			return;
		}
		
		if(password.length == 0){
			doAlert('error', 'Please Enter Password');
			$("#password").focus();
			$("#password").addClass("is-invalid");
			return;
		}
		
		if(password != repeatPassword) {
			$("#repeatPassword").addClass("is-invalid");
			doAlert('error', 'Passwords does not match');
			$("#repeatPassword").focus();
			$("#repeatPassword").addClass("is-invalid");
			return;
		}
		
		doRegisterSeller(email, password, firstName, lastName);
	})
	
});


function doRegister(email, password, firstName, lastName) {
	$.ajax({
		url: "/user/register",
		type: "POST",
		data: JSON.stringify({
			email : email,
			password : password,
			firstName: firstName,
			lastName: lastName
		}),
		dataType: "json",
		contentType: "application/json; charset=utf-8",
		beforeSend: function(){
			clearForm();
			$(".register-btn").addClass("disabled");
		},
		success: function(data){
			console.log(data)
			if(data == "Success"){
				doAlert("success", "Successfully registered. Redirecting to Login Page now.");
				disableRegistrationForm();
				setTimeout(redirectToLoginPage, 1500);
				return;
			}
			
			if(data == "Email already exists") {
				doAlert("error", data);
				return;
			}
			
			if(data == "Invalid Email") {
				doAlert("error", "Invalid Email. Check Email and try again.")
				return;
			}
			
			if(data == "Invalid Password") {
				doAlert("error", "Invalid Password. Password should be 6 to 16 characters long. Must contain at least one letter and one number.")
				return;
			}
			var msg = "Invalid ";
			$(data).each(function(index, value){
				if(index == 0)
					msg = msg.concat(value);
				else
					msg = msg.concat(", " + value);
			})
			doAlert("error", msg);
		},
		error: function(jqXHR, exception){
			console.log('Error occurred ' + exception);
		}
	}).always(function(data){
		$(".register-btn").removeClass("disabled");
	})
}


function doRegisterSeller(email, password, firstName, lastName) {
	$.ajax({
		url: "/user/register/seller",
		type: "POST",
		data: JSON.stringify({
			email : email,
			password : password,
			firstName: firstName,
			lastName: lastName
		}),
		dataType: "json",
		contentType: "application/json; charset=utf-8",
		beforeSend: function(){
			clearForm();
			$(".seller-register-btn").addClass("disabled");
		},
		success: function(data){
			console.log(data)
			if(data == "Success"){
				doAlert("success", "Successfully registered. Redirecting to Login Page now.");
				disableRegistrationForm();
				setTimeout(redirectToLoginPage, 1500);
				return;
			}
			
			if(data == "Email already exists") {
				doAlert("error", data);
				return;
			}
			
			if(data == "Invalid Email") {
				doAlert("error", "Invalid Email. Check Email and try again.")
				return;
			}
			
			if(data == "Invalid Password") {
				doAlert("error", "Invalid Password. Password should be 6 to 16 characters long. Must contain at least one letter and one number.")
				return;
			}
			var msg = "Invalid ";
			$(data).each(function(index, value){
				if(index == 0)
					msg = msg.concat(value);
				else
					msg = msg.concat(", " + value);
			})
			doAlert("error", msg);
		},
		error: function(jqXHR, exception){
			console.log('Error occurred ' + exception);
		}
	}).always(function(data){
		$(".register-btn").removeClass("disabled");
	})
}

function clearInvalid(){
	$("form input").each(function(){
		$(this).removeClass("is-invalid");
	})
}

function redirectToLoginPage(){
	window.location.href = "/login";
}

function disableRegistrationForm(){
	$("#userForm :input").prop("disabled", true);
}