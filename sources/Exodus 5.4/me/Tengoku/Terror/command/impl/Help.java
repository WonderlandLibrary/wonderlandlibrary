/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.command.impl;

import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.command.Command;
import me.Tengoku.Terror.command.impl.Bind;
import me.Tengoku.Terror.command.impl.ConfigLoad;
import me.Tengoku.Terror.command.impl.ConfigRemove;
import me.Tengoku.Terror.command.impl.ConfigSave;
import me.Tengoku.Terror.command.impl.Say;
import me.Tengoku.Terror.command.impl.StartBot;
import me.Tengoku.Terror.command.impl.Teams;
import me.Tengoku.Terror.command.impl.Toggle;

public class Help
extends Command {
    @Override
    public void onCommand(String[] stringArray, String string) {
        Exodus.addChatMessage("Bind - " + Exodus.commandManager.getCommandByClass(Bind.class).description);
        Exodus.addChatMessage("Help - " + Exodus.commandManager.getCommandByClass(Help.class).description);
        Exodus.addChatMessage("Toggle - " + Exodus.commandManager.getCommandByClass(Toggle.class).description);
        Exodus.addChatMessage("Say - " + Exodus.commandManager.getCommandByClass(Say.class).description);
        Exodus.addChatMessage("Teams  - " + Exodus.commandManager.getCommandByClass(Teams.class).description);
        Exodus.addChatMessage("StartBot - " + Exodus.commandManager.getCommandByClass(StartBot.class).description);
        Exodus.addChatMessage("ConfigSave - " + Exodus.commandManager.getCommandByClass(ConfigSave.class).description);
        Exodus.addChatMessage("ConfigLoad - " + Exodus.commandManager.getCommandByClass(ConfigLoad.class).description);
        Exodus.addChatMessage("ConfigRemove - " + Exodus.commandManager.getCommandByClass(ConfigRemove.class).description);
    }

    public Help() {
        super("Help", "Prints the help message.", "help", "help");
    }
}

