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
import { LoadingSpinner } from "./ui/loading-spinner";
import { GeneratedProjects } from "./generated-projects";

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
    generateProject,
    loading,
    error,
  } = useProjectStore();
  const { theme, setTheme } = useTheme();
  const { toast } = useToast();
  const [selectedSchema, setSelectedSchema] = React.useState<string | null>(null);

  React.useEffect(() => {
    // Load available templates when component mounts
    useProjectStore.getState().fetchTemplates();
  }, []);

  const handleGenerate = async () => {
    try {
      const project = await generateProject();
      toast({
        title: "Success",
        description: "Project generated successfully!",
        variant: "default",
      });
      
      // Automatically start download
      await useProjectStore.getState().downloadProject(project.id);
    } catch (error) {
      toast({
        title: "Error",
        description: error instanceof Error ? error.message : "Failed to generate project",
        variant: "destructive",
      });
    }
  };

  return (
    <div className="min-h-screen bg-background">
      {/* Header */}
      <header className="border-b">
        <div className="container mx-auto py-4">
          <div className="flex justify-between items-center">
            <div className="flex items-center gap-4">
              <Code2 className="h-8 w-8" />
              <h1 className="text-2xl font-bold">Project Generator</h1>
            </div>
            <div className="flex items-center gap-4">
              <Button
                variant="ghost"
                size="icon"
                onClick={() => setTheme(theme === "light" ? "dark" : "light")}
              >
                {theme === "light" ? (
                  <Moon className="h-5 w-5" />
                ) : (
                  <Sun className="h-5 w-5" />
                )}
              </Button>
              <Button variant="outline" className="gap-2">
                <Github className="h-4 w-4" />
                View on GitHub
              </Button>
              <Button variant="outline" className="gap-2">
                <Settings2 className="h-4 w-4" />
                Settings
              </Button>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="container mx-auto py-6">
        <div className="grid grid-cols-12 gap-6">
          {/* Left Column - Project Configuration */}
          <div className="col-span-8">
            <Card>
              <CardContent className="p-6 space-y-8">
                <ProjectType activeTab={activeTab} setActiveTab={setActiveTab} />
                <ProjectMetadata />
                <ArchitecturePattern />
                <DependenciesSelector
                  activeTab={activeTab}
                  selectedPackages={selectedPackages}
                  setSelectedPackages={setSelectedPackages}
                />
              </CardContent>
            </Card>
          </div>

          {/* Right Column - Schema Designer and Generated Projects */}
          <div className="col-span-4 space-y-6">
            {/* Schema Designer */}
            <Card>
              <CardContent className="p-6">
                <SchemaDesigner onSchemaSelect={setSelectedSchema} />
              </CardContent>
            </Card>

            {/* Generate Button */}
            <Button
              className="w-full"
              size="lg"
              onClick={handleGenerate}
              disabled={loading}
            >
              {loading ? (
                <>
                  <LoadingSpinner className="mr-2" />
                  Generating...
                </>
              ) : (
                "Generate Project"
              )}
            </Button>

            {/* Error Message */}
            {error && (
              <p className="text-sm text-destructive mt-2">{error}</p>
            )}

            {/* Generated Projects List */}
            <GeneratedProjects />
          </div>
        </div>
      </main>
    </div>
  );
}
