package com.codeforge.engine.generator;

import com.codeforge.engine.structure.ProjectStructure;
import com.codeforge.engine.template.Template;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

/**
 * Interface for code generation operations.
 * Responsible for generating actual code files from templates.
 */
public interface CodeGenerator {
    /**
     * Generates code files based on the provided project structure.
     *
     * @param structure The project structure containing file and directory information
     * @param outputPath The base output path where files should be generated
     * @return Map of generated file paths to their contents
     * @throws IOException If there's an error writing files
     */
    Map<Path, String> generateFiles(ProjectStructure structure, Path outputPath) throws IOException;
}
