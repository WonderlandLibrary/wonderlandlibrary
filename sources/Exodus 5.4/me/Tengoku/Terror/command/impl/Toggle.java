/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.command.impl;

import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.command.Command;
import me.Tengoku.Terror.module.Module;

public class Toggle
extends Command {
    @Override
    public void onCommand(String[] stringArray, String string) {
        if (stringArray.length > 0) {
            String string2 = stringArray[0];
            boolean bl = false;
            for (Module module : Exodus.INSTANCE.moduleManager.getModules()) {
                if (!module.getName().equalsIgnoreCase(string2)) continue;
                module.toggle();
                Exodus.addChatMessage(module.isToggled() ? "Enabled " + module.getName() : "Disabled " + module.getName());
                bl = true;
                break;
            }
            if (!bl) {
                Exodus.addChatMessage("Module not found!");
            }
        }
    }

    public Toggle() {
        super("Toggle", "Toggles a module.", "toggle (NAME)", "t");
    }
}

