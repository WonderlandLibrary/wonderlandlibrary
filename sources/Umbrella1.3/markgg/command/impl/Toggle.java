/*
 * Decompiled with CFR 0.150.
 */
package markgg.command.impl;

import markgg.Client;
import markgg.command.Command;
import markgg.modules.Module;

public class Toggle
extends Command {
    public Toggle() {
        super("Toggle", "Toggles a module on or off.", "toggle <name>", "t");
    }

    @Override
    public void onCommand(String[] args, String command) {
        if (args.length > 0) {
            String moduleName = args[0];
            boolean foundModule = false;
            for (Module module : Client.modules) {
                if (!module.name.equalsIgnoreCase(moduleName)) continue;
                module.toggle();
                Client.addChatMessage(String.valueOf(module.isEnabled() ? "Enabled" : "Disabled") + " " + module.name);
                foundModule = true;
                break;
            }
            if (!foundModule) {
                Client.addChatMessage("Could not find module!");
            }
        }
    }
}

