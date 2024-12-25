import { create } from "zustand";

export type FieldType = 
  | "String"
  | "Integer"
  | "Long"
  | "Float"
  | "Double"
  | "Boolean"
  | "Date"
  | "DateTime"
  | "Email"
  | "Password"
  | "UUID"
  | "Enum"
  | "JSON"
  | "Reference";

export interface SchemaField {
  name: string;
  type: FieldType;
  required: boolean;
  unique: boolean;
  reference?: string; // For Reference type, points to another entity
  enumValues?: string[]; // For Enum type
  description?: string;
}

export interface SchemaEntity {
  name: string;
  fields: SchemaField[];
  description?: string;
  timestamps?: boolean; // createdAt, updatedAt
  softDelete?: boolean; // deletedAt
  auditing?: boolean; // createdBy, updatedBy
}

interface SchemaState {
  entities: SchemaEntity[];
  addEntity: (entity: SchemaEntity) => void;
  updateEntity: (name: string, entity: SchemaEntity) => void;
  removeEntity: (name: string) => void;
  addField: (entityName: string, field: SchemaField) => void;
  updateField: (entityName: string, fieldName: string, field: SchemaField) => void;
  removeField: (entityName: string, fieldName: string) => void;
}

export const useSchemaStore = create<SchemaState>((set) => ({
  entities: [],
  addEntity: (entity) =>
    set((state) => ({
      entities: [...state.entities, entity],
    })),
  updateEntity: (name, entity) =>
    set((state) => ({
      entities: state.entities.map((e) => (e.name === name ? entity : e)),
    })),
  removeEntity: (name) =>
    set((state) => ({
      entities: state.entities.filter((e) => e.name !== name),
    })),
  addField: (entityName, field) =>
    set((state) => ({
      entities: state.entities.map((e) =>
        e.name === entityName
          ? { ...e, fields: [...e.fields, field] }
          : e
      ),
    })),
  updateField: (entityName, fieldName, field) =>
    set((state) => ({
      entities: state.entities.map((e) =>
        e.name === entityName
          ? {
              ...e,
              fields: e.fields.map((f) =>
                f.name === fieldName ? field : f
              ),
            }
          : e
      ),
    })),
  removeField: (entityName, fieldName) =>
    set((state) => ({
      entities: state.entities.map((e) =>
        e.name === entityName
          ? { ...e, fields: e.fields.filter((f) => f.name !== fieldName) }
          : e
      ),
    })),
}));
