/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.management.command;

import java.util.Arrays;
import me.arithmo.event.EventListener;
import me.arithmo.management.command.CommandManager;
import me.arithmo.management.command.Fireable;
import me.arithmo.util.misc.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

public abstract class Command
implements Fireable,
EventListener {
    private final String[] names;
    private final String description;
    public static final String chatPrefix = "\u00a7c[\u00a7fS\u00a7c]\u00a77 ";
    public Minecraft mc = Minecraft.getMinecraft();

    public Command(String[] names, String description) {
        this.names = names;
        this.description = description;
    }

    protected void printDescription() {
        String message = "\u00a7c[\u00a7fS\u00a7c]\u00a77 " + this.getName() + (Object)((Object)EnumChatFormatting.GRAY) + ": " + this.description;
        ChatUtil.printChat(message);
    }

    protected void printUsage() {
        String message = "\u00a7c[\u00a7fS\u00a7c]\u00a77 " + this.getName() + (Object)((Object)EnumChatFormatting.GRAY) + ": " + this.getUsage();
        ChatUtil.printChat(message);
    }

    public void register(CommandManager manager) {
        for (String name : this.names) {
            manager.addCommand(name.toLowerCase(), this);
        }
    }

    public abstract String getUsage();

    public String getName() {
        return this.names[0];
    }

    public boolean isMatch(String text) {
        return Arrays.asList(this.names).contains(text.toLowerCase());
    }

    public String getDescription() {
        return this.description;
    }
}

