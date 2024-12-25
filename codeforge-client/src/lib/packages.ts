export const NPM_PACKAGES = [
  // UI Frameworks
  { name: "react", description: "A JavaScript library for building user interfaces", version: "18.2.0", category: "UI Framework" },
  { name: "@angular/core", description: "Angular - the core framework", version: "17.0.0", category: "UI Framework" },
  { name: "vue", description: "Progressive JavaScript Framework", version: "3.3.0", category: "UI Framework" },
  { name: "svelte", description: "Cybernetically enhanced web apps", version: "4.2.0", category: "UI Framework" },
  
  // State Management
  { name: "redux", description: "Predictable state container", version: "5.0.0", category: "State Management" },
  { name: "mobx", description: "Simple, scalable state management", version: "6.10.0", category: "State Management" },
  { name: "recoil", description: "State management library for React", version: "0.7.0", category: "State Management" },
  { name: "zustand", description: "Bear necessities for state management", version: "4.4.0", category: "State Management" },
  
  // UI Components
  { name: "@mui/material", description: "React components that implement Material Design", version: "5.14.0", category: "UI Components" },
  { name: "@chakra-ui/react", description: "Modular and accessible component library", version: "2.8.0", category: "UI Components" },
  { name: "tailwindcss", description: "A utility-first CSS framework", version: "3.4.0", category: "Styling" },
  { name: "styled-components", description: "Visual primitives for the component age", version: "6.1.0", category: "Styling" },
  
  // Data Fetching
  { name: "axios", description: "Promise based HTTP client", version: "1.6.0", category: "Networking" },
  { name: "@tanstack/react-query", description: "Powerful asynchronous state management", version: "5.0.0", category: "Data Management" },
  { name: "swr", description: "React Hooks for Data Fetching", version: "2.2.0", category: "Data Management" },
  
  // Development Tools
  { name: "typescript", description: "TypeScript is JavaScript with syntax for types", version: "5.3.0", category: "Development" },
  { name: "vite", description: "Next Generation Frontend Tooling", version: "5.0.0", category: "Build Tools" },
  { name: "webpack", description: "Module bundler", version: "5.89.0", category: "Build Tools" },
]

export const FLUTTER_PACKAGES = [
  // State Management
  { name: "flutter_bloc", description: "State management library", version: "8.1.3", category: "State Management" },
  { name: "provider", description: "State management solution", version: "6.1.0", category: "State Management" },
  { name: "riverpod", description: "A reactive caching and data-binding framework", version: "2.4.0", category: "State Management" },
  { name: "get", description: "State management, dependency injection, route management", version: "4.6.0", category: "State Management" },
  
  // Networking
  { name: "dio", description: "HTTP client for Dart", version: "5.3.0", category: "Networking" },
  { name: "http", description: "A composable, multi-platform HTTP client", version: "1.1.0", category: "Networking" },
  { name: "graphql_flutter", description: "GraphQL client for Flutter", version: "5.1.0", category: "Networking" },
  
  // Storage
  { name: "shared_preferences", description: "Persistent storage for simple data", version: "2.2.0", category: "Storage" },
  { name: "hive", description: "Lightweight and blazing fast key-value database", version: "2.2.0", category: "Storage" },
  { name: "sqflite", description: "SQLite plugin for Flutter", version: "2.3.0", category: "Storage" },
  
  // UI Components
  { name: "flutter_svg", description: "SVG rendering and widget library", version: "2.0.0", category: "UI" },
  { name: "cached_network_image", description: "Load and cache network images", version: "3.3.0", category: "UI" },
  { name: "flutter_screenutil", description: "Adapter screen and font size", version: "5.9.0", category: "UI" },
  
  // Utils
  { name: "path_provider", description: "File system locations", version: "2.1.0", category: "Utils" },
  { name: "url_launcher", description: "URL Launcher", version: "6.2.0", category: "Utils" },
  { name: "permission_handler", description: "Permission plugin for Flutter", version: "11.0.0", category: "Utils" },
]

export const ANDROID_PACKAGES = [
  // UI Components
  { name: "androidx.compose.material3:material3", description: "Material Design 3 Components", version: "1.1.2", category: "UI Components" },
  { name: "androidx.compose.ui:ui", description: "Jetpack Compose UI toolkit", version: "1.5.4", category: "UI Components" },
  { name: "com.google.android.material:material", description: "Material Design Components", version: "1.11.0", category: "UI Components" },
  
  // Navigation
  { name: "androidx.navigation:navigation-compose", description: "Navigation for Compose", version: "2.7.5", category: "Navigation" },
  { name: "androidx.navigation:navigation-fragment-ktx", description: "Navigation Architecture Component", version: "2.7.5", category: "Navigation" },
  
  // Dependency Injection
  { name: "com.google.dagger:hilt-android", description: "Dependency injection library", version: "2.48", category: "DI" },
  { name: "org.koin:koin-android", description: "Lightweight dependency injection", version: "3.5.0", category: "DI" },
  
  // Networking
  { name: "com.squareup.retrofit2:retrofit", description: "Type-safe HTTP client", version: "2.9.0", category: "Networking" },
  { name: "com.squareup.okhttp3:okhttp", description: "HTTP client", version: "4.12.0", category: "Networking" },
  { name: "com.github.bumptech.glide:glide", description: "Image loading framework", version: "4.16.0", category: "Networking" },
  
  // Database
  { name: "androidx.room:room-runtime", description: "SQLite object mapping library", version: "2.6.1", category: "Database" },
  { name: "androidx.datastore:datastore-preferences", description: "Data storage solution", version: "1.0.0", category: "Storage" },
  
  // State Management
  { name: "org.jetbrains.kotlinx:kotlinx-coroutines-android", description: "Coroutines support", version: "1.7.3", category: "State Management" },
  { name: "androidx.lifecycle:lifecycle-viewmodel-compose", description: "ViewModel for Compose", version: "2.6.2", category: "State Management" },
  
  // Testing
  { name: "junit:junit", description: "Unit testing framework", version: "4.13.2", category: "Testing" },
  { name: "androidx.test.espresso:espresso-core", description: "UI testing framework", version: "3.5.1", category: "Testing" },
  
  // Utils
  { name: "com.jakewharton.timber:timber", description: "Logging utility", version: "5.0.1", category: "Utils" },
  { name: "androidx.core:core-ktx", description: "Kotlin extensions for Android", version: "1.12.0", category: "Utils" },
  { name: "org.jetbrains.kotlin:kotlin-stdlib", description: "Kotlin standard library", version: "1.9.20", category: "Utils" }
]
