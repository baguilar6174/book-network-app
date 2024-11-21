package com.baguilar.book_api.auth;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

    @GetMapping("/hello")
    public String helloPublic() {
        return "Hello world";
    }

    @GetMapping("/hello-secure")
    public String helloSecure() {
        return "Hello world secure";
    }
}
