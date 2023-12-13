// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.command.commands;

import com.elementars.eclient.Xulu;
import com.elementars.eclient.command.Command;

public class SaveCommand extends Command
{
    public SaveCommand() {
        super("save", "Saves the config", new String[0]);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length > 1 && args[1].equalsIgnoreCase("help")) {
            this.showSyntax(args[0]);
            return;
        }
        Xulu.save();
        Command.sendChatMessage("Config saved!");
    }
}
