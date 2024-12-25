package com.codeforge.engine.structure.types;

import com.codeforge.engine.structure.ProjectStructure;
import com.codeforge.engine.structure.ProjectType;
import com.codeforge.engine.structure.build.WebpackBuildSystem;
import com.codeforge.engine.structure.dependency.Dependency;
import com.codeforge.engine.structure.dependency.DependencyGroup;
import com.codeforge.engine.structure.dependency.DependencyManager;

/**
 * React project type implementation with webpack configuration.
 */
public class ReactProjectType implements ProjectType {
    private final WebpackBuildSystem buildSystem = new WebpackBuildSystem();

    @Override
    public ProjectStructure createProjectStructure(String basePackage, String projectName, DependencyManager dependencyManager) {
        ProjectStructure structure = new ProjectStructure(projectName);

        // Add standard directories
        structure.addDirectory("src");
        structure.addDirectory("src/components");
        structure.addDirectory("src/pages");
        structure.addDirectory("src/assets");
        structure.addDirectory("src/styles");
        structure.addDirectory("src/utils");
        structure.addDirectory("public");
        structure.addDirectory("tests");

        // Add configuration files
        structure.addFile("package.json", buildSystem.createPackageJson(projectName, "1.0.0", dependencyManager.getSelectedDependencies()));
        structure.addFile("webpack.config.js", buildSystem.createBuildConfig(projectName, "1.0.0", dependencyManager.getSelectedDependencies()));
        structure.addFile(".gitignore", createGitignore());
        structure.addFile("README.md", createReadme(projectName));
        structure.addFile(".babelrc", createBabelConfig());
        structure.addFile("src/index.js", createIndexJs());
        structure.addFile("src/App.js", createAppJs());
        structure.addFile("src/styles/App.css", createAppCss());
        structure.addFile("public/index.html", createIndexHtml(projectName));

        customizeStructure(structure);

        return structure;
    }

    @Override
    public DependencyManager getDefaultDependencyManager() {
        DependencyManager manager = new DependencyManager();

        // Core dependencies (required)
        DependencyGroup core = new DependencyGroup("Core", "Essential React dependencies", true);
        core.addDependency(Dependency.builder()
            .groupId("react")
            .artifactId("react")
            .version("18.2.0")
            .build());
        core.addDependency(Dependency.builder()
            .groupId("react-dom")
            .artifactId("react-dom")
            .version("18.2.0")
            .build());
        manager.addGroup(core);

        // Routing
        DependencyGroup routing = new DependencyGroup("Routing", "React Router for navigation", false);
        routing.addDependency(Dependency.builder()
            .groupId("react-router-dom")
            .artifactId("react-router-dom")
            .version("6.21.1")
            .build());
        manager.addGroup(routing);

        // State Management
        DependencyGroup state = new DependencyGroup("State Management", "State management libraries", false);
        state.addDependency(Dependency.builder()
            .groupId("redux")
            .artifactId("redux")
            .version("5.0.1")
            .build());
        state.addDependency(Dependency.builder()
            .groupId("react-redux")
            .artifactId("react-redux")
            .version("9.0.4")
            .build());
        manager.addGroup(state);

        return manager;
    }

    private String createGitignore() {
        return """
            # dependencies
            /node_modules
            
            # production
            /dist
            
            # misc
            .DS_Store
            .env.local
            .env.development.local
            .env.test.local
            .env.production.local
            
            npm-debug.log*
            yarn-debug.log*
            yarn-error.log*
            """;
    }

    private String createReadme(String projectName) {
        return """
            # %s
            
            This is a React application generated using CodeForge.
            
            ## Available Scripts
            
            In the project directory, you can run:
            
            ### `npm start`
            
            Runs the app in development mode.
            Open [http://localhost:3000](http://localhost:3000) to view it in your browser.
            
            ### `npm test`
            
            Launches the test runner in interactive watch mode.
            
            ### `npm run build`
            
            Builds the app for production to the `dist` folder.
            
            ## Project Structure
            
            ```
            src/
              components/  - Reusable components
              pages/      - Page components
              assets/     - Images, fonts, etc.
              styles/     - CSS files
              utils/      - Utility functions
              index.js    - Entry point
              App.js      - Root component
            ```
            """.formatted(projectName);
    }

    private String createBabelConfig() {
        return """
            {
              "presets": [
                "@babel/preset-env",
                "@babel/preset-react"
              ]
            }
            """;
    }

    private String createIndexJs() {
        return """
            import React from 'react';
            import { createRoot } from 'react-dom/client';
            import App from './App';
            import './styles/App.css';
            
            const container = document.getElementById('root');
            const root = createRoot(container);
            root.render(
              <React.StrictMode>
                <App />
              </React.StrictMode>
            );
            """;
    }

    private String createAppJs() {
        return """
            import React from 'react';
            
            function App() {
              return (
                <div className="App">
                  <header className="App-header">
                    <h1>Welcome to React</h1>
                    <p>
                      Edit <code>src/App.js</code> and save to reload.
                    </p>
                  </header>
                </div>
              );
            }
            
            export default App;
            """;
    }

    private String createAppCss() {
        return """
            .App {
              text-align: center;
            }
            
            .App-header {
              background-color: #282c34;
              min-height: 100vh;
              display: flex;
              flex-direction: column;
              align-items: center;
              justify-content: center;
              font-size: calc(10px + 2vmin);
              color: white;
            }
            """;
    }

    private String createIndexHtml(String projectName) {
        return """
            <!DOCTYPE html>
            <html lang="en">
              <head>
                <meta charset="utf-8" />
                <link rel="icon" href="favicon.ico" />
                <meta name="viewport" content="width=device-width, initial-scale=1" />
                <meta name="theme-color" content="#000000" />
                <meta name="description" content="%s - Created with CodeForge" />
                <title>%s</title>
              </head>
              <body>
                <noscript>You need to enable JavaScript to run this app.</noscript>
                <div id="root"></div>
              </body>
            </html>
            """.formatted(projectName, projectName);
    }

    @Override
    public void customizeStructure(ProjectStructure structure) {
        // Add React specific directories
        structure.addDirectory("src");
        structure.addDirectory("src/components");
        structure.addDirectory("src/pages");
        structure.addDirectory("src/assets");
        structure.addDirectory("src/styles");
        structure.addDirectory("public");

        // Add configuration files
        structure.addFile("package.json",
            "{\n" +
            "  \"name\": \"${projectName}\",\n" +
            "  \"private\": true,\n" +
            "  \"version\": \"0.0.0\",\n" +
            "  \"type\": \"module\",\n" +
            "  \"scripts\": {\n" +
            "    \"dev\": \"vite\",\n" +
            "    \"build\": \"tsc && vite build\",\n" +
            "    \"preview\": \"vite preview\"\n" +
            "  }\n" +
            "}\n"
        );

        structure.addFile("tsconfig.json",
            "{\n" +
            "  \"compilerOptions\": {\n" +
            "    \"target\": \"ES2020\",\n" +
            "    \"useDefineForClassFields\": true,\n" +
            "    \"lib\": [\"ES2020\", \"DOM\", \"DOM.Iterable\"],\n" +
            "    \"module\": \"ESNext\",\n" +
            "    \"skipLibCheck\": true,\n" +
            "    \"moduleResolution\": \"bundler\",\n" +
            "    \"allowImportingTsExtensions\": true,\n" +
            "    \"resolveJsonModule\": true,\n" +
            "    \"isolatedModules\": true,\n" +
            "    \"noEmit\": true,\n" +
            "    \"jsx\": \"react-jsx\",\n" +
            "    \"strict\": true,\n" +
            "    \"noUnusedLocals\": true,\n" +
            "    \"noUnusedParameters\": true,\n" +
            "    \"noFallthroughCasesInSwitch\": true\n" +
            "  },\n" +
            "  \"include\": [\"src\"],\n" +
            "  \"references\": [{ \"path\": \"./tsconfig.node.json\" }]\n" +
            "}\n"
        );

        structure.addFile("vite.config.ts",
            "import { defineConfig } from 'vite'\n" +
            "import react from '@vitejs/plugin-react'\n\n" +
            "export default defineConfig({\n" +
            "  plugins: [react()],\n" +
            "})\n"
        );

        // Add .gitignore
        structure.addFile(".gitignore",
            "# Logs\n" +
            "logs\n" +
            "*.log\n" +
            "npm-debug.log*\n" +
            "yarn-debug.log*\n" +
            "yarn-error.log*\n" +
            "pnpm-debug.log*\n" +
            "lerna-debug.log*\n\n" +
            "node_modules\n" +
            "dist\n" +
            "dist-ssr\n" +
            "*.local\n\n" +
            "# Editor directories and files\n" +
            ".vscode/*\n" +
            "!.vscode/extensions.json\n" +
            ".idea\n" +
            ".DS_Store\n" +
            "*.suo\n" +
            "*.ntvs*\n" +
            "*.njsproj\n" +
            "*.sln\n" +
            "*.sw?\n"
        );

        // Add basic React components
        structure.addFile("src/App.tsx",
            "import { useState } from 'react'\n" +
            "import './App.css'\n\n" +
            "function App() {\n" +
            "  const [count, setCount] = useState(0)\n\n" +
            "  return (\n" +
            "    <div className=\"App\">\n" +
            "      <h1>${projectName}</h1>\n" +
            "      <div className=\"card\">\n" +
            "        <button onClick={() => setCount((count) => count + 1)}>\n" +
            "          count is {count}\n" +
            "        </button>\n" +
            "      </div>\n" +
            "    </div>\n" +
            "  )\n" +
            "}\n\n" +
            "export default App\n"
        );

        structure.addFile("src/main.tsx",
            "import React from 'react'\n" +
            "import ReactDOM from 'react-dom/client'\n" +
            "import App from './App'\n" +
            "import './index.css'\n\n" +
            "ReactDOM.createRoot(document.getElementById('root')!).render(\n" +
            "  <React.StrictMode>\n" +
            "    <App />\n" +
            "  </React.StrictMode>,\n" +
            ")\n"
        );
    }

    @Override
    public String getTypeName() {
        return "React Application";
    }

    @Override
    public String getDescription() {
        return "Creates a React application with webpack configuration";
    }

    @Override
    public String getMinJavaVersion() {
        return "N/A";
    }
}
