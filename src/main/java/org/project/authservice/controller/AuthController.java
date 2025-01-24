package org.project.authservice.controller;

import lombok.AllArgsConstructor;
import org.project.authservice.dto.AuthenticateRequestDTO;
import org.project.authservice.dto.RegistrationRequestDTO;
import org.project.authservice.dto.ResponseDTO;
import org.project.authservice.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signUp")
    public ResponseEntity<ResponseDTO> signUp(@RequestBody RegistrationRequestDTO request) {
        return ResponseEntity.ok(authService.signUp(request));
    }

    @PostMapping("/singIn")
    public ResponseEntity<ResponseDTO> signIn(@RequestBody AuthenticateRequestDTO request){
        return ResponseEntity.ok(authService.signIn(request));
    }
}
