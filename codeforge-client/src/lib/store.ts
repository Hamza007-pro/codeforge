import { create } from 'zustand'
import { api } from './api'
import { ProjectConfig, GeneratedProject, ProjectTemplate } from './types'

interface ProjectMetadata {
  projectName: string
  description: string
  version: string
  author: string
}

interface ProjectState {
  projectType: string
  framework: string
  buildTool: string
  architecturePattern: string
  projectMetadata: ProjectMetadata
  dependencies: string[]
  packageName: string | null
  templates: ProjectTemplate[]
  generatedProjects: GeneratedProject[]
  loading: boolean
  error: string | null
  
  // Actions
  setProjectType: (type: string) => void
  setFramework: (framework: string) => void
  setBuildTool: (tool: string) => void
  setArchitecturePattern: (pattern: string) => void
  setProjectMetadata: (metadata: Partial<ProjectMetadata>) => void
  setDependencies: (deps: string[]) => void
  setPackageName: (name: string | null) => void
  reset: () => void
  
  // API Actions
  fetchTemplates: () => Promise<void>
  fetchProjects: () => Promise<void>
  generateProject: () => Promise<void>
  downloadProject: (id: string) => Promise<void>
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
  templates: [],
  generatedProjects: [],
  loading: false,
  error: null,
}

export const useProjectStore = create<ProjectState>((set, get) => ({
  ...initialState,

  setProjectType: (projectType) => set((state) => ({ ...state, projectType })),
  setFramework: (framework) => set((state) => ({ ...state, framework })),
  setBuildTool: (buildTool) => set((state) => ({ ...state, buildTool })),
  setArchitecturePattern: (architecturePattern) =>
    set((state) => ({ ...state, architecturePattern })),
  setProjectMetadata: (metadata) =>
    set((state) => ({
      ...state,
      projectMetadata: { ...state.projectMetadata, ...metadata },
    })),
  setDependencies: (dependencies) => set((state) => ({ ...state, dependencies })),
  setPackageName: (packageName) => set((state) => ({ ...state, packageName })),
  reset: () => set(initialState),

  fetchTemplates: async () => {
    try {
      set({ loading: true, error: null })
      const templates = await api.getAllTemplates()
      set({ templates, loading: false })
    } catch (error) {
      set({ error: 'Failed to fetch templates', loading: false })
    }
  },

  fetchProjects: async () => {
    try {
      set({ loading: true, error: null })
      const projects = await api.listProjects()
      set({ generatedProjects: projects, loading: false })
    } catch (error) {
      set({ error: 'Failed to fetch projects', loading: false })
    }
  },

  generateProject: async () => {
    const state = get()
    try {
      set({ loading: true, error: null })
      const config: ProjectConfig = {
        projectType: state.projectType,
        projectName: state.projectMetadata.projectName,
        projectDescription: state.projectMetadata.description,
        author: state.projectMetadata.author,
        version: state.projectMetadata.version,
        buildTool: state.buildTool,
        language: state.projectType.toLowerCase().includes('java') ? 'java' : 'typescript',
        framework: state.framework,
        packageName: state.packageName || 'com.example',
        architecturePattern: state.architecturePattern,
        dependencies: state.dependencies,
      }
      const project = await api.generateProject(config)
      set((state) => ({
        generatedProjects: [...state.generatedProjects, project],
        loading: false,
      }))
      return project
    } catch (error) {
      set({ error: 'Failed to generate project', loading: false })
      throw error
    }
  },

  downloadProject: async (id: string) => {
    try {
      set({ loading: true, error: null })
      const blob = await api.downloadProject(id)
      const url = window.URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = `project-${id}.zip`
      document.body.appendChild(a)
      a.click()
      window.URL.revokeObjectURL(url)
      document.body.removeChild(a)
      set({ loading: false })
    } catch (error) {
      set({ error: 'Failed to download project', loading: false })
      throw error
    }
  },
}))
