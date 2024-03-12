// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.commands.impl;

import dev.tenacity.module.Module;
import dev.tenacity.Tenacity;
import dev.tenacity.commands.Command;

public class ClearConfigCommand extends Command
{
    public ClearConfigCommand() {
        super("clearconfig", "Turns off all enabled modules", ".clearconfig", new String[0]);
    }
    
    @Override
    public void execute(final String[] args) {
        Tenacity.INSTANCE.getModuleCollection().getModules().stream().filter(Module::isEnabled).forEach(Module::toggle);
    }
}
