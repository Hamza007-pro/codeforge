export interface ProjectConfig {
  projectType: string;
  projectName: string;
  projectDescription: string;
  author: string;
  version: string;
  buildTool: string;
  language: string;
  framework: string;
  packageName: string;
  architecturePattern: string;
  schema?: string;
  dependencies?: string[];
}

export interface GeneratedProject {
  id: string;
  name: string;
  projectType: string;
  architecture: string;
  downloadUrl: string;
  generatedAt: string;
  zipPath: string;
}

export interface Dependencies {
  required: string[];
  optional: Record<string, string[]>;
}

export interface TemplateFile {
  path: string;
  template: string;
}

export interface ProjectTemplate {
  templateName: string;
  templateDescription: string;
  projectType: string;
  framework: string;
  platform: string;
  architecture: string;
  files: TemplateFile[];
  dependencies: Dependencies;
}

export interface Dependency {
  name: string;
  description: string;
  groupId: string;
  artifactId: string;
  version: string;
  type: string;
  category: string;
}
