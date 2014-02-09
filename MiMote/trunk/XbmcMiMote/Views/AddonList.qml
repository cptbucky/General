import QtQuick 2.0
import Ubuntu.Components 0.1
import Ubuntu.Components.ListItems 0.1 as ListItem

import "../Scripts/XbmcComms.js" as XbmcComms

import "../Models"

Column {
    anchors.centerIn: parent

    width: 720
    height: 960

    Component.onCompleted: XbmcComms.requestAddons();

    Row{
        anchors.fill: parent

    ListView {
        id: addonsListView
        anchors.fill: parent

        model: AddonsModel {}
        delegate:
            ListItem.Subtitled {
            width: parent.width
            text: name
            subText: version
//            onClicked: pageStack.push(Qt.resolvedUrl("Views/Home.qml"), {name: display, address: address, port: port});
            progression: true
        }
    }
}

    Row{
        id: row1
        anchors.fill: parent

        Label{
            id: errorMessage
            width: 400
            text: "temp Texts"
            wrapMode: Text.WordWrap
        }
    }
}
