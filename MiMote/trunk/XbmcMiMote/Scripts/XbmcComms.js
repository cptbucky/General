//var _traceToScreen = true;
var _traceToScreen = false;

//var _XBMCHOST = "http://192.168.0.154:8080/jsonrpc";
//    var _XBMCHOST = "http://192.168.0.151:8080/jsonrpc";

var playPauseCmd = "Player.PlayPause", stopCmd = "Player.Stop";
var upCmd = "Input.Up";
var leftCmd = "Input.Left";
var rightCmd = "Input.Right";
var downCmd = "Input.Down";
var backCmd = "Input.Back";
var selectCmd = "Input.Select";
var infoCmd = "Input.Info";
var menuCmd = "Input.ShowOSD";
var contextCmd = "Input.ContextMenu";

var paramsString = '"params": { "playerid": 1 }, ';

var activePlayer = '{"jsonrpc": "2.0", "method": "Player.GetActivePlayers", "id": 1}';
var introspect = '{ "jsonrpc": "2.0", "method": "JSONRPC.Introspect", "params": { "filter": { "id": "Addons.GetAddons", "type": "method" } }, "id": 1 }';

function executeRemoteCommand(methodName, params){
    trace("executeRemoteCommand..");

    if (typeof(params) == 'undefined'){
        params = "";
    }

    trace(params);

    var paramContent = typeof(params) == 'undefined' ? '' : params;

    var requestString = '{"jsonrpc": "2.0", "method": "' + methodName + '", ' + paramContent + '"id": 1}';

    trace(requestString);

    makeRequest(requestString);
}


function requestTvShows(){
    pageStack.push(showsPage);
    //    pageStack.push(Qt.resolvedUrl("../Views/Shows.qml"));

    var requestString = buildJSONRequestString("VideoLibrary.GetTVShows");

    makeRequest(requestString, tvShowsReceived);
}

function tvShowsReceived(data){
    var collection = data.result.tvshows;
    var model = tvShowsListView.model;
    model.clear();

    for (var index in collection){
        model.append({"title": collection[index].label, "tvshowid": collection[index].tvshowid})
    }
}

function requestTvShowSeasons(showId){
    pageStack.push(seasonsPage, {"showId": showId});

    trace(showId);

    //    var requestString = '{"jsonrpc": "2.0","method": "VideoLibrary.GetSeasons","params": {"tvshowid": ' + showId + ', "properties": ["season", "episode"], "sort": {"order": "ascending","method": "label","ignorearticle": true}},"id": "libTvShows"}';

    var requestString = buildJSONRequestString("VideoLibrary.GetSeasons", '"tvshowid": ' + showId + ', "properties": ["season", "episode"], ');

    makeRequest(requestString, tvShowSeasonsReceived);
}

function tvShowSeasonsReceived(data){
    var collection = data.result.seasons;
    var model = seasonsList.model;
    model.clear();

    for (var index in collection){
        model.append({"title": collection[index].label, "season": collection[index].season})
    }
}

function requestTvShowEpisodes(showId, seasonId){
    pageStack.push(episodesPage, {"seasonId": seasonId});

    var requestString = buildJSONRequestString("VideoLibrary.GetEpisodes", '"tvshowid": ' + showId + ', "season": ' + seasonId + ', "properties": ["season", "episode"], ');

    makeRequest(requestString, tvShowEpisodesReceived);
}

function tvShowEpisodesReceived(data){
    var collection = data.result.episodes;
    var model = episodesList.model;
    model.clear();

    for (var index in collection){
        model.append({"title": collection[index].label,
                         "episode": collection[index].episode,
                         "episodeid": collection[index].episodeid})
    }
}

function playTvEpisode(episodeId){
    //    '{"jsonrpc":"2.0","id":"1","method":"Player.Open","params":{"item":{"directory":"Images/"}}}'

    //    var requestString = buildJSONRequestString("Player.Open", '"item" : [ "episodeid" : ' + 301 + ' ]}');

    var requestString = '{"jsonrpc": "2.0","method": "Player.Open","params": {"item" : { "episodeid" : "301" }},"id": "UNKNOWN"}';

    makeRequest(requestString);
}

function requestMovies(){
    pageStack.push(filmsPage);

    //    var requestString = '{"jsonrpc": "2.0","method": "VideoLibrary.GetMovies","params": {"sort": {"order": "ascending","method": "label","ignorearticle": true}},"id": "libMovies"}';

    var requestString = buildJSONRequestString("VideoLibrary.GetMovies");

    makeRequest(requestString, moviesReceived);
}

function moviesReceived(data){
    var collection = data.result.movies;
    var model = filmListView.model;
    model.clear();

    for (var index in collection){
        model.append({"title": collection[index].label})
    }
}

function requestAddons(){
    pageStack.push(addonsPage);

    var requestString = '{"jsonrpc": "2.0","method": "Addons.GetAddons","params": { "type": "xbmc.addon.video", "properties": ["name", "version", "path"] },"id": "libAddons"}';

    //    var requestString = buildJSONRequestString("Addons.GetAddons", '"properties": ["name", "version"], ');

    makeRequest(requestString, addonsReceived);
}

function addonsReceived(data){
    var collection = data.result.addons;
    var model = addonsListView.model;
    model.clear();

    for (var index in collection){
        model.append({"name": collection[index].name,
                         "version": collection[index].version,
                         "path": collection[index].path,
                         "addonid": collection[index].addonid})
    }
}

function requestSources(){
    pageStack.push(sourcesPage);

    var requestString = '{"jsonrpc": "2.0","method": "Files.GetSources","params": { "media": "video" },"id": "libAddons"}';

    //    var requestString = buildJSONRequestString("Addons.GetAddons", '"properties": ["name", "version"], ');

    makeRequest(requestString, sourcesReceived);
}

function sourcesReceived(data){
    //    trace(JSON.stringify(data));

    var collection = data.result.sources;
    var model = sourcesListView.model;
    model.clear();

    for (var index in collection){
        model.append({"name": collection[index].label, "path": collection[index].file})
    }
}


function buildJSONRequestString(methodName, props){
    if (typeof(props) == 'undefined'){
        props = '';
    }

    return '{"jsonrpc": "2.0","method": "' + methodName + '","params": {' + props + '"sort": {"order": "ascending","method": "label","ignorearticle": true}},"id": "UNKNOWN"}';
}

function makeRequest(requestString, callBack){
    var connString = "http://" + rootPage.address + ":" + rootPage.port + "/jsonrpc";

    trace(requestString);

    var doc = new XMLHttpRequest();
    doc.open("POST", connString, true);
    doc.setRequestHeader('Content-Type', 'application/json')
    doc.send(requestString);
    doc.onreadystatechange = function() {
        if (doc.readyState == 4) {
            trace("Your data has been sent")
            if (doc.status == 200)
            {
                var root = doc.responseText;

                trace(root);

                if (typeof(callBack) != 'undefined')
                    callBack(JSON.parse(root));
            } else {
                trace("doc status: " + doc.status + ", " + connString)
            }
        }
        else trace("doc ready status: " + doc.readyState)
    }
}

function trace(message){
    if(_traceToScreen){
        errorMessage.text = message;
    }else{
        console.log(message);
    }
}
