// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.commands.impl;

import dev.tenacity.module.Module;
import dev.tenacity.Tenacity;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Arrays;
import dev.tenacity.commands.Command;

public class ToggleCommand extends Command
{
    public ToggleCommand() {
        super("toggle", "Toggles a module", ".t [module]", new String[] { "t" });
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            this.usage();
        }
        else {
            final String moduleName = Arrays.stream(args).skip(0L).collect(Collectors.joining(" "));
            final Module module = Tenacity.INSTANCE.getModuleCollection().getModuleByName(moduleName);
            if (module != null) {
                module.toggle();
                this.sendChatWithPrefix("Toggled " + module.getName() + "!");
            }
            else {
                this.sendChatWithPrefix("The module \"" + moduleName + "\" does not exist!");
            }
        }
    }
}
