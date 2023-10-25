/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.util.ResourceLeakDetector
 *  io.netty.util.ResourceLeakDetector$Level
 */
package com.viaversion.viaversion.commands.defaultsubs;

import com.viaversion.viaversion.api.command.ViaCommandSender;
import com.viaversion.viaversion.api.command.ViaSubCommand;
import io.netty.util.ResourceLeakDetector;

public class DisplayLeaksSubCmd
extends ViaSubCommand {
    @Override
    public String name() {
        return "displayleaks";
    }

    @Override
    public String description() {
        return "Try to hunt memory leaks!";
    }

    @Override
    public boolean execute(ViaCommandSender sender, String[] args) {
        if (ResourceLeakDetector.getLevel() != ResourceLeakDetector.Level.PARANOID) {
            ResourceLeakDetector.setLevel((ResourceLeakDetector.Level)ResourceLeakDetector.Level.PARANOID);
        } else {
            ResourceLeakDetector.setLevel((ResourceLeakDetector.Level)ResourceLeakDetector.Level.DISABLED);
        }
        DisplayLeaksSubCmd.sendMessage(sender, "&6Leak detector is now %s", ResourceLeakDetector.getLevel() == ResourceLeakDetector.Level.PARANOID ? "&aenabled" : "&cdisabled");
        return true;
    }
}

