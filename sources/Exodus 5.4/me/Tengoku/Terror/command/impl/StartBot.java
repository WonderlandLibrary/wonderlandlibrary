/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.command.impl;

import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.command.Command;
import me.Tengoku.Terror.module.skyblock.AutoFarm;

public class StartBot
extends Command {
    public StartBot() {
        super("StartBot", "Start the auto farm bot.", "StartBot", "startbot");
    }

    @Override
    public void onCommand(String[] stringArray, String string) {
        if (Exodus.INSTANCE.moduleManager.getModuleByClass(AutoFarm.class).isToggled()) {
            Exodus.INSTANCE.moduleManager.getModuleByClass(AutoFarm.class).toggle();
            Exodus.addChatMessage("\ufffdcBot stopped!");
        } else {
            Exodus.INSTANCE.moduleManager.getModuleByClass(AutoFarm.class).toggle();
            Exodus.addChatMessage("\ufffdaBot started!");
        }
    }
}

