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
public class AndroidProjectGenerator implements ProjectGenerator {
    private static final List<String> SUPPORTED_PROJECT_TYPES = Arrays.asList("Mobile Application");

    @Override
    public void generateProject(ProjectConfig config, Path outputPath) {
        try {
            // Create project directory structure
            Files.createDirectories(outputPath);
            Files.createDirectories(outputPath.resolve("app/src/main/java"));
            Files.createDirectories(outputPath.resolve("app/src/main/res/layout"));
            Files.createDirectories(outputPath.resolve("app/src/main/res/values"));
            
            // Generate project files
            generateGradleFiles(outputPath, config);
            generateManifest(outputPath, config);
            generateMainActivity(outputPath, config);
            generateLayoutFiles(outputPath, config);
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Android project", e);
        }
    }

    @Override
    public List<String> getSupportedProjectTypes() {
        return SUPPORTED_PROJECT_TYPES;
    }

    private void generateGradleFiles(Path outputPath, ProjectConfig config) throws IOException {
        String buildGradle = """
            plugins {
                id 'com.android.application'
            }
            
            android {
                namespace '%s'
                compileSdk 34
                
                defaultConfig {
                    applicationId "%s"
                    minSdk 24
                    targetSdk 34
                    versionCode 1
                    versionName "%s"
                }
            }
            
            dependencies {
                implementation 'androidx.appcompat:appcompat:1.6.1'
                implementation 'com.google.android.material:material:1.10.0'
                implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
                %s
            }
            """.formatted(
                config.getPackageName(),
                config.getPackageName(),
                config.getVersion(),
                generateDependencies(config.getDependencies())
            );
            
        Files.writeString(outputPath.resolve("app/build.gradle"), buildGradle);
    }

    private String generateDependencies(List<String> dependencies) {
        StringBuilder sb = new StringBuilder();
        for (String dep : dependencies) {
            switch (dep.toLowerCase()) {
                case "dagger":
                    sb.append("implementation 'com.google.dagger:dagger:2.48'\n");
                    sb.append("annotationProcessor 'com.google.dagger:dagger-compiler:2.48'\n");
                    break;
                case "room":
                    sb.append("implementation 'androidx.room:room-runtime:2.6.1'\n");
                    sb.append("annotationProcessor 'androidx.room:room-compiler:2.6.1'\n");
                    break;
            }
        }
        return sb.toString();
    }

    private void generateManifest(Path outputPath, ProjectConfig config) throws IOException {
        String manifest = """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android"
                package="%s">
                
                <application
                    android:allowBackup="true"
                    android:icon="@mipmap/ic_launcher"
                    android:label="@string/app_name"
                    android:roundIcon="@mipmap/ic_launcher_round"
                    android:supportsRtl="true"
                    android:theme="@style/Theme.AppCompat.Light.DarkActionBar">
                    <activity android:name=".MainActivity"
                        android:exported="true">
                        <intent-filter>
                            <action android:name="android.intent.action.MAIN" />
                            <category android:name="android.intent.category.LAUNCHER" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>
            """.formatted(config.getPackageName());
            
        Files.writeString(outputPath.resolve("app/src/main/AndroidManifest.xml"), manifest);
    }

    private void generateMainActivity(Path outputPath, ProjectConfig config) throws IOException {
        String packagePath = config.getPackageName().replace('.', '/');
        Path mainActivityPath = outputPath.resolve(
            String.format("app/src/main/java/%s/MainActivity.java", packagePath));
        Files.createDirectories(mainActivityPath.getParent());
        
        String mainActivity = """
            package %s;
            
            import android.os.Bundle;
            import androidx.appcompat.app.AppCompatActivity;
            
            public class MainActivity extends AppCompatActivity {
                @Override
                protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.activity_main);
                }
            }
            """.formatted(config.getPackageName());
            
        Files.writeString(mainActivityPath, mainActivity);
    }

    private void generateLayoutFiles(Path outputPath, ProjectConfig config) throws IOException {
        String mainLayout = """
            <?xml version="1.0" encoding="utf-8"?>
            <androidx.constraintlayout.widget.ConstraintLayout
                xmlns:android="http://schemas.android.com/apk/res/android"
                xmlns:app="http://schemas.android.com/apk/res-auto"
                android:layout_width="match_parent"
                android:layout_height="match_parent">
                
                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Welcome to %s"
                    android:textSize="24sp"
                    app:layout_constraintBottom_toBottomOf="parent"
                    app:layout_constraintLeft_toLeftOf="parent"
                    app:layout_constraintRight_toRightOf="parent"
                    app:layout_constraintTop_toTopOf="parent"/>
                    
            </androidx.constraintlayout.widget.ConstraintLayout>
            """.formatted(config.getProjectName());
            
        Files.writeString(outputPath.resolve("app/src/main/res/layout/activity_main.xml"), mainLayout);
    }
}
