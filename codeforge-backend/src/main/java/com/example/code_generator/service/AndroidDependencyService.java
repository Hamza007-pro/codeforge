package com.example.code_generator.service;

import com.example.code_generator.model.AndroidDependency;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;

@Service
public class AndroidDependencyService {
    private final List<AndroidDependency> dependencies;

    public AndroidDependencyService() {
        this.dependencies = initializeDependencies();
    }

    public List<AndroidDependency> getAllDependencies() {
        return dependencies;
    }

    public List<AndroidDependency> getDependenciesByCategory(String category) {
        return dependencies.stream()
                .filter(dep -> dep.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    private List<AndroidDependency> initializeDependencies() {
        List<AndroidDependency> deps = new ArrayList<>();

        // UI Dependencies
        deps.add(AndroidDependency.builder()
                .name("Material Design")
                .description("Material Design components for Android")
                .groupId("com.google.android.material")
                .artifactId("material")
                .version("1.10.0")
                .type("implementation")
                .category("UI")
                .build());

        // Dependency Injection
        deps.add(AndroidDependency.builder()
                .name("Dagger Hilt")
                .description("Dependency injection library for Android")
                .groupId("com.google.dagger")
                .artifactId("hilt-android")
                .version("2.48")
                .type("implementation")
                .category("DI")
                .build());

        deps.add(AndroidDependency.builder()
                .name("Dagger Hilt Compiler")
                .description("Annotation processor for Dagger Hilt")
                .groupId("com.google.dagger")
                .artifactId("hilt-compiler")
                .version("2.48")
                .type("annotationProcessor")
                .category("DI")
                .build());

        // Database
        deps.add(AndroidDependency.builder()
                .name("Room Runtime")
                .description("SQLite object mapping library")
                .groupId("androidx.room")
                .artifactId("room-runtime")
                .version("2.6.1")
                .type("implementation")
                .category("Database")
                .build());

        deps.add(AndroidDependency.builder()
                .name("Room Compiler")
                .description("Room annotation processor")
                .groupId("androidx.room")
                .artifactId("room-compiler")
                .version("2.6.1")
                .type("annotationProcessor")
                .category("Database")
                .build());

        // Networking
        deps.add(AndroidDependency.builder()
                .name("Retrofit")
                .description("Type-safe HTTP client")
                .groupId("com.squareup.retrofit2")
                .artifactId("retrofit")
                .version("2.9.0")
                .type("implementation")
                .category("Network")
                .build());

        deps.add(AndroidDependency.builder()
                .name("OkHttp")
                .description("HTTP client")
                .groupId("com.squareup.okhttp3")
                .artifactId("okhttp")
                .version("4.12.0")
                .type("implementation")
                .category("Network")
                .build());

        // Image Loading
        deps.add(AndroidDependency.builder()
                .name("Glide")
                .description("Image loading library")
                .groupId("com.github.bumptech.glide")
                .artifactId("glide")
                .version("4.16.0")
                .type("implementation")
                .category("Image")
                .build());

        // Async Programming
        deps.add(AndroidDependency.builder()
                .name("Coroutines Core")
                .description("Kotlin coroutines support")
                .groupId("org.jetbrains.kotlinx")
                .artifactId("kotlinx-coroutines-android")
                .version("1.7.3")
                .type("implementation")
                .category("Async")
                .build());

        // Testing
        deps.add(AndroidDependency.builder()
                .name("JUnit")
                .description("Unit testing framework")
                .groupId("junit")
                .artifactId("junit")
                .version("4.13.2")
                .type("testImplementation")
                .category("Testing")
                .build());

        return deps;
    }
}
