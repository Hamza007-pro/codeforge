package com.example.code_generator.controller;

import com.example.code_generator.model.AndroidDependency;
import com.example.code_generator.service.AndroidDependencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/android/dependencies")
@Tag(name = "Android Dependencies", description = "API endpoints for managing Android dependencies")
@CrossOrigin(origins = "*")
public class AndroidDependencyController {

    private final AndroidDependencyService dependencyService;

    @Autowired
    public AndroidDependencyController(AndroidDependencyService dependencyService) {
        this.dependencyService = dependencyService;
    }

    @GetMapping
    @Operation(summary = "Get all Android dependencies", 
               description = "Returns a list of all available Android dependencies with their details")
    public List<AndroidDependency> getAllDependencies() {
        return dependencyService.getAllDependencies();
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get dependencies by category", 
               description = "Returns a list of Android dependencies filtered by category (UI, Database, Network, DI, etc.)")
    public List<AndroidDependency> getDependenciesByCategory(@PathVariable String category) {
        return dependencyService.getDependenciesByCategory(category);
    }
}
