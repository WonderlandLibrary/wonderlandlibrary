package com.initial.commands.impl;

import com.initial.commands.*;
import com.initial.*;
import com.initial.modules.*;
import org.lwjgl.input.*;
import java.util.*;

public class Bind extends Command
{
    public Bind() {
        super("Bind", "Binds a module", "bind <name> <key> |q clear", new String[] { "b" });
    }
    
    @Override
    public void onCommand(final String[] args, final String command) {
        if (args.length == 2) {
            final String moduleName = args[0];
            final String keyName = args[1];
            boolean found = false;
            for (final Module module : Astomero.instance.moduleManager.getModules()) {
                if (module.getName().equalsIgnoreCase(moduleName)) {
                    module.setKey(Keyboard.getKeyIndex(keyName.toUpperCase()));
                    Astomero.addChatMessage(String.format("Bound %s to %s", module.getName(), Keyboard.getKeyName(module.getKey())));
                    found = true;
                    break;
                }
            }
            if (!found) {
                Astomero.addChatMessage("Could not find the module.");
            }
        }
        if (args.length == 1) {
            boolean found2 = false;
            if (args[0].equalsIgnoreCase("clear")) {
                for (final Module module2 : Astomero.instance.moduleManager.getModules()) {
                    if (!module2.getName().equalsIgnoreCase("clickgui")) {
                        module2.setKey(0);
                    }
                }
                Astomero.addChatMessage("Cleared all your keybinds.");
                found2 = true;
            }
            if (!found2) {
                Astomero.addChatMessage("Could not find the module.");
            }
        }
    }
}
