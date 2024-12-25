import { parseSchema, Schema, FieldType, Constraint, Link } from "./visitor";
import { SimplSchemaLexer } from "./lexer";
import { parser as SimplSchemaParser } from "./parser";
import { IToken } from "chevrotain";

export interface ValidationError {
  line: number;
  column: number;
  message: string;
}

export interface SchemaValidationResult {
  isValid: boolean;
  errors: ValidationError[];
}

export interface ModelInfo {
  name: string;
  fields: { name: string; type: FieldType; constraints: Constraint[] }[];
}

export interface RelationshipInfo {
  from: string;
  to: string;
  type: "one-to-one" | "one-to-many" | "many-to-many";
  bidirectional: boolean;
  attributes?: { name: string; type: FieldType; constraints: Constraint[] }[];
}

interface ParserError {
  expectedIterationPaths?: string[][];
  message?: string;
  token?: IToken;
}

export class SimplSchemaService {
  /**
   * Validates a schema without parsing it into a full Schema object
   * @param schemaText The schema text to validate
   * @returns Validation result with any errors
   */
  static validateSchema(schemaText: string): { isValid: boolean; errors: { line: number; column: number; message: string }[] } {
    try {
      const lexResult = SimplSchemaLexer.tokenize(schemaText);
      
      // Handle lexer errors
      if (lexResult.errors.length > 0) {
        return {
          isValid: false,
          errors: lexResult.errors.map(err => ({
            line: err.line || 0,
            column: err.column || 0,
            message: err.message || 'Unknown lexer error'
          }))
        };
      }

      SimplSchemaParser.input = lexResult.tokens;
      const cst = SimplSchemaParser.schema();

      // Handle parser errors
      if (SimplSchemaParser.errors.length > 0) {
        return {
          isValid: false,
          errors: SimplSchemaParser.errors.map(err => {
            const errorObj = err.message;
            let message = '';
            let line = 0;
            let column = 0;

            // Extract line and column from token
            if (err.token) {
              line = err.token.startLine ?? 0;
              column = err.token.startColumn ?? 0;
            }

            // Extract message from error object
            if (typeof errorObj === 'object' && errorObj !== null) {
              const error = errorObj as ParserError;
              if (error.expectedIterationPaths && error.expectedIterationPaths.length > 0) {
                message = `Parsing failed: expecting at least one ${error.expectedIterationPaths[0].join(" or ")}`;
              } else if (error.message) {
                message = String(error.message);
              } else {
                message = JSON.stringify(errorObj);
              }
            } else {
              message = String(errorObj || 'Unknown parser error');
            }

            console.log('Parser error:', {
              original: err,
              processed: { line, column, message }
            });

            return { line, column, message };
          })
        };
      }

      // If we get here, schema is valid
      return {
        isValid: true,
        errors: []
      };
    } catch (error: any) {
      console.error('Validation error:', error);
      
      let message = '';
      let line = 0;
      let column = 0;

      if (error?.token) {
        line = error.token.startLine ?? 0;
        column = error.token.startColumn ?? 0;
      }

      if (typeof error === 'object' && error !== null) {
        if ('message' in error) {
          message = String(error.message);
        } else {
          message = JSON.stringify(error);
        }
      } else {
        message = String(error || 'Unknown error');
      }

      return {
        isValid: false,
        errors: [{
          line,
          column,
          message
        }]
      };
    }
  }

  /**
   * Parses a schema text into a Schema object
   * @param schemaText The schema text to parse
   * @returns Parsed Schema object
   * @throws Error if parsing fails
   */
  static parseSchema(schemaText: string): Schema {
    return parseSchema(schemaText);
  }

  /**
   * Extracts information about all models in the schema
   * @param schema The parsed Schema object
   * @returns Array of model information
   */
  static getModels(schema: Schema): ModelInfo[] {
    return schema.models.map(model => ({
      name: model.name,
      fields: model.fields.map(field => ({
        name: field.name,
        type: field.type,
        constraints: field.constraints
      }))
    }));
  }

  /**
   * Extracts information about all relationships in the schema
   * @param schema The parsed Schema object
   * @returns Array of relationship information
   */
  static getRelationships(schema: Schema): RelationshipInfo[] {
    return schema.links?.map(link => ({
      from: link.from,
      to: link.to,
      type: link.type,
      bidirectional: link.bidirectional,
      attributes: link.attributes?.map(attr => ({
        name: attr.name,
        type: attr.type,
        constraints: attr.constraints
      }))
    })) || [];
  }

  /**
   * Checks if a model exists in the schema
   * @param schema The parsed Schema object
   * @param modelName Name of the model to check
   * @returns boolean indicating if the model exists
   */
  static hasModel(schema: Schema, modelName: string): boolean {
    return schema.models.some(model => model.name === modelName);
  }

  /**
   * Gets all relationships for a specific model
   * @param schema The parsed Schema object
   * @param modelName Name of the model
   * @returns Array of relationships where the model is involved
   */
  static getModelRelationships(schema: Schema, modelName: string): RelationshipInfo[] {
    return (schema.links || []).filter(link => 
      link.from === modelName || link.to === modelName
    ).map(link => ({
      from: link.from,
      to: link.to,
      type: link.type,
      bidirectional: link.bidirectional,
      attributes: link.attributes?.map(attr => ({
        name: attr.name,
        type: attr.type,
        constraints: attr.constraints
      }))
    }));
  }

  /**
   * Gets a specific model's information
   * @param schema The parsed Schema object
   * @param modelName Name of the model
   * @returns Model information or null if not found
   */
  static getModel(schema: Schema, modelName: string): ModelInfo | null {
    const model = schema.models.find(m => m.name === modelName);
    if (!model) return null;

    return {
      name: model.name,
      fields: model.fields.map(field => ({
        name: field.name,
        type: field.type,
        constraints: field.constraints
      }))
    };
  }

  /**
   * Generates a sample schema based on common patterns
   * @param type The type of schema to generate ('blog' | 'ecommerce' | 'basic')
   * @returns A string containing the generated schema
   */
  static generateSampleSchema(type: 'blog' | 'ecommerce' | 'basic' = 'basic'): string {
    switch (type) {
      case 'blog':
        return `model Author {
  id: number required
  name: text required
  email: text required unique
  bio: text long
  created_at: date
}

model Post {
  id: number required
  title: text required
  content: text long required
  published: boolean default:false
  created_at: date
}

model Comment {
  id: number required
  content: text required
  created_at: date
}

// Relationships
link Author -> Post (one to many)
link Post -> Comment (one to many)`;

      case 'ecommerce':
        return `model User {
  id: number required
  email: text required unique
  name: text required
  created_at: date
}

model Product {
  id: number required
  name: text required
  price: number required positive
  stock: number required positive
  description: text long
  created_at: date
}

model Order {
  id: number required
  status: text required
  total: number required positive
  created_at: date
}

// Relationships
link User -> Order (one to many)
link Order <-> Product (many to many) {
  quantity: number required positive
  price_at_order: number required positive
}`;

      default:
        return `model User {
  id: number required
  name: text required
  email: text required unique
  created_at: date
}

model Profile {
  id: number required
  bio: text long
  avatar: file
}

// Relationships
link User -> Profile (one to one)`;
    }
  }
}
