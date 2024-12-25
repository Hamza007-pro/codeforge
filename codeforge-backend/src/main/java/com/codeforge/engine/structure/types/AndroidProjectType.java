package com.codeforge.engine.structure.types;

import com.codeforge.engine.structure.ProjectStructure;
import com.codeforge.engine.structure.ProjectType;
import com.codeforge.engine.structure.dependency.DependencyManager;
import org.springframework.stereotype.Component;

/**
 * Android project type implementation.
 */
@Component
public class AndroidProjectType implements ProjectType {
    private static final String KOTLIN_VERSION = "1.9.21";
    private static final String GRADLE_VERSION = "8.2.0";
    private static final String AGP_VERSION = "8.2.0";
    private static final String COMPILE_SDK = "34";
    private static final String MIN_SDK = "24";
    private static final String TARGET_SDK = "34";

    @Override
    public ProjectStructure createProjectStructure(String basePackage, String projectName, DependencyManager dependencyManager) {
        ProjectStructure structure = new ProjectStructure(projectName);
        String packagePath = basePackage.replace(".", "/");

        // Add standard directories
        structure.addDirectory("app");
        structure.addDirectory("app/src/main/java/" + packagePath);
        structure.addDirectory("app/src/main/res/layout");
        structure.addDirectory("app/src/main/res/values");
        structure.addDirectory("app/src/main/res/drawable");
        structure.addDirectory("app/src/main/res/mipmap-hdpi");
        structure.addDirectory("app/src/main/res/mipmap-mdpi");
        structure.addDirectory("app/src/main/res/mipmap-xhdpi");
        structure.addDirectory("app/src/main/res/mipmap-xxhdpi");
        structure.addDirectory("app/src/main/res/mipmap-xxxhdpi");
        structure.addDirectory("app/src/test/java/" + packagePath);
        structure.addDirectory("app/src/androidTest/java/" + packagePath);

        // Add configuration files
        structure.addFile("settings.gradle.kts", createSettingsGradle(projectName));
        structure.addFile("build.gradle.kts", createRootBuildGradle());
        structure.addFile("app/build.gradle.kts", createAppBuildGradle(basePackage, dependencyManager));
        structure.addFile("gradle/wrapper/gradle-wrapper.properties", createGradleWrapperProperties());
        structure.addFile(".gitignore", createGitignore());
        structure.addFile("README.md", createReadme(projectName));
        structure.addFile("app/src/main/AndroidManifest.xml", createAndroidManifest(basePackage));
        structure.addFile("app/src/main/java/" + packagePath + "/MainActivity.kt", 
                         createMainActivity(basePackage));
        structure.addFile("app/src/main/res/layout/activity_main.xml", createMainActivityLayout());
        structure.addFile("app/src/main/res/values/strings.xml", createStringsXml(projectName));
        structure.addFile("app/src/main/res/values/colors.xml", createColorsXml());

        return structure;
    }

    @Override
    public DependencyManager getDefaultDependencyManager() {
        return DependencyManager.createAndroidDependencies();
    }

    private String createSettingsGradle(String projectName) {
        return """
            pluginManagement {
                repositories {
                    google()
                    mavenCentral()
                    gradlePluginPortal()
                }
            }
            
            dependencyResolutionManagement {
                repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
                repositories {
                    google()
                    mavenCentral()
                }
            }
            
            rootProject.name = "%s"
            include(":app")
            """.formatted(projectName);
    }

    private String createRootBuildGradle() {
        return """
            plugins {
                id("com.android.application") version "%s" apply false
                id("org.jetbrains.kotlin.android") version "%s" apply false
            }
            """.formatted(AGP_VERSION, KOTLIN_VERSION);
    }

    private String createAppBuildGradle(String basePackage, DependencyManager dependencyManager) {
        return """
            plugins {
                id("com.android.application")
                id("org.jetbrains.kotlin.android")
            }
            
            android {
                namespace = "%s"
                compileSdk = %s
                
                defaultConfig {
                    applicationId = "%s"
                    minSdk = %s
                    targetSdk = %s
                    versionCode = 1
                    versionName = "1.0"
                    
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
                
                buildTypes {
                    release {
                        isMinifyEnabled = false
                        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
                    }
                }
                
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }
                
                kotlinOptions {
                    jvmTarget = "17"
                }
                
                buildFeatures {
                    viewBinding = true
                }
            }
            
            dependencies {
            %s
            }
            """.formatted(basePackage, COMPILE_SDK, basePackage, MIN_SDK, TARGET_SDK,
                         dependencyManager.generateGradleDependencies());
    }

    private String createGradleWrapperProperties() {
        return """
            distributionBase=GRADLE_USER_HOME
            distributionPath=wrapper/dists
            distributionUrl=https\\://services.gradle.org/distributions/gradle-%s-bin.zip
            zipStoreBase=GRADLE_USER_HOME
            zipStorePath=wrapper/dists
            """.formatted(GRADLE_VERSION);
    }

    private String createAndroidManifest(String basePackage) {
        return """
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
                    <activity
                        android:name=".MainActivity"
                        android:exported="true">
                        <intent-filter>
                            <action android:name="android.intent.action.MAIN" />
                            <category android:name="android.intent.category.LAUNCHER" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>
            """.formatted(basePackage);
    }

    private String createMainActivity(String basePackage) {
        return """
            package %s
            
            import androidx.appcompat.app.AppCompatActivity
            import android.os.Bundle
            
            class MainActivity : AppCompatActivity() {
                override fun onCreate(savedInstanceState: Bundle?) {
                    super.onCreate(savedInstanceState)
                    setContentView(R.layout.activity_main)
                }
            }
            """.formatted(basePackage);
    }

    private String createMainActivityLayout() {
        return """
            <?xml version="1.0" encoding="utf-8"?>
            <androidx.constraintlayout.widget.ConstraintLayout 
                xmlns:android="http://schemas.android.com/apk/res/android"
                xmlns:app="http://schemas.android.com/apk/res-auto"
                android:layout_width="match_parent"
                android:layout_height="match_parent">
            
                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="@string/hello_world"
                    android:textSize="24sp"
                    app:layout_constraintBottom_toBottomOf="parent"
                    app:layout_constraintEnd_toEndOf="parent"
                    app:layout_constraintStart_toStartOf="parent"
                    app:layout_constraintTop_toTopOf="parent" />
            
            </androidx.constraintlayout.widget.ConstraintLayout>
            """;
    }

    private String createStringsXml(String projectName) {
        return """
            <?xml version="1.0" encoding="utf-8"?>
            <resources>
                <string name="app_name">%s</string>
                <string name="hello_world">Hello World!</string>
            </resources>
            """.formatted(projectName);
    }

    private String createColorsXml() {
        return """
            <?xml version="1.0" encoding="utf-8"?>
            <resources>
                <color name="purple_200">#FFBB86FC</color>
                <color name="purple_500">#FF6200EE</color>
                <color name="purple_700">#FF3700B3</color>
                <color name="teal_200">#FF03DAC5</color>
                <color name="teal_700">#FF018786</color>
                <color name="black">#FF000000</color>
                <color name="white">#FFFFFFFF</color>
            </resources>
            """;
    }

    private String createGitignore() {
        return """
            *.iml
            .gradle
            /local.properties
            /.idea
            .DS_Store
            /build
            /captures
            .externalNativeBuild
            .cxx
            local.properties
            """;
    }

    private String createReadme(String projectName) {
        return """
            # %s
            
            ## Description
            This is an Android application generated using CodeForge.
            
            ## Prerequisites
            - Android Studio Hedgehog or higher
            - JDK 17 or higher
            - Android SDK with API level %s
            
            ## Getting Started
            1. Clone the repository
            2. Open the project in Android Studio
            3. Sync the project with Gradle files
            4. Run the application on an emulator or physical device
            
            ## Features
            - Kotlin-based Android application
            - Material Design components
            - ViewBinding enabled
            - Basic activity setup
            
            ## Project Structure
            ```
            app/                    - Application module
                src/main/          - Main source set
                    java/          - Kotlin/Java source files
                    res/           - Resources
                    AndroidManifest.xml
                src/test/          - Unit tests
                src/androidTest/   - Instrumentation tests
            ```
            """.formatted(projectName, TARGET_SDK);
    }

    @Override
    public void customizeStructure(ProjectStructure structure) {
        // Add Android specific directories
        structure.addDirectory("app/src/main/java");
        structure.addDirectory("app/src/main/res/layout");
        structure.addDirectory("app/src/main/res/values");
        structure.addDirectory("app/src/main/res/drawable");
        structure.addDirectory("app/src/main/res/mipmap");
        structure.addDirectory("app/src/androidTest/java");
        structure.addDirectory("app/src/test/java");
        structure.addDirectory("gradle/wrapper");

        // Add basic configuration files
        structure.addFile("app/build.gradle",
            "plugins {\n" +
            "    id 'com.android.application'\n" +
            "    id 'org.jetbrains.kotlin.android'\n" +
            "}\n\n" +
            "android {\n" +
            "    namespace '${basePackage}'\n" +
            "    compileSdk 34\n\n" +
            "    defaultConfig {\n" +
            "        applicationId \"${basePackage}\"\n" +
            "        minSdk 24\n" +
            "        targetSdk 34\n" +
            "        versionCode 1\n" +
            "        versionName \"1.0\"\n" +
            "    }\n" +
            "}\n"
        );

        structure.addFile("build.gradle",
            "buildscript {\n" +
            "    repositories {\n" +
            "        google()\n" +
            "        mavenCentral()\n" +
            "    }\n" +
            "    dependencies {\n" +
            "        classpath 'com.android.tools.build:gradle:8.2.0'\n" +
            "        classpath 'org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.20'\n" +
            "    }\n" +
            "}\n"
        );

        structure.addFile("settings.gradle",
            "pluginManagement {\n" +
            "    repositories {\n" +
            "        google()\n" +
            "        mavenCentral()\n" +
            "        gradlePluginPortal()\n" +
            "    }\n" +
            "}\n" +
            "rootProject.name = \"${projectName}\"\n" +
            "include ':app'\n"
        );

        // Add .gitignore
        structure.addFile(".gitignore",
            "*.iml\n" +
            ".gradle\n" +
            "/local.properties\n" +
            "/.idea\n" +
            ".DS_Store\n" +
            "/build\n" +
            "/captures\n" +
            ".externalNativeBuild\n" +
            ".cxx\n" +
            "local.properties\n"
        );
    }

    @Override
    public String getTypeName() {
        return "Android Application";
    }

    @Override
    public String getDescription() {
        return "Creates an Android application with Kotlin and modern Android development practices";
    }

    @Override
    public String getMinJavaVersion() {
        return "17";
    }
}
