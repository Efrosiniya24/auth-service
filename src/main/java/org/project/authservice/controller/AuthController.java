package org.project.authservice.controller;

import lombok.AllArgsConstructor;
import org.project.authservice.dto.AuthenticateRequestDTO;
import org.project.authservice.dto.RegistrationRequestDTO;
import org.project.authservice.dto.ResponseDTO;
import org.project.authservice.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @PostMapping("/signIn")
    public ResponseEntity<ResponseDTO> signIn(@RequestBody AuthenticateRequestDTO request){
        try {
            return ResponseEntity.ok(authService.signIn(request));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            SecurityContextHolder.clearContext();
        }
        return ResponseEntity.ok().build();
    }

}
