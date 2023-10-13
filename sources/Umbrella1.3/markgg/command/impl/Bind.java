/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package markgg.command.impl;

import markgg.Client;
import markgg.command.Command;
import markgg.modules.Module;
import org.lwjgl.input.Keyboard;

public class Bind
extends Command {
    public Bind() {
        super("Bind", "Binds a module to a key.", "bind <name> <key> | clear", "b");
    }

    @Override
    public void onCommand(String[] args, String command) {
        if (args.length == 2) {
            String moduleName = args[0];
            String keyName = args[1];
            boolean foundModule = false;
            for (Module module : Client.modules) {
                if (!module.name.equalsIgnoreCase(moduleName)) continue;
                module.keyCode.setKeyCode(Keyboard.getKeyIndex((String)keyName.toUpperCase()));
                Client.addChatMessage(String.format("Bound %s to %s", module.name, Keyboard.getKeyName((int)module.getKey())));
                foundModule = true;
                break;
            }
            if (!foundModule) {
                Client.addChatMessage("Could not find module!");
            }
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("clear")) {
                for (Module module : Client.modules) {
                    module.keyCode.setKeyCode(0);
                }
            }
            Client.addChatMessage("Cleared all binds!");
        }
    }
}

