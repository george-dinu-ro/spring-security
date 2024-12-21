package my.work.config;

import java.util.Objects;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import my.work.repository.MyUserRepository;

@Service
@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService {

	private final MyUserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var user = userRepository.findByUsername(username);

		if (user.isPresent()) {
			var userObj = user.get();

			return User.builder()
					.username(userObj.getUsername())
					.password(userObj.getPassword())
					.roles(getRoles(userObj.getRole()))
					.build();

		} else {
			throw new UsernameNotFoundException(username);
		}
	}

	private String[] getRoles(String role) {
		return Objects.isNull(role) ? new String[] { "USER" } : role.split(",");
	}

}
