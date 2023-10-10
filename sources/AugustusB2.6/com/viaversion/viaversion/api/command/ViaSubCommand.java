// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.command;

import com.viaversion.viaversion.util.ChatColorUtil;
import java.util.Collections;
import java.util.List;

public abstract class ViaSubCommand
{
    public abstract String name();
    
    public abstract String description();
    
    public String usage() {
        return this.name();
    }
    
    public String permission() {
        return "viaversion.admin";
    }
    
    public abstract boolean execute(final ViaCommandSender p0, final String[] p1);
    
    public List<String> onTabComplete(final ViaCommandSender sender, final String[] args) {
        return Collections.emptyList();
    }
    
    public static String color(final String s) {
        return ChatColorUtil.translateAlternateColorCodes(s);
    }
    
    public static void sendMessage(final ViaCommandSender sender, final String message, final Object... args) {
        sender.sendMessage(color((args == null) ? message : String.format(message, args)));
    }
}
