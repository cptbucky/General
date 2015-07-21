var notificator = (function(){
    var model = {},
        interests = {};

    model.notify = function(name, data) {
        for (var i = 0; i < interests[name].length; i++) {
            console.log(name + ': ' + JSON.stringify(data));

            interests[name][i].updated(data);
        }
    };

    model.registerInterest = function(name, interest) {
        if (!interests[name]) {
            interests[name] = [];
        }

        interests[name].push(interest);
    };

    return model;
}());