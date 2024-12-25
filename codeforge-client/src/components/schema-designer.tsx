"use client";

import * as React from "react";
import { Database, Plus, Pencil, Check, X } from "lucide-react";
import { Button } from "./ui/button";
import { ScrollArea } from "./ui/scroll-area";
import { Checkbox } from "./ui/checkbox";
import { Card, CardDescription, CardHeader, CardTitle } from "./ui/card";
import { cn } from "@/lib/utils";
import { useRouter } from "next/navigation";

interface SchemaDesignerProps {
  onSchemaSelect?: (schemaId: string | null) => void;
}

export function SchemaDesigner({ onSchemaSelect }: SchemaDesignerProps) {
  const router = useRouter();
  const [selectedSchema, setSelectedSchema] = React.useState<string | null>(
    null
  );
  const [recentSchemas] = React.useState([
    {
      id: "blog",
      name: "Blog Schema",
      description: "A simple blog with posts and comments",
    },
    {
      id: "ecommerce",
      name: "E-commerce",
      description: "Online store with products and orders",
    },
    {
      id: "basic",
      name: "Basic App",
      description: "User profiles and authentication",
    },
  ]);

  const handleSchemaSelect = (schemaId: string) => {
    const newSelection = selectedSchema === schemaId ? null : schemaId;
    setSelectedSchema(newSelection);
    onSchemaSelect?.(newSelection);
  };

  const handleEditSchema = (schemaId: string) => {
    router.push(`/schema-editor?template=${schemaId}`);
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-2">
          <h2 className="text-lg font-semibold">Data Schema</h2>
        </div>
        <div className="flex gap-2">
          <Button
            variant="outline"
            onClick={() => router.push("/schema-editor")}
            className="gap-2"
          >
            <Plus className="h-4 w-4" />
            Create New Schema
          </Button>
        </div>
      </div>
      <div>
        {selectedSchema && (
          <Button
            variant="outline"
            onClick={() => handleSchemaSelect(selectedSchema)}
            className="gap-2"
          >
            <X className="h-4 w-4" />
            Clear Selection{" "}
            {selectedSchema && (
              <span className="text-sm text-muted-foreground">1 selected</span>
            )}
          </Button>
        )}
      </div>

      <ScrollArea className="h-[300px] bg-slate-50 rounded-xl p-4 border">
        <div className="grid grid-cols-1 gap-4">
          {recentSchemas.map((schema) => (
            <Card
              key={schema.id}
              className={cn(
                "transition-colors",
                selectedSchema === schema.id
                  ? "border-primary bg-gradient-to-r from-[hsl(var(--primary-start))] to-[hsl(var(--primary-end))] text-white"
                  : ""
              )}
            >
              <CardHeader className="p-3 flex flex-row items-center justify-between">
                <div className="flex items-center gap-4">
                  <Checkbox
                    checked={selectedSchema === schema.id}
                    onCheckedChange={() => handleSchemaSelect(schema.id)}
                    className={cn(
                      selectedSchema === schema.id
                        ? "border-white data-[state=checked]:bg-white data-[state=checked]:text-primary"
                        : ""
                    )}
                  />
                  <div>
                    <CardTitle className="text-md">{schema.name}</CardTitle>
                    <CardDescription
                      className={cn(
                        selectedSchema === schema.id
                          ? "text-white/90"
                          : ""
                      )}
                    >
                      {schema.description}
                    </CardDescription>
                  </div>
                </div>
                <Button
                  variant="ghost"
                  size="icon"
                  onClick={() => handleEditSchema(schema.id)}
                  className={cn(
                    selectedSchema === schema.id
                      ? "text-white hover:text-white/90 hover:bg-white/10"
                      : ""
                  )}
                >
                  <Pencil className="h-4 w-4" />
                </Button>
              </CardHeader>
            </Card>
          ))}
        </div>
      </ScrollArea>
    </div>
  );
}
