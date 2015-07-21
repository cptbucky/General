// should define a renderable component that updates the service dependency
(function () {
    var model = {},
        outputNode,
        dependencyView;

    function notify() {
        var fieldDefs = dependencyView.querySelectorAll('.field-def'),
            fieldsResponse = [];

        for (var i = 0; i < fieldDefs.length; i++) {
            fieldsResponse.push({
                header: fieldDefs[i].querySelectorAll('.field-heading')[0].value,
                requestProp: fieldDefs[i].querySelectorAll('.field-name')[0].value,
                template: fieldDefs[i].querySelectorAll('.field-template')[0].value
            });
        }

        notificator.notify('InfoUpdaterService', fieldsResponse);
    }

    // view interface - need a better view engine piece / templating
    function renderDependency(doc) {
        var source = doc.querySelector("#field-input-template").innerHTML;

        //Compile that baby into a template
        var template = Handlebars.compile(source);

        dependencyView = doc.getElementById('dependencyView');

        dependencyView.innerHTML = template({});

        var field = doc.querySelector("#field-partial").innerHTML;

        var fieldTemplate = Handlebars.compile(field);

        var fieldContainer = dependencyView.querySelector('#infoUpdateServiceContainer');

        dependencyView.getElementsByClassName('fa-plus')[0].addEventListener('click', function() {
            fieldContainer.insertAdjacentHTML('beforeend', fieldTemplate({}));
        });

        var button = dependencyView.getElementsByClassName('submit')[0];

        button.addEventListener('click', notify);
    }

    function renderOutput(doc) {
        var parent = doc.getElementById('outputView');
        outputNode = doc.createElement('textarea');
        outputNode.classList.add('json-view');
        parent.appendChild(outputNode);
    }

    model.initialiseDOM = function (doc) {
        renderDependency(doc);

        renderOutput(doc);
    };

    bladeRunner.registerBlade('InfoUpdaterBench', model);

    // service interface
    model.updateField = function (fields) {
        outputNode.textContent = JSON.stringify(fields, null, 4);
    };

    serviceContainer.registerService('InfoUpdaterService', model);
}());