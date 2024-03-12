// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.commands.impl;

import dev.tenacity.module.settings.impl.KeybindSetting;
import java.util.Iterator;
import dev.tenacity.module.Module;
import dev.tenacity.Tenacity;
import dev.tenacity.commands.Command;

public class ClearBindsCommand extends Command
{
    public ClearBindsCommand() {
        super("clearbinds", "Clears all of your keybinds", ".clearbinds", new String[0]);
    }
    
    @Override
    public void execute(final String[] args) {
        int count = 0;
        for (final Module module : Tenacity.INSTANCE.getModuleCollection().getModules()) {
            final KeybindSetting keybind = module.getKeybind();
            if (keybind.getCode() != 0) {
                keybind.setCode(0);
                ++count;
            }
        }
        this.sendChatWithPrefix("Binds cleared! " + count + " modules affected.");
    }
}
