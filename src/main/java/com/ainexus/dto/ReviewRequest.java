package com.ainexus.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReviewRequest {
    @NotNull @Min(0) @Max(2)
    private Integer reviewNo;
    @NotBlank @Size(max = 5000)
    private String content;

    public Integer getReviewNo() { return reviewNo; }
    public String getContent() { return content; }
    public void setReviewNo(Integer v) { reviewNo = v; }
    public void setContent(String v) { content = v; }
}
