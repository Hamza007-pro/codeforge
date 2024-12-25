"use client";

import * as React from "react";
import { Code2, Github, Settings2, Moon, Sun } from "lucide-react";
import { Card, CardContent } from "@/components/ui/card";
import { ProjectMetadata } from "./project-metadata";
import { useProjectStore } from "@/lib/store";
import DependenciesSelector from "./dependencies-selector";
import ProjectType from "./project-type";
import ArchitecturePattern from "./architecture-pattern";
import { SchemaDesigner } from "./schema-designer";
import { Button } from "./ui/button";
import { useTheme } from "next-themes";
import { useToast } from "./ui/use-toast";
import {postData} from "@/utils/api";

export function AppGenerator() {
  const [activeTab, setActiveTab] = React.useState("web");
  const [selectedPackages, setSelectedPackages] = React.useState<string[]>([]);
  const {
    setDependencies,
    projectType,
    framework,
    buildTool,
    packageName,
    architecturePattern,
    projectMetadata,
    dependencies,
  } = useProjectStore();
  const { theme, setTheme } = useTheme();
  const { toast } = useToast();
  const [selectedSchema, setSelectedSchema] = React.useState<string | null>(
    null
  );

  // Project configuration based on active tab
  const getProjectConfig = () => {
    switch (activeTab) {
      case "web":
        return {
          projectName: "demo",
          buildTool: "MAVEN",
          language: "java",
          framework: "spring-boot",
          basePackage: "com.example.web",
        };
      case "mobile":
        return {
          projectName: "demo",
          buildTool: "NPM",
          language: "typescript",
          framework: "react-native",
          basePackage: "com.example.mobile",
        };
      case "desktop":
        return {
          projectName: "demo",
          buildTool: "NPM",
          language: "typescript",
          framework: "electron",
          basePackage: "com.example.desktop",
        };
      default:
        return {
          projectName: "demo",
          buildTool: "MAVEN",
          language: "java",
          framework: "spring-boot",
          basePackage: "com.example",
        };
    }
  };

  React.useEffect(() => {
    setDependencies(selectedPackages);
  }, [selectedPackages, setDependencies]);

  const handleGenerate = () => {
    // Check if a schema is selected
    if (!selectedSchema) {
      toast({
        title: "Error",
        description: "Please select a schema first",
        variant: "destructive",
      });
      return;
    }

    // Get the saved schema from local storage
    const savedSchema = localStorage.getItem("lastSavedSchema");
    if (!savedSchema) {
      toast({
        title: "Error",
        description: "Schema data not found. Please save the schema first",
        variant: "destructive",
      });
      return;
    }

    // Create the project configuration using store values
    const config = {
      projectType,
      projectName: projectMetadata.projectName,
      projectDescription: projectMetadata.description,
      author: projectMetadata.author,
      version: projectMetadata.version,
      buildTool,
      language: activeTab === "web" ? "typescript" : "java",
      framework,
      packageName,
      architecturePattern,
      schema: savedSchema,
      dependencies,
    };

    // Log the formatted JSON
    console.log("Project Configuration:", JSON.stringify(config, null, 2));

    toast({
      title: "Project Configuration",
      description: "Check the console for the formatted JSON output",
    });

    // Send the config to the server

    postData("/api/projects/generate", config)
      .then((data) => {
        console.log("Success:", data);
        toast({
          title: "Project Generated",
          description: "Check the console for the generated project",
        });
      })
      .catch((error) => {
        console.error("Error:", error);
        toast({
          title: "Error",
          description: "Failed to generate the project",
          variant: "destructive",
        });
      }
    );

  };

  return (
    <div className="flex flex-col h-full">
      <header className="shrink-0 border-b border-border/50 bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
        <div className="flex items-center justify-between p-4">
          <div className="flex items-center gap-2">
            <Code2 className="h-6 w-6 text-transparent bg-clip-text bg-gradient-to-r from-[hsl(var(--primary-start))] to-[hsl(var(--primary-end))]" />
            <h1 className="text-xl font-semibold text-transparent bg-clip-text bg-gradient-to-r from-[hsl(var(--primary-start))] to-[hsl(var(--primary-end))]">
              CodeForge
            </h1>
          </div>
          <div className="flex items-center gap-2">
            <Button
              variant="ghost"
              size="icon"
              className="text-muted-foreground hover:text-[hsl(var(--primary-start))]"
              onClick={() => setTheme(theme === "dark" ? "light" : "dark")}
            >
              <Sun className="h-5 w-5 rotate-0 scale-100 transition-all dark:-rotate-90 dark:scale-0" />
              <Moon className="absolute h-5 w-5 rotate-90 scale-0 transition-all dark:rotate-0 dark:scale-100" />
            </Button>
            <Button
              variant="ghost"
              size="icon"
              className="text-muted-foreground hover:text-[hsl(var(--primary-start))]"
            >
              <Github className="h-5 w-5" />
            </Button>
            <Button
              variant="ghost"
              size="icon"
              className="text-muted-foreground hover:text-[hsl(var(--primary-start))]"
            >
              <Settings2 className="h-5 w-5" />
            </Button>
          </div>
        </div>
      </header>

      <main className="flex-1 overflow-hidden">
        <Card className="h-full border-0 rounded-none bg-background">
          <CardContent className="grid h-full grid-cols-2 gap-0 p-0">
            <div className="border-r border-border/50 p-6 space-y-6 overflow-y-auto">
              <ProjectType activeTab={activeTab} setActiveTab={setActiveTab} />
              <ArchitecturePattern />
              <ProjectMetadata />
              <SchemaDesigner onSchemaSelect={setSelectedSchema} />
            </div>
            <div className="p-6">
              <DependenciesSelector
                activeTab={activeTab}
                selectedPackages={selectedPackages}
                setSelectedPackages={setSelectedPackages}
              />
            </div>
          </CardContent>
        </Card>
      </main>

      <footer className="shrink-0 border-t border-border/50 bg-secondary backdrop-blur supports-[backdrop-filter]:bg-secondary/80">
        <div className="flex items-center justify-between p-4">
          <div className="flex items-center gap-4">
            <Button className="gradient-button" onClick={handleGenerate}>
              GENERATE
              <kbd className="ml-2 keyboard-shortcut">CTRL + ⏎</kbd>
            </Button>
            <Button
              variant="secondary"
              className="bg-background hover:bg-background/90"
            >
              EXPLORE
              <kbd className="ml-2 keyboard-shortcut">CTRL + SPACE</kbd>
            </Button>
          </div>
          <div className="flex items-center gap-2">
            <Button
              variant="ghost"
              size="sm"
              className="text-sm text-muted-foreground hover:text-[hsl(var(--primary-start))] hover:bg-background/90"
            >
              Add Dependencies...
              <kbd className="ml-2 keyboard-shortcut">CTRL + B</kbd>
            </Button>
          </div>
        </div>
      </footer>
    </div>
  );
}
