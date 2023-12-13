/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.commands.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import tk.rektsky.Client;
import tk.rektsky.commands.Command;
import tk.rektsky.commands.impl.HelpCommand;
import tk.rektsky.module.Module;
import tk.rektsky.module.ModulesManager;

public class ToggleCommand
extends Command {
    public ToggleCommand() {
        super("toggle", new String[]{"t"}, "<Module Name>", "Toggles Module with command");
    }

    @Override
    public void onCommand(String label, String[] args) {
        if (args.length != 1) {
            HelpCommand.displayCommandInfomation(this);
            return;
        }
        for (Module module : ModulesManager.getModules()) {
            if (!module.name.equalsIgnoreCase(args[0])) continue;
            module.toggle();
            if (module.isToggled()) {
                Client.addClientChat((Object)((Object)ChatFormatting.GREEN) + "Enabled Module: " + module.name);
            } else {
                Client.addClientChat((Object)((Object)ChatFormatting.RED) + "Disabled Module: " + module.name);
            }
            return;
        }
        Client.addClientChat((Object)((Object)ChatFormatting.RED) + "Module Not Found!");
    }
}

