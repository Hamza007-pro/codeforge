"use client";

import * as React from "react";
import { Smartphone } from "lucide-react";
import { Label } from "@/components/ui/label";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Input } from "@/components/ui/input";
import { useProjectStore } from "@/lib/store";
import { cn } from "@/lib/utils";
import { api } from "@/lib/api";
import { LoadingSpinner } from "./ui/loading-spinner";

interface ProjectTypeProps {
  activeTab: string;
  setActiveTab: (tab: string) => void;
}

export default function ProjectType({ activeTab, setActiveTab }: ProjectTypeProps) {
  const {
    setProjectType,
    setFramework,
    setBuildTool,
    setPackageName,
    projectType,
    framework,
    buildTool,
    packageName,
  } = useProjectStore();

  const [projectTypes, setProjectTypes] = React.useState<string[]>([]);
  const [frameworks, setFrameworks] = React.useState<string[]>([]);
  const [platforms, setPlatforms] = React.useState<string[]>([]);
  const [loading, setLoading] = React.useState(false);

  // Fetch project types on mount
  React.useEffect(() => {
    const fetchProjectTypes = async () => {
      setLoading(true);
      try {
        const types = await api.getProjectTypes();
        setProjectTypes(types);
        if (types.length > 0 && !projectType) {
          setProjectType(types[0]);
        }
      } catch (error) {
        console.error("Failed to fetch project types:", error);
      }
      setLoading(false);
    };

    fetchProjectTypes();
  }, []);

  // Fetch frameworks when project type changes
  React.useEffect(() => {
    const fetchFrameworks = async () => {
      if (!projectType) return;
      
      setLoading(true);
      try {
        const [frameworksList, platformsList] = await Promise.all([
          api.getFrameworks(projectType),
          api.getPlatforms(projectType)
        ]);
        setFrameworks(frameworksList);
        setPlatforms(platformsList);
        
        if (frameworksList.length > 0 && !framework) {
          setFramework(frameworksList[0]);
        }
      } catch (error) {
        console.error("Failed to fetch frameworks:", error);
      }
      setLoading(false);
    };

    fetchFrameworks();
  }, [projectType]);

  if (loading && projectTypes.length === 0) {
    return (
      <div className="flex justify-center items-center h-40">
        <LoadingSpinner />
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="space-y-4">
        <Label>Project Type</Label>
        <Select value={projectType} onValueChange={setProjectType}>
          <SelectTrigger>
            <SelectValue placeholder="Select project type" />
          </SelectTrigger>
          <SelectContent>
            {projectTypes.map((type) => (
              <SelectItem key={type} value={type}>
                {type}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
      </div>

      {projectType && (
        <>
          <div className="space-y-4">
            <Label>Framework</Label>
            <Select value={framework} onValueChange={setFramework}>
              <SelectTrigger>
                <SelectValue placeholder="Select framework" />
              </SelectTrigger>
              <SelectContent>
                {frameworks.map((fw) => (
                  <SelectItem key={fw} value={fw}>
                    {fw}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          <div className="space-y-4">
            <Label>Platform</Label>
            <Select
              value={activeTab}
              onValueChange={(value) => {
                setActiveTab(value);
                // Update package name based on platform
                setPackageName(
                  `com.example.${value.toLowerCase()}`
                );
              }}
            >
              <SelectTrigger>
                <SelectValue placeholder="Select platform" />
              </SelectTrigger>
              <SelectContent>
                {platforms.map((platform) => (
                  <SelectItem key={platform} value={platform}>
                    {platform}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          <div className="space-y-4">
            <Label>Package Name</Label>
            <Input
              value={packageName || ""}
              onChange={(e) => setPackageName(e.target.value)}
              placeholder="com.example.app"
            />
          </div>
        </>
      )}
    </div>
  );
}
