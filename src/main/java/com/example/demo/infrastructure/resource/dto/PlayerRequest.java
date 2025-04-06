package com.example.demo.infrastructure.resource.dto;

import jakarta.validation.constraints.NotBlank;

public class PlayerRequest {
  @NotBlank(message = "Name is required")
  public String name;
}
