package com.systemedebons.bonification.Service;

import com.systemedebons.bonification.Entity.RefreshToken;
import com.systemedebons.bonification.Repository.RefreshTokenRepository;
import com.systemedebons.bonification.Security.Jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.systemedebons.bonification.payload.exception.TokenRefreshException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RefreshTokenService {
    @Value("${jwt.refresh.expiration}")
    private int jwtRefreshExpirationMs;

    private RefreshTokenRepository refreshTokenRepository;

    private JwtUtils jwtUtils;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JwtUtils jwtUtils) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtils = jwtUtils;
    }

    public RefreshToken createRefreshToken(String userId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(jwtUtils.generateRefreshToken(userId));
        refreshToken.setUserId(userId);
        refreshToken.setExpiryDate(Instant.now().plusMillis(jwtUtils.getJwtRefreshExpirationMs()));
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new sign-in request");
        }

        return token;
    }

    @Scheduled(fixedRate = 86400000) // Exécuter tous les jours
    public void deleteExpiredTokens() {
        Instant now = Instant.now();
        List<RefreshToken> tokens = refreshTokenRepository.findAll();
        tokens.forEach(token -> {
            if (token.getExpiryDate().isBefore(now)) {
                refreshTokenRepository.delete(token);
            }
        });
    }

    public void updateRefreshToken(String userId, String newToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new TokenRefreshException("User ID " + userId, "User not found"));
        refreshToken.setToken(newToken);
        refreshToken.setExpiryDate(new Date(System.currentTimeMillis() + jwtRefreshExpirationMs).toInstant());
        refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
