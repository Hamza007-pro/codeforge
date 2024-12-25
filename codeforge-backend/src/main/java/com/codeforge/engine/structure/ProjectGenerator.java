package com.codeforge.engine.structure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Generates project structure on the file system.
 */
public class ProjectGenerator {
    private final ProjectStructure structure;
    private final Map<String, String> variables;

    public ProjectGenerator(ProjectStructure structure, Map<String, String> variables) {
        this.structure = structure;
        this.variables = variables;
    }

    /**
     * Generates the project structure in the specified directory.
     *
     * @param outputDirectory The directory where the project should be generated
     * @throws IOException If there's an error creating directories or files
     */
    public void generate(Path outputDirectory) throws IOException {
        // Create base directory if it doesn't exist
        Files.createDirectories(outputDirectory);

        // Create all directories
        for (String dir : structure.getDirectories()) {
            Path dirPath = outputDirectory.resolve(dir);
            Files.createDirectories(dirPath);
        }

        // Create all files
        for (Map.Entry<String, String> file : structure.getFiles().entrySet()) {
            Path filePath = outputDirectory.resolve(file.getKey());
            String content = processTemplate(file.getValue());
            Files.writeString(filePath, content);
        }
    }

    /**
     * Process a template string by replacing variables.
     *
     * @param template The template string
     * @return The processed string
     */
    private String processTemplate(String template) {
        String result = template;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            result = result.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }

    /**
     * Creates a new project with the specified configuration.
     *
     * @param projectName The name of the project
     * @param basePackage The base package name
     * @param outputDirectory The directory where the project should be created
     * @throws IOException If there's an error creating the project
     */
    public static void createProject(String projectName, String basePackage, Path outputDirectory) throws IOException {
        ProjectStructure structure = ProjectStructure.createBasicJavaStructure(basePackage, projectName);
        
        Map<String, String> variables = Map.of(
            "projectName", projectName,
            "basePackage", basePackage
        );

        ProjectGenerator generator = new ProjectGenerator(structure, variables);
        generator.generate(outputDirectory);
    }
}
