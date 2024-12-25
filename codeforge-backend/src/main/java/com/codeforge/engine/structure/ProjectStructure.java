package com.codeforge.engine.structure;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a project structure with directories and files.
 */
public class ProjectStructure {
    private final String name;
    private String basePackage;
    private final String projectType;
    private final Map<String, String> metadata;
    private final List<String> directories;
    private final Map<String, String> files;

    public ProjectStructure(String name) {
        this(name, null);
    }

    public ProjectStructure(String name, String projectType) {
        this.name = name;
        this.projectType = projectType;
        this.metadata = new HashMap<>();
        this.directories = new ArrayList<>();
        this.files = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public String getProjectType() {
        return projectType;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public void addMetadata(String key, String value) {
        metadata.put(key, value);
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void addDirectory(String relativePath) {
        directories.add(relativePath);
    }

    public List<String> getDirectories() {
        return directories;
    }

    public void addFile(String relativePath, String content) {
        files.put(relativePath, content);
    }

    public Map<String, String> getFiles() {
        return files;
    }

    /**
     * Creates a basic Java project structure.
     *
     * @param basePackage The base package name (e.g., "com.example.myapp")
     * @param projectName The project name
     * @return A ProjectStructure instance with basic Java project structure
     */
    public static ProjectStructure createBasicJavaStructure(String basePackage, String projectName) {
        ProjectStructure structure = new ProjectStructure(projectName);
        
        // Convert package to path
        String packagePath = basePackage.replace(".", "/");
        
        // Add standard directories
        structure.addDirectory("src/main/java/" + packagePath);
        structure.addDirectory("src/main/resources");
        structure.addDirectory("src/test/java/" + packagePath);
        structure.addDirectory("src/test/resources");
        
        // Add basic configuration files
        structure.addFile("pom.xml", getBasicPomXml(basePackage, projectName));
        structure.addFile(".gitignore", getBasicGitignore());
        structure.addFile("README.md", getBasicReadme(projectName));
        
        return structure;
    }

    private static String getBasicPomXml(String basePackage, String projectName) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
               "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
               "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
               "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
               "    <modelVersion>4.0.0</modelVersion>\n\n" +
               "    <groupId>" + basePackage + "</groupId>\n" +
               "    <artifactId>" + projectName + "</artifactId>\n" +
               "    <version>1.0-SNAPSHOT</version>\n\n" +
               "    <properties>\n" +
               "        <maven.compiler.source>17</maven.compiler.source>\n" +
               "        <maven.compiler.target>17</maven.compiler.target>\n" +
               "        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n" +
               "    </properties>\n" +
               "</project>";
    }

    private static String getBasicGitignore() {
        return "target/\n" +
               "*.iml\n" +
               ".idea/\n" +
               "*.class\n" +
               "*.log\n" +
               ".project\n" +
               ".classpath\n" +
               ".settings/";
    }

    private static String getBasicReadme(String projectName) {
        return "# " + projectName + "\n\n" +
               "## Description\n" +
               "Add your project description here.\n\n" +
               "## Getting Started\n" +
               "1. Clone the repository\n" +
               "2. Build the project: `mvn clean install`\n" +
               "3. Run the application\n\n" +
               "## Features\n" +
               "- Feature 1\n" +
               "- Feature 2\n\n" +
               "## Contributing\n" +
               "Describe how others can contribute to this project.\n";
    }
}
