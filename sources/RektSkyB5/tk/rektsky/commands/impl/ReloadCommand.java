/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.commands.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import tk.rektsky.Client;
import tk.rektsky.commands.Command;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.utils.YamlUtil;

public class ReloadCommand
extends Command {
    public ReloadCommand() {
        super("reload", "", "Reloads the client");
    }

    @Override
    public void onCommand(String label, String[] args) {
        Client.addClientChat((Object)((Object)ChatFormatting.YELLOW) + "Reloading...");
        new Thread("reload"){

            @Override
            public void run() {
                for (YamlUtil.ConfiguredModule module : YamlUtil.getModuleSetting()) {
                    ModulesManager.loadModuleSetting(module);
                }
                Client.addClientChat((Object)((Object)ChatFormatting.GREEN) + "Reload Completed!");
            }
        }.start();
    }
}

