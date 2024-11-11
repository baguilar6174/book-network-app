package com.baguilar.book_api.auth;

import com.baguilar.book_api.role.RoleRepository;
import com.baguilar.book_api.user.User;
import com.baguilar.book_api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void register(RegistrationRequest request) {
        var userRole = roleRepository.findByName("USER").orElseThrow(() -> new IllegalStateException("ROLE USER was not initiated"));
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);
        // TODO: generate token
    }
}
