package com.codeforge.engine.template;

/**
 * Custom exception for template processing errors.
 */
public class TemplateProcessingException extends Exception {
    public TemplateProcessingException(String message) {
        super(message);
    }

    public TemplateProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
