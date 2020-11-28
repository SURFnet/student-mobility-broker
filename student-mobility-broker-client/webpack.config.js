const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const HtmlWebpackPlugin = require("html-webpack-plugin");
const path = require('path');

const mode = process.env.NODE_ENV || 'development';
const prod = mode === 'production';

module.exports = {
    entry: {
        bundle: ['./src/main.js']
    },
    resolve: {
        alias: {
            svelte: path.resolve('node_modules', 'svelte')
        },
        extensions: ['.mjs', '.js', '.svelte'],
        mainFields: ['svelte', 'browser', 'module', 'main']
    },
    output: {
        path: __dirname + '/public',
        filename: '[name].[hash].js',
        chunkFilename: '[name].[hash].js',
        publicPath: '/',
    },
    module: {
        rules: [
            {
                test: /\.m?js$/,
                use: {
                    loader: 'babel-loader'
                },
                exclude: /\bcore-js\b/,
            },
            {
                test: /\.svelte$/,
                use: [
                    {
                        loader: 'babel-loader',
                    },
                    {
                        loader: 'svelte-loader',
                        options: {
                            preprocess: require('svelte-preprocess')({
                                paths: ["src", "src/stylesheets"],
                                postcss: true
                            }),
                            emitCss: true,
                            hotReload: true,
                            accessors: true,
                            dev: true
                        },
                    }
                ],
            },
            {
                test: /\.css$/,
                use: [
                    prod ? MiniCssExtractPlugin.loader : 'style-loader',
                    'css-loader'
                ]
            },
            {
                test: /\.(png|jpg|jpeg|gif)$/,
                use:  [
                    'file-loader',
                ],
            },
            {
                test: /\.svg$/,
                loader: 'svg-inline-loader',
                options: {
                    removeSVGTagAttrs: false
                }
            }
        ]
    },
    mode,
    plugins: [
        new MiniCssExtractPlugin({
            filename: '[name].[hash].css'
        }),
        new HtmlWebpackPlugin({
            template: "src/index.html.ejs",
            favicon: "src/favicon.ico",
            hash: true
        }),
    ],
    devtool: prod ? false : 'source-map',
    devServer: {
        port: 3003,
        proxy: {
            '/api/enrollment': 'http://localhost:8092',
            '/api': 'http://localhost:8091'
        },
        historyApiFallback: true
    }
};
