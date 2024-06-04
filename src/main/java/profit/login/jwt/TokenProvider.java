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
import profit.login.oauth2.service.OAuth2UserPrincipal;

import java.security.Key;
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

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    private Key key;

    @PostConstruct
    public void init() {
        // Base64 디코딩된 비밀 키를 사용하여 Key 객체를 생성합니다.
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // JWT 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            log.info("JWT token is valid");
            return true;
        } catch (UnsupportedJwtException | MalformedJwtException exception) {
            log.error("JWT is not valid: {}", exception.getMessage());
        } catch (SignatureException exception) {
            log.error("JWT signature validation fails: {}", exception.getMessage());
        } catch (ExpiredJwtException exception) {
            log.error("JWT is expired: {}", exception.getMessage());
        } catch (IllegalArgumentException exception) {
            log.error("JWT is null or empty or only whitespace: {}", exception.getMessage());
        } catch (Exception exception) {
            log.error("JWT validation fails", exception);
        }
        return false;
    }

    // 액세스 토큰 생성
    public String createToken(Authentication authentication) {
        Date date = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME_IN_MILLISECONDS);


        String token = Jwts.builder()
                .setSubject("더미더미더미")
                .setIssuedAt(date)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        log.info("JWT token created: {}", token);
        return token;
    }

    // 리프레시 토큰 생성
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

    // 인증 정보 추출
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
