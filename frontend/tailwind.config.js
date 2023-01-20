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
      's_lrg': { 'max': '1020px' },
      's_mid': { 'max': '600px' },
      's_sml': { 'max': '440px' }
    }
  },
  plugins: [],
}
