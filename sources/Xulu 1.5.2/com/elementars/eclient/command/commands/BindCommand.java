// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.command.commands;

import com.elementars.eclient.module.Module;
import org.lwjgl.input.Keyboard;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.command.Command;

public class BindCommand extends Command
{
    public BindCommand() {
        super("bind", "binds a module to a key", new String[0]);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length < 2) {
            Command.sendChatMessage("Please specify which module you want bound");
            return;
        }
        if (args[1].equalsIgnoreCase("help")) {
            this.showSyntax(args[0]);
            return;
        }
        if (args.length < 3) {
            Command.sendChatMessage("Please specify the key you would like to bind");
            return;
        }
        final Module m = Xulu.MODULE_MANAGER.getModuleByName(args[1]);
        if (m == null) {
            Command.sendChatMessage("Module not found.");
            return;
        }
        m.setKey(Keyboard.getKeyIndex(args[2].toUpperCase()));
        Command.sendChatMessage(m.getDisplayName() + " bound to " + args[2].toUpperCase());
    }
}
