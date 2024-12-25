package com.codeforge.api.dto;

import jakarta.validation.constraints.NotBlank;

public record DependencyDTO(
    @NotBlank(message = "Group ID is required")
    String groupId,

    @NotBlank(message = "Artifact ID is required")
    String artifactId,

    @NotBlank(message = "Version is required")
    String version,

    String scope,
    
    boolean optional
) {}
