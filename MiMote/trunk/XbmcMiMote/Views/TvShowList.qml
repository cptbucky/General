import QtQuick 2.0
import Ubuntu.Components 0.1
import Ubuntu.Components.ListItems 0.1 as ListItem

import "../Scripts/XbmcComms.js" as XbmcComms

import "../Models"

PageStack {
    id: tvShowsStack

    anchors.fill: parent

    Component.onCompleted: XbmcComms.requestTvShows();

    Page {
        id: showsPage
        anchors.fill: parent

        ListView {
            id: tvShowsListView
            anchors.fill: parent

            model: TvShowsModel {}
            delegate:
                ListItem.Subtitled {
                width: parent.width
                text: title
                subText: tvshowid
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
                ListItem.Subtitled {
                width: parent.width
                text: title
                subText: seasonsPage.showId + ", " + season
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
                ListItem.Standard {
                width: parent.width
                text: title
                //            subText: address
                //                    onClicked: tvShowsStack.push(episodesPage);
                //                progression: true
            }
        }
    }

    Row{
        anchors.fill: parent

        Label{
            id: errorMessage
            width: 400
            text: "test"
            wrapMode: Text.WordWrap
        }
    }
}
