package com.codeforge.api.dto;

import com.codeforge.engine.structure.dependency.DependencyManager;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Map;

public class ProjectGenerationRequestDTO {
    @NotBlank(message = "Project type is required")
    private String projectType;

    @NotBlank(message = "Project name is required")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9-_]*$", message = "Project name must start with a letter and contain only letters, numbers, hyphens, and underscores")
    private String projectName;

    @NotBlank(message = "Base package is required")
    @Pattern(regexp = "^[a-z]+(\\.[a-z][a-z0-9]*)*$", message = "Base package must be a valid Java package name")
    private String basePackage;

    @NotBlank(message = "Output path is required")
    private String outputPath;

    @NotNull
    private DependencyManager dependencyManager;

    private Map<String, Object> templates;

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public DependencyManager getDependencyManager() {
        return dependencyManager;
    }

    public void setDependencyManager(DependencyManager dependencyManager) {
        this.dependencyManager = dependencyManager;
    }

    public Map<String, Object> getTemplates() {
        return templates;
    }

    public void setTemplates(Map<String, Object> templates) {
        this.templates = templates;
    }
}
