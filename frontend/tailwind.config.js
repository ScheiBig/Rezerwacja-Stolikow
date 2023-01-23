/** @type {import('tailwindcss').Config} */
module.exports = {
  purge: [
    './src/**/*.html',
    './src/**/*.js',
    './src/**/*.jsx',
    './src/**/*.ts',
    './src/**/*.tsx',
  ],
  content: [],
  theme: {
    extend: {},
    screens: {
      's_xlg': { 'min': '1021px' },
      's_lrg': { 'max': '1020px' },
      's_lmd': { 'max': '800px' },
      's_mid': { 'max': '600px' },
      's_sml': { 'max': '440px' },
      'sh_mid': { 'raw': '(max-height: 800px)' },
      'sh_shr': { 'raw': '(max-height: 600px)' },
    }
  },
  plugins: [],
}
