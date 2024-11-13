package com.baguilar.book_api.auth;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

    private final AuthenticationService service;

    @GetMapping("/hello")
    public String helloPublic() {
        return "Hello world";
    }

    @GetMapping("/hello-secure")
    public String helloSecure() {
        return "Hello world secure";
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequest request) {
        service.register(request);
        return ResponseEntity.accepted().build();
    }
}
