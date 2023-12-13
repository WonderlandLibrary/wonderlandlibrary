// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.command.commands;

import com.elementars.eclient.command.Command;

public class PrefixCommand extends Command
{
    public PrefixCommand() {
        super("prefix", "Changes the prefix for commands", new String[0]);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length < 2) {
            Command.sendChatMessage("Specify what prefix you would like to change to.");
            Command.sendChatMessage("Current prefix is: " + Command.getPrefix());
            return;
        }
        if (args[1].equalsIgnoreCase("help")) {
            this.showSyntax(args[0]);
            return;
        }
        Command.setPrefix(args[1]);
        Command.sendChatMessage("Set the prefix to: " + Command.getPrefix());
    }
}
