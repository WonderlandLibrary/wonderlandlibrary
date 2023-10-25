/*
 * Decompiled with CFR 0.150.
 */
package com.viaversion.viaversion.commands.defaultsubs;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import com.viaversion.viaversion.api.command.ViaSubCommand;

public class DebugSubCmd
extends ViaSubCommand {
    @Override
    public String name() {
        return "debug";
    }

    @Override
    public String description() {
        return "Toggle debug mode";
    }

    @Override
    public boolean execute(ViaCommandSender sender, String[] args) {
        Via.getManager().setDebug(!Via.getManager().isDebug());
        DebugSubCmd.sendMessage(sender, "&6Debug mode is now %s", Via.getManager().isDebug() ? "&aenabled" : "&cdisabled");
        return true;
    }
}

