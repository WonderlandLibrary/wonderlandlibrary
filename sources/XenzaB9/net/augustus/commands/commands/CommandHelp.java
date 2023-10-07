// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.commands.commands;

import java.util.Iterator;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import net.augustus.Augustus;
import net.augustus.commands.Command;

public class CommandHelp extends Command
{
    public CommandHelp() {
        super(".help");
    }
    
    @Override
    public void commandAction(final String[] message) {
        super.commandAction(message);
        final String client = "§6[§9" + Augustus.getInstance().getName() + "§6] ";
        CommandHelp.mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("\n\n" + client + "§7Help commands:"));
        for (final Command command : Augustus.getInstance().getCommandManager().getCommands()) {
            command.helpMessage();
        }
    }
}
