// Monaco Editor language configuration for SIMPL DSL

export const monarchLanguage = {
    defaultToken: 'invalid',
    tokenizer: {
        root: [
            // Keywords
            [/\b(model|link)\b/, 'keyword'],
            [/\b(required|unique)\b/, 'keyword'],
            [/\b(text|number|date|boolean|file)\b/, 'type'],

            // Identifiers
            [/[a-zA-Z_]\w*/, 'identifier'],

            // Whitespace
            [/[ \t\r\n]+/, 'white'],

            // Comments
            [/\/\/.*$/, 'comment'],
            [/\/\*/, 'comment', '@comment'],

            // Delimiters
            [/[{}():]/, 'delimiter'],
        ],
        comment: [
            [/[^/*]+/, 'comment'],
            [/\*\//, 'comment', '@pop'],
            [/[/*]/, 'comment']
        ],
    }
};

export const languageConfiguration = {
    comments: {
        lineComment: '//',
        blockComment: ['/*', '*/']
    },
    brackets: [
        ['{', '}'],
        ['[', ']'],
        ['(', ')']
    ],
    autoClosingPairs: [
        { open: '{', close: '}' },
        { open: '[', close: ']' },
        { open: '(', close: ')' },
        { open: '"', close: '"' },
        { open: "'", close: "'" }
    ],
    surroundingPairs: [
        { open: '{', close: '}' },
        { open: '[', close: ']' },
        { open: '(', close: ')' },
        { open: '"', close: '"' },
        { open: "'", close: "'" }
    ]
};
