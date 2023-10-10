// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.velocity.command;

import com.velocitypowered.api.command.CommandInvocation;
import java.util.List;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import com.viaversion.viaversion.api.command.ViaSubCommand;
import com.viaversion.viaversion.velocity.command.subs.ProbeSubCmd;
import com.velocitypowered.api.command.SimpleCommand;
import com.viaversion.viaversion.commands.ViaCommandHandler;

public class VelocityCommandHandler extends ViaCommandHandler implements SimpleCommand
{
    public VelocityCommandHandler() {
        try {
            this.registerSubCommand(new ProbeSubCmd());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void execute(final SimpleCommand.Invocation invocation) {
        this.onCommand(new VelocityCommandSender(invocation.source()), (String[])invocation.arguments());
    }
    
    public List<String> suggest(final SimpleCommand.Invocation invocation) {
        return this.onTabComplete(new VelocityCommandSender(invocation.source()), (String[])invocation.arguments());
    }
}
