package com.masterof13fps.features.commands.impl;

import com.masterof13fps.Client;
import com.masterof13fps.features.commands.Command;
import com.masterof13fps.utils.NotifyUtil;
import com.masterof13fps.manager.notificationmanager.NotificationType;

public class Info extends Command {

    String syntax = Client.main().getClientPrefix() + "info";

    public Info() {
        super("info", "information");
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            notify.chat("Client-Name: " + Client.main().getClientName());
            notify.chat("Client-Version: " + Client.main().getClientVersion());
            notify.chat("Client-Author: " + Client.main().getClientCoder());
        } else {
            notify.notification("Falscher Syntax!", "Nutze §c" + syntax + "§r!", NotificationType.ERROR, 5);
        }
    }

}
