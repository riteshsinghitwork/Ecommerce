package com.ecommerce.project.controller;


import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.User;
import com.ecommerce.project.repositories.RoleRepository;
import com.ecommerce.project.repositories.UserRepository;
import com.ecommerce.project.security.jwt.*;
import com.ecommerce.project.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    // NOTE: You will need to create RoleRepository if you haven't yet!
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        // 1. Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        // 2. Set the authentication in the Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Extract User Details
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // 4. Generate the JWT Cookie using your JwtUtils
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        // 5. Get user roles
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // 6. Pass the string value of the token into your LoginResponse constructor
        LoginResponse response = new LoginResponse(userDetails.getUsername(), roles);

        // 7. Return the response with the cookie attached to the HTTP header
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(response);
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        // --- Role Fix Added Here ---
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);
        user.setRoles(roles);
        // ---------------------------

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @GetMapping("/username")
    public String currentusername(Authentication authentication)
    {
        if(authentication !=null)
        {
            return authentication.getName();
        } else {
            return "No user Logged in";
        }
    }
    @GetMapping("/user")
    public ResponseEntity<LoginResponse> getUserDetails(Authentication authentication)
    {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        // 5. Get user roles
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // 6. Pass the string value of the token into your LoginResponse constructor
        LoginResponse response = new LoginResponse(userDetails.getUsername(), roles);

        // 7. Return the response with the cookie attached to the HTTP header
        return ResponseEntity.ok()
                .body(response);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOutUser()
    {
        ResponseCookie cookie = jwtUtils.getCleanCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You have been signed out!"));
    }
}