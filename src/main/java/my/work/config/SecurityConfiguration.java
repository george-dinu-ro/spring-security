package my.work.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

	private final MyUserDetailService userDetailService;
	
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(registry -> 
					registry.requestMatchers("/home", "/register/**").permitAll()
							.requestMatchers("/user/**").hasRole("USER")
							.requestMatchers("/admin/**").hasRole("ADMIN")
							.anyRequest().authenticated())
				.formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
				.build();
	}

	@Bean
	AuthenticationProvider authenticationProvider() {
		var provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}
	
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
