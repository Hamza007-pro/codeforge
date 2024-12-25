package com.codeforge.engine.service;

import com.codeforge.engine.structure.ProjectStructure;
import com.codeforge.engine.structure.ProjectType;
import com.codeforge.engine.structure.ProjectTypeRegistry;
import com.codeforge.engine.structure.dependency.DependencyManager;
import com.codeforge.engine.template.Template;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Service layer for coordinating project generation, including project structure,
 * dependencies, and code generation.
 */
@Service
public class ProjectGenerationService {
    private final ProjectTypeRegistry projectTypeRegistry;
    private final TemplateService templateService;

    public ProjectGenerationService(
            ProjectTypeRegistry projectTypeRegistry,
            TemplateService templateService) {
        this.projectTypeRegistry = projectTypeRegistry;
        this.templateService = templateService;
    }

    /**
     * Gets all available project types.
     *
     * @return Set of project type names
     */
    public Set<String> getAvailableProjectTypes() {
        return projectTypeRegistry.getAvailableTypes();
    }

    /**
     * Gets the default dependencies for a project type.
     *
     * @param projectType Project type name
     * @return Default dependency manager for the project type
     */
    public DependencyManager getDefaultDependencies(String projectType) {
        ProjectType type = projectTypeRegistry.getProjectType(projectType);
        return type.getDefaultDependencyManager();
    }

    /**
     * Generates a project based on the provided configuration.
     *
     * @param config Project generation configuration
     * @return Generated project structure
     */
    public ProjectStructure generateProject(ProjectGenerationConfig config) {
        // Get project type
        ProjectType type = projectTypeRegistry.getProjectType(config.projectType());
        if (type == null) {
            throw new IllegalArgumentException("Unknown project type: " + config.projectType());
        }

        // Create basic structure
        ProjectStructure structure = ProjectStructure.createBasicJavaStructure(
            config.basePackage(),
            config.projectName()
        );

        // Apply project type customizations
        type.customizeStructure(structure);

        // Process templates
        if (config.templates() != null) {
            for (Map.Entry<String, Object> entry : config.templates().entrySet()) {
                String templateName = entry.getKey();
                Template template = templateService.getTemplate(templateName);
                
                if (template != null) {
                    Map<String, Object> variables = new HashMap<>(config.templates());
                    variables.put("projectName", config.projectName());
                    variables.put("basePackage", config.basePackage());
                    
                    String content = templateService.processTemplate(templateName, variables);
                    structure.addFile(templateName, content);
                }
            }
        }

        return structure;
    }
}
