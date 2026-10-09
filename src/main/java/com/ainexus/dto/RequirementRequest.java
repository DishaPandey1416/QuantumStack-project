package com.ainexus.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RequirementRequest {
    @NotBlank @Size(max = 200)
    private String title;
    @NotBlank @Size(max = 5000)
    private String description;
    @Size(max = 40) private String problemType;
    @Size(max = 40) private String datasetSize;
    @Size(max = 40) private String priority;
    @Size(max = 40) private String budget;

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getProblemType() { return problemType; }
    public String getDatasetSize() { return datasetSize; }
    public String getPriority() { return priority; }
    public String getBudget() { return budget; }
    public void setTitle(String v) { title = v; }
    public void setDescription(String v) { description = v; }
    public void setProblemType(String v) { problemType = v; }
    public void setDatasetSize(String v) { datasetSize = v; }
    public void setPriority(String v) { priority = v; }
    public void setBudget(String v) { budget = v; }
}
