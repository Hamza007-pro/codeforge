package com.codeforge.engine.service;

import com.codeforge.engine.structure.ProjectStructure;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service for initializing and setting up generated projects.
 */
@Service
public class ProjectInitializationService {
    
    /**
     * Initializes a generated project by running necessary build and setup commands.
     *
     * @param structure Project structure
     * @param projectPath Path to the generated project
     * @return List of initialization steps and their status
     */
    public CompletableFuture<List<InitializationStep>> initializeProject(ProjectStructure structure, Path projectPath) {
        return CompletableFuture.supplyAsync(() -> {
            List<InitializationStep> steps = new ArrayList<>();
            
            try {
                // Run project-specific initialization steps
                switch (structure.getProjectType()) {
                    case "React Application" -> initializeReactProject(projectPath, steps);
                    case "Flutter Application" -> initializeFlutterProject(projectPath, steps);
                    case "Spring Boot Application" -> initializeSpringBootProject(projectPath, steps);
                    case "Android Application" -> initializeAndroidProject(projectPath, steps);
                }
                
                return steps;
            } catch (Exception e) {
                InitializationStep errorStep = new InitializationStep(
                    "Project Initialization",
                    "Failed: " + e.getMessage(),
                    false
                );
                steps.add(errorStep);
                return steps;
            }
        });
    }
    
    private void initializeReactProject(Path projectPath, List<InitializationStep> steps) throws IOException {
        // Install dependencies
        executeCommand(projectPath, "npm install", steps, "Installing dependencies");
        
        // Initialize git repository
        executeCommand(projectPath, "git init", steps, "Initializing Git repository");
        
        // Create initial commit
        executeCommand(projectPath, "git add .", steps, "Adding files to Git");
        executeCommand(projectPath, "git commit -m \"Initial commit\"", steps, "Creating initial commit");
    }
    
    private void initializeFlutterProject(Path projectPath, List<InitializationStep> steps) throws IOException {
        // Get dependencies
        executeCommand(projectPath, "flutter pub get", steps, "Getting dependencies");
        
        // Initialize git repository
        executeCommand(projectPath, "git init", steps, "Initializing Git repository");
        executeCommand(projectPath, "git add .", steps, "Adding files to Git");
        executeCommand(projectPath, "git commit -m \"Initial commit\"", steps, "Creating initial commit");
    }
    
    private void initializeSpringBootProject(Path projectPath, List<InitializationStep> steps) throws IOException {
        // Build project
        executeCommand(projectPath, "mvn clean install", steps, "Building project");
        
        // Initialize git repository
        executeCommand(projectPath, "git init", steps, "Initializing Git repository");
        executeCommand(projectPath, "git add .", steps, "Adding files to Git");
        executeCommand(projectPath, "git commit -m \"Initial commit\"", steps, "Creating initial commit");
    }
    
    private void initializeAndroidProject(Path projectPath, List<InitializationStep> steps) throws IOException {
        // Build project
        executeCommand(projectPath, "./gradlew build", steps, "Building project");
        
        // Initialize git repository
        executeCommand(projectPath, "git init", steps, "Initializing Git repository");
        executeCommand(projectPath, "git add .", steps, "Adding files to Git");
        executeCommand(projectPath, "git commit -m \"Initial commit\"", steps, "Creating initial commit");
    }
    
    private void executeCommand(Path workingDir, String command, List<InitializationStep> steps, String description) 
            throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(workingDir.toFile());
        
        // Handle Windows/Unix differences
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            processBuilder.command("cmd.exe", "/c", command);
        } else {
            processBuilder.command("sh", "-c", command);
        }
        
        Process process = processBuilder.start();
        try {
            int exitCode = process.waitFor();
            steps.add(new InitializationStep(
                description,
                exitCode == 0 ? "Success" : "Failed with exit code " + exitCode,
                exitCode == 0
            ));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            steps.add(new InitializationStep(description, "Interrupted: " + e.getMessage(), false));
        }
    }
}
