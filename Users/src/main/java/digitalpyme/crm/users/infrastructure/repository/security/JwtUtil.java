package digitalpyme.crm.users.infrastructure.repository.security;

import lombok.extern.log4j.Log4j2;
import digitalpyme.crm.users.domain.User;
import digitalpyme.crm.users.application.rest.response.AuthResponse;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

import java.util.Base64;
import java.util.Date;

@Log4j2
public class JwtUtil {

    private static final String SecretKeyJWT = "eyJhbGciOiJIUzUxMiJ9eyJzdWIiOiJkaWVnb2hnOEBnbWFpbC5jb20iLCJpZCI6MSwibmFtZSI6IkFkbWluIiwic3VybmFt";
    private static final long ExpirationTimeJWT = 3600_000;

    public static AuthResponse generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + ExpirationTimeJWT);

        return new AuthResponse(Jwts.builder()
                .setSubject(user.getEmail())
                .claim("id", user.getIdUser())
                .claim("name", user.getName())
                .claim("surname", user.getSurname())
                .claim("isAdmin", user.getIsAdmin())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, getSecretKey())
                .compact(), "Bearer", ExpirationTimeJWT / 1000);
    }

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
}