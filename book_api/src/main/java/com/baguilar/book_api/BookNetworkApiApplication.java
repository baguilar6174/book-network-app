package com.baguilar.book_api;

import com.baguilar.book_api.permission.Permission;
import com.baguilar.book_api.role.Role;
import com.baguilar.book_api.role.RoleEnum;
import com.baguilar.book_api.user.UserEntity;
import com.baguilar.book_api.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class BookNetworkApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookNetworkApiApplication.class, args);
	}

	// Populate our database
	@Bean
	CommandLineRunner init(UserRepository userRepository) {
		return args -> {
			// Create permissions
			Permission createPermission = Permission.builder().name("CREATE").createdDate(LocalDateTime.now()).build();
			Permission readPermission = Permission.builder().name("READ").createdDate(LocalDateTime.now()).build();

			// Create roles
			Role roleAdmin = Role.builder().roleEnum(RoleEnum.ADMIN).permissions(Set.of(createPermission, readPermission)).createdDate(LocalDateTime.now()).build();
			Role roleUser = Role.builder().roleEnum(RoleEnum.USER).permissions(Set.of(readPermission)).createdDate(LocalDateTime.now()).build();

			// Create users
			UserEntity admin = UserEntity.builder()
					.firstname("Bryan")
					.lastname("Aguilar")
					.dateOfBirth(LocalDate.of(1996, 5, 6))
					.email("admin@test.com")
					.password("$2a$10$uhM4jkZWSe9hzCPsdAT/ouERrvlqMv3A1Wv9hUAJj/oPk3cRH7o3W") // 123456
					.isEnabled(true)
					.accountNoExpired(true)
					.accountLocked(false)
					.credentialNoExpired(true)
					.roles(Set.of(roleAdmin))
					.createdDate(LocalDateTime.now())
					.build();

			UserEntity user = UserEntity.builder()
					.firstname("Alexander")
					.lastname("Yaguana")
					.dateOfBirth(LocalDate.of(1996, 5, 6))
					.email("user@test.com")
					.password("$2a$10$uhM4jkZWSe9hzCPsdAT/ouERrvlqMv3A1Wv9hUAJj/oPk3cRH7o3W") // 123456
					.isEnabled(true)
					.accountNoExpired(true)
					.accountLocked(false)
					.credentialNoExpired(true)
					.roles(Set.of(roleUser))
					.createdDate(LocalDateTime.now())
					.build();

			userRepository.saveAll(List.of(admin, user));
		};
	}

}
