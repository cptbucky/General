define(['durandal/app'], function(app) {
    var model = function () {
        this.testMe = function(bool) {
            return bool;
        };
    };

    return new model();
});

//define(function (require) {
//    var model = function () {
//        this.testMe = function(bool) {
//            return bool;
//        };
//    };

//    return new model();
//});