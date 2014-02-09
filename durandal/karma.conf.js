// Karma configuration
// Generated on Sun Dec 15 2013 10:18:03 GMT+0000 (GMT Standard Time)

module.exports = function(config) {
    config.set({
        // base path, that will be used to resolve files and exclude
        basePath: '',


        // frameworks to use
        frameworks: ['qunit', 'requirejs'],


        // list of files / patterns to load in the browser
        files: [
            { pattern: 'Scripts/durandal/*.js', included: false },
            { pattern: 'Scripts/knockout-2.3.0.js', included: false },
            { pattern: 'App/**/*.js', included: false },
            { pattern: 'test/**/*Test.js', included: false },
            'Scripts/jquery-1.9.1.js',
            'test/test-main.js'
        ],


        // list of files to exclude
        exclude: [
            'App/main.js'
        ],


        // test results reporter to use
        // possible values: 'dots', 'progress', 'junit', 'growl', 'coverage'
        reporters: ['progress', 'coverage'],

        preprocessors: { 'App/**/*.js': ['coverage'] },

        // web server port
        port: 9876,


        // enable / disable colors in the output (reporters and logs)
        colors: true,


        // level of logging
        // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
        logLevel: config.LOG_INFO,


        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: true,


        // Start these browsers, currently available:
        // - Chrome
        // - ChromeCanary
        // - Firefox
        // - Opera
        // - Safari (only Mac)
        // - PhantomJS
        // - IE (only Windows)
        browsers: ['PhantomJS', 'Chrome', 'IE'],


        // If browser does not capture in given timeout [ms], kill it
        captureTimeout: 60000,


        // Continuous Integration mode
        // if true, it capture browsers, run tests and exit
        singleRun: false,

        //plugins: [
        //    'karma-requirejs',
        //    'karma-qunit'
        //]
    });
};