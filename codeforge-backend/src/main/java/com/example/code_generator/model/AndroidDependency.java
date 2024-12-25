package com.example.code_generator.model;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AndroidDependency {
    private String name;
    private String description;
    private String groupId;
    private String artifactId;
    private String version;
    private String type; // e.g., "implementation", "annotationProcessor", "kapt", etc.
    private String category; // e.g., "UI", "Database", "Network", "DI", etc.
}
