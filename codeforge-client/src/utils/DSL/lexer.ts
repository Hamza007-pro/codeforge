import { createToken, Lexer } from "chevrotain";

// Keywords
export const ModelKeyword = createToken({ 
    name: "ModelKeyword", 
    pattern: /model/,
    line_breaks: false,
    start_chars_hint: ["m"]
});
export const LinkKeyword = createToken({ 
    name: "LinkKeyword", 
    pattern: /link/,
    line_breaks: false,
    start_chars_hint: ["l"]
});
export const RequiredKeyword = createToken({ 
    name: "RequiredKeyword", 
    pattern: /required/,
    line_breaks: false,
    start_chars_hint: ["r"]
});
export const UniqueKeyword = createToken({ 
    name: "UniqueKeyword", 
    pattern: /unique/,
    line_breaks: false,
    start_chars_hint: ["u"]
});
export const DefaultKeyword = createToken({ 
    name: "DefaultKeyword", 
    pattern: /default/,
    line_breaks: false,
    start_chars_hint: ["d"]
});
export const PositiveKeyword = createToken({ 
    name: "PositiveKeyword", 
    pattern: /positive/,
    line_breaks: false,
    start_chars_hint: ["p"]
});
export const LongKeyword = createToken({ 
    name: "LongKeyword", 
    pattern: /long/,
    line_breaks: false,
    start_chars_hint: ["l"]
});

// Data Types
export const TextType = createToken({ 
    name: "TextType", 
    pattern: /text/,
    line_breaks: false,
    start_chars_hint: ["t"]
});
export const NumberType = createToken({ 
    name: "NumberType", 
    pattern: /number/,
    line_breaks: false,
    start_chars_hint: ["n"]
});
export const DateType = createToken({ 
    name: "DateType", 
    pattern: /date/,
    line_breaks: false,
    start_chars_hint: ["d"]
});
export const BooleanType = createToken({ 
    name: "BooleanType", 
    pattern: /boolean/,
    line_breaks: false,
    start_chars_hint: ["b"]
});
export const ListType = createToken({ 
    name: "ListType", 
    pattern: /list/,
    line_breaks: false,
    start_chars_hint: ["l"]
});
export const FileType = createToken({ 
    name: "FileType", 
    pattern: /file/,
    line_breaks: false,
    start_chars_hint: ["f"]
});

// Relationship Types
export const OneToOne = createToken({ 
    name: "OneToOne", 
    pattern: /one\s+to\s+one/,
    line_breaks: false,
    start_chars_hint: ["o"]
});
export const OneToMany = createToken({ 
    name: "OneToMany", 
    pattern: /one\s+to\s+many/,
    line_breaks: false,
    start_chars_hint: ["o"]
});
export const ManyToMany = createToken({ 
    name: "ManyToMany", 
    pattern: /many\s+to\s+many/,
    line_breaks: false,
    start_chars_hint: ["m"]
});

// Symbols and Operators
export const LCurly = createToken({ 
    name: "LCurly", 
    pattern: /{/,
    line_breaks: false,
    start_chars_hint: ["{"]
});
export const RCurly = createToken({ 
    name: "RCurly", 
    pattern: /}/,
    line_breaks: false,
    start_chars_hint: ["}"]
});
export const LParen = createToken({ 
    name: "LParen", 
    pattern: /\(/,
    line_breaks: false,
    start_chars_hint: ["("]
});
export const RParen = createToken({ 
    name: "RParen", 
    pattern: /\)/,
    line_breaks: false,
    start_chars_hint: [")"]
});
export const Colon = createToken({ 
    name: "Colon", 
    pattern: /:/,
    line_breaks: false,
    start_chars_hint: [":"]
});
export const Arrow = createToken({ 
    name: "Arrow", 
    pattern: /->/,
    line_breaks: false,
    start_chars_hint: ["-"]
});
export const BiArrow = createToken({ 
    name: "BiArrow", 
    pattern: /<->/,
    line_breaks: false,
    start_chars_hint: ["<"]
});

// Values
export const StringLiteral = createToken({
    name: "StringLiteral",
    pattern: /"[^"]*"|'[^']*'/,
    line_breaks: false,
    start_chars_hint: ["'", '"']
});
export const NumberLiteral = createToken({
    name: "NumberLiteral",
    pattern: /-?\d+(\.\d+)?/,
    line_breaks: false,
    start_chars_hint: ["-", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"]
});
export const BooleanLiteral = createToken({
    name: "BooleanLiteral",
    pattern: /true|false/,
    line_breaks: false,
    start_chars_hint: ["t", "f"]
});
export const Identifier = createToken({
    name: "Identifier",
    pattern: /[a-zA-Z_][a-zA-Z0-9_]*/,
    line_breaks: false
});

// Whitespace and Comments
export const WhiteSpace = createToken({
    name: "WhiteSpace",
    pattern: /[ \t\r\n]+/,
    group: Lexer.SKIPPED,
    line_breaks: true
});
export const Comment = createToken({
    name: "Comment",
    pattern: /\/\/[^\n\r]*/,
    group: Lexer.SKIPPED,
    line_breaks: false
});
export const MultiLineComment = createToken({
    name: "MultiLineComment",
    pattern: /\/\*[^*]*\*+([^/*][^*]*\*+)*\//,
    group: Lexer.SKIPPED,
    line_breaks: true
});

export const allTokens = [
    WhiteSpace,
    Comment,
    MultiLineComment,
    
    // Keywords
    ModelKeyword,
    LinkKeyword,
    RequiredKeyword,
    UniqueKeyword,
    DefaultKeyword,
    PositiveKeyword,
    LongKeyword,

    // Data Types
    TextType,
    NumberType,
    DateType,
    BooleanType,
    ListType,
    FileType,

    // Relationship Types
    OneToOne,
    OneToMany,
    ManyToMany,

    // Symbols
    LCurly,
    RCurly,
    LParen,
    RParen,
    Colon,
    Arrow,
    BiArrow,

    // Values
    StringLiteral,
    NumberLiteral,
    BooleanLiteral,
    
    // Identifier must be last to avoid matching keywords
    Identifier
];

// Create a Lexer instance
export const SimplSchemaLexer = new Lexer(allTokens, {
    // Enable position tracking for better error messages
    positionTracking: "full",
    ensureOptimizations: true,
    skipValidations: false,
    lineTerminatorsPattern: /\n|\r\n?/g,
    lineTerminatorCharacters: ["\n", "\r"],
    errorMessageProvider: {
        buildUnexpectedCharactersMessage: (fullText, startOffset, length, line, column) => {
            return `Unexpected character "${fullText.substr(startOffset, length)}" at line: ${line}, column: ${column}`;
        },
        buildNotAllInputParsedMessage: (firstRedundantToken) => {
            return `Redundant input starting at line: ${firstRedundantToken.startLine}, column: ${firstRedundantToken.startColumn}`;
        }
    }
});
