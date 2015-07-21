var serviceContainer = (function(){
    var model = {},
        services = {},
        consumers = {};

    model.registerService = function(name, service) {
        services[name] = service;

        if (consumers[name] && consumers[name].length > 0) {
            for (var i = 0; i < consumers[name].length; i++) {
                consumers[name][i].serviceRegistered(services[name]);
            }
        }
    };

    model.requestService = function(name, consumer) {
        if (services[name]) {
            consumer.serviceRegistered(services[name]); // consumer is locked to 1 service
        } else {
            if (!consumers[name]) {
                consumers[name] = [];
            }
            consumers[name].push(consumer);
        }
    };

    return model;
}());