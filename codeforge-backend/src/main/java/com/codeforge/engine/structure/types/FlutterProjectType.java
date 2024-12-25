package com.codeforge.engine.structure.types;

import com.codeforge.engine.structure.ProjectStructure;
import com.codeforge.engine.structure.ProjectType;
import com.codeforge.engine.structure.build.PubBuildSystem;
import com.codeforge.engine.structure.dependency.Dependency;
import com.codeforge.engine.structure.dependency.DependencyGroup;
import com.codeforge.engine.structure.dependency.DependencyManager;

/**
 * Flutter project type implementation.
 */
public class FlutterProjectType implements ProjectType {
    private final PubBuildSystem buildSystem = new PubBuildSystem();

    @Override
    public ProjectStructure createProjectStructure(String basePackage, String projectName, DependencyManager dependencyManager) {
        ProjectStructure structure = new ProjectStructure(projectName);
        String packagePath = basePackage.replace(".", "/");

        // Add standard directories
        structure.addDirectory("lib");
        structure.addDirectory("lib/src");
        structure.addDirectory("lib/src/models");
        structure.addDirectory("lib/src/screens");
        structure.addDirectory("lib/src/widgets");
        structure.addDirectory("lib/src/services");
        structure.addDirectory("lib/src/utils");
        structure.addDirectory("test");
        structure.addDirectory("assets");
        structure.addDirectory("assets/images");
        structure.addDirectory("assets/icons");

        // Add configuration files
        structure.addFile("pubspec.yaml", buildSystem.createBuildConfig(projectName, "1.0.0", dependencyManager.getSelectedDependencies()));
        structure.addFile("analysis_options.yaml", buildSystem.createAnalysisOptions());
        structure.addFile(".gitignore", createGitignore());
        structure.addFile("README.md", createReadme(projectName));
        structure.addFile("lib/main.dart", createMainDart(projectName));
        structure.addFile("lib/src/app.dart", createAppDart(projectName));
        structure.addFile("lib/src/screens/home_screen.dart", createHomeScreenDart());
        structure.addFile("test/widget_test.dart", createWidgetTest(projectName));

        customizeStructure(structure);

        return structure;
    }

    @Override
    public DependencyManager getDefaultDependencyManager() {
        DependencyManager manager = new DependencyManager();

        // Core dependencies (required)
        DependencyGroup core = new DependencyGroup("Core", "Essential Flutter dependencies", true);
        core.addDependency(Dependency.builder()
            .groupId("cupertino_icons")
            .artifactId("cupertino_icons")
            .version("1.0.6")
            .build());
        manager.addGroup(core);

        // State Management
        DependencyGroup state = new DependencyGroup("State Management", "State management libraries", false);
        state.addDependency(Dependency.builder()
            .groupId("provider")
            .artifactId("provider")
            .version("6.1.1")
            .build());
        state.addDependency(Dependency.builder()
            .groupId("flutter_bloc")
            .artifactId("flutter_bloc")
            .version("8.1.3")
            .build());
        manager.addGroup(state);

        // Networking
        DependencyGroup networking = new DependencyGroup("Networking", "HTTP and API dependencies", false);
        networking.addDependency(Dependency.builder()
            .groupId("http")
            .artifactId("http")
            .version("1.1.2")
            .build());
        networking.addDependency(Dependency.builder()
            .groupId("dio")
            .artifactId("dio")
            .version("5.4.0")
            .build());
        manager.addGroup(networking);

        return manager;
    }

    private String createGitignore() {
        return """
            # Miscellaneous
            *.class
            *.log
            *.pyc
            *.swp
            .DS_Store
            .atom/
            .buildlog/
            .history
            .svn/
            migrate_working_dir/
            
            # IntelliJ related
            *.iml
            *.ipr
            *.iws
            .idea/
            
            # Flutter/Dart/Pub related
            **/doc/api/
            **/ios/Flutter/.last_build_id
            .dart_tool/
            .flutter-plugins
            .flutter-plugins-dependencies
            .packages
            .pub-cache/
            .pub/
            build/
            
            # Android related
            **/android/**/gradle-wrapper.jar
            **/android/.gradle
            **/android/captures/
            **/android/gradlew
            **/android/gradlew.bat
            **/android/local.properties
            **/android/**/GeneratedPluginRegistrant.java
            
            # iOS/XCode related
            **/ios/**/*.mode1v3
            **/ios/**/*.mode2v3
            **/ios/**/*.moved-aside
            **/ios/**/*.pbxuser
            **/ios/**/*.perspectivev3
            **/ios/**/*sync/
            **/ios/**/.sconsign.dblite
            **/ios/**/.tags*
            **/ios/**/.vagrant/
            **/ios/**/DerivedData/
            **/ios/**/Icon?
            **/ios/**/Pods/
            **/ios/**/.symlinks/
            **/ios/**/profile
            **/ios/**/xcuserdata
            **/ios/.generated/
            **/ios/Flutter/App.framework
            **/ios/Flutter/Flutter.framework
            **/ios/Flutter/Flutter.podspec
            **/ios/Flutter/Generated.xcconfig
            **/ios/Flutter/app.flx
            **/ios/Flutter/app.zip
            **/ios/Flutter/flutter_assets/
            **/ios/Flutter/flutter_export_environment.sh
            **/ios/ServiceDefinitions.json
            **/ios/Runner/GeneratedPluginRegistrant.*
            """;
    }

    private String createReadme(String projectName) {
        return """
            # %s
            
            A Flutter application generated using CodeForge.
            
            ## Getting Started
            
            This project is a starting point for a Flutter application.
            
            A few resources to get you started:
            
            - [Lab: Write your first Flutter app](https://docs.flutter.dev/get-started/codelab)
            - [Cookbook: Useful Flutter samples](https://docs.flutter.dev/cookbook)
            
            For help getting started with Flutter development, view the
            [online documentation](https://docs.flutter.dev/), which offers tutorials,
            samples, guidance on mobile development, and a full API reference.
            
            ## Project Structure
            
            ```
            lib/
              src/
                models/    - Data models
                screens/  - Screen widgets
                widgets/  - Reusable widgets
                services/ - Business logic and services
                utils/    - Utility functions
                app.dart  - App widget
              main.dart   - Entry point
            ```
            """.formatted(projectName);
    }

    private String createMainDart(String projectName) {
        return """
            import 'package:flutter/material.dart';
            import 'src/app.dart';
            
            void main() {
              runApp(const %sApp());
            }
            """.formatted(projectName.replaceAll(" ", ""));
    }

    private String createAppDart(String projectName) {
        String appClassName = projectName.replaceAll(" ", "") + "App";
        return """
            import 'package:flutter/material.dart';
            import 'screens/home_screen.dart';
            
            class %s extends StatelessWidget {
              const %s({super.key});
            
              @override
              Widget build(BuildContext context) {
                return MaterialApp(
                  title: '%s',
                  theme: ThemeData(
                    colorScheme: ColorScheme.fromSeed(seedColor: Colors.blue),
                    useMaterial3: true,
                  ),
                  home: const HomeScreen(),
                );
              }
            }
            """.formatted(appClassName, appClassName, projectName);
    }

    private String createHomeScreenDart() {
        return """
            import 'package:flutter/material.dart';
            
            class HomeScreen extends StatelessWidget {
              const HomeScreen({super.key});
            
              @override
              Widget build(BuildContext context) {
                return Scaffold(
                  appBar: AppBar(
                    title: const Text('Home'),
                    backgroundColor: Theme.of(context).colorScheme.inversePrimary,
                  ),
                  body: Center(
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: <Widget>[
                        const Text(
                          'Welcome to Flutter!',
                          style: TextStyle(fontSize: 24),
                        ),
                        const SizedBox(height: 16),
                        Text(
                          'Start editing to see some magic happen',
                          style: Theme.of(context).textTheme.bodyLarge,
                        ),
                      ],
                    ),
                  ),
                  floatingActionButton: FloatingActionButton(
                    onPressed: () {},
                    tooltip: 'Add',
                    child: const Icon(Icons.add),
                  ),
                );
              }
            }
            """;
    }

    private String createWidgetTest(String projectName) {
        String appClassName = projectName.replaceAll(" ", "") + "App";
        return """
            import 'package:flutter_test/flutter_test.dart';
            import 'package:flutter/material.dart';
            import 'package:%s/src/app.dart';
            
            void main() {
              testWidgets('App smoke test', (WidgetTester tester) async {
                await tester.pumpWidget(const %s());
                expect(find.byType(MaterialApp), findsOneWidget);
              });
            }
            """.formatted(projectName.toLowerCase().replaceAll(" ", "_"), appClassName);
    }

    @Override
    public void customizeStructure(ProjectStructure structure) {
        // Add Flutter specific directories
        structure.addDirectory("integration_test");
        structure.addDirectory("android");
        structure.addDirectory("ios");
        structure.addDirectory("web");
        structure.addDirectory("assets/fonts");

        // Add configuration files
        structure.addFile("pubspec.yaml",
            "name: ${structure.getProjectName()}\n" +
            "description: A new Flutter project\n" +
            "publish_to: 'none'\n" +
            "version: 1.0.0+1\n\n" +
            "environment:\n" +
            "  sdk: '>=3.0.0 <4.0.0'\n\n" +
            "dependencies:\n" +
            "  flutter:\n" +
            "    sdk: flutter\n" +
            "  cupertino_icons: ^1.0.6\n\n" +
            "dev_dependencies:\n" +
            "  flutter_test:\n" +
            "    sdk: flutter\n" +
            "  integration_test:\n" +
            "    sdk: flutter\n\n" +
            "flutter:\n" +
            "  uses-material-design: true\n"
        );

        // Add basic Flutter app
        structure.addFile("lib/main.dart",
            "import 'package:flutter/material.dart';\n\n" +
            "void main() {\n" +
            "  runApp(const MyApp());\n" +
            "}\n\n" +
            "class MyApp extends StatelessWidget {\n" +
            "  const MyApp({super.key});\n\n" +
            "  @override\n" +
            "  Widget build(BuildContext context) {\n" +
            "    return MaterialApp(\n" +
            "      title: '${structure.getProjectName()}',\n" +
            "      theme: ThemeData(\n" +
            "        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),\n" +
            "        useMaterial3: true,\n" +
            "      ),\n" +
            "      home: const MyHomePage(title: '${structure.getProjectName()} Home Page'),\n" +
            "    );\n" +
            "  }\n" +
            "}\n\n" +
            "class MyHomePage extends StatefulWidget {\n" +
            "  const MyHomePage({super.key, required this.title});\n" +
            "  final String title;\n\n" +
            "  @override\n" +
            "  State<MyHomePage> createState() => _MyHomePageState();\n" +
            "}\n\n" +
            "class _MyHomePageState extends State<MyHomePage> {\n" +
            "  int _counter = 0;\n\n" +
            "  void _incrementCounter() {\n" +
            "    setState(() {\n" +
            "      _counter++;\n" +
            "    });\n" +
            "  }\n\n" +
            "  @override\n" +
            "  Widget build(BuildContext context) {\n" +
            "    return Scaffold(\n" +
            "      appBar: AppBar(\n" +
            "        backgroundColor: Theme.of(context).colorScheme.inversePrimary,\n" +
            "        title: Text(widget.title),\n" +
            "      ),\n" +
            "      body: Center(\n" +
            "        child: Column(\n" +
            "          mainAxisAlignment: MainAxisAlignment.center,\n" +
            "          children: <Widget>[\n" +
            "            const Text(\n" +
            "              'You have pushed the button this many times:',\n" +
            "            ),\n" +
            "            Text(\n" +
            "              '$_counter',\n" +
            "              style: Theme.of(context).textTheme.headlineMedium,\n" +
            "            ),\n" +
            "          ],\n" +
            "        ),\n" +
            "      ),\n" +
            "      floatingActionButton: FloatingActionButton(\n" +
            "        onPressed: _incrementCounter,\n" +
            "        tooltip: 'Increment',\n" +
            "        child: const Icon(Icons.add),\n" +
            "      ),\n" +
            "    );\n" +
            "  }\n" +
            "}\n"
        );

        // Add .gitignore
        structure.addFile(".gitignore",
            "# Miscellaneous\n" +
            "*.class\n" +
            "*.log\n" +
            "*.pyc\n" +
            "*.swp\n" +
            ".DS_Store\n" +
            ".atom/\n" +
            ".buildlog/\n" +
            ".history\n" +
            ".svn/\n" +
            "migrate_working_dir/\n\n" +
            "# IntelliJ related\n" +
            "*.iml\n" +
            "*.ipr\n" +
            "*.iws\n" +
            ".idea/\n\n" +
            "# Flutter/Dart/Pub related\n" +
            "**/doc/api/\n" +
            "**/ios/Flutter/.last_build_id\n" +
            ".dart_tool/\n" +
            ".flutter-plugins\n" +
            ".flutter-plugins-dependencies\n" +
            ".pub-cache/\n" +
            ".pub/\n" +
            "/build/\n\n" +
            "# Web related\n" +
            "lib/generated_plugin_registrant.dart\n\n" +
            "# Symbolication related\n" +
            "app.*.symbols\n\n" +
            "# Obfuscation related\n" +
            "app.*.map.json\n"
        );
    }

    @Override
    public String getTypeName() {
        return "Flutter Application";
    }

    @Override
    public String getDescription() {
        return "Creates a Flutter application with modern architecture and best practices";
    }

    @Override
    public String getMinJavaVersion() {
        return "N/A";
    }
}
