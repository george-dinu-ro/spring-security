package my.work.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import my.work.model.MyUser;
import my.work.repository.MyUserRepository;

@RequestMapping("/register")
@RestController
@RequiredArgsConstructor
public class RegistrationController {

	private final MyUserRepository myUserRepository;

	private final PasswordEncoder passwordEncoder;

	@PostMapping("/users")
	MyUser registerUser(@RequestBody MyUser user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		return myUserRepository.save(user);
	}
}
