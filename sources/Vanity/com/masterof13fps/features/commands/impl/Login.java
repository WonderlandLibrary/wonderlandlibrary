package com.masterof13fps.features.commands.impl;

import com.masterof13fps.Wrapper;
import com.masterof13fps.features.commands.Command;
import com.masterof13fps.manager.notificationmanager.NotificationType;
import com.masterof13fps.utils.NotifyUtil;

public class Login extends Command implements Wrapper {

    String syntax = getClientPrefix() + "login <Password>";

    public Login() {
        super("login", "log");
    }

    @Override
    public void execute(String[] args) {
        switch (args.length) {
            case 1:
                sendChatMessage("/login " + args[0]);
                notify.notification("Erfolgreich angemeldet!", "Du hast dich mit dem Passwort '" + args[0] +
                        "' angemeldet!", NotificationType.INFO, 5);
                break;
            default:
                notify.notification("Falscher Syntax!", "Nutze §c" + syntax + "§r!", NotificationType.ERROR, 5);
                break;
        }
    }
}
