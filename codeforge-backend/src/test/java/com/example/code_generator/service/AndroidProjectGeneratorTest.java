package com.example.code_generator.service;

import com.example.code_generator.model.ProjectConfig;
import com.example.code_generator.service.impl.AndroidProjectGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.util.Arrays;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class AndroidProjectGeneratorTest {
    private AndroidProjectGenerator generator;
    private ProjectConfig config;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        generator = new AndroidProjectGenerator();
        config = new ProjectConfig();
        config.setProjectType("Mobile Application");
        config.setProjectName("TestApp");
        config.setPackageName("com.example.testapp");
        config.setVersion("1.0.0");
        config.setArchitecturePattern("mvvm");
        config.setDependencies(Arrays.asList("dagger", "room"));
    }

    @Test
    void testGenerateProject() {
        generator.generateProject(config, tempDir);
        
        // Add assertions to verify the generated files
        assertTrue(tempDir.resolve("app/build.gradle").toFile().exists(), "build.gradle should exist");
        assertTrue(tempDir.resolve("app/src/main/AndroidManifest.xml").toFile().exists(), "AndroidManifest.xml should exist");
        assertTrue(tempDir.resolve("app/src/main/java/com/example/testapp/MainActivity.java").toFile().exists(), "MainActivity.java should exist");
    }

    @Test
    void testSupportedProjectTypes() {
        assertTrue(generator.getSupportedProjectTypes().contains("Mobile Application"));
        assertFalse(generator.getSupportedProjectTypes().contains("Web Application"));
    }
}
