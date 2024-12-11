package com.baguilar.book_api.security;

import com.baguilar.book_api.auth.dto.AuthLoginRequest;
import com.baguilar.book_api.auth.dto.AuthRegisterRequest;
import com.baguilar.book_api.auth.dto.AuthResponse;
import com.baguilar.book_api.role.Role;
import com.baguilar.book_api.role.RoleRepository;
import com.baguilar.book_api.user.UserEntity;
import com.baguilar.book_api.user.UserRepository;
import com.baguilar.book_api.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public AuthResponse login(AuthLoginRequest loginRequest) {
        String username = loginRequest.username();
        String password = loginRequest.password();
        Authentication authentication = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = this.jwtUtil.createToken(authentication);
        return new AuthResponse(username, "User logged success", accessToken, true);
    }

    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = this.loadUserByUsername(username);
        if(userDetails == null) throw new BadCredentialsException("Invalid username or password");
        if(!passwordEncoder.matches(password, userDetails.getPassword())) throw new BadCredentialsException("Invalid username or password");
        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }

    public AuthResponse register(AuthRegisterRequest authRegisterRequest) {
        String firstname = authRegisterRequest.firstname();
        String lastname = authRegisterRequest.lastname();
        LocalDate dateOfBirth = authRegisterRequest.dateOfBirth();
        String email = authRegisterRequest.email();
        String password = authRegisterRequest.password();
        List<String> rolesRequest = authRegisterRequest.authRoleRequest().roleListName();
        Set<Role> roles = new HashSet<>(roleRepository.findRoleByRoleEnumIn(rolesRequest));
        if(roles.isEmpty()) throw new IllegalArgumentException("The roles specified does not exist!");
        UserEntity user = UserEntity.builder()
                .firstname(firstname)
                .lastname(lastname)
                .dateOfBirth(dateOfBirth)
                .email(email)
                .password(passwordEncoder.encode(password))
                .isEnabled(true)
                .accountNoExpired(true)
                .accountNoLocked(true)
                .credentialNoExpired(true)
                .roles(roles)
                .createdDate(LocalDateTime.now())
                .build();
        UserEntity userCreated = userRepository.save(user);
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        userCreated.getRoles().forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));
        userCreated.getRoles().stream().flatMap(role -> role.getPermissions().stream()).forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userCreated.getEmail(), userCreated.getPassword(), authorityList);
        String accessToken = this.jwtUtil.createToken(authentication);
        return new AuthResponse(userCreated.getEmail(), "User created success", accessToken, true);
    }
}
