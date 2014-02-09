
"use strict";
define(['viewmodels/testMod'], function (testMod) {
    module("module one", {
        setup: function() {

        },
        teardown: function() {

        }
    });

    test('test one', function() {
        ok(true, 'test passed');
    });

    test('test two', function () {
        var expected = true;

        var deferred = $.Deferred();

        deferred.promise();

        var test = ko.observable();

        var actual = testMod.testMe(true);
        
        equal(actual, expected, 'test passed - bool equal');
    });
});