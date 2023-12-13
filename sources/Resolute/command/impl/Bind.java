// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.command.impl;

import java.util.Iterator;
import org.lwjgl.input.Keyboard;
import vip.Resolute.modules.Module;
import vip.Resolute.Resolute;
import vip.Resolute.command.Command;

public class Bind extends Command
{
    public Bind() {
        super("Bind", "Binds a module by name.", "bind <name> <key> | clear", new String[] { "b" });
    }
    
    @Override
    public void onCommand(final String[] args, final String command) {
        if (args.length == 2) {
            final String moduleName = args[0];
            final String keyName = args[1];
            boolean foundModule = false;
            for (final Module module : Resolute.modules) {
                if (module.name.equalsIgnoreCase(moduleName)) {
                    module.keyBind.setCode(Keyboard.getKeyIndex(keyName.toUpperCase()));
                    Resolute.addChatMessage(String.format("Bound %s to %s", module.name, Keyboard.getKeyName(module.getKey())));
                    foundModule = true;
                    break;
                }
            }
            if (!foundModule) {
                Resolute.addChatMessage("Could not find given module");
            }
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("clear")) {
                for (final Module module2 : Resolute.modules) {
                    module2.keyBind.setCode(0);
                }
            }
            Resolute.addChatMessage("Cleared all keybinds");
        }
    }
}
