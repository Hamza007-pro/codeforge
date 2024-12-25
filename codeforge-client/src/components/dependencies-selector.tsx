"use client";

import * as React from "react";
import { Info } from "lucide-react";
import { Badge } from "@/components/ui/badge";
import { Card } from "@/components/ui/card";
import { Checkbox } from "@/components/ui/checkbox";
import { ScrollArea } from "@/components/ui/scroll-area";
import { useProjectStore } from "@/lib/store";
import { api } from "@/lib/api";
import { Dependency } from "@/lib/types";
import { LoadingSpinner } from "./ui/loading-spinner";

interface DependenciesSelectorProps {
  activeTab: string;
  selectedPackages: string[];
  setSelectedPackages: (packages: string[]) => void;
}

export default function DependenciesSelector({
  activeTab,
  selectedPackages,
  setSelectedPackages,
}: DependenciesSelectorProps) {
  const [dependencies, setDependencies] = React.useState<Dependency[]>([]);
  const [loading, setLoading] = React.useState(false);
  const [error, setError] = React.useState<string | null>(null);

  // Group dependencies by category
  const dependenciesByCategory = React.useMemo(() => {
    const grouped: Record<string, Dependency[]> = {};
    dependencies.forEach((dep) => {
      if (!grouped[dep.category]) {
        grouped[dep.category] = [];
      }
      grouped[dep.category].push(dep);
    });
    return grouped;
  }, [dependencies]);

  React.useEffect(() => {
    const fetchDependencies = async () => {
      setLoading(true);
      setError(null);
      try {
        const platform = activeTab.toLowerCase();
        const deps = await api.getDependencies(platform);
        setDependencies(deps);
      } catch (err) {
        setError("Failed to fetch dependencies");
        console.error(err);
      }
      setLoading(false);
    };

    fetchDependencies();
  }, [activeTab]);

  const togglePackage = (artifactId: string) => {
    setSelectedPackages(
      selectedPackages.includes(artifactId)
        ? selectedPackages.filter((p) => p !== artifactId)
        : [...selectedPackages, artifactId]
    );
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-40">
        <LoadingSpinner />
      </div>
    );
  }

  if (error) {
    return (
      <div className="text-center text-destructive">
        <p>{error}</p>
      </div>
    );
  }

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <h2 className="text-lg font-semibold">Dependencies</h2>
        <Badge variant="secondary">
          {selectedPackages.length} selected
        </Badge>
      </div>

      <ScrollArea className="h-[400px] rounded-md border p-4">
        <div className="space-y-8">
          {Object.entries(dependenciesByCategory).map(([category, deps]) => (
            <div key={category} className="space-y-4">
              <h3 className="font-medium text-sm text-muted-foreground">
                {category}
              </h3>
              <div className="space-y-2">
                {deps.map((dep) => (
                  <Card key={dep.artifactId} className="p-4">
                    <div className="flex items-start gap-4">
                      <Checkbox
                        checked={selectedPackages.includes(dep.artifactId)}
                        onCheckedChange={() => togglePackage(dep.artifactId)}
                      />
                      <div className="flex-1 space-y-1">
                        <div className="flex items-center gap-2">
                          <span className="font-medium">{dep.name}</span>
                          <div 
                            className="group relative inline-block"
                            title={`${dep.description}\n${dep.groupId}:${dep.artifactId}:${dep.version}`}
                          >
                            <Info className="h-4 w-4 text-muted-foreground hover:text-foreground transition-colors" />
                          </div>
                          <Badge variant="outline" className="ml-auto">
                            {dep.type}
                          </Badge>
                        </div>
                        <p className="text-sm text-muted-foreground">
                          {dep.description}
                        </p>
                      </div>
                    </div>
                  </Card>
                ))}
              </div>
            </div>
          ))}
        </div>
      </ScrollArea>
    </div>
  );
}
