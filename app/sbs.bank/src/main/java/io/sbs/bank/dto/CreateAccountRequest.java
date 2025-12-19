package io.sbs.bank.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateAccountRequest(
    @NotBlank(message = "Owner name is required")
    String ownerName
) {}

