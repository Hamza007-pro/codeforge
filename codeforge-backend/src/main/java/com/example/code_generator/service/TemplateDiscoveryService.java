package com.example.code_generator.service;

import com.example.code_generator.model.ProjectTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TemplateDiscoveryService {
    private static final Logger logger = LoggerFactory.getLogger(TemplateDiscoveryService.class);
    private final ObjectMapper objectMapper;
    private List<ProjectTemplate> templates;

    public TemplateDiscoveryService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        loadTemplates();
    }

    private void loadTemplates() {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            String pattern = "classpath*:templates/**/template.json";
            logger.info("Looking for templates with pattern: {}", pattern);
            
            Resource[] resources = resolver.getResources(pattern);
            logger.info("Found {} template resources", resources.length);
            
            templates = new ArrayList<>();
            for (Resource resource : resources) {
                logger.info("Loading template from: {}", resource.getURI());
                try {
                    ProjectTemplate template = objectMapper.readValue(resource.getInputStream(), ProjectTemplate.class);
                    templates.add(template);
                    logger.info("Successfully loaded template: {}", template.getTemplateName());
                } catch (IOException e) {
                    logger.error("Error loading template from {}: {}", resource.getURI(), e.getMessage());
                }
            }
            
            logger.info("Total templates loaded: {}", templates.size());
        } catch (IOException e) {
            logger.error("Error loading templates: {}", e.getMessage());
            templates = new ArrayList<>();
        }
    }

    public List<String> getProjectTypes() {
        return templates.stream()
                .map(ProjectTemplate::getProjectType)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getFrameworks(String projectType) {
        return templates.stream()
                .filter(t -> t.getProjectType().equals(projectType))
                .map(ProjectTemplate::getFramework)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getPlatforms(String projectType) {
        return templates.stream()
                .filter(t -> t.getProjectType().equals(projectType))
                .map(ProjectTemplate::getPlatform)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getArchitectures(String projectType, String platform) {
        return templates.stream()
                .filter(t -> t.getProjectType().equals(projectType) && t.getPlatform().equals(platform))
                .map(ProjectTemplate::getArchitecture)
                .distinct()
                .collect(Collectors.toList());
    }

    public Optional<ProjectTemplate> findTemplate(String projectType, String platform, String architecture) {
        return templates.stream()
                .filter(t -> t.getProjectType().equals(projectType) 
                        && t.getPlatform().equals(platform)
                        && t.getArchitecture().equals(architecture))
                .findFirst();
    }

    public List<ProjectTemplate> getAllTemplates() {
        return templates;
    }
}
