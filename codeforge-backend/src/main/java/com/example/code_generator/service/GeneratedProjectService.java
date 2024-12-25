package com.example.code_generator.service;

import com.example.code_generator.model.GeneratedProject;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.FileSystemUtils;

@Service
public class GeneratedProjectService {
    private final Map<String, GeneratedProject> projects = new HashMap<>();
    private final String baseDir;
    private final String downloadBaseUrl;

    public GeneratedProjectService(
            @Value("${project.generated.dir:generated-projects}") String baseDir,
            @Value("${server.port:8082}") String serverPort) {
        this.baseDir = baseDir;
        this.downloadBaseUrl = "http://localhost:" + serverPort + "/api/generator/download/";
        createDirectories();
    }

    private void createDirectories() {
        try {
            Files.createDirectories(Paths.get(baseDir));
            Files.createDirectories(Paths.get(baseDir, "zips"));
        } catch (IOException e) {
            throw new RuntimeException("Could not create necessary directories", e);
        }
    }

    public GeneratedProject saveProject(String projectName, String projectType, String architecture, Path projectPath) {
        String id = UUID.randomUUID().toString();
        String zipFileName = id + ".zip";
        Path zipPath = Paths.get(baseDir, "zips", zipFileName);

        try {
            zipProject(projectPath, zipPath);
            FileSystemUtils.deleteRecursively(projectPath);

            GeneratedProject project = GeneratedProject.builder()
                    .id(id)
                    .name(projectName)
                    .projectType(projectType)
                    .architecture(architecture)
                    .downloadUrl(downloadBaseUrl + id)
                    .generatedAt(LocalDateTime.now())
                    .zipPath(zipPath.toString())
                    .build();

            projects.put(id, project);
            return project;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save project", e);
        }
    }

    public List<GeneratedProject> getAllProjects() {
        return new ArrayList<>(projects.values());
    }

    public Optional<GeneratedProject> getProject(String id) {
        return Optional.ofNullable(projects.get(id));
    }

    public Resource getProjectZipFile(String id) throws IOException {
        GeneratedProject project = projects.get(id);
        if (project == null) {
            throw new IllegalArgumentException("Project not found: " + id);
        }

        Path zipPath = Paths.get(project.getZipPath());
        Resource resource = new UrlResource(zipPath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new IOException("Could not read zip file: " + zipPath);
        }
    }

    private void zipProject(Path sourcePath, Path zipPath) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath.toFile()))) {
            Files.walk(sourcePath)
                .filter(path -> !Files.isDirectory(path))
                .forEach(path -> {
                    ZipEntry zipEntry = new ZipEntry(sourcePath.relativize(path).toString());
                    try {
                        zos.putNextEntry(zipEntry);
                        Files.copy(path, zos);
                        zos.closeEntry();
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to zip project", e);
                    }
                });
        }
    }
}
