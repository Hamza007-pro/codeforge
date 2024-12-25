package com.codeforge.engine.structure.dependency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a logical group of dependencies (e.g., "Web", "Database", "Testing").
 */
public class DependencyGroup {
    private final String name;
    private final String description;
    private final List<Dependency> dependencies;
    private final boolean required;

    public DependencyGroup(String name, String description, boolean required) {
        this.name = name;
        this.description = description;
        this.dependencies = new ArrayList<>();
        this.required = required;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isRequired() {
        return required;
    }

    public void addDependency(Dependency dependency) {
        dependencies.add(dependency);
    }

    public List<Dependency> getDependencies() {
        return Collections.unmodifiableList(dependencies);
    }
}
