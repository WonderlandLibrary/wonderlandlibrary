/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.cmd.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.celestial.client.cmd.CommandAbstract;
import org.celestial.client.helpers.misc.ChatHelper;

public class HelpCommand
extends CommandAbstract {
    public HelpCommand() {
        super("help", "help", ".help", "help");
    }

    @Override
    public void execute(String ... args) {
        if (args.length == 1) {
            if (args[0].equals("help")) {
                ChatHelper.addChatMessage((Object)((Object)ChatFormatting.RED) + "All Commands:");
                ChatHelper.addChatMessage((Object)((Object)ChatFormatting.LIGHT_PURPLE) + ".bind");
                ChatHelper.addChatMessage((Object)((Object)ChatFormatting.LIGHT_PURPLE) + ".macro");
                ChatHelper.addChatMessage((Object)((Object)ChatFormatting.LIGHT_PURPLE) + ".vclip | .hclip");
                ChatHelper.addChatMessage((Object)((Object)ChatFormatting.LIGHT_PURPLE) + ".tp");
                ChatHelper.addChatMessage((Object)((Object)ChatFormatting.LIGHT_PURPLE) + ".fakehack");
                ChatHelper.addChatMessage((Object)((Object)ChatFormatting.LIGHT_PURPLE) + ".fakename");
                ChatHelper.addChatMessage((Object)((Object)ChatFormatting.LIGHT_PURPLE) + ".friend");
                ChatHelper.addChatMessage((Object)((Object)ChatFormatting.LIGHT_PURPLE) + ".config");
                ChatHelper.addChatMessage((Object)((Object)ChatFormatting.LIGHT_PURPLE) + ".clip");
                ChatHelper.addChatMessage((Object)((Object)ChatFormatting.LIGHT_PURPLE) + ".xray");
                ChatHelper.addChatMessage((Object)((Object)ChatFormatting.LIGHT_PURPLE) + ".res");
                ChatHelper.addChatMessage((Object)((Object)ChatFormatting.LIGHT_PURPLE) + ".set");
            }
        } else {
            ChatHelper.addChatMessage(this.getUsage());
        }
    }
}

