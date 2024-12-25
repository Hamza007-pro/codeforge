package com.codeforge.engine.structure;

import com.codeforge.engine.structure.dependency.DependencyManager;

/**
 * Interface for different project types.
 * Each project type defines its own structure and configuration.
 */
public interface ProjectType {
    /**
     * Creates a project structure for this project type.
     *
     * @param basePackage The base package name for the project
     * @param projectName The name of the project
     * @param dependencyManager The dependency manager for customizing dependencies
     * @return A ProjectStructure configured for this project type
     */
    ProjectStructure createProjectStructure(String basePackage, String projectName, DependencyManager dependencyManager);

    /**
     * Gets the default dependency manager for this project type.
     *
     * @return The default dependency manager
     */
    DependencyManager getDefaultDependencyManager();

    /**
     * Gets the name of this project type.
     *
     * @return The project type name
     */
    String getTypeName();

    /**
     * Gets the description of this project type.
     *
     * @return The project type description
     */
    String getDescription();

    /**
     * Gets the minimum Java version required for this project type.
     *
     * @return The minimum Java version
     */
    String getMinJavaVersion();

    /**
     * Customizes the project structure for this project type.
     *
     * @param structure The project structure to customize
     */
    void customizeStructure(ProjectStructure structure);
}
