var bladeRunner = (function () {
    var model = {},
        blades = {},
        bladeRequesters = {};

    // register a new view in case anyone needs it
    model.registerBlade = function (name, blade) {
        blades[name] = blade;
    };

    // async request will be notified later
    model.requestBlade = function (name, requester) {
        if (blades[name]) {
            requester.bladeRegistered(blades[name]);
        } else {
            bladeRequesters[name] = requester;
        }
    };

    // render views requested on load
    window.docReady(function () {
        for (var name in blades) {
            //blades[name].initialiseDOM(document);

            if (bladeRequesters[name]) {
                bladeRequesters[name].bladeRegistered(blades[name]);
            }
        }
    });

    return model;
}());