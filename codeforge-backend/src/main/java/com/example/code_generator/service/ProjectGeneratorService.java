package com.example.code_generator.service;

import com.example.code_generator.model.ProjectConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

@Service
public class ProjectGeneratorService {
    private final Map<String, ProjectGenerator> generators;
    private final String baseDir;

    @Autowired
    public ProjectGeneratorService(List<ProjectGenerator> projectGenerators) {
        this.generators = new HashMap<>();
        for (ProjectGenerator generator : projectGenerators) {
            for (String type : generator.getSupportedProjectTypes()) {
                generators.put(type, generator);
            }
        }
        this.baseDir = "generated-projects";
    }

    public Path generateProject(ProjectConfig config) {
        ProjectGenerator generator = generators.get(config.getProjectType());
        if (generator == null) {
            throw new IllegalArgumentException("Unsupported project type: " + config.getProjectType());
        }

        Path projectPath = Paths.get(baseDir, config.getProjectName());
        generator.generateProject(config, projectPath);
        return projectPath;
    }
}
