package com.example.code_generator.service;

import com.example.code_generator.model.ProjectConfig;
import java.nio.file.Path;
import java.util.List;

public interface ProjectGenerator {
    void generateProject(ProjectConfig config, Path outputPath);
    List<String> getSupportedProjectTypes();
}
