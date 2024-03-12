// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.commands;

import net.minecraft.util.EnumChatFormatting;
import dev.tenacity.utils.player.ChatUtil;
import net.minecraft.client.Minecraft;

public abstract class Command
{
    protected final Minecraft mc;
    private final String name;
    private final String description;
    private final String usage;
    private final String[] otherPrefixes;
    public static boolean sendSuccess;
    
    public Command(final String name, final String description, final String usage, final String... otherPrefixes) {
        this.mc = Minecraft.getMinecraft();
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.otherPrefixes = otherPrefixes;
    }
    
    public abstract void execute(final String[] p0);
    
    public String getName() {
        return this.name;
    }
    
    public void sendChatWithPrefix(final String message) {
        if (Command.sendSuccess) {
            ChatUtil.print(message);
        }
    }
    
    public void sendChatError(final String message) {
        ChatUtil.print("Error", EnumChatFormatting.RED, message);
    }
    
    public void sendChatWithInfo(final String message) {
        if (Command.sendSuccess) {
            ChatUtil.print("Info", EnumChatFormatting.BLUE, message);
        }
    }
    
    public void usage() {
        ChatUtil.error("Usage: " + this.usage);
    }
    
    public String getUsage() {
        return this.usage;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String[] getOtherPrefixes() {
        return this.otherPrefixes;
    }
    
    static {
        Command.sendSuccess = true;
    }
}
