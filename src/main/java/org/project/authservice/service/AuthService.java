package org.project.authservice.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.project.authservice.dto.RegistrationRequestDTO;
import org.project.authservice.dto.ResponseDTO;
import org.project.authservice.entity.UserEntity;
import org.project.authservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JWTService jwtService;

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
}
