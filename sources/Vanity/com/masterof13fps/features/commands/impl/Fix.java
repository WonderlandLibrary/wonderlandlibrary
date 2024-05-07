package com.masterof13fps.features.commands.impl;

import com.masterof13fps.Client;
import com.masterof13fps.Wrapper;
import com.masterof13fps.features.commands.Command;
import com.masterof13fps.utils.NotifyUtil;
import com.masterof13fps.manager.notificationmanager.NotificationType;

public class Fix extends Command {

    String syntax = Client.main().getClientPrefix() + "fix";

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            Wrapper.mc.thePlayer.motionY = 0.1;
            notify.notification("Befehl ausgeführt", "Deine PlayerPos wurde gefixed", NotificationType.INFO, 5);
        } else {
            notify.notification("Falscher Syntax!", "Nutze §c" + syntax + "§r!", NotificationType.ERROR, 5);
        }
    }

    public Fix() {
        super("fix", "fix");
    }

}
