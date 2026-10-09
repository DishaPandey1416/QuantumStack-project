package com.ainexus.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TeamRequest {
    @NotBlank @Size(max = 100)
    private String name;
    public String getName() { return name; }
    public void setName(String v) { name = v; }
}
