// get dependencies - services for the fields to update
// get listeners that are interested when updates are made
// render method kicks out html give it to renderer
// data binding for component???
// notify listeners on action

(function () {
    function newInstance(displayInfo) {
        var model = {},
            parent,
            infoUpdaterBlade;


        // view interface - need a better view engine piece / templating
        model.initialiseDOM = function (doc, container) {
            var templateSource = doc.querySelector('#field-display-multi-template').innerHTML;

            var template = Handlebars.compile(templateSource);

            parent = container;

            parent.innerHTML += template(displayInfo);
        };

        model.getSelection = function() {
            var checkedInputs = parent.querySelectorAll('input[type="checkbox"]:checked'),
                values = [];

            for (var i = 0; i < checkedInputs.length; i++) {
                values[i] = checkedInputs[i].value;
            }

            return values;
        };

        bladeRunner.requestBlade('InfoUpdater', {
            bladeRegistered: function(blade) {
                infoUpdaterBlade = blade;
            }
        });

        return model;
    }

    bladeRunner.registerBlade('InfoUpdateSelect', newInstance);
}());