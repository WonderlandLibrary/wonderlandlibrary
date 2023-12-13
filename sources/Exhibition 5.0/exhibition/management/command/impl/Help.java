// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.command.impl;

import net.minecraft.util.EnumChatFormatting;
import java.util.Iterator;
import exhibition.util.misc.ChatUtil;
import exhibition.Client;
import java.util.ArrayList;
import exhibition.management.command.Command;

public class Help extends Command
{
    public Help(final String[] names, final String description) {
        super(names, description);
    }
    
    @Override
    public void fire(final String[] args) {
        int i = 1;
        if (args == null) {
            final ArrayList<String> used = new ArrayList<String>();
            for (final Command command : Client.commandManager.getCommands()) {
                if (used.contains(command.getName())) {
                    continue;
                }
                used.add(command.getName());
                ChatUtil.printChat("§4[§cE§4]§8 " + i + ". " + command.getName() + " - " + command.getDescription());
                ++i;
            }
            ChatUtil.printChat("§4[§cE§4]§8 Specify a name of a command for details about it.");
        }
        else if (args.length > 0) {
            final String name = args[0];
            final Command command2 = Client.commandManager.getCommand(name);
            if (command2 == null) {
                ChatUtil.printChat("§4[§cE§4]§8 Could not find: " + name);
                return;
            }
            ChatUtil.printChat("§4[§cE§4]§8 " + command2.getName() + ": " + command2.getDescription());
            ChatUtil.printChat(command2.getUsage());
        }
    }
    
    @Override
    public String getUsage() {
        return "Help " + EnumChatFormatting.ITALIC + " [optional] " + EnumChatFormatting.RESET + "<Command>";
    }
}
