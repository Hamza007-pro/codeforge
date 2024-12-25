package com.example.code_generator.model;

import lombok.Data;
import java.util.Map;
import java.util.List;

@Data
public class ProjectTemplate {
    private String templateName;
    private String templateDescription;
    private String projectType;
    private String framework;
    private String platform;
    private String architecture;
    private List<TemplateFile> files;
    private Dependencies dependencies;

    @Data
    public static class TemplateFile {
        private String path;
        private String template;
    }

    @Data
    public static class Dependencies {
        private List<String> required;
        private Map<String, List<String>> optional;
    }
}
