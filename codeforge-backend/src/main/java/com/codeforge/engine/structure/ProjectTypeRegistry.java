package com.codeforge.engine.structure;

import com.codeforge.engine.structure.types.AndroidProjectType;
import com.codeforge.engine.structure.types.FlutterProjectType;
import com.codeforge.engine.structure.types.ReactProjectType;
import com.codeforge.engine.structure.types.SpringBootProjectType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Registry for all available project types.
 */
@Component
public class ProjectTypeRegistry {
    private final Map<String, ProjectType> projectTypes;

    public ProjectTypeRegistry() {
        projectTypes = new HashMap<>();
        registerBuiltInTypes();
    }

    private void registerBuiltInTypes() {
        // Register Spring Boot project type
        SpringBootProjectType springBootType = new SpringBootProjectType();
        projectTypes.put("spring-boot", springBootType);

        // Register Android project type
        AndroidProjectType androidType = new AndroidProjectType();
        projectTypes.put("android", androidType);

        // Register Flutter project type
        FlutterProjectType flutterType = new FlutterProjectType();
        projectTypes.put("flutter", flutterType);

        // Register React project type
        ReactProjectType reactType = new ReactProjectType();
        projectTypes.put("react", reactType);
    }

    /**
     * Gets a project type by its name.
     *
     * @param type The name of the project type
     * @return The project type, or null if not found
     */
    public ProjectType getProjectType(String type) {
        return projectTypes.get(type);
    }

    /**
     * Gets all registered project types.
     *
     * @return A set of all registered project types
     */
    public Set<String> getAvailableTypes() {
        return projectTypes.keySet();
    }

    /**
     * Gets the names of all registered project types.
     *
     * @return A set of names of all registered project types
     */
    public Set<String> getTypeNames() {
        return projectTypes.values().stream()
            .map(ProjectType::getTypeName)
            .collect(Collectors.toSet());
    }

    /**
     * Creates a project structure for the specified project type.
     *
     * @param type The name of the project type
     * @param basePackage The base package for the project
     * @param projectName The name of the project
     * @return The created project structure
     * @throws IllegalArgumentException if the project type is not found
     */
    public ProjectStructure createProjectStructure(String type, String basePackage, String projectName) {
        ProjectType projectType = getProjectType(type);
        if (projectType == null) {
            throw new IllegalArgumentException("Invalid project type: " + type);
        }

        ProjectStructure structure = new ProjectStructure(projectName);
        structure.setBasePackage(basePackage);
        projectType.customizeStructure(structure);
        return structure;
    }
}
