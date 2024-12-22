package my.work.webtoken;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("${secretKey.value}")
	private String secretKey;

	@Value("${secretKey.validity}")
	private int secretKeyValidity;

	public String generateToken(UserDetails userDetails) {
		var claims = new HashMap<String, String>();
		claims.put("iss", "http://my-url");
		claims.put("name", "my-name");

		return Jwts.builder()
				.subject(userDetails.getUsername())
				.issuedAt(Date.from(Instant.now()))
				.expiration(Date.from(Instant.now().plusMillis(getSecretKeyValidity())))
				.claims(claims)
				.signWith(getSecretKey())
				.compact();
	}

	public String getUsername(String jwt) {
		var claims = getClaims(jwt);
		return claims.getSubject();
	}
	
	public boolean isValidToken(String jwt) {
		var claims = getClaims(jwt);
		return claims.getExpiration().after(Date.from(Instant.now()));
	}

	private long getSecretKeyValidity() {
		return TimeUnit.MINUTES.toMillis(secretKeyValidity);
	}

	private SecretKey getSecretKey() {
		var decodedKey = Base64.getDecoder().decode(secretKey);
		return Keys.hmacShaKeyFor(decodedKey);
	}
	
	private Claims getClaims(String jwt) {
		return Jwts.parser()
				.verifyWith(getSecretKey())
				.build()
				.parseSignedClaims(jwt)
				.getPayload();
	}
}
