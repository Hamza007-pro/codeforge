package com.codeforge.api.service;

import com.codeforge.api.dto.ProjectGenerationRequestDTO;
import com.codeforge.api.dto.ProjectGenerationResponseDTO;
import com.codeforge.api.exception.ProjectGenerationException;
import com.codeforge.engine.service.ProjectGenerationConfig;
import com.codeforge.engine.service.ProjectGenerationService;
import com.codeforge.engine.service.ProjectInitializationService;
import com.codeforge.engine.structure.ProjectStructure;
import com.codeforge.engine.structure.dependency.DependencyManager;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProjectService {
    private final ProjectGenerationService projectGenerationService;
    private final ProjectInitializationService projectInitializationService;
    private final Map<String, ProjectGenerationResponseDTO> projectStatusMap;
    private final Map<String, List<String>> projectLogsMap;
    private final Map<String, List<String>> projectFilesMap;

    public ProjectService(ProjectGenerationService projectGenerationService,
                         ProjectInitializationService projectInitializationService) {
        this.projectGenerationService = projectGenerationService;
        this.projectInitializationService = projectInitializationService;
        this.projectStatusMap = new ConcurrentHashMap<>();
        this.projectLogsMap = new ConcurrentHashMap<>();
        this.projectFilesMap = new ConcurrentHashMap<>();
    }

    public CompletableFuture<ProjectGenerationResponseDTO> generateProject(ProjectGenerationRequestDTO request) {
        if (!projectGenerationService.getAvailableProjectTypes().contains(request.getProjectType())) {
            throw new ProjectGenerationException("Invalid project type: " + request.getProjectType());
        }

        String projectId = UUID.randomUUID().toString();
        ProjectGenerationConfig config = ProjectGenerationConfig.builder()
            .projectType(request.getProjectType())
            .projectName(request.getProjectName())
            .basePackage(request.getBasePackage())
            .build();

        ProjectStructure structure = projectGenerationService.generateProject(config);
        
        ProjectGenerationResponseDTO response = new ProjectGenerationResponseDTO();
        response.setProjectId(projectId);
        response.setStatus("PENDING");
        response.setProjectType(request.getProjectType());
        response.setProjectName(request.getProjectName());
        
        projectStatusMap.put(projectId, response);
        projectLogsMap.put(projectId, new ArrayList<>());
        projectFilesMap.put(projectId, new ArrayList<>());
        
        return CompletableFuture.completedFuture(response);
    }

    public ProjectGenerationResponseDTO getProjectStatus(String projectId) {
        ProjectGenerationResponseDTO status = projectStatusMap.get(projectId);
        if (status == null) {
            throw new ProjectGenerationException("Project not found: " + projectId);
        }
        return status;
    }

    public List<String> getProjectFiles(String projectId) {
        List<String> files = projectFilesMap.get(projectId);
        if (files == null) {
            throw new ProjectGenerationException("Project not found: " + projectId);
        }
        return files;
    }

    public List<String> getProjectLogs(String projectId) {
        List<String> logs = projectLogsMap.get(projectId);
        if (logs == null) {
            throw new ProjectGenerationException("Project not found: " + projectId);
        }
        return logs;
    }

    public Set<String> getAvailableProjectTypes() {
        return projectGenerationService.getAvailableProjectTypes();
    }

    public DependencyManager getDefaultDependencies(String projectType) {
        return projectGenerationService.getDefaultDependencies(projectType);
    }
}
