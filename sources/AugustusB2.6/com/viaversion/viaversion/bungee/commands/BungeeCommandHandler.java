// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bungee.commands;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.command.ViaSubCommand;
import com.viaversion.viaversion.bungee.commands.subs.ProbeSubCmd;
import com.viaversion.viaversion.commands.ViaCommandHandler;

public class BungeeCommandHandler extends ViaCommandHandler
{
    public BungeeCommandHandler() {
        try {
            this.registerSubCommand(new ProbeSubCmd());
        }
        catch (Exception e) {
            Via.getPlatform().getLogger().severe("Failed to register Bungee subcommands");
            e.printStackTrace();
        }
    }
}
