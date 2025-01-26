package org.project.authservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.authservice.dto.AuthenticateRequestDTO;
import org.project.authservice.dto.RegistrationRequestDTO;
import org.project.authservice.dto.ResponseDTO;
import org.project.authservice.entity.UserEntity;
import org.project.authservice.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserEntity userEntity;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void signUpTest() {
        //given
        RegistrationRequestDTO requestDTO = new RegistrationRequestDTO("username", "password", "email");
        UserEntity userEntity = new UserEntity(1L, "username", "encodedPassword", "email");

        //when
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(jwtService.generateAccessToken(any(UserEntity.class))).thenReturn("token");
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        //then
        ResponseDTO responseDTO = authService.signUp(requestDTO);
        assertEquals("token", responseDTO.getAccessToken());

        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(jwtService, times(1)).generateAccessToken(any(UserEntity.class));
    }

    @Test
    void signIn() {
        //given
        String email = "test@test.com";
        String token = "token";
        AuthenticateRequestDTO request = new AuthenticateRequestDTO(email, "password");
        UserEntity userEntity = new UserEntity(1L, "username", "password", email);

        //when
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(jwtService.generateAccessToken(userEntity)).thenReturn(token);

        //then
        ResponseDTO response = authService.signIn(request);

        assertEquals(token, response.getAccessToken());
        assertEquals(1L, response.getUser_id());

        verify(authenticationManager, times(1))
                .authenticate(new UsernamePasswordAuthenticationToken(email, "password"));

        verify(userRepository, times(1)).findByEmail(email);
        verify(jwtService, times(1)).generateAccessToken(userEntity);
    }

}