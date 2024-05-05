// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.command.impl;

import java.util.Iterator;
import vip.Resolute.modules.Module;
import vip.Resolute.Resolute;
import vip.Resolute.command.Command;

public class Toggle extends Command
{
    public Toggle() {
        super("Toggle", "Toggles a module by name.", "toggle <name>", new String[] { "t" });
    }
    
    @Override
    public void onCommand(final String[] args, final String command) {
        if (args.length > 0) {
            final String moduleName = args[0];
            boolean foundModule = false;
            for (final Module module : Resolute.modules) {
                if (module.name.equalsIgnoreCase(moduleName)) {
                    module.toggle();
                    Resolute.addChatMessage((module.isEnabled() ? "Enabled" : "Disabled") + " " + module.name);
                    foundModule = true;
                    break;
                }
            }
            if (!foundModule) {
                Resolute.addChatMessage("Could not locate given module");
            }
        }
    }
}
