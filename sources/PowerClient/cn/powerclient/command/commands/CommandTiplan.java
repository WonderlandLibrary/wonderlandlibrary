/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.command.commands;

import me.AveReborn.Tiplan;
import me.AveReborn.command.Command;
import me.AveReborn.util.ChatType;
import me.AveReborn.util.ClientUtil;

public class CommandTiplan
extends Command {
    public CommandTiplan(String[] commands) {
        super(commands);
    }

    @Override
    public void onCmd(String[] args) {
        ClientUtil.sendChatMessage("Changing tiplan...", ChatType.INFO);
        new me.AveReborn.Tiplan();
    }
}

