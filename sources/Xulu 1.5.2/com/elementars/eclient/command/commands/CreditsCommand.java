// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.command.commands;

import com.elementars.eclient.command.Command;

public class CreditsCommand extends Command
{
    String[] credits;
    
    public CreditsCommand() {
        super("credits", "Shows the people who helped come up with ideas for modules and ect.", new String[0]);
        this.credits = new String[] { "Sago", "WeWide", "Nemac", "Jumpy/Xdolf", "Naughty", "John", "Mtnl" };
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length > 1 && args[1].equalsIgnoreCase("help")) {
            this.showSyntax(args[0]);
            return;
        }
        Command.sendChatMessage("Here's a list of people who helped brainstorm ideas for the client:");
        String out = "";
        boolean start = true;
        for (final String s : this.credits) {
            if (start) {
                out = s;
            }
            else {
                out = out + ", " + s;
            }
            start = false;
        }
        Command.sendChatMessage(out);
    }
}
