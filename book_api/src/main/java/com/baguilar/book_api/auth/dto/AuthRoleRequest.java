package com.baguilar.book_api.auth.dto;

import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public record AuthRoleRequest(
        @Size(max = 2, message = "The user cannot have mora than 2 roles") List<String> roleListName
) { }
