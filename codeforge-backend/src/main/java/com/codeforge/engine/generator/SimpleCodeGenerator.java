package com.codeforge.engine.generator;

import com.codeforge.engine.structure.ProjectStructure;
import com.codeforge.engine.template.Template;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple implementation of the CodeGenerator interface.
 */
@Component
public class SimpleCodeGenerator implements CodeGenerator {
    @Override
    public Map<Path, String> generateFiles(ProjectStructure structure, Path outputPath) throws IOException {
        Map<Path, String> generatedFiles = new HashMap<>();

        // Validate output path
        if (!Files.exists(outputPath) && !Files.isDirectory(outputPath)) {
            throw new IOException("Invalid output path: " + outputPath);
        }

        // Create directories
        for (String dir : structure.getDirectories()) {
            Path dirPath = outputPath.resolve(dir);
            Files.createDirectories(dirPath);
        }

        // Create files
        for (Map.Entry<String, String> entry : structure.getFiles().entrySet()) {
            Path filePath = outputPath.resolve(entry.getKey());
            String content = entry.getValue();
            Files.createDirectories(filePath.getParent());
            Files.writeString(filePath, content);
            generatedFiles.put(filePath, content);
        }

        return generatedFiles;
    }
}
