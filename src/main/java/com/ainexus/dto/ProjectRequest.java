package com.ainexus.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProjectRequest {
    @NotBlank @Size(max = 200)
    private String title;
    @NotBlank @Size(max = 3000)
    private String description;

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public void setTitle(String v) { title = v; }
    public void setDescription(String v) { description = v; }
}
