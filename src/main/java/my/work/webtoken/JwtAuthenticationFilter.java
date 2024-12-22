package my.work.webtoken;

import java.io.IOException;
import java.util.Objects;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import my.work.config.MyUserDetailService;

@Configuration
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;

	private final MyUserDetailService userDetailService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		var authHeader = request.getHeader("Authorization");

		if (!bearerIsPresent(authHeader)) {
			filterChain.doFilter(request, response);
			return;
		}

		var jwt = authHeader.substring(7);

		processToken(jwt, request, response, filterChain);
	}

	private boolean bearerIsPresent(String authHeader) {
		return (Objects.nonNull(authHeader) && authHeader.startsWith("Bearer "));
	}

	private void processToken(String jwt, HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {

		var username = jwtService.getUsername(jwt);

		if (shouldAuthenticate(username)) {
			validateUserAndToken(username, jwt, request);
		}

		filterChain.doFilter(request, response);
	}

	private void validateUserAndToken(String username, String jwt, HttpServletRequest request) {
		var userDetails = userDetailService.loadUserByUsername(username);

		if (mayAuthenticate(userDetails, jwt)) {
			doAuthentication(userDetails, request);
		}
	}

	private boolean shouldAuthenticate(String username) {
		return (Objects.nonNull(username) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication()));
	}

	private boolean mayAuthenticate(UserDetails userDetails, String jwt) {
		return (Objects.nonNull(userDetails) && jwtService.isValidToken(jwt));
	}

	private void doAuthentication(UserDetails userDetails, HttpServletRequest request) {
		var authenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
				userDetails.getPassword(), userDetails.getAuthorities());

		authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	}
}
