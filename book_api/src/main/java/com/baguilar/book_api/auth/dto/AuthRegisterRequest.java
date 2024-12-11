package com.baguilar.book_api.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AuthRegisterRequest(
        @NotBlank String firstname,
        @NotBlank String lastname,
        @NotNull @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate dateOfBirth,
        @NotBlank String email,
        @NotBlank String password,
        @Valid AuthRoleRequest authRoleRequest
        ) {
}
