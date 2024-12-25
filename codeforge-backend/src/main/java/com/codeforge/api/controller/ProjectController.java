package com.codeforge.api.controller;

import com.codeforge.api.dto.ProjectGenerationRequestDTO;
import com.codeforge.api.dto.ProjectGenerationResponseDTO;
import com.codeforge.api.service.ProjectService;
import com.codeforge.engine.structure.dependency.DependencyManager;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/projects")
@CrossOrigin(origins = "*")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/types")
    public ResponseEntity<Set<String>> getProjectTypes() {
        return ResponseEntity.ok(projectService.getAvailableProjectTypes());
    }

    @GetMapping("/types/{type}/dependencies")
    public ResponseEntity<DependencyManager> getDefaultDependencies(@PathVariable String type) {
        return ResponseEntity.ok(projectService.getDefaultDependencies(type));
    }

    @PostMapping
    public ResponseEntity<CompletableFuture<ProjectGenerationResponseDTO>> generateProject(
            @Valid @RequestBody ProjectGenerationRequestDTO request) {
        CompletableFuture<ProjectGenerationResponseDTO> future = projectService.generateProject(request);
        return ResponseEntity.accepted().body(future);
    }

    @GetMapping("/{projectId}/status")
    public ResponseEntity<ProjectGenerationResponseDTO> getProjectStatus(@PathVariable String projectId) {
        return ResponseEntity.ok(projectService.getProjectStatus(projectId));
    }

    @GetMapping("/{projectId}/files")
    public ResponseEntity<List<String>> getProjectFiles(@PathVariable String projectId) {
        return ResponseEntity.ok(projectService.getProjectFiles(projectId));
    }

    @GetMapping("/{projectId}/logs")
    public ResponseEntity<List<String>> getProjectLogs(@PathVariable String projectId) {
        return ResponseEntity.ok(projectService.getProjectLogs(projectId));
    }
}
