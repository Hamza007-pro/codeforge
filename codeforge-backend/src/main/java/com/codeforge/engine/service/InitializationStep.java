package com.codeforge.engine.service;

/**
 * Record representing a project initialization step.
 */
public record InitializationStep(
    String description,
    String status,
    boolean success
) {}
