package org.project.authservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.project.authservice.dto.AuthenticateRequestDTO;
import org.project.authservice.dto.RegistrationRequestDTO;
import org.project.authservice.dto.ResponseDTO;
import org.project.authservice.entity.UserEntity;
import org.project.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public ResponseDTO signUp(RegistrationRequestDTO request) {
        UserEntity user = UserEntity
                .builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .build();
        userRepository.save(user);

        String token = jwtService.generateAccessToken(user);

        return ResponseDTO.builder()
                .accessToken(token)
                .build();
    }

    public ResponseDTO signIn(AuthenticateRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(request.getEmail()));

        String token = jwtService.generateAccessToken(user);

        return ResponseDTO.builder()
                .accessToken(token)
                .user_id(user.getId())
                .build();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = jwtService.extractAllClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (SignatureException | IllegalArgumentException e) {
            return false;
        }
    }
}
