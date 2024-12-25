import { ProjectConfig, GeneratedProject, ProjectTemplate, Dependency } from './types';

const API_BASE_URL = 'http://localhost:8080';

export const api = {
  async generateProject(config: ProjectConfig): Promise<GeneratedProject> {
    const response = await fetch(`${API_BASE_URL}/api/generator/generate`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(config),
    });
    return response.json();
  },

  async getAllTemplates(): Promise<ProjectTemplate[]> {
    const response = await fetch(`${API_BASE_URL}/api/generator/templates`);
    return response.json();
  },

  async getTemplate(projectType: string, platform: string, architecture: string): Promise<ProjectTemplate> {
    const response = await fetch(
      `${API_BASE_URL}/api/generator/template/${projectType}/${platform}/${architecture}`
    );
    return response.json();
  },

  async listProjects(): Promise<GeneratedProject[]> {
    const response = await fetch(`${API_BASE_URL}/api/generator/projects`);
    return response.json();
  },

  async getProjectTypes(): Promise<string[]> {
    const response = await fetch(`${API_BASE_URL}/api/generator/project-types`);
    return response.json();
  },

  async getPlatforms(projectType: string): Promise<string[]> {
    const response = await fetch(`${API_BASE_URL}/api/generator/platforms/${projectType}`);
    return response.json();
  },

  async getFrameworks(projectType: string): Promise<string[]> {
    const response = await fetch(`${API_BASE_URL}/api/generator/frameworks/${projectType}`);
    return response.json();
  },

  async getArchitectures(projectType: string, platform: string): Promise<string[]> {
    const response = await fetch(
      `${API_BASE_URL}/api/generator/architectures/${projectType}/${platform}`
    );
    return response.json();
  },

  async downloadProject(id: string): Promise<Blob> {
    const response = await fetch(`${API_BASE_URL}/api/generator/download/${id}`);
    return response.blob();
  },

  async getDependencies(platform: string): Promise<Dependency[]> {
    const response = await fetch(`${API_BASE_URL}/api/${platform}/dependencies`);
    return response.json();
  },
};
