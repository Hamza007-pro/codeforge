package com.example.code_generator.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class GeneratedProject {
    private String id;
    private String name;
    private String projectType;
    private String architecture;
    private String downloadUrl;
    private LocalDateTime generatedAt;
    private String zipPath;
}
