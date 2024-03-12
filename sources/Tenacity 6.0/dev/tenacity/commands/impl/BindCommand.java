// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.commands.impl;

import dev.tenacity.module.Module;
import org.lwjgl.input.Keyboard;
import dev.tenacity.Tenacity;
import dev.tenacity.commands.Command;

public class BindCommand extends Command
{
    public BindCommand() {
        super("bind", "Binds a module to a certain key", ".bind [module] [key]", new String[0]);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length != 2) {
            this.usage();
        }
        else {
            final String stringModule = args[0];
            try {
                final Module module = Tenacity.INSTANCE.getModuleCollection().getModuleByName(stringModule);
                module.getKeybind().setCode(Keyboard.getKeyIndex(args[1].toUpperCase()));
                this.sendChatWithPrefix("Set keybind for " + module.getName() + " to " + args[1].toUpperCase());
            }
            catch (Exception e) {
                this.usage();
            }
        }
    }
}
