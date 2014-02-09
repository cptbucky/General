import QtQuick 2.0
import Ubuntu.Components 0.1
import Ubuntu.Components.ListItems 0.1 as ListItem

import "../Scripts/XbmcComms.js" as XbmcComms

import "../Models"

Column {
    anchors.fill: parent

    Row{
        anchors.fill: parent

        property string showId

        ListView {
            id: seasonsList
            anchors.fill: parent

            model: TvSeasonsModel {}
            delegate:
                ListItem.Subtitled {
                width: parent.width
                text: title
                subText: seasonsPage.showId + ", " + season
                onClicked: XbmcComms.requestTvShowEpisodes(seasonsPage.showId, season)
                progression: true
            }
        }
    }
}
