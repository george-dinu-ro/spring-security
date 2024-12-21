package my.work.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
				.authorizeHttpRequests(registry -> 
					registry.requestMatchers("/home").permitAll()
							.requestMatchers("/user/**").hasRole("USER")
							.requestMatchers("/admin/**").hasRole("ADMIN")
							.anyRequest().authenticated())
				.formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
				.build();
	}

	@Bean
	UserDetailsService userDetailsService() {
		var user = User.builder()
				.username("user")
				.password("$2y$10$XwwiJESsXAJoe5NAxYpPCujO7YfEDeIGXKnlhhkHOuJ/aMV.XLAf6")
				.roles("USER")
				.build();
		
		var admin = User.builder()
				.username("admin")
				.password("$2y$10$0MOftkvrUyLmV4WKAxiGbOlI9Qh7dL.Hfa4SR0MzDasMU0cv6q1yG")
				.roles("USER", "ADMIN")
				.build();

		return new InMemoryUserDetailsManager(user, admin);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
