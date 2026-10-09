package com.ainexus.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @NotBlank @Size(max = 80)
    private String name;
    @NotBlank @Email @Size(max = 160)
    private String email;
    @NotBlank @Size(min = 6, max = 72)
    private String password;

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public void setName(String v) { name = v; }
    public void setEmail(String v) { email = v; }
    public void setPassword(String v) { password = v; }
}
