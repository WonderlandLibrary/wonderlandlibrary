// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.command.commands;

import java.util.Iterator;
import com.elementars.eclient.command.CommandManager;
import com.elementars.eclient.command.Command;

public class HelpCommand extends Command
{
    public HelpCommand() {
        super("help", "Shows a list of commands", new String[0]);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length > 1 && args[1].equalsIgnoreCase("help")) {
            this.showSyntax(args[0]);
            return;
        }
        Command.sendChatMessage("Here's a list of commands:");
        for (final Command command : CommandManager.getCommands()) {
            Command.sendChatMessage(command.getName() + " : " + command.getDescription());
        }
        Command.sendChatMessage("Follow any command with help to see command options");
    }
}
