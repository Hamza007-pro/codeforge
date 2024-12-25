package com.codeforge.engine.structure.build;

import com.codeforge.engine.structure.dependency.Dependency;
import java.util.List;
import java.util.stream.Collectors;

public class WebpackBuildSystem implements BuildSystem {
    @Override
    public String getName() {
        return "webpack";
    }

    @Override
    public String getConfigFileExtension() {
        return "js";
    }

    @Override
    public String formatDependencies(List<Dependency> dependencies) {
        return dependencies.stream()
            .map(d -> String.format("    \"%s\": \"%s\"", d.getArtifactId(), d.getVersion()))
            .collect(Collectors.joining(",\n"));
    }

    @Override
    public String createBuildConfig(String projectName, String version, List<Dependency> dependencies) {
        return """
            const path = require('path');
            const HtmlWebpackPlugin = require('html-webpack-plugin');
            const MiniCssExtractPlugin = require('mini-css-extract-plugin');
            
            module.exports = {
                entry: './src/index.js',
                output: {
                    path: path.resolve(__dirname, 'dist'),
                    filename: '[name].[contenthash].js',
                    clean: true,
                },
                module: {
                    rules: [
                        {
                            test: /\\.(js|jsx)$/,
                            exclude: /node_modules/,
                            use: {
                                loader: 'babel-loader',
                                options: {
                                    presets: ['@babel/preset-env', '@babel/preset-react']
                                }
                            }
                        },
                        {
                            test: /\\.css$/,
                            use: [MiniCssExtractPlugin.loader, 'css-loader']
                        },
                        {
                            test: /\\.(png|svg|jpg|jpeg|gif)$/i,
                            type: 'asset/resource',
                        },
                    ],
                },
                plugins: [
                    new HtmlWebpackPlugin({
                        template: './public/index.html',
                        favicon: './public/favicon.ico'
                    }),
                    new MiniCssExtractPlugin({
                        filename: '[name].[contenthash].css'
                    })
                ],
                resolve: {
                    extensions: ['.js', '.jsx'],
                },
                optimization: {
                    splitChunks: {
                        chunks: 'all',
                    },
                },
                devServer: {
                    historyApiFallback: true,
                    hot: true,
                    port: 3000,
                },
            };
            """;
    }

    @Override
    public String getBuildCommand() {
        return "npm run build";
    }

    @Override
    public String getRunCommand() {
        return "npm start";
    }

    @Override
    public String getTestCommand() {
        return "npm test";
    }

    public String createPackageJson(String projectName, String version, List<Dependency> dependencies) {
        return """
            {
              "name": "%s",
              "version": "%s",
              "private": true,
              "dependencies": {
            %s
              },
              "devDependencies": {
                "@babel/core": "^7.23.6",
                "@babel/preset-env": "^7.23.6",
                "@babel/preset-react": "^7.23.3",
                "babel-loader": "^9.1.3",
                "css-loader": "^6.8.1",
                "html-webpack-plugin": "^5.6.0",
                "mini-css-extract-plugin": "^2.7.6",
                "webpack": "^5.89.0",
                "webpack-cli": "^5.1.4",
                "webpack-dev-server": "^4.15.1"
              },
              "scripts": {
                "start": "webpack serve --mode development",
                "build": "webpack --mode production",
                "test": "jest"
              },
              "browserslist": {
                "production": [
                  ">0.2%%",
                  "not dead",
                  "not op_mini all"
                ],
                "development": [
                  "last 1 chrome version",
                  "last 1 firefox version",
                  "last 1 safari version"
                ]
              }
            }
            """.formatted(projectName, version, formatDependencies(dependencies));
    }
}
