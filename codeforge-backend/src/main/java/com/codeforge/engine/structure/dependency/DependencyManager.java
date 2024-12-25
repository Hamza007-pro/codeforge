package com.codeforge.engine.structure.dependency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages dependencies for a project, organizing them into logical groups.
 */
public class DependencyManager {
    private final List<DependencyGroup> groups;
    private final List<Dependency> selectedDependencies;

    public DependencyManager() {
        this.groups = new ArrayList<>();
        this.selectedDependencies = new ArrayList<>();
    }

    public void addGroup(DependencyGroup group) {
        groups.add(group);
        if (group.isRequired()) {
            selectedDependencies.addAll(group.getDependencies());
        }
    }

    public List<DependencyGroup> getGroups() {
        return Collections.unmodifiableList(groups);
    }

    public void selectDependency(Dependency dependency) {
        if (!selectedDependencies.contains(dependency)) {
            selectedDependencies.add(dependency);
        }
    }

    public void unselectDependency(Dependency dependency) {
        selectedDependencies.remove(dependency);
    }

    public List<Dependency> getSelectedDependencies() {
        return Collections.unmodifiableList(selectedDependencies);
    }

    public String generateMavenDependencies() {
        return selectedDependencies.stream()
                .map(Dependency::toMavenXml)
                .collect(Collectors.joining("\n"));
    }

    public String generateGradleDependencies() {
        return selectedDependencies.stream()
                .map(Dependency::toGradleString)
                .collect(Collectors.joining("\n"));
    }

    /**
     * Creates a DependencyManager with common Spring Boot dependencies.
     */
    public static DependencyManager createSpringBootDependencies() {
        DependencyManager manager = new DependencyManager();

        // Core dependencies (required)
        DependencyGroup core = new DependencyGroup("Core", "Essential Spring Boot dependencies", true);
        core.addDependency(Dependency.builder()
            .groupId("org.springframework.boot")
            .artifactId("spring-boot-starter")
            .build());
        core.addDependency(Dependency.builder()
            .groupId("org.springframework.boot")
            .artifactId("spring-boot-starter-test")
            .scope("test")
            .build());
        manager.addGroup(core);

        // Web dependencies
        DependencyGroup web = new DependencyGroup("Web", "Web application dependencies", false);
        web.addDependency(Dependency.builder()
            .groupId("org.springframework.boot")
            .artifactId("spring-boot-starter-web")
            .build());
        web.addDependency(Dependency.builder()
            .groupId("org.springframework.boot")
            .artifactId("spring-boot-starter-validation")
            .build());
        manager.addGroup(web);

        // Database dependencies
        DependencyGroup database = new DependencyGroup("Database", "Database access dependencies", false);
        database.addDependency(Dependency.builder()
            .groupId("org.springframework.boot")
            .artifactId("spring-boot-starter-data-jpa")
            .build());
        database.addDependency(Dependency.builder()
            .groupId("com.h2database")
            .artifactId("h2")
            .scope("runtime")
            .build());
        manager.addGroup(database);

        // Security dependencies
        DependencyGroup security = new DependencyGroup("Security", "Security-related dependencies", false);
        security.addDependency(Dependency.builder()
            .groupId("org.springframework.boot")
            .artifactId("spring-boot-starter-security")
            .build());
        security.addDependency(Dependency.builder()
            .groupId("io.jsonwebtoken")
            .artifactId("jjwt")
            .version("0.9.1")
            .build());
        manager.addGroup(security);

        return manager;
    }

    /**
     * Creates a DependencyManager with common Android dependencies.
     */
    public static DependencyManager createAndroidDependencies() {
        DependencyManager manager = new DependencyManager();

        // Core dependencies (required)
        DependencyGroup core = new DependencyGroup("Core", "Essential Android dependencies", true);
        core.addDependency(Dependency.builder()
            .groupId("androidx.core")
            .artifactId("core-ktx")
            .version("1.12.0")
            .build());
        core.addDependency(Dependency.builder()
            .groupId("androidx.appcompat")
            .artifactId("appcompat")
            .version("1.6.1")
            .build());
        core.addDependency(Dependency.builder()
            .groupId("com.google.android.material")
            .artifactId("material")
            .version("1.11.0")
            .build());
        manager.addGroup(core);

        // UI dependencies
        DependencyGroup ui = new DependencyGroup("UI", "UI-related dependencies", false);
        ui.addDependency(Dependency.builder()
            .groupId("androidx.constraintlayout")
            .artifactId("constraintlayout")
            .version("2.1.4")
            .build());
        ui.addDependency(Dependency.builder()
            .groupId("androidx.recyclerview")
            .artifactId("recyclerview")
            .version("1.3.2")
            .build());
        manager.addGroup(ui);

        // Architecture Components
        DependencyGroup arch = new DependencyGroup("Architecture", "Android Architecture Components", false);
        arch.addDependency(Dependency.builder()
            .groupId("androidx.lifecycle")
            .artifactId("lifecycle-viewmodel-ktx")
            .version("2.6.2")
            .build());
        arch.addDependency(Dependency.builder()
            .groupId("androidx.lifecycle")
            .artifactId("lifecycle-livedata-ktx")
            .version("2.6.2")
            .build());
        manager.addGroup(arch);

        // Networking
        DependencyGroup networking = new DependencyGroup("Networking", "Networking dependencies", false);
        networking.addDependency(Dependency.builder()
            .groupId("com.squareup.retrofit2")
            .artifactId("retrofit")
            .version("2.9.0")
            .build());
        networking.addDependency(Dependency.builder()
            .groupId("com.squareup.okhttp3")
            .artifactId("okhttp")
            .version("4.12.0")
            .build());
        manager.addGroup(networking);

        return manager;
    }
}
