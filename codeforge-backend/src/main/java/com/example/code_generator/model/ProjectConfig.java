package com.example.code_generator.model;

import java.util.List;
import lombok.Data;

@Data
public class ProjectConfig {
    private String projectType;
    private String projectName;
    private String projectDescription;
    private String author;
    private String version;
    private String buildTool;
    private String language;
    private String framework;
    private String packageName;
    private String architecturePattern;
    private String schema;
    private List<String> dependencies;
}
