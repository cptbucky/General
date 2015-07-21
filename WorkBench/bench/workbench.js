// render dependency on inputs
// render dummy listener on outputs

(function () {
    var infoUpdater, infoUpdaterOut;

    // do i need to handle the render?!

    bladeRunner.requestBlade('InfoUpdater', {
        bladeRegistered: function (blade) {
            infoUpdater = blade;
        }
    });

    bladeRunner.requestBlade('InfoUpdaterBench', {
        bladeRegistered: function (blade) {
            infoUpdaterOut = blade;
        }
    });

    window.docReady(function () {
        infoUpdaterOut.initialiseDOM(document);
        infoUpdater.initialiseDOM(document, document.getElementById('scratch'));
    });

    //bladeRunner.renderView('WorkBenchOutView', 'outputView');

    //infoUpdaterOutput.render();

    //infoUpdater.render();
}());