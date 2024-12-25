package com.codeforge.engine.template;

import java.util.Map;
import java.util.HashMap;

/**
 * Represents a design pattern template with its associated metadata and content.
 */
public class Template {
    private String name;
    private String patternType;
    private String description;
    private Map<String, String> templateFiles;
    private Map<String, Object> metadata;

    public Template(String name, String patternType) {
        this.name = name;
        this.patternType = patternType;
        this.templateFiles = new HashMap<>();
        this.metadata = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatternType() {
        return patternType;
    }

    public void setPatternType(String patternType) {
        this.patternType = patternType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, String> getTemplateFiles() {
        return templateFiles;
    }

    public void addTemplateFile(String path, String content) {
        this.templateFiles.put(path, content);
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void addMetadata(String key, Object value) {
        this.metadata.put(key, value);
    }

    public String getContent() {
        // TO DO: implement getContent method
        return null;
    }

    public String[] getRequiredVariables() {
        // TO DO: implement getRequiredVariables method
        return null;
    }

    public String process(Map<String, Object> variables) {
        // TO DO: implement process method
        return null;
    }
}
