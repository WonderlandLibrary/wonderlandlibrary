/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.commands.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import tk.rektsky.Client;
import tk.rektsky.commands.Command;
import tk.rektsky.utils.file.FileManager;

public class SaveCommand
extends Command {
    public SaveCommand() {
        super("save", "", "Save settings file (Auto save by default but won't save if game crashed)");
    }

    @Override
    public void onCommand(String label, String[] args) {
        FileManager.replaceAndSaveSettings();
        Client.addClientChat((Object)((Object)ChatFormatting.GREEN) + "Setting file saved!");
    }
}

