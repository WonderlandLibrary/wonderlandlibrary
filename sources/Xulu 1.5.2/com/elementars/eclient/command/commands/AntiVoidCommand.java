// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.command.commands;

import com.elementars.eclient.module.player.AntiVoid;
import com.elementars.eclient.command.Command;

public class AntiVoidCommand extends Command
{
    public AntiVoidCommand() {
        super("antivoid", "Shows if you have logged from antivoid on a server", new String[0]);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length == 1) {
            Command.sendChatMessage("Please specify a server IP");
            return;
        }
        if (AntiVoid.INSTANCE.ipList.isEmpty() || !AntiVoid.INSTANCE.ipList.contains(args[1])) {
            Command.sendChatMessage("You did not trigger AntiVoid on this server!");
        }
        else {
            Command.sendChatMessage("You did fall below the Y level in AntiVoid! Be careful!");
        }
    }
}
