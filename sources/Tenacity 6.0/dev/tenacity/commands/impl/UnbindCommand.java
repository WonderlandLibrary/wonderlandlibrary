// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.commands.impl;

import dev.tenacity.module.Module;
import dev.tenacity.Tenacity;
import dev.tenacity.commands.Command;

public final class UnbindCommand extends Command
{
    public UnbindCommand() {
        super("unbind", "unbinds a module", ".unbind [module]", new String[0]);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length != 1) {
            this.usage();
        }
        else {
            final String stringMod = args[0];
            try {
                final Module module = Tenacity.INSTANCE.getModuleCollection().getModuleByName(stringMod);
                module.getKeybind().setCode(0);
                this.sendChatWithPrefix("Set keybind for " + module.getName() + " to NONE");
            }
            catch (Exception e) {
                this.usage();
            }
        }
    }
}
