package com.baguilar.book_api.role;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {
    // Optional<Role> findByName(String name);
}
