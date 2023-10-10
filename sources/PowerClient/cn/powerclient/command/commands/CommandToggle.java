/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.command.commands;

import me.AveReborn.command.Command;
import me.AveReborn.mod.Mod;
import me.AveReborn.mod.ModManager;
import me.AveReborn.ui.ClientNotification;
import me.AveReborn.util.ClientUtil;

public class CommandToggle
extends Command {
    public CommandToggle(String[] commands) {
        super(commands);
        this.setArgs("-toggle <module>");
    }

    @Override
    public void onCmd(String[] args) {
        if (args.length < 2) {
            ClientUtil.sendClientMessage(this.getArgs(), ClientNotification.Type.WARNING);
        } else {
            String mod = args[1];
            for (Mod m2 : ModManager.getModList()) {
                if (!m2.getName().equalsIgnoreCase(mod)) continue;
                m2.set(m2.isEnabled());
                ClientUtil.sendClientMessage("Module " + m2.getName() + " toggled", ClientNotification.Type.SUCCESS);
                return;
            }
        }
    }
}

