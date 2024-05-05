/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.command.commands;

import java.util.ArrayList;
import me.AveReborn.Client;
import me.AveReborn.command.Command;
import me.AveReborn.mod.Mod;
import me.AveReborn.mod.ModManager;
import me.AveReborn.ui.ClientNotification;
import me.AveReborn.util.ClientUtil;
import me.AveReborn.util.FileUtil;
import org.lwjgl.input.Keyboard;

public class CommandBind
extends Command {
    public CommandBind(String[] command) {
        super(command);
        this.setArgs("-bind <mod> <key>");
    }

    @Override
    public void onCmd(String[] args) {
        if (args.length < 3) {
            ClientUtil.sendClientMessage(this.getArgs(), ClientNotification.Type.WARNING);
        } else {
            String mod = args[1];
            int key = Keyboard.getKeyIndex(args[2].toUpperCase());
            for (Mod m2 : ModManager.modList) {
                if (!m2.getName().equalsIgnoreCase(mod)) continue;
                m2.setKey(key);
                ClientUtil.sendClientMessage(String.valueOf(String.valueOf(m2.getName())) + " was bound to " + Keyboard.getKeyName(key), Keyboard.getKeyName(key).equals("NONE") ? ClientNotification.Type.ERROR : ClientNotification.Type.SUCCESS);
                Client.instance.fileMgr.saveKeys();
                return;
            }
            ClientUtil.sendClientMessage("Cannot find Module : " + mod, ClientNotification.Type.ERROR);
        }
    }
}

