import QtQuick 2.0
import Ubuntu.Components 0.1

import "../Scripts/XbmcComms.js" as XbmcComms

Page {
    id: remotePage
    visible: false

    anchors.fill: parent

    ViewStyle { id: style }

    Column{
        id: column1
        anchors.centerIn: parent
        spacing: units.gu(1)

        Row{
            spacing: units.gu(1)

            Button{
                text: "stop"
                height: style.height
                width: style.width
                onClicked: XbmcComms.executeRemoteCommand(XbmcComms.stopCmd, XbmcComms.paramsString)
            }
            Button{
                text: "play/pause"
                height: style.height
                width: style.width
                onClicked: XbmcComms.executeRemoteCommand(XbmcComms.playPauseCmd, XbmcComms.paramsString)
            }
            Button{
                text: ""
                height: style.height
                width: style.width
            }
        }

        Row{
            spacing: units.gu(1)
            Button{
                text: "info"
                height: style.height
                width: style.width
                onClicked: XbmcComms.executeRemoteCommand(XbmcComms.infoCmd)
            }
            Button{
                height: style.height
                width: style.width
                text: "up"
                onClicked: XbmcComms.executeRemoteCommand(XbmcComms.upCmd)
            }
            Button{
                text: "back"
                height: style.height
                width: style.width
                onClicked: XbmcComms.executeRemoteCommand(XbmcComms.backCmd)
            }
        }

        Row{
            spacing: units.gu(1)

            Button{
                text: "left"
                height: style.height
                width: style.width
                onClicked: XbmcComms.executeRemoteCommand(XbmcComms.leftCmd)
            }
            Button{
                text: "select"
                height: style.height
                width: style.width
                onClicked: XbmcComms.executeRemoteCommand(XbmcComms.selectCmd)
            }
            Button{
                text: "right"
                height: style.height
                width: style.width
                onClicked: XbmcComms.executeRemoteCommand(XbmcComms.rightCmd)
            }
        }

        Row{
            spacing: units.gu(1)

            Button{
                text: "osd"
                height: style.height
                width: style.width
                onClicked: XbmcComms.executeRemoteCommand(XbmcComms.menuCmd)
            }
            Button{
                text: "down"
                height: style.height
                width: style.width
                onClicked: XbmcComms.executeRemoteCommand(XbmcComms.downCmd)
            }
            Button{
                text: "context"
                height: style.height
                width: style.width
                onClicked: XbmcComms.executeRemoteCommand(XbmcComms.contextCmd)
            }
        }
    }
}

