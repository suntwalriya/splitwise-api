package com.example.splitwise.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {

    //TODO::this is next steps to applu auth to every internal API that will make sure that the user is logged in before making any API calls.
    private final String secretKey = "myVeryStrongSecretKeyThatIsLongAndRandom";

    @Value("${jwt.signature.key}")
    private String jwtSignatureKey;

    // TODO: Ensure this key is configured differently in each environment (dev, staging, production).
    public String createToken(String username) {
        // Use jwtSignatureKey to create the token
        return username;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7);

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            request.setAttribute("claims", claims);
        } catch (Exception e) {
            // If the token is invalid or expired, we can return an unauthorized response
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
            return;
        }

        // Continue with the filter chain if the token is valid
        filterChain.doFilter(request, response);
    }

}
