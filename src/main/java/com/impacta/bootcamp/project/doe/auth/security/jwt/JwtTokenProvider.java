package com.impacta.bootcamp.project.doe.auth.security.jwt;

import com.impacta.bootcamp.project.doe.auth.exceptions.InvalidJWTAuthenticationException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey = "secret";
    @Value("${security.jwt.token.expire-lenght:3600000}")
    private long validityInMiliseconds = 3600000;

    @Autowired
    private UserDetailsService userDetailsService;
    @PostConstruct
    public void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());

    }
    public String createToken(String username,Long userId, List<String> roles)
    {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles",roles);
        claims.put("userId",userId);
        Date now = new Date();
        Date validity = new Date(now.getTime()+ validityInMiliseconds);


        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUserName(token));
        return  new UsernamePasswordAuthenticationToken(userDetails,userDetails.getAuthorities());
    }
    public UserDetails getUserDetails(String token) {
            return this.userDetailsService.loadUserByUsername(getUserName(token));
    }

    private String getUserName(String token) {

        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
    public String resolveToken(HttpServletRequest req)
    {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;

    }
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJWTAuthenticationException("Expired or invalid JWT token");
        }
    }
}
