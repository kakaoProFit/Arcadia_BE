package profit.login.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenProvider {

    @Value("${security.jwt.expiration-time}")
    private long ACCESS_TOKEN_EXPIRE_TIME_IN_MILLISECONDS;

    @Value("${security.jwt.refresh-expiration-time}")
    private long REFRESH_TOKEN_EXPIRE_TIME_IN_MILLISECONDS;

    @Value("${security.jwt.secret-key-encoding-base64}")
    private String secretKey;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            log.info("JWT token is valid");
            return true;
        } catch (UnsupportedJwtException | MalformedJwtException exception) {
            log.error("JWT is not valid");
        } catch (SignatureException exception) {
            log.error("JWT signature validation fails");
        } catch (ExpiredJwtException exception) {
            log.error("JWT is expired");
        } catch (IllegalArgumentException exception) {
            log.error("JWT is null or empty or only whitespace");
        } catch (Exception exception) {
            log.error("JWT validation fails", exception);
        }
        return false;
    }

    public String createToken(Authentication authentication) {
        Date date = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME_IN_MILLISECONDS);

        String token = Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(date)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        log.info("JWT token created: {}", token);
        return token;
    }

    public String createRefreshToken(Authentication authentication) {
        Date date = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME_IN_MILLISECONDS);

        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(date)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        log.info("Refresh token created: {}", refreshToken);
        return refreshToken;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        UserDetails user = new User(claims.getSubject(), "", Collections.emptyList());
        return new UsernamePasswordAuthenticationToken(user, "", Collections.emptyList());
    }
}
