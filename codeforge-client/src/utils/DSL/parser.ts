import { CstParser } from "chevrotain";
import {
    ModelKeyword,
    LinkKeyword,
    RequiredKeyword,
    UniqueKeyword,
    DefaultKeyword,
    PositiveKeyword,
    LongKeyword,
    TextType,
    NumberType,
    DateType,
    BooleanType,
    ListType,
    FileType,
    OneToOne,
    OneToMany,
    ManyToMany,
    LCurly,
    RCurly,
    LParen,
    RParen,
    Colon,
    Arrow,
    BiArrow,
    StringLiteral,
    NumberLiteral,
    BooleanLiteral,
    Identifier,
    allTokens,
    SimplSchemaLexer
} from "./lexer";

export class SimplSchemaParser extends CstParser {
    constructor() {
        super(allTokens, {
            recoveryEnabled: true,
            nodeLocationTracking: "full",
            errorMessageProvider: {
                buildMismatchTokenMessage: (options) => {
                    const token = options.actual;
                    return `Unexpected token "${token.image}". Expected ${options.expected}`;
                },
                buildNotAllInputParsedMessage: (options) => {
                    const token = options.firstRedundant;
                    return `Unexpected token "${token.image}" - this may indicate a missing "{"`;
                },
                buildNoViableAltMessage: (options) => {
                    const token = options.actual[0];
                    return `Invalid syntax near "${token.image}"`;
                },
                buildEarlyExitMessage: (options) => {
                    return `Parsing failed: expecting at least one ${options.expectedIterationPaths[0].join(" or ")}`;
                }
            }
        });
        this.performSelfAnalysis();
    }

    // Entry point
    public schema = this.RULE("schema", () => {
        this.MANY(() => {
            this.OR([
                { ALT: () => this.SUBRULE(this.modelDefinition) },
                { ALT: () => this.SUBRULE(this.linkDefinition) }
            ]);
        });
    });

    // Model definition
    private modelDefinition = this.RULE("modelDefinition", () => {
        this.CONSUME(ModelKeyword);
        const modelName = this.CONSUME(Identifier);
        this.CONSUME(LCurly, {
            ERR_MSG: `Model "${modelName.image}" is missing its body. Expected "{" after model name`
        });
        this.MANY(() => {
            this.SUBRULE(this.fieldDefinition);
        });
        this.CONSUME(RCurly, {
            ERR_MSG: `Model "${modelName.image}" is missing its closing "}"`
        });
    });

    // Field definition
    private fieldDefinition = this.RULE("fieldDefinition", () => {
        this.CONSUME(Identifier);
        this.CONSUME(Colon);
        this.SUBRULE(this.fieldType);
        this.MANY(() => {
            this.SUBRULE(this.fieldConstraint);
        });
    });

    // Field type
    private fieldType = this.RULE("fieldType", () => {
        this.OR([
            { ALT: () => this.CONSUME(TextType) },
            { ALT: () => this.CONSUME(NumberType) },
            { ALT: () => this.CONSUME(DateType) },
            { ALT: () => this.CONSUME(BooleanType) },
            { ALT: () => this.CONSUME(ListType) },
            { ALT: () => this.CONSUME(FileType) }
        ]);
    });

    // Field constraints
    private fieldConstraint = this.RULE("fieldConstraint", () => {
        this.OR([
            { ALT: () => this.CONSUME(RequiredKeyword) },
            { ALT: () => this.CONSUME(UniqueKeyword) },
            { ALT: () => this.SUBRULE(this.defaultValue) },
            { ALT: () => this.CONSUME(PositiveKeyword) },
            { ALT: () => this.CONSUME(LongKeyword) }
        ]);
    });

    // Default value
    private defaultValue = this.RULE("defaultValue", () => {
        this.CONSUME(DefaultKeyword);
        this.CONSUME1(Colon);
        this.OR([
            { ALT: () => this.CONSUME(StringLiteral) },
            { ALT: () => this.CONSUME(NumberLiteral) },
            { ALT: () => this.CONSUME(BooleanLiteral) }
        ]);
    });

    // Link definition
    private linkDefinition = this.RULE("linkDefinition", () => {
        this.CONSUME(LinkKeyword);
        this.CONSUME1(Identifier); // From model
        this.OR([
            { ALT: () => this.CONSUME(Arrow) },
            { ALT: () => this.CONSUME(BiArrow) }
        ]);
        this.CONSUME2(Identifier); // To model
        this.CONSUME(LParen);
        this.SUBRULE(this.relationshipType);
        this.CONSUME(RParen);
        this.OPTION(() => {
            this.SUBRULE(this.linkAttributes);
        });
    });

    // Relationship type
    private relationshipType = this.RULE("relationshipType", () => {
        this.OR([
            { ALT: () => this.CONSUME(OneToOne) },
            { ALT: () => this.CONSUME(OneToMany) },
            { ALT: () => this.CONSUME(ManyToMany) }
        ]);
    });

    // Link attributes
    private linkAttributes = this.RULE("linkAttributes", () => {
        this.CONSUME(LCurly);
        this.MANY(() => {
            this.SUBRULE(this.fieldDefinition);
        });
        this.CONSUME(RCurly);
    });

    // Helper method to tokenize input
    public tokenize(input: string) {
        return SimplSchemaLexer.tokenize(input);
    }
}

// Create a singleton instance
export const parser = new SimplSchemaParser();
