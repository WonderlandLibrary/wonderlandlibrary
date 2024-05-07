package com.masterof13fps.features.commands.impl;

import com.masterof13fps.Client;
import com.masterof13fps.features.commands.Command;
import com.masterof13fps.utils.NotifyUtil;
import com.masterof13fps.manager.notificationmanager.NotificationType;

public class NCP extends Command {

    String syntax = Client.main().getClientPrefix() + "ncp <Name>";

    @Override
    public void execute(String[] args) {
        if (args.length == 1) {
            sendChatMessage("/testncp input " + args[0]);
            notify.notification("Input gesetzt!", "Falls TestNCP auf diesem Server vorhanden ist, wurde dein Input auf \"" + args[0] + "\" gesetzt!", NotificationType.INFO, 5);
        } else {
            notify.notification("Falscher Syntax!", "Nutze §c" + syntax + "§r!", NotificationType.ERROR, 5);
        }
    }

    public NCP() {
        super("ncp", "testncp");
    }
}
