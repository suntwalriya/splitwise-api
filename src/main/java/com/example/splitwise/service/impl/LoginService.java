package com.example.splitwise.service.impl;

import com.example.splitwise.entities.request.LoginRequest;
import com.example.splitwise.exception.InvalidPasswordException;
import com.example.splitwise.exception.UserNotFoundException;
import com.example.splitwise.repository.dao.IUserDAO;
import com.example.splitwise.repository.table.User;
import com.example.splitwise.service.ILoginService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class LoginService implements ILoginService {
    @Autowired
    private IUserDAO iUserDAO;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    //TODO:: use the access token and secret key for signing for auth filter

    @Value("${jwt.access.token.expiry}")
    private long accessTokenExpiry;

    @Value("${jwt.secret.key}")
    private String secretKey;

    // TODO: Move register and login functionality to a separate auth service like Keycloak or OAuth.

    public Map<String, Object> loginUser(LoginRequest loginRequest) {
        Optional<User> userOptional = iUserDAO.findByUsername(loginRequest.getUsername());

        // Step 1. Fetch the user who is creating the group
        if (!userOptional.isPresent()) {
            log.debug("User not found for userId: {}", userOptional.get().getId());
            throw new UserNotFoundException("Invalid credentials");
        }

        User user = userOptional.get();

        // Step 2. Check if the user is active
        if (Boolean.FALSE.equals(user.getIsActive())) {
            user.setIsActive(Boolean.TRUE);
            iUserDAO.save(user);
        }

        // Step 3. Check if the login password and user password matches
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.debug("Invalid password: {}", loginRequest.getPassword());
            throw new InvalidPasswordException("Invalid credentials");
        }

        // Step 4. Compute a JWT auth token for a userName and configurable expiration
        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiry)) //10 hours expiry in ms
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        // TODO:: Generate Refresh Token (with a longer expiration time)

        // Step 5. Prepare the response map
        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", token);

        return response;
    }
}
