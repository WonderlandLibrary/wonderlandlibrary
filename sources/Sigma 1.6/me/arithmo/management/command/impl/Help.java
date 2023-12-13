/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.management.command.impl;

import java.util.ArrayList;
import java.util.Collection;
import me.arithmo.Client;
import me.arithmo.event.Event;
import me.arithmo.management.command.Command;
import me.arithmo.management.command.CommandManager;
import me.arithmo.util.misc.ChatUtil;
import net.minecraft.util.EnumChatFormatting;

public class Help
extends Command {
    public Help(String[] names, String description) {
        super(names, description);
    }

    @Override
    public void fire(String[] args) {
        int i = 1;
        if (args == null) {
            ArrayList<String> used = new ArrayList<String>();
            for (Command command : Client.commandManager.getCommands()) {
                if (used.contains(command.getName())) continue;
                used.add(command.getName());
                ChatUtil.printChat("\u00a7c[\u00a7fS\u00a7c]\u00a77 " + i + ". " + command.getName() + " - " + command.getDescription());
                ++i;
            }
            ChatUtil.printChat("\u00a7c[\u00a7fS\u00a7c]\u00a77 Specify a name of a command for details about it.");
        } else if (args.length > 0) {
            String name = args[0];
            Command command = Client.commandManager.getCommand(name);
            if (command == null) {
                ChatUtil.printChat("\u00a7c[\u00a7fS\u00a7c]\u00a77 Could not find: " + name);
                return;
            }
            ChatUtil.printChat("\u00a7c[\u00a7fS\u00a7c]\u00a77 " + command.getName() + ": " + command.getDescription());
            ChatUtil.printChat(command.getUsage());
        }
    }

    @Override
    public String getUsage() {
        return "Help " + (Object)((Object)EnumChatFormatting.ITALIC) + " [optional] " + (Object)((Object)EnumChatFormatting.RESET) + "<Command>";
    }

    public void onEvent(Event event) {
    }
}

