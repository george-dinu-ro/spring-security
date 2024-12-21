package my.work.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContentController {

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
}
