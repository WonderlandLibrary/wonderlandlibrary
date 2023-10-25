/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.Tengoku.Terror.command.impl;

import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.command.Command;
import me.Tengoku.Terror.module.Module;
import org.lwjgl.input.Keyboard;

public class Bind
extends Command {
    public Bind() {
        super("Bind", "Binds a module.", "bind (NAME) (KEY)", "bind");
    }

    @Override
    public void onCommand(String[] stringArray, String string) {
        if (stringArray.length < 2) {
            Exodus.addChatMessage("Syntax error!");
        } else {
            Module module = Exodus.INSTANCE.moduleManager.getModuleByName(stringArray[0]);
            if (module != null) {
                int n = Keyboard.getKeyIndex((String)stringArray[1].toUpperCase());
                if (n != -1) {
                    module.setKey(n);
                    Exodus.addChatMessage(String.valueOf(module.getName()) + " has been set to: " + Keyboard.getKeyName((int)module.getKey()));
                } else {
                    Exodus.addChatMessage("Key not found!");
                }
            } else {
                Exodus.addChatMessage("Module not found! (Don't include spaces or '.')");
            }
        }
    }
}

