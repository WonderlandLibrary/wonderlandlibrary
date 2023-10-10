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
import tk.rektsky.module.settings.Setting;

public class SettingCommand
extends Command {
    public SettingCommand() {
        super("setting", "<Module> <Setting Name> [Value (Leave empty to reset)]", "Change setting for a specific Module");
    }

    @Override
    public void onCommand(String label, String[] args) {
        if (args.length != 3 && args.length != 2) {
            HelpCommand.displayCommandInfomation(this);
            return;
        }
        Module module = ModulesManager.getModuleByName(args[0]);
        if (module == null) {
            Client.addClientChat((Object)((Object)ChatFormatting.RED) + "Invalid Module Name!");
            return;
        }
        Setting setting = null;
        for (Setting s2 : module.settings) {
            if (!s2.name.equalsIgnoreCase(args[1])) continue;
            setting = s2;
        }
        if (setting == null) {
            Client.addClientChat((Object)((Object)ChatFormatting.RED) + "Invalid Setting Name!");
            return;
        }
        if (args.length == 2) {
            setting.setValue(setting.getDefaultValue());
            return;
        }
        setting.setValue(args[2]);
    }
}

