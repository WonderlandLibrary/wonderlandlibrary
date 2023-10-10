/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.command.commands;

import me.AveReborn.Client;
import me.AveReborn.command.Command;
import me.AveReborn.ui.ClientNotification;
import me.AveReborn.util.ClientUtil;

public class CommandVersion
extends Command {
    public CommandVersion(String[] commands) {
        super(commands);
    }

    @Override
    public void onCmd(String[] args) {
        ClientUtil.sendClientMessage(String.valueOf(Client.CLIENT_NAME) + " v" + Client.CLIENT_VER + " by TenmaGabrielWhite <3", ClientNotification.Type.INFO);
    }
}

