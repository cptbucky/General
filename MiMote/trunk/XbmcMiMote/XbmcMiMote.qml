import QtQuick 2.0
import Ubuntu.Components 0.1
import Ubuntu.Components.ListItems 0.1 as ListItem

import "./Scripts/XbmcComms.js" as XbmcComms
import "Models"
import "Views"

//Column {
//    ListItem.Header { text: "Title" }
//    ListItem.Standard {
//        text: "lounge"
//        onClicked: function(){
//            XbmcComms.currentMce = new XbmcComms.Mce("192.168.0.151:8080")
//        }
//    }
//    ListItem.Standard {
//        text: "antec"
//        onClicked: Qt.resolvedUrl("Views/Remote.qml") //XbmcComms.navigateToHome("192.168.0.154:8080")
////        onClicked: function(){
////            errorMessage.text = "worked"
////            XbmcComms.currentMce = new XbmcComms.Mce("192.168.0.154:8080")
////        }
//        progression: true
//    }
//    Row{
//        spacing: units.gu(1)

//        Label{
//            id: errorMessage
//            text: "temp"
//        }
//    }
//}



MainView{
    PageStack {
        id: pageStack
        anchors.fill: parent

        Component.onCompleted: pageStack.push(mceSelectionPage)

        Page {
            id: mceSelectionPage
            title: "MCEs"
            anchors.fill: parent

            //            GridView {
            //                id: view
            //                //                anchors.left: parent.left
            //                //                anchors.right: parent.right
            //                //                anchors.top: parent.top
            //                anchors.margins: 50 // Sets all margins at once
            //                anchors.fill: parent

            //                //                cellWidth: 250; cellHeight: 250

            //                model: McesModel {}
            //                delegate:
            //                    //                    Column{

            //                    //                    Button {
            //                    //                        text: display
            //                    //                        height: 200
            //                    //                        width: 200
            //                    //                        //                        anchors.fill: parent
            //                    //                        onClicked: pageStack.push(rootPage, {name: display, address: address, port: port});
            //                    //                    }
            //                    //                }
            //                    Column{
            //                    ListItem.Standard {
            //                        text: display
            //                        onClicked: pageStack.push(Qt.resolvedUrl("Views/Remote.qml"));
            //                    }
            //                }
            //                //                    highlight: highlight
            //                //                    highlightFollowsCurrentItem: false
            //                focus: true
            //            }
            //            }

            ListView {
                anchors.fill: parent

                model: McesModel {}
                delegate:
                    ListItem.Subtitled {
                    width: parent.width
                    text: display
                    subText: address
                    onClicked: pageStack.push(rootPage, {name: display, address: address, port: port});
                    progression: true
                }
            }
        }

        Page {
            id: rootPage
            title: rootPage.name
            anchors.fill: parent
            visible: false

            property string name
            property string address
            property string port

            Column {
                anchors.fill: parent

                ListItem.Standard {
                    text: "Remote Control"
                    onClicked: pageStack.push(Qt.resolvedUrl("Views/Remote.qml"));
                }
                ListItem.Standard {
                    text: "Tv Shows"
                    onClicked: XbmcComms.requestTvShows();
                }
                ListItem.Standard{
                    text: "Movies"
                    onClicked: XbmcComms.requestMovies();
                }
                ListItem.Standard {
                    text: "Video Addons"
                    onClicked: XbmcComms.requestAddons();
                }
                ListItem.Standard {
                    text: "Sources"
                    onClicked: XbmcComms.requestSources();
                }
                //                model: MenuModel {}
                //                delegate:
                //                    ListItem.Standard {
                //                    width: parent.width
                //                    text: title
                ////                    subText: address
                ////                    onClicked: pageStack.push(Qt.resolvedUrl(page), {name: display, address: address, port: port});
                //                    progression: true
                //                }
            }
        }


        Page {
            id: showsPage
            title: "Tv Shows"
            visible: false

            anchors.fill: parent

            ListView {
                id: tvShowsListView
                anchors.fill: parent

                model: TvShowsModel {}
                delegate:
                    ListItem.Standard {
                    width: parent.width
                    text: title
                    //                    subText: tvshowid
                    onClicked: XbmcComms.requestTvShowSeasons(tvshowid)
                    progression: true
                }
            }
        }

        Page {
            id: seasonsPage
            anchors.fill: parent
            visible: false

            property string showId

            ListView {
                id: seasonsList
                anchors.fill: parent

                model: TvSeasonsModel {}
                delegate:
                    ListItem.Standard {
                    width: parent.width
                    text: title
                    //                    subText: seasonsPage.showId + ", " + season
                    onClicked: XbmcComms.requestTvShowEpisodes(seasonsPage.showId, season)
                    progression: true
                }
            }
        }

        Page {
            id: episodesPage
            anchors.fill: parent
            visible: false

            property string seasonId

            ListView {
                id: episodesList
                anchors.fill: parent

                model: TvEpisodesModel {}
                delegate:
                    ListItem.Subtitled {
                    width: parent.width
                    text: title
                    subText: episodeid
                    onClicked: XbmcComms.playTvEpisode(episodeid)
                    //                progression: true
                }
            }
        }


        Page {
            id: filmsPage
            title: "Movies"
            anchors.fill: parent
            visible: false

            ListView {
                id: filmListView
                anchors.fill: parent

                model: FilmsModel {}
                delegate:
                    ListItem.Standard {
                    width: parent.width
                    text: title
                    //            subText: address
                    //            onClicked: pageStack.push(Qt.resolvedUrl("Views/Home.qml"), {name: display, address: address, port: port});
                    progression: true
                }
            }
        }

        Page {
            id: addonsPage
            title: "Video Addons"
            anchors.fill: parent
            visible: false

            ListView {
                id: addonsListView
                anchors.fill: parent

                model: AddonsModel {}
                delegate:
                    ListItem.Subtitled {
                    width: parent.width
                    text: name
                    subText: path
                    onClicked: pageStack.push(Qt.resolvedUrl("Views/Home.qml"), {name: display, address: address, port: port});
                    progression: true
                }
            }
        }

        Page {
            id: sourcesPage
            title: "Sources"
            anchors.fill: parent
            visible: false

            ListView {
                id: sourcesListView
                anchors.fill: parent

                model: SourcesModel {}
                delegate:
                    ListItem.Subtitled {
                    width: parent.width
                    text: name
                    subText: path
                    onClicked: pageStack.push(sourcesPage);
                    progression: true
                }
            }
        }


        Row{
            anchors.fill: parent

            Label{
                id: errorMessage
                width: 400
                text: ""
                wrapMode: Text.WordWrap
            }
        }
    }
}
