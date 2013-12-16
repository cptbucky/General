var tests = [];
for (var file in window.__karma__.files) {
    if (/Test\.js$/.test(file)) {
        tests.push(file);
    }
}

requirejs.config({
    // Karma serves files from '/base'
    baseUrl: '/base/App',

    paths: {
        'jquery': '../Scripts/jquery-1.9.1',
        'knockout': '../Scripts/knockout-2.3.0',
        'text': '../Scripts/text',
        'durandal': '../Scripts/durandal',
        'plugins': '../Scripts/durandal/plugins',
        'transitions': '../Scripts/durandal/transitions'
    }, 

    shim: {
        'durandal': {
            deps: ['ko', 'jquery'],
            exports: 'durandal'
        }
    }, 

    //shim: {
    //    'ko': { 'exports': 'knockout' },
    //},
    
    // ask Require.js to load these files (all our tests)
    deps: tests,

    // start test run, once Require.js is done
    callback: window.__karma__.start
});

require(["knockout"], function (ko) {
    //manually set the global ko property
    window.ko = ko;
});

//define('jquery', function () { return jQuery; });
//define('knockout', ko);