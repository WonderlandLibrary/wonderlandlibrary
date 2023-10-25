/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.cmd.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.celestial.client.cmd.CommandAbstract;
import org.celestial.client.feature.impl.misc.FakeHack;
import org.celestial.client.helpers.misc.ChatHelper;
import org.celestial.client.ui.notification.NotificationManager;
import org.celestial.client.ui.notification.NotificationType;

public class FakeHackCommand
extends CommandAbstract {
    public FakeHackCommand() {
        super("fakehack", "fakehack", "\u00a76.fakehack" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " add | del | clear\u00a73<name>", "\u00a76.fakehack" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " add | del | clear \u00a73<name>", "fakehack");
    }

    @Override
    public void execute(String ... arguments) {
        try {
            if (arguments.length > 1) {
                if (arguments[0].equals("fakehack")) {
                    if (arguments[1].equals("add")) {
                        FakeHack.fakeHackers.add(arguments[2]);
                        ChatHelper.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "Added player " + (Object)((Object)ChatFormatting.RED) + arguments[2] + (Object)((Object)ChatFormatting.WHITE) + " as HACKER!");
                        NotificationManager.publicity("FakeHack Manager", (Object)((Object)ChatFormatting.GREEN) + "Added player " + (Object)((Object)ChatFormatting.RED) + arguments[2] + (Object)((Object)ChatFormatting.WHITE) + " as HACKER!", 4, NotificationType.SUCCESS);
                    }
                    if (arguments[1].equals("del")) {
                        EntityPlayer player = Minecraft.getMinecraft().world.getPlayerEntityByName(arguments[2]);
                        if (player == null) {
                            ChatHelper.addChatMessage("\u00a7cThat player could not be found!");
                            return;
                        }
                        if (FakeHack.isFakeHacker(player)) {
                            FakeHack.removeHacker(player);
                            ChatHelper.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "Hacker " + (Object)((Object)ChatFormatting.RED) + player.getName() + " " + (Object)((Object)ChatFormatting.WHITE) + "was removed!");
                            NotificationManager.publicity("FakeHack Manager", (Object)((Object)ChatFormatting.GREEN) + "Hacker " + (Object)((Object)ChatFormatting.WHITE) + "was removed!", 4, NotificationType.SUCCESS);
                        }
                    }
                    if (arguments[1].equals("clear")) {
                        if (FakeHack.fakeHackers.isEmpty()) {
                            ChatHelper.addChatMessage((Object)((Object)ChatFormatting.RED) + "Your FakeHack list is empty!");
                            NotificationManager.publicity("FakeHack Manager", "Your FakeHack list is empty!", 4, NotificationType.ERROR);
                            return;
                        }
                        FakeHack.fakeHackers.clear();
                        ChatHelper.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "Your FakeHack list " + (Object)((Object)ChatFormatting.WHITE) + " successfully cleared!");
                        NotificationManager.publicity("FakeHack Manager", (Object)((Object)ChatFormatting.GREEN) + "Your FakeHack list " + (Object)((Object)ChatFormatting.WHITE) + " successfully cleared!", 4, NotificationType.SUCCESS);
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

