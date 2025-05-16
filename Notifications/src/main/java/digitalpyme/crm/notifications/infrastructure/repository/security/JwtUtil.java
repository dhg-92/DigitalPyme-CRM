package digitalpyme.crm.notifications.infrastructure.repository.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Log4j2
public class JwtUtil {

    private static final String SecretKeyJWT = "eyJhbGciOiJIUzUxMiJ9eyJzdWIiOiJkaWVnb2hnOEBnbWFpbC5jb20iLCJpZCI6MSwibmFtZSI6IkFkbWluIiwic3VybmFt";
    private static final long ExpirationTimeJWT = 3600_000;

    private static SecretKey getSecretKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SecretKeyJWT);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(getSecretKey()).build().parseClaimsJws(token);

            return true;
        } catch (Exception e) {

            return false;
        }
    }

    public static User extractUsername(String token) {
        if (validateToken(token)) {
            Claims claims = Jwts.parser()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            User user = new User();
            user.setIdUser(claims.get("id", Long.class));
            user.setName(claims.get("name", String.class));
            user.setSurname(claims.get("surname", String.class));
            user.setIsAdmin(claims.get("isAdmin", Boolean.class));
            user.setEmail(claims.getSubject());
            return user;
        } else {
            return null;
        }
    }

    public static String generateToken() {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + ExpirationTimeJWT);

        return Jwts.builder()
                .setSubject("admin@admin.com")
                .claim("id", 1)
                .claim("name", "Admin")
                .claim("surname", "Admin")
                .claim("isAdmin", false)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, getSecretKey())
                .compact();
    }
}