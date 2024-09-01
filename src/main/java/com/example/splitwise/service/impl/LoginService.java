package com.example.splitwise.service.impl;

import com.example.splitwise.entities.request.LoginRequest;
import com.example.splitwise.exception.InvalidPasswordException;
import com.example.splitwise.exception.UserNotFoundException;
import com.example.splitwise.repository.dao.IUserDAO;
import com.example.splitwise.repository.table.User;
import com.example.splitwise.service.ILoginService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class LoginService implements ILoginService {
    @Autowired
    private IUserDAO iUserDAO;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Map<String, Object> loginUser(LoginRequest loginRequest) {
        Optional<User> userOptional = iUserDAO.findByUsername(loginRequest.getUsername());

        // Step 1. Fetch the user who is creating the group
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("User not found");
        }

        User user = userOptional.get();

        // Step 2. Check if the user is active
        if (Boolean.FALSE.equals(user.getIsActive())) {
            user.setIsActive(Boolean.TRUE);
            iUserDAO.save(user);
        }

        // Step 3. Check if the login password and user password matches
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Invalid password");
        }

        // Step 4. Compute a JWT auth token for a userName and 10 hours expiration (To be used in other methods)
        String token = Jwts.builder()
                    .setSubject(user.getUsername())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours expiration
                    .signWith(SignatureAlgorithm.HS256, "myVeryStrongSecretKeyThatIsLongAndRandom")
                    .compact();

        // Step 5. Generate Refresh Token (with a longer expiration time)
        String refreshToken = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 30)) // 30 days expiration
                .signWith(SignatureAlgorithm.HS256, "myVeryStrongSecretKeyThatIsLongAndRandom")
                .compact();

        // Step 6. Prepare the response map
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Logged in successfully");
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("id", user.getId());
        userDetails.put("name", user.getName());
        userDetails.put("contactNumber", user.getContactNumber());
        response.put("user", userDetails);
        response.put("tokenId", token);
        response.put("refreshToken", refreshToken);

        return response;
    }
}
