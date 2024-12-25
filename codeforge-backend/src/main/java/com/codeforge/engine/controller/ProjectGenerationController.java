package com.codeforge.engine.controller;

import com.codeforge.engine.service.ProjectGenerationService;
import com.codeforge.engine.service.ProjectGenerationConfig;
import com.codeforge.engine.service.ProjectInitializationService;
import com.codeforge.engine.service.InitializationStep;
import com.codeforge.engine.structure.ProjectStructure;
import com.codeforge.engine.structure.dependency.DependencyManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/projects")
public class ProjectGenerationController {
    private final ProjectGenerationService projectGenerationService;
    private final ProjectInitializationService projectInitializationService;

    public ProjectGenerationController(
        ProjectGenerationService projectGenerationService,
        ProjectInitializationService projectInitializationService
    ) {
        this.projectGenerationService = projectGenerationService;
        this.projectInitializationService = projectInitializationService;
    }

    @GetMapping("/types")
    public ResponseEntity<Set<String>> getProjectTypes() {
        return ResponseEntity.ok(projectGenerationService.getAvailableProjectTypes());
    }

    @GetMapping("/types/{type}/dependencies")
    public ResponseEntity<DependencyManager> getDefaultDependencies(@PathVariable String type) {
        return ResponseEntity.ok(projectGenerationService.getDefaultDependencies(type));
    }

    @PostMapping("/generate")
    public ResponseEntity<CompletableFuture<List<InitializationStep>>> generateProject(
            @RequestBody ProjectGenerationRequest request) {
        // Create configuration from request
        ProjectGenerationConfig config = ProjectGenerationConfig.builder()
            .projectType(request.projectType())
            .projectName(request.projectName())
            .basePackage(request.basePackage())
            .outputPath(Path.of(request.outputPath()))
            .dependencyManager(request.dependencyManager())
            .templates(request.templates())
            .build();

        // Generate project structure
        ProjectStructure structure = projectGenerationService.generateProject(config);

        // Initialize project asynchronously
        CompletableFuture<List<InitializationStep>> initializationFuture = 
            projectInitializationService.initializeProject(structure, config.outputPath());

        return ResponseEntity.ok(initializationFuture);
    }
}

/**
 * Record for project generation request data.
 */
record ProjectGenerationRequest(
    String projectType,
    String projectName,
    String basePackage,
    String outputPath,
    DependencyManager dependencyManager,
    Map<String, Object> templates
) {}
