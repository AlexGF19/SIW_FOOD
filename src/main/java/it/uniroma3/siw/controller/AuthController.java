package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.configuration.AuthConfiguration;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Cook;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.validation.CredentialsValidator;
import it.uniroma3.siw.service.CookService;
import jakarta.validation.Valid;

@Controller
public class AuthController {
	@Autowired CredentialsService credentialsService;
	@Autowired CookService userService;
	@Autowired CredentialsValidator credentialsValidator;
	
	@GetMapping("/register")
	public String registerForm(Model model) {
		model.addAttribute("credentials", new Credentials());
		model.addAttribute("cook", new Cook());
		return "registerForm.html";
	}
	
	@PostMapping("/register")
	public String newUserRegisterForm(@Valid @ModelAttribute("credentials") Credentials credentials, BindingResult bindingResult, @Valid @ModelAttribute("cook") Cook cook, Model model) {
		this.credentialsValidator.validate(credentials, bindingResult);
		if(bindingResult.hasErrors()) {
			return "registerForm.html";
		}else {
			PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String password = credentials.getPassword();
			credentials.setPassword(passwordEncoder.encode(password));
			credentials.setCook(cook);
			cook.setCredentials(credentials);
			this.credentialsService.save(credentials);
			return "redirect:/login";
		}
	}
	
	
	@GetMapping("/login")
	public String getLoginForm(Model model) {
		model.addAttribute("credentials", new Credentials());
		return "loginForm.html";
	}
	
	@PostMapping("/login")
	public void login() {}
	
	@GetMapping("/loginError")
	public String getLoginFormError(@ModelAttribute("credentials") Credentials credentials, BindingResult bindingResult) {
		bindingResult.reject("wrongCredentials");
		return "loginForm.html";
	}
	
	@GetMapping("/logout")
	public void logout() {}
	
	@GetMapping("/loginRedirection")
	public String loginRedirection(@ModelAttribute("userDetails") UserDetails userDetails) {
		Credentials credentials = this.credentialsService.getCredentials(userDetails.getUsername());
		String role = credentials.getRole();
		if(role!=null && role.equals(AuthConfiguration.ADMIN_ROLE))
			return "redirect:/admin/";
		else
			return "redirect:/";
	}
	
	@GetMapping("/profile/{username}")
	public String getProfile(@PathVariable("username") String username, Model model) {
		Cook cook = this.credentialsService.getCredentials(username).getCook();
		model.addAttribute("cook", cook);
		return "profile.html";
	}
	
	@GetMapping("/admin/")
	public String getIndexAdmin() {
		return "indexAdmin.html";
	}
	
	@GetMapping("/")
	public String getIndex(@ModelAttribute("userDetails") UserDetails userDetails) {
		if(userDetails!=null) {
			Credentials credentials = this.credentialsService.getCredentials(userDetails.getUsername());
			String role = credentials.getRole();
			if(role!=null && role.equals(AuthConfiguration.ADMIN_ROLE))
				return "redirect:/admin/";
		}
		return "index.html";
	}

}
