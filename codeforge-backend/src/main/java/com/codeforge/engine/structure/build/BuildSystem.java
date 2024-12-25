package com.codeforge.engine.structure.build;

import com.codeforge.engine.structure.dependency.Dependency;
import java.util.List;

/**
 * Interface for different build systems (Maven, Gradle, npm, pub).
 */
public interface BuildSystem {
    /**
     * Gets the name of the build system.
     */
    String getName();

    /**
     * Gets the file extension for the build configuration file.
     */
    String getConfigFileExtension();

    /**
     * Formats dependencies according to the build system's format.
     */
    String formatDependencies(List<Dependency> dependencies);

    /**
     * Creates the build configuration file content.
     */
    String createBuildConfig(String projectName, String version, List<Dependency> dependencies);

    /**
     * Gets the build command to execute the project.
     */
    String getBuildCommand();

    /**
     * Gets the run command to start the project.
     */
    String getRunCommand();

    /**
     * Gets the test command to run tests.
     */
    String getTestCommand();
}
