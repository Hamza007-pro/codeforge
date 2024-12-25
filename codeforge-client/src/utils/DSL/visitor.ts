import { parser } from "./parser";
import { CstNode, ICstVisitor } from "chevrotain";

// AST Types
export interface Schema {
    models: Model[];
    links: Link[];
}

export interface Model {
    name: string;
    fields: Field[];
}

export interface Field {
    name: string;
    type: FieldType;
    constraints: Constraint[];
}

export type FieldType = {
    kind: "text" | "number" | "date" | "boolean" | "list" | "file";
};

export type Constraint = 
    | { kind: "required" }
    | { kind: "unique" }
    | { kind: "positive" }
    | { kind: "long" }
    | { kind: "default"; value: string | number | boolean };

export interface Link {
    from: string;
    to: string;
    type: "one-to-one" | "one-to-many" | "many-to-many";
    bidirectional: boolean;
    attributes?: Field[];
}

// Create the visitor
class SimplSchemaVisitor implements ICstVisitor<any, any> {
    constructor() {
        this.validateVisitor();
    }

    visit(cst: any): any {
        if (!cst) return undefined;

        // Handle root node
        if (cst.name === "schema") {
            return this.schema(cst);
        }

        // Handle other nodes based on their content
        if (cst.children) {
            if (cst.name === "modelDefinition") {
                return this.modelDefinition(cst);
            }
            if (cst.name === "fieldDefinition") {
                return this.fieldDefinition(cst);
            }
            if (cst.name === "fieldType") {
                return this.fieldType(cst);
            }
            if (cst.name === "fieldConstraint") {
                return this.fieldConstraint(cst);
            }
            if (cst.name === "defaultValue") {
                return this.defaultValue(cst);
            }
            if (cst.name === "linkDefinition") {
                return this.linkDefinition(cst);
            }
            if (cst.name === "relationshipType") {
                return this.relationshipType(cst);
            }
            if (cst.name === "linkAttributes") {
                return this.linkAttributes(cst);
            }
        }

        throw new Error(`Unable to determine node type for: ${JSON.stringify(Object.keys(cst))}`);
    }

    validateVisitor() {
        // Validate that all methods exist
        const methodNames = Object.getOwnPropertyNames(SimplSchemaVisitor.prototype);
        const requiredMethods = ["schema", "modelDefinition", "fieldDefinition", "fieldType", "fieldConstraint", "defaultValue", "linkDefinition", "relationshipType", "linkAttributes"];
        
        for (const method of requiredMethods) {
            if (!methodNames.includes(method)) {
                throw new Error(`Missing visitor method: ${method}`);
            }
        }
    }

    schema(ctx: any): Schema {
        const models: Model[] = [];
        const links: Link[] = [];

        // Process all model and link definitions from the root node
        const modelDefs = ctx.children?.modelDefinition || [];
        const linkDefs = ctx.children?.linkDefinition || [];

        // Process all model and link definitions
        if (Array.isArray(modelDefs)) {
            models.push(...modelDefs.map((def: CstNode) => this.modelDefinition(def)));
        }
        if (Array.isArray(linkDefs)) {
            links.push(...linkDefs.map((def: CstNode) => this.linkDefinition(def)));
        }

        return { models, links };
    }

    modelDefinition(ctx: any): Model {
        return {
            name: ctx.children.Identifier[0].image,
            fields: ctx.children.fieldDefinition ? ctx.children.fieldDefinition.map((def: CstNode) => this.fieldDefinition(def)) : []
        };
    }

    fieldDefinition(ctx: any): Field {
        return {
            name: ctx.children.Identifier[0].image,
            type: this.visit(ctx.children.fieldType[0]),
            constraints: ctx.children.fieldConstraint ? ctx.children.fieldConstraint.map((c: CstNode) => this.visit(c)) : []
        };
    }

    fieldType(ctx: any): FieldType {
        if (ctx.children.TextType) return { kind: "text" };
        if (ctx.children.NumberType) return { kind: "number" };
        if (ctx.children.DateType) return { kind: "date" };
        if (ctx.children.BooleanType) return { kind: "boolean" };
        if (ctx.children.ListType) return { kind: "list" };
        if (ctx.children.FileType) return { kind: "file" };
        throw new Error("Unknown field type");
    }

    fieldConstraint(ctx: any): Constraint {
        if (ctx.children.RequiredKeyword) return { kind: "required" };
        if (ctx.children.UniqueKeyword) return { kind: "unique" };
        if (ctx.children.PositiveKeyword) return { kind: "positive" };
        if (ctx.children.LongKeyword) return { kind: "long" };
        if (ctx.children.defaultValue) return this.visit(ctx.children.defaultValue[0]);
        throw new Error("Unknown constraint");
    }

    defaultValue(ctx: any): Constraint {
        let value: string | number | boolean;
        const children = ctx.children;
        if (children.StringLiteral) {
            value = children.StringLiteral[0].image.slice(1, -1); // Remove quotes
        } else if (children.NumberLiteral) {
            value = Number(children.NumberLiteral[0].image);
        } else if (children.BooleanLiteral) {
            value = children.BooleanLiteral[0].image === "true";
        } else {
            throw new Error("Invalid default value");
        }
        return { kind: "default", value };
    }

    linkDefinition(ctx: any): Link {
        const children = ctx.children;
        const fromModel = children.Identifier[0].image;
        const toModel = children.Identifier[1].image;
        const relationshipType = this.visit(children.relationshipType[0]);
        const bidirectional = Boolean(children.BiArrow);
        const attributes = children.linkAttributes ? this.visit(children.linkAttributes[0]) : undefined;

        return {
            from: fromModel,
            to: toModel,
            type: relationshipType,
            bidirectional,
            attributes
        };
    }

    relationshipType(ctx: any): "one-to-one" | "one-to-many" | "many-to-many" {
        if (ctx.children.OneToOne) return "one-to-one";
        if (ctx.children.OneToMany) return "one-to-many";
        if (ctx.children.ManyToMany) return "many-to-many";
        throw new Error("Unknown relationship type");
    }

    linkAttributes(ctx: any): Field[] {
        return ctx.children.fieldDefinition.map((def: CstNode) => this.fieldDefinition(def));
    }
}

// Create a singleton instance
export const visitor = new SimplSchemaVisitor();

// Main parse function
export function parseSchema(input: string): Schema {
    const lexResult = parser.tokenize(input);
    if (lexResult.errors.length > 0) {
        throw new Error(`Lexing error: ${lexResult.errors[0].message}`);
    }

    // Set the input tokens
    parser.input = lexResult.tokens;

    // Parse the tokens into a CST
    const cst = parser.schema();

    if (parser.errors.length > 0) {
        throw new Error(`Parse error: ${parser.errors[0].message}`);
    }

    return visitor.visit(cst);
}
