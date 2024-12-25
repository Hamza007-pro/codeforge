package com.codeforge.engine.service;

import com.codeforge.engine.structure.dependency.DependencyManager;

import java.nio.file.Path;
import java.util.Map;

/**
 * Configuration record for project generation.
 */
public record ProjectGenerationConfig(
    String projectType,
    String projectName,
    String basePackage,
    Path outputPath,
    DependencyManager dependencyManager,
    Map<String, Object> templates
) {
    /**
     * Creates a builder for ProjectGenerationConfig.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for ProjectGenerationConfig.
     */
    public static class Builder {
        private String projectType;
        private String projectName;
        private String basePackage;
        private Path outputPath;
        private DependencyManager dependencyManager;
        private Map<String, Object> templates;

        public Builder projectType(String projectType) {
            this.projectType = projectType;
            return this;
        }

        public Builder projectName(String projectName) {
            this.projectName = projectName;
            return this;
        }

        public Builder basePackage(String basePackage) {
            this.basePackage = basePackage;
            return this;
        }

        public Builder outputPath(Path outputPath) {
            this.outputPath = outputPath;
            return this;
        }

        public Builder dependencyManager(DependencyManager dependencyManager) {
            this.dependencyManager = dependencyManager;
            return this;
        }

        public Builder templates(Map<String, Object> templates) {
            this.templates = templates;
            return this;
        }

        public ProjectGenerationConfig build() {
            return new ProjectGenerationConfig(
                projectType,
                projectName,
                basePackage,
                outputPath,
                dependencyManager,
                templates
            );
        }
    }
}
