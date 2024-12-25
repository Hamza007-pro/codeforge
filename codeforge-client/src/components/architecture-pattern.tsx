"use client";

import * as React from "react";
import { Label } from "@/components/ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { useProjectStore } from "@/lib/store";
import { api } from "@/lib/api";
import { LoadingSpinner } from "./ui/loading-spinner";

export default function ArchitecturePattern() {
  const { setArchitecturePattern, architecturePattern, projectType } = useProjectStore();
  const [architectures, setArchitectures] = React.useState<string[]>([]);
  const [loading, setLoading] = React.useState(false);

  React.useEffect(() => {
    const fetchArchitectures = async () => {
      if (!projectType) return;
      
      setLoading(true);
      try {
        const patterns = await api.getArchitectures(projectType, "web"); // Using "web" as default platform
        setArchitectures(patterns);
        if (patterns.length > 0 && !architecturePattern) {
          setArchitecturePattern(patterns[0]);
        }
      } catch (error) {
        console.error("Failed to fetch architectures:", error);
      }
      setLoading(false);
    };

    fetchArchitectures();
  }, [projectType]);

  if (loading && architectures.length === 0) {
    return (
      <div className="flex justify-center items-center h-20">
        <LoadingSpinner />
      </div>
    );
  }

  return (
    <div className="space-y-4">
      <Label>Architecture Pattern</Label>
      <Select value={architecturePattern} onValueChange={setArchitecturePattern}>
        <SelectTrigger>
          <SelectValue placeholder="Select architecture pattern" />
        </SelectTrigger>
        <SelectContent>
          {architectures.map((pattern) => (
            <SelectItem key={pattern} value={pattern}>
              {pattern}
            </SelectItem>
          ))}
        </SelectContent>
      </Select>
    </div>
  );
}
