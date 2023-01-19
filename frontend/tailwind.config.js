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
      'phone': { 'max': '599px' },
      'computer': '600px'
    }
  },
  plugins: [],
}
