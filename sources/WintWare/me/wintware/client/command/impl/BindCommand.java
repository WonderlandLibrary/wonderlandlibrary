/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.wintware.client.command.impl;

import me.wintware.client.Main;
import me.wintware.client.command.AbstractCommand;
import me.wintware.client.module.Module;
import me.wintware.client.utils.other.ChatUtils;
import org.lwjgl.input.Keyboard;

public final class BindCommand
extends AbstractCommand {
    public BindCommand() {
        super("Bind", "Set and delete key binds.", "bind module key", "bind", "b");
    }

    @Override
    public void execute(String ... arguments) {
        if (arguments.length == 3) {
            String moduleName = arguments[1];
            Module module = Main.instance.moduleManager.getModuleByName(moduleName);
            String bind = arguments[2].toUpperCase();
            if (module != null) {
                module.setKey(Keyboard.getKeyIndex(bind));
                ChatUtils.addChatMessage("" + module.getName() + " bind: " + bind + ".");
            } else if (!moduleName.equals("*")) {
                ChatUtils.addChatMessage("");
            }
        } else {
            this.usage();
        }
    }
}

