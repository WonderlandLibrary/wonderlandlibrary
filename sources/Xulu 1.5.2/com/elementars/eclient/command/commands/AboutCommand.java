// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.command.commands;

import com.elementars.eclient.command.Command;

public class AboutCommand extends Command
{
    public AboutCommand() {
        super("about", "Shows general information", new String[0]);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length > 1 && args[1].equalsIgnoreCase("help")) {
            this.showSyntax(args[0]);
            return;
        }
        Command.sendChatMessage("Xulu v1.5.2 by Elementars and John200410");
        Command.sendChatMessage("Do .help to see a list of commands");
    }
}
