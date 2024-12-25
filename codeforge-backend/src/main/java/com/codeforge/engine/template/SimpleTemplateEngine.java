package com.codeforge.engine.template;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple implementation of the TemplateEngine interface that handles basic variable substitution.
 */
@Service
public class SimpleTemplateEngine implements TemplateEngine {
    private final Map<String, Template> templateRegistry;
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{([^}]+)}");

    public SimpleTemplateEngine() {
        this.templateRegistry = new HashMap<>();
    }

    @Override
    public String processTemplate(Template template, Map<String, Object> variables) throws TemplateProcessingException {
        if (!validateTemplate(template)) {
            throw new TemplateProcessingException("Invalid template: " + template.getName());
        }

        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> entry : template.getTemplateFiles().entrySet()) {
            String processedContent = replaceVariables(entry.getValue(), variables);
            result.append(processedContent).append("\n");
        }

        return result.toString();
    }

    private String replaceVariables(String content, Map<String, Object> variables) {
        Matcher matcher = VARIABLE_PATTERN.matcher(content);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String variable = matcher.group(1);
            Object value = variables.get(variable);
            matcher.appendReplacement(buffer, value != null ? value.toString() : "");
        }
        matcher.appendTail(buffer);

        return buffer.toString();
    }

    @Override
    public boolean validateTemplate(Template template) {
        if (template == null || template.getName() == null || template.getName().isEmpty()) {
            return false;
        }
        // A template without files is valid during initial creation
        return template.getTemplateFiles() != null;
    }

    @Override
    public void registerTemplate(Template template) throws TemplateProcessingException {
        if (!validateTemplate(template)) {
            throw new TemplateProcessingException("Cannot register invalid template: " + template.getName());
        }
        templateRegistry.put(template.getName(), template);
    }

    /**
     * Retrieves a registered template by name.
     *
     * @param name The name of the template to retrieve
     * @return The template if found, null otherwise
     */
    public Template getTemplate(String name) {
        return templateRegistry.get(name);
    }
}
