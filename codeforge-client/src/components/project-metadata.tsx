"use client";

import { useProjectStore } from "@/lib/store";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";

export function ProjectMetadata() {
  const { projectMetadata, setProjectMetadata } = useProjectStore();

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <h2 className="text-lg font-semibold">Project Metadata</h2>
      </div>
      <div className="space-y-4">
        <div className="grid w-full items-center gap-1.5">
          <Label htmlFor="projectName">Project Name</Label>
          <Input
            id="projectName"
            value={projectMetadata.projectName}
            onChange={(e) =>
              setProjectMetadata({ projectName: e.target.value })
            }
            placeholder="my-awesome-project"
          />
        </div>

        <div className="grid w-full items-center gap-1.5">
          <Label htmlFor="description">Description</Label>
          <Input
            id="description"
            value={projectMetadata.description}
            onChange={(e) =>
              setProjectMetadata({ description: e.target.value })
            }
            placeholder="A brief description of your project"
          />
        </div>

        <div className="grid w-full items-center gap-1.5">
          <Label htmlFor="version">Version</Label>
          <Input
            id="version"
            value={projectMetadata.version}
            onChange={(e) => setProjectMetadata({ version: e.target.value })}
            placeholder="0.1.0"
          />
        </div>

        <div className="grid w-full items-center gap-1.5">
          <Label htmlFor="author">Author</Label>
          <Input
            id="author"
            value={projectMetadata.author}
            onChange={(e) => setProjectMetadata({ author: e.target.value })}
            placeholder="Your Name"
          />
        </div>
      </div>
    </div>
  );
}
