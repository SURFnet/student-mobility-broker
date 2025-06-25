export default {

    preset: "ts-jest/presets/default-esm",
    testEnvironment: 'node',
    testMatch: ['<rootDir>/src/__tests__/**/*.js'],
    testPathIgnorePatterns: ['/node_modules/'],
    coverageDirectory: './coverage',
    coveragePathIgnorePatterns: ['node_modules', 'src/database', 'src/test', 'src/types'],
    reporters: ['default'],
    transform: {
        "node_modules/variables/.+\\.(j|t)sx?$": "ts-jest"
    },
    transformIgnorePatterns: [
        "node_modules/(?!variables/.*)"
    ]
};