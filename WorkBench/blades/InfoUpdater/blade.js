// get dependencies - services for the fields to update
// get listeners that are interested when updates are made
// render method kicks out html give it to renderer
// data binding for component???
// notify listeners on action

(function () {
    var model = {},
        fieldService,
        schema,
        parent,
        container,
        slides,
        currentItem = 0,
        skip,
        save,
        theDoc,
        ctrls = [];

    function progress() {
        slides[currentItem].classList.remove('slide-vis');

        if (currentItem === slides.length - 1) {
            parent.classList.remove('container-vis');
            return;
        }

        currentItem++;

        if (currentItem < slides.length) {
            slides[currentItem].classList.add('slide-vis');
        }
    }

    function updaterNav(e) {
        if (e.target.classList.contains('close')) {
            model.quit();
        } else if (e.target.classList.contains('save')) {
            model.save();
        } else if (e.target.classList.contains('skip')) {
            model.skip();
        }
    }

    // notificator interface
    model.updated = function (data) {
        schema = data;

        currentItem = 0;

        parent.removeEventListener("click", updaterNav, false);

        parent.innerHTML = '';

        for (var i = 0; i < schema.length; i++) {
            var ctrl;
            switch (schema[i].template) {
                case 'title':
                case 'description':
                    ctrl = 'InfoUpdateText';
                    break;
                case 'housematecount':
                case 'roomtype':
                case 'furnished':
                case 'amenities':
                    ctrl = 'InfoUpdateSelect';
                    break;
            }

            (function(control, position){
                bladeRunner.requestBlade(control, {
                    bladeRegistered: function (blade) {
                        ctrls[position] = new blade({
                            header: schema[position].header,
                            slideClass: position === 0 ? 'slide-vis' : ''
                        });
                    }
                });
            }(ctrl, i));
        }

        for (var c = 0; c < ctrls.length; c++) {
            ctrls[c].initialiseDOM(theDoc, parent);
        }

        // get template string and inject here???
        //titleEntry.initialiseDOM(theDoc, parent);
        //amenitiesEntry.initialiseDOM(theDoc, parent);

        parent.addEventListener("click", updaterNav, false);

        slides = parent.getElementsByClassName('slide');

        parent.classList.add('container-vis');
    };

    notificator.registerInterest('InfoUpdaterService', model);

    model.skip = function () {
        progress();
    };

    model.save = function () {
        var request = {
            endPoint: '/account/listings/room/update',
            data: {}
        };

        request.data[schema[currentItem].requestProp] = ctrls[currentItem].getSelection();

        fieldService.updateField(request);

        progress();
    };

    model.quit = function () {
        parent.classList.remove('container-vis');
    };

    // view interface - need a better view engine piece / templating
    model.initialiseDOM = function (doc, container) {
        theDoc = doc;

        parent = container;
    };

    bladeRunner.registerBlade('InfoUpdater', model);

    // service provider
    model.serviceRegistered = function (service) {
        fieldService = service;
    };

    serviceContainer.requestService('InfoUpdaterService', model);
}());