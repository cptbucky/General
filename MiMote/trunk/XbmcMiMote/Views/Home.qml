import QtQuick 2.0
import Ubuntu.Components 0.1
import Ubuntu.Components.ListItems 0.1 as ListItem

import "../Scripts/XbmcComms.js" as XbmcComms

import "../Models"

Page {
    property string name
    property string address
    property string port

    id: homePage
    title: homePage.name
    anchors.fill: parent

    tools: toolbarActions1
    ToolbarActions {
        id: toolbarActions1
        Action {
            text: "choose mce"
            //            iconSource: Qt.resolvedUrl("avatar_contacts_list.png")
        }
        //        Action {
        //            text: "action 2"
        //            iconSource: Qt.resolvedUrl("call_icon.png")
        //        }
        //        lock: lockSwitch.checked
    }

    Tabs {
        anchors.fill: parent

        Tab {
            title: "Remote"
            page: Qt.resolvedUrl("Remote.qml")
        }
        Tab {
            title: "Tv Shows"
            //            page: Qt.resolvedUrl("TvShowList.qml")
            page: Qt.resolvedUrl("Shows.qml")

        }
        Tab {
            title: "Movies"
            page: Qt.resolvedUrl("FilmList.qml")
        }
        Tab {
            title: "Addons"
            page: Qt.resolvedUrl("AddonList.qml")
        }
    }
}
