import QtQuick 2.0
import Ubuntu.Components 0.1
import Ubuntu.Components.ListItems 0.1 as ListItem

import "../Scripts/XbmcComms.js" as XbmcComms

import "../Models"

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
