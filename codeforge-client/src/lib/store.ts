import { create } from 'zustand'

interface ProjectState {
  projectType: string
  framework: string
  buildTool: string
  architecturePattern: string
  projectMetadata: ProjectMetadata
  dependencies: string[]
  packageName: string | null
  
  // Actions
  setProjectType: (type: string) => void
  setFramework: (framework: string) => void
  setBuildTool: (tool: string) => void
  setArchitecturePattern: (pattern: string) => void
  setProjectMetadata: (metadata: Partial<ProjectMetadata>) => void
  setDependencies: (deps: string[]) => void
  setPackageName: (name: string | null) => void
  reset: () => void
}

interface ProjectMetadata {
  projectName: string
  description: string
  version: string
  author: string
}

const initialState = {
  projectType: 'Web Application',
  framework: 'nextjs',
  buildTool: 'vite',
  architecturePattern: 'MVVM',
  projectMetadata: {
    projectName: 'my-app',
    description: 'A new project generated with project-generator',
    version: '0.1.0',
    author: '',
  },
  dependencies: [],
  packageName: null,
}

export const useProjectStore = create<ProjectState>((set) => ({
  ...initialState,

  setProjectType: (projectType) => 
    set((state) => ({ ...state, projectType })),

  setFramework: (framework) => 
    set((state) => ({ ...state, framework })),

  setBuildTool: (buildTool) => 
    set((state) => ({ ...state, buildTool })),

  setArchitecturePattern: (architecturePattern) => 
    set((state) => ({ ...state, architecturePattern })),

  setProjectMetadata: (metadata) => 
    set((state) => ({ 
      ...state, 
      projectMetadata: { ...state.projectMetadata, ...metadata } 
    })),

  setDependencies: (dependencies) => 
    set((state) => ({ ...state, dependencies })),

  setPackageName: (packageName) => 
    set((state) => ({ ...state, packageName })),

  reset: () => set(initialState),
}))
