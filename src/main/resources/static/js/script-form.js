$(document).ready(function(){

	$(document).on('focus', '.inputbox input[type=password]', function(){
		$(this).siblings(".password-icon").addClass("show-password")
		$(this).siblings(".password-icon").attr("name", "eye-outline")
	})

	$(document).on('blur', '.inputbox input[type=password]', function(){
		
	})

	$(document).on('click', '.show-password', function(){
		$(this).siblings("input[type=password]").attr("type", "text");
		$(this).addClass("hide-password");
		$(this).removeClass("show-password");
		$(this).attr("name", "eye-off-outline");
	})

	$(document).on('click', '.hide-password', function(){
		$(this).siblings("input[type=text]").attr("type", "password");
		$(this).addClass("show-password");
		$(this).removeClass("hide-password");
		$(this).attr("name", "eye-outline");
	})

})