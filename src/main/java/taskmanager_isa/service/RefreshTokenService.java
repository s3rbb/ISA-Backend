package taskmanager_isa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taskmanager_isa.entity.RefreshToken;
import taskmanager_isa.entity.User;
import taskmanager_isa.repository.RefreshTokenRepository;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private static final long REFRESH_TOKEN_EXPIRY_MS = 7 * 24 * 60 * 60 * 1000L;

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public RefreshToken createRefreshToken(User user) {
        RefreshToken token = refreshTokenRepository.findByUser(user)
                .orElse(RefreshToken.builder().user(user).build());

        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Instant.now().plusMillis(REFRESH_TOKEN_EXPIRY_MS));

        return refreshTokenRepository.save(token);
    }

    public RefreshToken verifyRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired");
        }

        return refreshToken;
    }
}
