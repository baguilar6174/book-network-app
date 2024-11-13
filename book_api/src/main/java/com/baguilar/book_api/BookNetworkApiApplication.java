package com.baguilar.book_api;

import com.baguilar.book_api.role.Role;
import com.baguilar.book_api.user.User;
import com.baguilar.book_api.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@SpringBootApplication
@EnableJpaAuditing
public class BookNetworkApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookNetworkApiApplication.class, args);
	}

	// Populate our database
	@Bean
	CommandLineRunner init(UserRepository userRepository) {
		return args -> {
			// Create roles
			Role roleAdmin = Role.builder().name("ADMIN").createdDate(LocalDateTime.now()).build();
			Role roleUser = Role.builder().name("USER").createdDate(LocalDateTime.now()).build();

			// Create users
			User admin = User.builder()
					.firstname("Bryan")
					.lastname("Aguilar")
					.dateOfBirth(LocalDate.of(1996, 5, 6))
					.email("admin@test.com")
					.password("$2a$10$uhM4jkZWSe9hzCPsdAT/ouERrvlqMv3A1Wv9hUAJj/oPk3cRH7o3W")
					.isEnabled(true)
					.accountNoExpired(true)
					.accountLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleAdmin))
					.createdDate(LocalDateTime.now())
					.build();

			User user = User.builder()
					.firstname("Alexander")
					.lastname("Yaguana")
					.dateOfBirth(LocalDate.of(1996, 5, 6))
					.email("user@test.com")
					.password("$2a$10$uhM4jkZWSe9hzCPsdAT/ouERrvlqMv3A1Wv9hUAJj/oPk3cRH7o3W")
					.isEnabled(true)
					.accountNoExpired(true)
					.accountLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleUser))
					.createdDate(LocalDateTime.now())
					.build();

			userRepository.saveAll(List.of(admin, user));
		};
	}

}
