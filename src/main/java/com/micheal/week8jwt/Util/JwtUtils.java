package com.micheal.week8jwt.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class JwtUtils {

    @Value("f09bbc3ffbf3d8b45e5f72d07eeeff5cf63ff88bbba0e7794b39a6cb7b9457d985106750eec64e8e84d9d8c2b915247f31516bec6d05e0a6cdcbecdcd36535c2")
    private String secretKey;

    private final Supplier<SecretKeySpec> getKey =() -> {
        Key key = Keys.hmacShaKeyFor(secretKey
                .getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(key.getEncoded(), key.getAlgorithm());
    };

    private final Supplier<Date> expirationTime = ()-> Date
            .from(LocalDateTime.now()
                    .plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant());
    private <T> T extractClaims(String token, Function<Claims, T> claimResolver){
        final Claims claims = Jwts.parser()
                .verifyWith(getKey.get())
                .build().parseSignedClaims(token).getPayload();
        return claimResolver.apply(claims);
    }
    public Function<String, String> extractUsername = token->
            extractClaims(token, Claims::getSubject);
    private final Function<String, Date> extractExpirationDate = token->
            extractClaims(token, Claims::getExpiration);

    public Function<String, Boolean> isTokenExpired = token->extractExpirationDate.apply(token)
            .after(new Date(System.currentTimeMillis()));

    public BiFunction<String, String, Boolean> isTokenValid = (token, username) ->
            isTokenExpired.apply(token)&&Objects.equals(extractUsername.apply(token), username);
    public Function<UserDetails, String> createJwt = userDetails -> {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .signWith(getKey.get())
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expirationTime.get())
                .compact();
    };

}

