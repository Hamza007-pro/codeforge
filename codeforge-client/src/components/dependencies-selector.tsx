"use client";

import * as React from "react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Search, Package } from "lucide-react";
import { useProjectStore } from "@/lib/store";

const NPM_PACKAGES = [
  {
    name: "@tanstack/react-query",
    description: "Powerful asynchronous state management",
    version: "5.0.0",
    category: "Development",
  },
  {
    name: "swr",
    description: "React Hooks for Data Fetching",
    version: "2.2.0",
    category: "Development",
  },
  {
    name: "typescript",
    description: "TypeScript is JavaScript with syntax for types",
    version: "5.3.0",
    category: "Development",
  },
  {
    name: "vite",
    description: "Next Generation Frontend Tooling",
    version: "5.0.0",
    category: "Build Tools",
  },
  {
    name: "webpack",
    description: "Module bundler",
    version: "5.89.0",
    category: "Build Tools",
  },
];

const FLUTTER_PACKAGES = [
  {
    name: "provider",
    description: "A wrapper around InheritedWidget",
    version: "6.1.1",
    category: "State Management",
  },
  {
    name: "get_it",
    description: "Simple Service Locator",
    version: "7.6.4",
    category: "Dependency Injection",
  },
];

export default function DependenciesSelector({
  activeTab,
  selectedPackages,
  setSelectedPackages,
}: {
  activeTab: string;
  selectedPackages: string[];
  setSelectedPackages: (packages: string[]) => void;
}) {
  const [searchQuery, setSearchQuery] = React.useState("");
  const packages = activeTab === "web" ? NPM_PACKAGES : FLUTTER_PACKAGES;

  const filteredPackages = packages.filter((pkg) =>
    pkg.name.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const groupedPackages = filteredPackages.reduce((acc, pkg) => {
    if (!acc[pkg.category]) {
      acc[pkg.category] = [];
    }
    acc[pkg.category].push(pkg);
    return acc;
  }, {} as Record<string, typeof NPM_PACKAGES>);

  return (
    <div className="h-full flex flex-col">
      <div className="flex flex-col space-y-4">
        <div className="flex items-center gap-2">
          <Package className="h-5 w-5 text-transparent bg-clip-text bg-gradient-to-r from-[hsl(var(--primary-start))] to-[hsl(var(--primary-end))]" />
          <h2 className="text-lg font-semibold">Dependencies</h2>
        </div>
        <div className="relative">
          <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
          <Input
            type="search"
            placeholder="Search dependencies..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="pl-9 input-field"
          />
        </div>
      </div>

      <div className="mt-6 flex-1 overflow-auto">
        {Object.entries(groupedPackages).map(([category, pkgs]) => (
          <div key={category} className="mb-6 last:mb-0">
            <div className="mb-2 text-sm font-medium text-muted-foreground">
              {category}
            </div>
            <div className="space-y-1">
              {pkgs.map((pkg) => (
                <button
                  key={pkg.name}
                  onClick={() => {
                    if (selectedPackages.includes(pkg.name)) {
                      setSelectedPackages(
                        selectedPackages.filter((name) => name !== pkg.name)
                      );
                    } else {
                      setSelectedPackages([...selectedPackages, pkg.name]);
                    }
                  }}
                  className={`w-full text-left px-3 py-2 rounded-md text-sm transition-colors hover:bg-secondary ${
                    selectedPackages.includes(pkg.name)
                      ? "bg-gradient-to-r from-[hsl(var(--primary-start))] to-[hsl(var(--primary-end))]"
                      : "text-foreground hover:text-foreground"
                  }`}
                >
                  <div className="flex items-center justify-between">
                    <div>
                      <div className={`font-medium ${selectedPackages.includes(pkg.name) ? "text-white" : ""}`}>
                        {pkg.name}
                      </div>
                      <div className={`text-xs ${selectedPackages.includes(pkg.name) ? "text-white/80" : "text-muted-foreground"}`}>
                        {pkg.description}
                      </div>
                    </div>
                    <div className={`text-xs ${selectedPackages.includes(pkg.name) ? "text-white/70" : "text-muted-foreground"}`}>
                      {pkg.version}
                    </div>
                  </div>
                </button>
              ))}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
