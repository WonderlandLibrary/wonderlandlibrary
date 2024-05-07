package com.masterof13fps.features.commands.impl;

import com.masterof13fps.Wrapper;
import com.masterof13fps.features.commands.Command;
import com.masterof13fps.manager.notificationmanager.NotificationType;
import com.masterof13fps.utils.NotifyUtil;

public class Register extends Command implements Wrapper {

    String syntax = getClientPrefix() + "register <Password> / " + getClientPrefix() + "register <Password> <Confirm " +
            "Password>";

    public Register() {
        super("register", "reg");
    }

    @Override
    public void execute(String[] args) {
        switch (args.length) {
            case 1:
                sendChatMessage("/register " + args[0]);
                notify.notification("Registration abgeschlossen!", "Du hast dich mit dem Passwort '" + args[0] +
                        "' registriert!", NotificationType.INFO, 5);
                break;
            case 2:
                if (args[1].equalsIgnoreCase(args[0])) {
                    sendChatMessage("/register " + args[0] + " " + args[1]);
                    notify.notification("Registration abgeschlossen!", "Du hast dich mit dem Passwort '" + args[0] +
                            "' registriert!", NotificationType.INFO, 5);
                } else {
                    notify.notification("Passwörter nicht gleich!", "Die beiden Passwörter stimmen nicht überein " +
                            "und wurden nicht abgesendet!", NotificationType.ERROR, 5);
                }
                break;
            default:
                notify.notification("Falscher Syntax!", "Nutze §c" + syntax + "§r!", NotificationType.ERROR, 5);
                break;
        }
    }
}
