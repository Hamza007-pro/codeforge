package com.example.code_generator.controller;

import com.example.code_generator.model.ProjectConfig;
import com.example.code_generator.model.ProjectTemplate;
import com.example.code_generator.model.GeneratedProject;
import com.example.code_generator.service.ProjectGeneratorService;
import com.example.code_generator.service.TemplateDiscoveryService;
import com.example.code_generator.service.GeneratedProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/generator")
@CrossOrigin(origins = "*")
public class ProjectGeneratorController {
    
    private final ProjectGeneratorService projectGeneratorService;
    private final TemplateDiscoveryService templateDiscoveryService;
    private final GeneratedProjectService generatedProjectService;

    @Autowired
    public ProjectGeneratorController(
            ProjectGeneratorService projectGeneratorService,
            TemplateDiscoveryService templateDiscoveryService,
            GeneratedProjectService generatedProjectService) {
        this.projectGeneratorService = projectGeneratorService;
        this.templateDiscoveryService = templateDiscoveryService;
        this.generatedProjectService = generatedProjectService;
    }

    @GetMapping("/project-types")
    public ResponseEntity<List<String>> getProjectTypes() {
        return ResponseEntity.ok(templateDiscoveryService.getProjectTypes());
    }

    @GetMapping("/frameworks/{projectType}")
    public ResponseEntity<List<String>> getFrameworks(@PathVariable String projectType) {
        return ResponseEntity.ok(templateDiscoveryService.getFrameworks(projectType));
    }

    @GetMapping("/platforms/{projectType}")
    public ResponseEntity<List<String>> getPlatforms(@PathVariable String projectType) {
        return ResponseEntity.ok(templateDiscoveryService.getPlatforms(projectType));
    }

    @GetMapping("/architectures/{projectType}/{platform}")
    public ResponseEntity<List<String>> getArchitectures(
            @PathVariable String projectType,
            @PathVariable String platform) {
        return ResponseEntity.ok(templateDiscoveryService.getArchitectures(projectType, platform));
    }

    @GetMapping("/template/{projectType}/{platform}/{architecture}")
    public ResponseEntity<ProjectTemplate> getTemplate(
            @PathVariable String projectType,
            @PathVariable String platform,
            @PathVariable String architecture) {
        Optional<ProjectTemplate> template = templateDiscoveryService.findTemplate(projectType, platform, architecture);
        return template.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/templates")
    public ResponseEntity<List<ProjectTemplate>> getAllTemplates() {
        return ResponseEntity.ok(templateDiscoveryService.getAllTemplates());
    }

    @PostMapping("/generate")
    public ResponseEntity<GeneratedProject> generateProject(@RequestBody ProjectConfig config) {
        Path projectPath = projectGeneratorService.generateProject(config);
        GeneratedProject generatedProject = generatedProjectService.saveProject(
            config.getProjectName(),
            config.getProjectType(),
            config.getArchitecturePattern(),
            projectPath
        );
        return ResponseEntity.ok(generatedProject);
    }

    @GetMapping("/projects")
    public ResponseEntity<List<GeneratedProject>> listProjects() {
        return ResponseEntity.ok(generatedProjectService.getAllProjects());
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadProject(@PathVariable String id) {
        try {
            Resource resource = generatedProjectService.getProjectZipFile(id);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + id + ".zip\"")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
