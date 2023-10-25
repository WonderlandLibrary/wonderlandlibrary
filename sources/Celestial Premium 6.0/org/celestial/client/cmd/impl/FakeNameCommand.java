/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.cmd.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.celestial.client.cmd.CommandAbstract;
import org.celestial.client.helpers.misc.ChatHelper;
import org.celestial.client.ui.notification.NotificationManager;
import org.celestial.client.ui.notification.NotificationType;

public class FakeNameCommand
extends CommandAbstract {
    public static String oldName;
    public static String currentName;
    public static boolean canChange;

    public FakeNameCommand() {
        super("fakename", "fakename", "\u00a76.fakename" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " set \u00a73<name> |" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " reset", "\u00a76.fakename" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " set \u00a73<name> |" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " reset", "fakename");
    }

    @Override
    public void execute(String ... arguments) {
        try {
            if (arguments.length >= 2) {
                oldName = FakeNameCommand.mc.player.getName();
                if (arguments[0].equalsIgnoreCase("fakename")) {
                    if (arguments[1].equalsIgnoreCase("set")) {
                        currentName = arguments[2];
                        canChange = true;
                        ChatHelper.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "Successfully" + (Object)((Object)ChatFormatting.WHITE) + " changed your name to " + (Object)((Object)ChatFormatting.RED) + arguments[2]);
                        NotificationManager.publicity("FakeName Manager", (Object)((Object)ChatFormatting.GREEN) + "Successfully" + (Object)((Object)ChatFormatting.WHITE) + " changed your name to " + (Object)((Object)ChatFormatting.RED) + arguments[2], 4, NotificationType.SUCCESS);
                    }
                    if (arguments[1].equalsIgnoreCase("reset")) {
                        currentName = oldName;
                        canChange = false;
                        ChatHelper.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "Successfully" + (Object)((Object)ChatFormatting.WHITE) + " reset your name!");
                        NotificationManager.publicity("FakeName Manager", (Object)((Object)ChatFormatting.GREEN) + "Successfully" + (Object)((Object)ChatFormatting.WHITE) + " reset your name!", 4, NotificationType.SUCCESS);
                    }
                }
            } else {
                ChatHelper.addChatMessage(this.getUsage());
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

