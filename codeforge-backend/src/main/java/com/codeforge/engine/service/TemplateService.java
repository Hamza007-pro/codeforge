package com.codeforge.engine.service;

import com.codeforge.engine.template.Template;
import com.codeforge.engine.template.TemplateEngine;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for managing and applying templates to projects.
 */
@Service
public class TemplateService {
    private final TemplateEngine templateEngine;
    private final Map<String, Template> templateCache;

    public TemplateService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        this.templateCache = new ConcurrentHashMap<>();
    }

    /**
     * Registers a new template.
     *
     * @param name Template name
     * @param template Template content
     */
    public void registerTemplate(String name, Template template) {
        templateCache.put(name, template);
    }

    /**
     * Gets a template by name.
     *
     * @param name Template name
     * @return Template if found, null otherwise
     */
    public Template getTemplate(String name) {
        return templateCache.get(name);
    }

    /**
     * Processes a template with the given variables.
     *
     * @param templateName Template name
     * @param variables Template variables
     * @return Processed template content
     */
    public String processTemplate(String templateName, Map<String, Object> variables) {
        Template template = getTemplate(templateName);
        if (template == null) {
            throw new IllegalArgumentException("Template not found: " + templateName);
        }
        return template.process(variables);
    }

    /**
     * Creates template variables for a project.
     *
     * @param projectName Project name
     * @param basePackage Base package name
     * @return Map of template variables
     */
    public Map<String, Object> createTemplateVariables(String projectName, String basePackage) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("projectName", projectName);
        variables.put("basePackage", basePackage);
        variables.put("packagePath", basePackage.replace(".", "/"));
        return variables;
    }
}
