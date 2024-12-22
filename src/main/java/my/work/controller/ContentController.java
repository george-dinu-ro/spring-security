package my.work.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import my.work.webtoken.JwtService;
import my.work.webtoken.LoginForm;

@RestController
@RequiredArgsConstructor
public class ContentController {

	private final AuthenticationManager authenticationManager;

	private final JwtService jwtService;

	@GetMapping("/home")
	String getHomePage() {
		return "home";
	}

	@GetMapping("/user/home")
	String getUserHomePage() {
		return "user-home";
	}

	@GetMapping("/admin/home")
	String getAdminHomePage() {
		return "admin-home";
	}

	@PostMapping("/authenticate")
	String getToken(@RequestBody LoginForm loginForm) {
		var authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginForm.username(), loginForm.password()));

		if (authentication.isAuthenticated()) {
			return jwtService.generateToken((UserDetails) authentication.getPrincipal());

		} else {
			throw new UsernameNotFoundException("Invalid credentials");
		}
	}
}
