"use client";

import * as React from "react";
import * as monaco from 'monaco-editor';
import { SimplSchemaService } from "@/utils/DSL/service";
import { Schema } from "@/utils/DSL/visitor";
import Editor, { useMonaco } from "@monaco-editor/react";

interface EditorProps {
  initialValue?: string;
}

export const SchemaEditor: React.FC<EditorProps> = ({ initialValue }) => {
  const [schemaText, setSchemaText] = React.useState(
    initialValue || SimplSchemaService.generateSampleSchema("basic")
  );
  const [schema, setSchema] = React.useState<Schema | null>(null);
  const editorRef = React.useRef<monaco.editor.IStandaloneCodeEditor | null>(null);
  const monacoInstance = useMonaco();

  // Basic editor mount handler
  const handleEditorDidMount = React.useCallback((editor: monaco.editor.IStandaloneCodeEditor) => {
    console.log("Editor mounted");
    editorRef.current = editor;
  }, []);

  // Basic Monaco setup
  React.useEffect(() => {
    if (monacoInstance) {
      // Register the language
      monacoInstance.languages.register({ id: "simpl" });

      // Basic syntax highlighting
      monacoInstance.languages.setMonarchTokensProvider("simpl", {
        keywords: ["model", "link", "required", "unique", "text", "number", "date", "boolean", "file"],
        tokenizer: {
          root: [
            [/model|link/, "keyword"],
            [/required|unique/, "keyword"],
            [/text|number|date|boolean|file/, "type"],
            [/[a-zA-Z_]\w*/, "identifier"],
            [/[{}():]/, "delimiter"],
            [/\/\/.*$/, "comment"],
          ],
        },
      });

      // Basic theme
      monacoInstance.editor.defineTheme("simplTheme", {
        base: "vs-dark",
        inherit: true,
        rules: [],
        colors: {
          'editor.background': '#1e1e1e',
        },
      });

      monacoInstance.editor.setTheme("simplTheme");
    }
  }, [monacoInstance]);

  // Handle schema changes
  const handleSchemaChange = (value: string | undefined) => {
    if (!value) return;
    setSchemaText(value);

    try {
      const validation = SimplSchemaService.validateSchema(value);
      if (validation.isValid) {
        const parsedSchema = SimplSchemaService.parseSchema(value);
        setSchema(parsedSchema);
      } else {
        setSchema(null);
      }
    } catch (error) {
      console.error("Schema validation error:", error);
      setSchema(null);
    }
  };

  return (
    <div className="w-full h-full min-h-[500px] border rounded-md overflow-hidden">
      <Editor
        height="500px"
        defaultLanguage="simpl"
        value={schemaText}
        onChange={handleSchemaChange}
        onMount={handleEditorDidMount}
        options={{
          minimap: { enabled: false },
          fontSize: 14,
          lineNumbers: "on",
          roundedSelection: false,
          scrollBeyondLastLine: false,
          readOnly: false,
          theme: "simplTheme"
        }}
      />
    </div>
  );
};
