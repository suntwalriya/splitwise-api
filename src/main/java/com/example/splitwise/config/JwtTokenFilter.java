package com.example.splitwise.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    //TODO::this is next steps to applu auth to every internal API that will make sure that the user is logged in before making any API calls except register, login, healthCheck API
    @Value("${jwt.secret.key}")
    private String secretKey;

    private final List<String> excludedEndpoints;

    public JwtTokenFilter(String... excludedEndpoints) {
        this.excludedEndpoints = Arrays.asList(excludedEndpoints);
    }

    // TODO: Ensure this key is configured differently in each environment (dev, staging, production).
    public String createToken(String username) {
        // Use jwtSignatureKey to create the token
        return username;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        String requestPath = request.getServletPath();

        if (excludedEndpoints.stream().anyMatch(requestPath::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        log.debug("authorizationHeader: {}", authorizationHeader);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.replace("Bearer ", "");
            log.debug("token: {}", token);
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(secretKey)  // Ensure this key is not null or empty
                        .parseClaimsJws(token)
                        .getBody();

                String tokenUserId = claims.getSubject();
                log.debug("Decoded User ID from JWT: {}", tokenUserId);

                filterChain.doFilter(request, response);
            } catch (SignatureException e) {
                log.error("Invalid JWT signature: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid JWT token");
            } catch (Exception e) {
                log.error("JWT token processing failed: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("JWT token processing failed");
            }
        } else {
            log.error("Authorization header is missing or does not start with 'Bearer '");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or malformed token");
        }
    }

}
