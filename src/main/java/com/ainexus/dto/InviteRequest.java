package com.ainexus.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class InviteRequest {
    @NotBlank @Email @Size(max = 160)
    private String email;
    public String getEmail() { return email; }
    public void setEmail(String v) { email = v; }
}
