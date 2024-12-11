package com.baguilar.book_api.auth;

import com.baguilar.book_api.auth.dto.AuthLoginRequest;
import com.baguilar.book_api.auth.dto.AuthRegisterRequest;
import com.baguilar.book_api.auth.dto.AuthResponse;
import com.baguilar.book_api.security.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest authLoginRequest) {
        return new ResponseEntity<>(this.userDetailsService.login(authLoginRequest), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthRegisterRequest authRegisterRequest){
        return new ResponseEntity<>(this.userDetailsService.register(authRegisterRequest), HttpStatus.CREATED);
    }
}
