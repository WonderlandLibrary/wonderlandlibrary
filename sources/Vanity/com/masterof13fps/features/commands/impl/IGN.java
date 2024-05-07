package com.masterof13fps.features.commands.impl;

import com.masterof13fps.features.commands.Command;
import com.masterof13fps.manager.notificationmanager.NotificationType;
import com.masterof13fps.utils.NotifyUtil;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class IGN extends Command {

    String syntax = getClientPrefix() + "ign / " + getClientPrefix() + "ign copy";

    public IGN() {
        super("ign", "name");
    }

    @Override
    public void execute(String[] args) {
        switch (args.length) {
            case 0:
                notify.notification("Dein Username", "Dein Username lautet '" + mc.session.getUsername() + "'",
                        NotificationType.INFO, 5);
                break;
            case 1:
                if (args[0].equalsIgnoreCase("copy")) {
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(mc.session.getUsername()), null);
                    notify.notification("Username kopiert", "Dein aktueller Username wurde in die Zwischenablage " +
                            "kopiert!", NotificationType.INFO, 5);
                } else {
                    notify.notification("Falscher Syntax!", "Nutze §c" + syntax + "§r!", NotificationType.ERROR, 5);
                }
                break;
            default:
                notify.notification("Falscher Syntax!", "Nutze §c" + syntax + "§r!", NotificationType.ERROR, 5);
                break;
        }
    }
}
