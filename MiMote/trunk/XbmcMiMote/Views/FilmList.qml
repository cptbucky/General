import QtQuick 2.0
import Ubuntu.Components 0.1
import Ubuntu.Components.ListItems 0.1 as ListItem

import "../Scripts/XbmcComms.js" as XbmcComms

import "../Models"

Column {
    anchors.fill: parent

    Component.onCompleted: XbmcComms.requestMovies();

    Row{
        anchors.fill: parent

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

    Row{
        id: row1
        spacing: units.gu(1)

        Label{
            id: errorMessage
            width: 400
            text: "temp Texts"
            wrapMode: Text.WordWrap
        }
    }
}
