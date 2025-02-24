package com.systemedebons.bonification.Controller;

import com.systemedebons.bonification.Entity.ERole;
import com.systemedebons.bonification.Entity.RefreshToken;
import com.systemedebons.bonification.Entity.Role;
import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Repository.RefreshTokenRepository;
import com.systemedebons.bonification.Repository.RoleRepository;
import com.systemedebons.bonification.Repository.UserRepository;
import com.systemedebons.bonification.Security.Jwt.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.systemedebons.bonification.Security.Service.UserDetailsImpl;
import com.systemedebons.bonification.Security.Service.UserDetailsServiceImpl;
import com.systemedebons.bonification.Service.RefreshTokenService;
import com.systemedebons.bonification.payload.exception.TokenRefreshException;
import com.systemedebons.bonification.payload.request.LogoutRequest;
import com.systemedebons.bonification.payload.request.SignupRequest;
import com.systemedebons.bonification.payload.response.JwtResponse;
import com.systemedebons.bonification.payload.response.MessageResponse;
import com.systemedebons.bonification.payload.response.TokenRefreshResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.systemedebons.bonification.payload.request.LoginRequest;
import com.systemedebons.bonification.payload.request.TokenRefreshRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Authentication Controller", description = "Endpoints for user authentication and registration")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    AuthenticationManager authenticationManager;
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder encoder;
    JwtUtils jwtUtils;
    private RefreshTokenService refreshTokenService;
    private RefreshTokenRepository refreshTokenRepository;
    private UserDetailsServiceImpl userDetailsService;

    @Operation(summary = "Authenticate a user", description = "Authenticates the user by username and password, and returns JWT and refresh tokens.")
    @ApiResponse(responseCode = "200", description = "Successfully authenticated user", content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid login credentials")
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        logger.debug("Hello world");
        Authentication authentication;
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword()));
        logger.debug("Hello world");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(authentication.getName());
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        logger.info("jwt = {}", jwt);
        logger.info("roles = {}", roles);
        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(),
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @Operation(summary = "Register a new user", description = "Creates a new user account with a username, email, and password.")
    @ApiResponse(responseCode = "200", description = "User successfully registered")
    @ApiResponse(responseCode = "400", description = "Username or email already exists")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByLogin(signUpRequest.getLogin())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = new User(signUpRequest.getLogin(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> {
            logger.error("Role not found: {}", ERole.ROLE_USER);
            return new RuntimeException("Internal Error");
        });
        roles.add(role);

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @Operation(summary = "Refresh the JWT token", description = "Refreshes the JWT token using a valid refresh token.")
    @ApiResponse(responseCode = "200", description = "Token successfully refreshed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenRefreshResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid or expired refresh token")
    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserId)
                .map(user -> {
                    String token = jwtUtils.generateJwtTokenFromUserId(user);
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @Operation(summary = "Logout user", description = "Logs out the user by deleting the refresh token.")
    @ApiResponse(responseCode = "200", description = "User successfully logged out")
    @ApiResponse(responseCode = "400", description = "Invalid or missing refresh token")
    @DeleteMapping("/signout")
    public ResponseEntity<?> logoutUser(@Valid @RequestBody LogoutRequest logoutRequest) {
        String requestToken = logoutRequest.getRefreshToken();
        logger.info("Received logout request with refresh token: {}", requestToken);
        if (requestToken == null || requestToken.isEmpty()) {
            logger.error("Refresh token is missing!");
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Refresh token is missing!"));
        }

        Optional<RefreshToken> optionalRefreshToken = refreshTokenService.findByToken(requestToken);
        if (!optionalRefreshToken.isPresent()) {
            logger.error("Refresh token not found in database!");
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Refresh token is not in database!"));
        }

        RefreshToken refreshToken = optionalRefreshToken.get();
        logger.info("Deleting refresh token: {}", requestToken);
        refreshTokenService.deleteByToken(requestToken);
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
    }
}