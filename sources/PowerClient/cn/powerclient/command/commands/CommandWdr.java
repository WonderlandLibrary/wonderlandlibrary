/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.command.commands;

import me.AveReborn.command.Command;
import me.AveReborn.ui.ClientNotification;
import me.AveReborn.util.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class CommandWdr
extends Command {
    public CommandWdr(String[] commands) {
        super(commands);
        this.setArgs("-wdr <Playername>");
    }

    @Override
    public void onCmd(String[] args) {
        if (args.length < 2) {
            ClientUtil.sendClientMessage(this.getArgs(), ClientNotification.Type.WARNING);
        } else {
            ClientUtil.sendClientMessage("Reported " + args[1], ClientNotification.Type.SUCCESS);
            Minecraft.getMinecraft();
            Minecraft.thePlayer.sendChatMessage("/wdr " + args[1] + " Fly KillAura AutoClicker Speed AntiKnockBack Reach Dolphin");
        }
    }
}

