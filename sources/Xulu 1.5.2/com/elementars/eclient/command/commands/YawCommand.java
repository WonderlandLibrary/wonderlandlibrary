// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.command.commands;

import com.elementars.eclient.util.Wrapper;
import com.elementars.eclient.command.Command;

public class YawCommand extends Command
{
    public YawCommand() {
        super("setyaw", "Sets the yaw of the player", new String[0]);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length < 2) {
            Command.sendChatMessage("Please specify the yaw!");
            return;
        }
        Wrapper.getMinecraft().player.rotationYaw = Integer.valueOf(args[1]);
    }
}
