package my.work;

import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;

class JwtSecretMakerTest {

	@Test
	void generateSecretKey() {
		var key = Jwts.SIG.HS512.key().build();
		var encodedKey = DatatypeConverter.printHexBinary(key.getEncoded());
		System.out.println(encodedKey);
	}
}
