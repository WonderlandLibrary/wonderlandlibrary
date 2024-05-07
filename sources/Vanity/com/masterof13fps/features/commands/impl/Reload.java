package com.masterof13fps.features.commands.impl;

import com.masterof13fps.features.commands.Command;
import com.masterof13fps.manager.notificationmanager.NotificationType;
import com.masterof13fps.utils.NotifyUtil;

public class Reload extends Command {

    String syntax = getClientPrefix() + "reload";

    @Override
    public void execute(String[] args) {
        if(args.length == 0){
            reloadClient();
        }else{
            notify.notification("Falscher Syntax!", "Nutze §c" + syntax + "§r!", NotificationType.ERROR, 5);
        }
    }

    public Reload() {
        super("reload", "rl");
    }
}
