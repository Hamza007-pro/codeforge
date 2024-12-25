package com.codeforge.engine.generator;

/**
 * Exception thrown when there's an error during code generation.
 */
public class CodeGenerationException extends Exception {
    public CodeGenerationException(String message) {
        super(message);
    }

    public CodeGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
