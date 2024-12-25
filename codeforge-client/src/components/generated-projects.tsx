"use client";

import * as React from "react";
import { Download, Trash2 } from "lucide-react";
import { Card, CardContent, CardHeader, CardTitle } from "./ui/card";
import { Button } from "./ui/button";
import { useProjectStore } from "@/lib/store";
import { LoadingSpinner } from "./ui/loading-spinner";
import { formatDistanceToNow } from "date-fns";

export function GeneratedProjects() {
  const { generatedProjects, loading, downloadProject } = useProjectStore();

  React.useEffect(() => {
    // Load projects when component mounts
    useProjectStore.getState().fetchProjects();
  }, []);

  if (loading && generatedProjects.length === 0) {
    return (
      <div className="flex justify-center items-center h-40">
        <LoadingSpinner />
      </div>
    );
  }

  return (
    <Card>
      <CardHeader>
        <CardTitle>Generated Projects</CardTitle>
      </CardHeader>
      <CardContent>
        <div className="space-y-4">
          {generatedProjects.length === 0 ? (
            <p className="text-center text-muted-foreground">
              No projects generated yet
            </p>
          ) : (
            generatedProjects.map((project) => (
              <div
                key={project.id}
                className="flex items-center justify-between p-4 rounded-lg border"
              >
                <div>
                  <h3 className="font-medium">{project.name}</h3>
                  <div className="text-sm text-muted-foreground space-y-1">
                    <p>Type: {project.projectType}</p>
                    <p>Architecture: {project.architecture}</p>
                    <p>
                      Generated:{" "}
                      {formatDistanceToNow(new Date(project.generatedAt), {
                        addSuffix: true,
                      })}
                    </p>
                  </div>
                </div>
                <div className="flex gap-2">
                  <Button
                    variant="outline"
                    size="icon"
                    onClick={() => downloadProject(project.id)}
                  >
                    <Download className="h-4 w-4" />
                  </Button>
                </div>
              </div>
            ))
          )}
        </div>
      </CardContent>
    </Card>
  );
}
