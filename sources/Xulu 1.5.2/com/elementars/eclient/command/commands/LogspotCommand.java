// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.command.commands;

import com.elementars.eclient.module.player.SelfLogoutSpot;
import com.elementars.eclient.command.Command;

public class LogspotCommand extends Command
{
    public LogspotCommand() {
        super("logspot", "Shows your logout spot from a given server", new String[0]);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length == 1) {
            Command.sendChatMessage("Please specify a server IP");
            return;
        }
        if (SelfLogoutSpot.INSTANCE.logoutMap.isEmpty() || SelfLogoutSpot.INSTANCE.logoutMap.get(args[1]) == null) {
            Command.sendChatMessage("Your logout spot is not saved for that server!");
        }
        else {
            Command.sendChatMessage("Your logout spot is - " + SelfLogoutSpot.INSTANCE.logoutMap.get(args[1]));
        }
    }
}
