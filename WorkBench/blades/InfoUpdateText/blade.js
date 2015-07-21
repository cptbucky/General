(function () {
    function newInstance(displayInfo) {
        var model = {},
            parent,
            infoUpdaterBlade;


        // view interface - need a better view engine piece / templating
        model.initialiseDOM = function (doc, container) {
            var templateSource = doc.querySelector('#field-display-text-template').innerHTML;

            var template = Handlebars.compile(templateSource);

            parent = container;

            parent.innerHTML += template(displayInfo);
        };

        model.getSelection = function() {
            var input = parent.querySelectorAll('input[type="text"]')[0];

            return input.value;
        };

        bladeRunner.requestBlade('InfoUpdater', {
            bladeRegistered: function(blade) {
                infoUpdaterBlade = blade;
            }
        });

        return model;
    }

    bladeRunner.registerBlade('InfoUpdateText', newInstance);
}());