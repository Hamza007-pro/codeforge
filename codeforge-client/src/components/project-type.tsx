"use client";

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

interface ProjectTypeProps {
  activeTab: string;
  setActiveTab: (tab: string) => void;
}

export default function ProjectType({
  activeTab,
  setActiveTab,
}: ProjectTypeProps) {
  const {
    setProjectType,
    setFramework,
    setBuildTool,
    setPackageName,
    setDependencies,
  } = useProjectStore();

  const handleTabChange = (value: string) => {
    setActiveTab(value);
    setProjectType(value === "web" ? "Web Application" : "Mobile Application");

    // Reset dependencies when switching project type
    setDependencies([]);

    // Set appropriate defaults based on project type
    if (value === "web") {
      setFramework("nextjs");
      setBuildTool("vite");
      setPackageName(null);
    } else {
      setFramework("native");
      setBuildTool("gradle");
      setPackageName("");
    }
  };

  return (
    <div className="space-y-3">
      <h2 className="text-lg font-semibold">Project Type</h2>
      <Tabs
        value={activeTab}
        onValueChange={handleTabChange}
        className="w-full"
      >
        <TabsList className="grid h-auto gap-2 w-full grid-cols-2 p-1 bg-card">
          <TabsTrigger
            value="web"
            className={cn(
              "flex items-center justify-center rounded-md border border-muted bg-card p-3",
              "transition-all",
              "data-[state=active]:border-primary data-[state=active]:bg-gradient-to-r data-[state=active]:from-[hsl(var(--primary-start))] data-[state=active]:to-[hsl(var(--primary-end))] data-[state=active]:text-white"
            )}
          >
            Web Application
          </TabsTrigger>
          <TabsTrigger
            value="mobile"
            className={cn(
              "flex items-center justify-center rounded-md border border-muted bg-card p-3",
              "transition-all",
              "data-[state=active]:border-primary data-[state=active]:bg-gradient-to-r data-[state=active]:from-[hsl(var(--primary-start))] data-[state=active]:to-[hsl(var(--primary-end))] data-[state=active]:text-white"
            )}
          >
            <Smartphone className="mr-2 h-4 w-4" />
            Mobile Application
          </TabsTrigger>
        </TabsList>
        <TabsContent value="web" className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label>Framework</Label>
              <Select
                defaultValue="nextjs"
                onValueChange={(value) => setFramework(value)}
              >
                <SelectTrigger>
                  <SelectValue placeholder="Select framework" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="nextjs">Next.js</SelectItem>
                  <SelectItem
                    value="react"
                    disabled
                    className="text-muted-foreground"
                  >
                    React (Coming Soon)
                  </SelectItem>
                  <SelectItem
                    value="angular"
                    disabled
                    className="text-muted-foreground"
                  >
                    Angular (Coming Soon)
                  </SelectItem>
                  <SelectItem
                    value="vue"
                    disabled
                    className="text-muted-foreground"
                  >
                    Vue.js (Coming Soon)
                  </SelectItem>
                  <SelectItem
                    value="svelte"
                    disabled
                    className="text-muted-foreground"
                  >
                    Svelte (Coming Soon)
                  </SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div className="space-y-2">
              <Label>Build Tool</Label>
              <Select
                defaultValue="vite"
                onValueChange={(value) => setBuildTool(value)}
              >
                <SelectTrigger>
                  <SelectValue placeholder="Select build tool" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="vite">Vite</SelectItem>
                  <SelectItem value="webpack">Webpack</SelectItem>
                  <SelectItem value="parcel">Parcel</SelectItem>
                </SelectContent>
              </Select>
            </div>
          </div>
        </TabsContent>
        <TabsContent value="mobile" className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label>Framework</Label>
              <Select
                defaultValue="native-android"
                onValueChange={(value) => setFramework(value)}
              >
                <SelectTrigger>
                  <SelectValue placeholder="Select framework" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="native-android">Native android</SelectItem>
                  <SelectItem
                    value="native-ios"
                    disabled
                    className="text-muted-foreground"
                  >
                    Native ios (Coming Soon)
                  </SelectItem>
                  <SelectItem
                    value="flutter"
                    disabled
                    className="text-muted-foreground"
                  >
                    Flutter (Coming Soon)
                  </SelectItem>
                  <SelectItem
                    value="react-native"
                    disabled
                    className="text-muted-foreground"
                  >
                    React Native (Coming Soon)
                  </SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div className="space-y-2">
              <Label>Build Tool</Label>
              <Select
                defaultValue="gradle"
                onValueChange={(value) => setBuildTool(value)}
              >
                <SelectTrigger>
                  <SelectValue placeholder="Select build tool" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="gradle">Gradle</SelectItem>
                </SelectContent>
              </Select>
            </div>
          </div>
          <div className="space-y-2">
            <Label>Package Name</Label>
            <Input
              placeholder="com.example.myapp"
              onChange={(e) => setPackageName(e.target.value)}
            />
          </div>
        </TabsContent>
      </Tabs>
    </div>
  );
}
