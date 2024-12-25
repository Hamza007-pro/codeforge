"use client";

import React, { useCallback, useEffect, useRef } from "react";
import { ArrowLeft, Sun, Moon } from "lucide-react";
import { SimplSchemaService } from "@/utils/DSL/service";
import { Schema } from "@/utils/DSL/visitor";
import { SchemaViewer } from "@/components/schema-viewer";
import { Button } from "@/components/ui/button";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { useRouter, useSearchParams } from "next/navigation";
import Editor, { loader } from "@monaco-editor/react";
import { useToast } from "@/components/ui/use-toast";

loader.config({ monaco: undefined });

export default function SchemaEditorPage() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const template = searchParams.get("template") as
    | "blog"
    | "ecommerce"
    | "basic"
    | null;

  const [schemaText, setSchemaText] = React.useState(
    SimplSchemaService.generateSampleSchema(template || "basic")
  );
  const [schema, setSchema] = React.useState<Schema | null>(null);
  const [isDarkTheme, setIsDarkTheme] = React.useState(true);
  const [validationResult, setValidationResult] = React.useState<{
    isValid: boolean;
    errors: any[];
  }>({
    isValid: true,
    errors: [],
  });

  const editorRef = useRef<any>(null);
  const monacoRef = useRef<any>(null);

  interface DetailedError {
    original?: {
      token?: {
        image: string;
        startOffset: number;
        endOffset: number;
        startLine: number;
        endLine: number;
        startColumn: number;
        endColumn: number;
        tokenType: {
          name: string;
        };
      };
    };
    processed?: {
      line: number;
      column: number;
      message: string;
    };
  }

  const updateErrorMarkers = useCallback((errors: DetailedError[]) => {
    if (!editorRef.current || !monacoRef.current) return;

    const model = editorRef.current.getModel();
    if (!model) return;

    if (errors.length === 0) {
      monacoRef.current.editor.setModelMarkers(model, "schema-validator", []);
      return;
    }

    const markers = errors.map((error) => {
      if (error.original?.token) {
        const token = error.original.token;
        return {
          severity: monacoRef.current.MarkerSeverity.Error,
          startLineNumber: token.startLine,
          startColumn: token.startColumn,
          endLineNumber: token.endLine,
          endColumn: token.endColumn,
          message: error.processed?.message || `Invalid token "${token.image}"`,
          source: "schema-validator",
        };
      } else if (error.processed) {
        return {
          severity: monacoRef.current.MarkerSeverity.Error,
          startLineNumber: error.processed.line,
          startColumn: error.processed.column,
          endLineNumber: error.processed.line,
          endColumn: error.processed.column + 1,
          message: error.processed.message,
          source: "schema-validator",
        };
      } else {
        // Fallback for simple error format
        const err = error as any;
        return {
          severity: monacoRef.current.MarkerSeverity.Error,
          startLineNumber: err.line || 1,
          startColumn: err.column || 1,
          endLineNumber: err.line || 1,
          endColumn: (err.column || 1) + 1,
          message: err.message || "Unknown error",
          source: "schema-validator",
        };
      }
    });

    monacoRef.current.editor.setModelMarkers(
      model,
      "schema-validator",
      markers
    );
  }, []);

  useEffect(() => {
    // Always call updateErrorMarkers to either set new markers or clear existing ones
    updateErrorMarkers(validationResult.errors);
  }, [validationResult, updateErrorMarkers]);

  const handleEditorWillMount = useCallback(
    (monaco: typeof import("monaco-editor")) => {
      // Register the schema language
      monaco.languages.register({ id: "simpl" });

      // Language syntax highlighting configuration
      monaco.languages.setMonarchTokensProvider("simpl", {
        keywords: ["model", "link", "required", "unique", "positive", "long"],
        typeKeywords: ["text", "number", "date", "boolean", "file"],
        operators: ["->", "<->"],

        tokenizer: {
          root: [
            [
              /[a-zA-Z_]\w*/,
              {
                cases: {
                  "@keywords": "keyword",
                  "@typeKeywords": "type",
                  "@default": "identifier",
                },
              },
            ],
            [/->|<->/, "operator"],
            [/[{}()\[\]]/, "delimiter"],
            [/\/\/.*$/, "comment"],
            [/:/, "delimiter"],
          ],
        },
      });

      // Language configuration for brackets and auto-closing
      monaco.languages.setLanguageConfiguration("simpl", {
        brackets: [
          ["{", "}"],
          ["[", "]"],
          ["(", ")"],
        ],
        autoClosingPairs: [
          { open: "{", close: "}" },
          { open: "(", close: ")" },
          { open: "[", close: "]" },
        ],
        surroundingPairs: [
          { open: "{", close: "}" },
          { open: "(", close: ")" },
          { open: "[", close: "]" },
        ],
      });

      // Add error decoration styling
      monaco.editor.defineTheme("simpl-dark", {
        base: "vs-dark",
        inherit: true,
        rules: [
          { token: "keyword", foreground: "569CD6" },
          { token: "type", foreground: "4EC9B0" },
          { token: "identifier", foreground: "9CDCFE" },
          { token: "operator", foreground: "D4D4D4" },
          { token: "delimiter", foreground: "D4D4D4" },
          { token: "comment", foreground: "6A9955" },
          { token: "error", foreground: "ff0000", fontStyle: "bold" },
        ],
        colors: {
          "editor.background": "#1e1e1e",
          "editorError.foreground": "#ff0000",
          "editorError.border": "#ff0000",
          "editorError.background": "#ff000020",
        },
      });

      monaco.editor.defineTheme("simpl-light", {
        base: "vs",
        inherit: true,
        rules: [
          { token: "keyword", foreground: "0000FF" },
          { token: "type", foreground: "267F99" },
          { token: "identifier", foreground: "001080" },
          { token: "operator", foreground: "000000" },
          { token: "delimiter", foreground: "000000" },
          { token: "comment", foreground: "008000" },
          { token: "error", foreground: "ff0000", fontStyle: "bold" },
        ],
        colors: {
          "editorError.foreground": "#ff0000",
          "editorError.border": "#ff0000",
          "editorError.background": "#ff000020",
        },
      });
    },
    []
  );

  const handleEditorDidMount = useCallback(
    (editor: any, monaco: any) => {
      editorRef.current = editor;
      monacoRef.current = monaco;

      // Set initial error markers if any
      if (validationResult.errors.length > 0) {
        updateErrorMarkers(validationResult.errors);
      }
    },
    [validationResult, updateErrorMarkers]
  );

  const handleSchemaChange = (value: string | undefined) => {
    if (!value) return;
    setSchemaText(value);

    try {
      const validation = SimplSchemaService.validateSchema(value);
      console.log("Validation result:", validation);

      setValidationResult(validation);

      if (validation.isValid) {
        // Clear any existing error markers
        updateErrorMarkers([]);
        try {
          const parsedSchema = SimplSchemaService.parseSchema(value);
          setSchema(parsedSchema);
        } catch (error) {
          console.error("Parse error:", error);
          setSchema(null);
        }
      } else {
        setSchema(null);
      }
    } catch (error) {
      console.error("Schema validation error:", error);
      setSchema(null);
    }
  };

  const handleSampleChange = (type: "blog" | "ecommerce" | "basic") => {
    const newSchema = SimplSchemaService.generateSampleSchema(type);
    setSchemaText(newSchema);
    handleSchemaChange(newSchema);
  };

  const { toast } = useToast();

  const handleSave = useCallback(async () => {
    if (!schemaText) {
      toast({
        title: "Error",
        description: "Please enter a schema first",
        variant: "destructive",
      });
      return;
    }

    // Validate schema
    const validation = SimplSchemaService.validateSchema(schemaText);
    if (!validation.isValid) {
      toast({
        title: "Schema Validation Error",
        description: "Please fix the errors in your schema before saving",
        variant: "destructive",
      });
      return;
    }

    try {
      // Parse schema to get model information
      const schema = SimplSchemaService.parseSchema(schemaText);
      
      // Here you would typically save to local storage or your backend
      localStorage.setItem('lastSavedSchema', schemaText);
      
      toast({
        title: "Success",
        description: "Schema saved successfully",
      });
      
      // Navigate back to home
      router.push('/');
    } catch (error) {
      console.error("Schema save error:", error);
      toast({
        title: "Error",
        description: error instanceof Error ? error.message : "Failed to save schema",
        variant: "destructive",
      });
    }
  }, [schemaText, toast, router]);

  useEffect(() => {
    handleSchemaChange(schemaText);
  }, []);

  return (
    <div className="min-h-screen bg-background">
      <div className="h-[60px] border-b flex items-center justify-between px-4">
        <div className="flex items-center gap-4">
          <Button variant="ghost" size="icon" onClick={() => router.back()}>
            <ArrowLeft className="h-4 w-4" />
          </Button>
          <div className="flex items-center">
            <h1 className="text-lg font-semibold">Schema Editor</h1>
          </div>
        </div>
        <div className="flex items-center gap-2">
          <Button variant="outline" onClick={() => handleSampleChange("basic")}>
            Basic App
          </Button>
          <Button variant="outline" onClick={() => handleSampleChange("blog")}>
            Blog
          </Button>
          <Button
            variant="outline"
            onClick={() => handleSampleChange("ecommerce")}
          >
            E-commerce
          </Button>
          <Button
            variant="ghost"
            size="icon"
            onClick={() => setIsDarkTheme(!isDarkTheme)}
          >
            {isDarkTheme ? (
              <Sun className="h-4 w-4" />
            ) : (
              <Moon className="h-4 w-4" />
            )}
          </Button>
          <Button onClick={handleSave}>Save Schema</Button>
        </div>
      </div>

      <div className="container p-6 mx-auto">
        <Tabs defaultValue="code" className="w-full">
          <TabsList>
            <TabsTrigger value="code">Code</TabsTrigger>
            <TabsTrigger value="preview">Preview</TabsTrigger>
          </TabsList>

          <TabsContent value="code" className="space-y-4">
            <div className="min-h-[600px] border rounded-md overflow-hidden">
              <Editor
                height="600px"
                defaultLanguage="simpl"
                theme={isDarkTheme ? "simpl-dark" : "simpl-light"}
                beforeMount={handleEditorWillMount}
                onMount={handleEditorDidMount}
                value={schemaText}
                onChange={handleSchemaChange}
                options={{
                  minimap: { enabled: false },
                  fontSize: 14,
                  lineNumbers: "on",
                  roundedSelection: false,
                  scrollBeyondLastLine: false,
                  readOnly: false,
                  automaticLayout: true,
                }}
              />
            </div>
            {!validationResult.isValid &&
              validationResult.errors.length > 0 && (
                <div className="p-4 border border-destructive bg-destructive/10 rounded-md space-y-2">
                  <div className="font-medium text-destructive">
                    Schema Validation Errors:
                  </div>
                  <div className="text-sm text-destructive space-y-1">
                    {validationResult.errors.map((error, index) => {
                      let message =
                        error.processed?.message ||
                        error.original?.token?.image ||
                        "Unknown error";
                      return (
                        <div key={index} className="flex items-start gap-2">
                          <div className="font-mono whitespace-nowrap">
                            {error.processed?.line ||
                              error.original?.token?.startLine ||
                              1}
                            :
                            {error.processed?.column ||
                              error.original?.token?.startColumn ||
                              1}
                          </div>
                          <div>{message}</div>
                        </div>
                      );
                    })}
                  </div>
                </div>
              )}
          </TabsContent>

          <TabsContent value="preview" className="min-h-[600px]">
            <div className="border rounded-md h-[600px] bg-secondary/10">
              <SchemaViewer schema={schema} />
            </div>
          </TabsContent>
        </Tabs>
      </div>
    </div>
  );
}
