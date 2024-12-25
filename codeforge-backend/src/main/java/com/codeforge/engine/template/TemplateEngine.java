package com.codeforge.engine.template;

import java.util.Map;

/**
 * Interface defining the contract for template processing engines.
 */
public interface TemplateEngine {
    /**
     * Processes a template with given variables and returns the generated content.
     *
     * @param template The template to process
     * @param variables Variables to be used in template processing
     * @return The processed template content
     * @throws TemplateProcessingException if there's an error during processing
     */
    String processTemplate(Template template, Map<String, Object> variables) throws TemplateProcessingException;

    /**
     * Validates if a template is properly formatted and can be processed.
     *
     * @param template The template to validate
     * @return true if the template is valid, false otherwise
     */
    boolean validateTemplate(Template template);

    /**
     * Registers a new template in the engine.
     *
     * @param template The template to register
     * @throws TemplateProcessingException if there's an error during registration
     */
    void registerTemplate(Template template) throws TemplateProcessingException;
}
