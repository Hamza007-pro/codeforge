package com.example.code_generator.service.impl;

import com.example.code_generator.model.ProjectConfig;
import com.example.code_generator.service.ProjectGenerator;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Service
public class ReactProjectGenerator implements ProjectGenerator {
    private static final List<String> SUPPORTED_PROJECT_TYPES = Arrays.asList("Web Application");

    @Override
    public void generateProject(ProjectConfig config, Path outputPath) {
        try {
            // Create project directory structure
            Files.createDirectories(outputPath);
            Files.createDirectories(outputPath.resolve("src"));
            Files.createDirectories(outputPath.resolve("public"));
            
            // Generate package.json, index.html, etc.
            generatePackageJson(outputPath, config);
            generateIndexHtml(outputPath, config);
            generateAppJs(outputPath, config);
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate React project", e);
        }
    }

    @Override
    public List<String> getSupportedProjectTypes() {
        return SUPPORTED_PROJECT_TYPES;
    }

    private void generatePackageJson(Path outputPath, ProjectConfig config) throws IOException {
        String content = "{\n" +
                "  \"name\": \"" + config.getProjectName() + "\",\n" +
                "  \"version\": \"" + config.getVersion() + "\",\n" +
                "  \"private\": true,\n" +
                "  \"dependencies\": {\n" +
                "    \"react\": \"^18.2.0\",\n" +
                "    \"react-dom\": \"^18.2.0\",\n" +
                "    \"react-scripts\": \"5.0.1\"\n" +
                "  },\n" +
                "  \"scripts\": {\n" +
                "    \"start\": \"react-scripts start\",\n" +
                "    \"build\": \"react-scripts build\",\n" +
                "    \"test\": \"react-scripts test\",\n" +
                "    \"eject\": \"react-scripts eject\"\n" +
                "  }\n" +
                "}";
        Files.writeString(outputPath.resolve("package.json"), content);
    }

    private void generateIndexHtml(Path outputPath, ProjectConfig config) throws IOException {
        String content = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "  <head>\n" +
                "    <meta charset=\"utf-8\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />\n" +
                "    <title>" + config.getProjectName() + "</title>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <div id=\"root\"></div>\n" +
                "  </body>\n" +
                "</html>";
        Files.writeString(outputPath.resolve("public/index.html"), content);
    }

    private void generateAppJs(Path outputPath, ProjectConfig config) throws IOException {
        String content = "import React from 'react';\n\n" +
                "function App() {\n" +
                "  return (\n" +
                "    <div className=\"App\">\n" +
                "      <h1>" + config.getProjectName() + "</h1>\n" +
                "      <p>Welcome to your new React app!</p>\n" +
                "    </div>\n" +
                "  );\n" +
                "}\n\n" +
                "export default App;";
        Files.writeString(outputPath.resolve("src/App.js"), content);
    }
}
