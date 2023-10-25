/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.cmd.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import org.celestial.client.Celestial;
import org.celestial.client.cmd.CommandAbstract;
import org.celestial.client.helpers.misc.ChatHelper;
import org.celestial.client.ui.notification.NotificationManager;
import org.celestial.client.ui.notification.NotificationType;

public class FriendCommand
extends CommandAbstract {
    public FriendCommand() {
        super("friend", "friend list", "\u00a76.friend" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " add \u00a73<nickname> | \u00a76.friend" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " del \u00a73<nickname> | \u00a76.friend" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " list | \u00a76.friend" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " clear", "friend");
    }

    @Override
    public void execute(String ... arguments) {
        try {
            if (arguments.length > 1) {
                if (arguments[0].equalsIgnoreCase("friend")) {
                    String name;
                    if (arguments[1].equalsIgnoreCase("add")) {
                        name = arguments[2];
                        if (name.equals(Minecraft.getMinecraft().player.getName())) {
                            ChatHelper.addChatMessage((Object)((Object)ChatFormatting.RED) + "You can't add yourself!");
                            NotificationManager.publicity("Friend Manager", "You can't add yourself!", 4, NotificationType.ERROR);
                            return;
                        }
                        if (!Celestial.instance.friendManager.isFriend(name)) {
                            Celestial.instance.friendManager.addFriend(name);
                            ChatHelper.addChatMessage("Friend " + (Object)((Object)ChatFormatting.GREEN) + name + (Object)((Object)ChatFormatting.WHITE) + " successfully added to your friend list!");
                            NotificationManager.publicity("Friend Manager", "Friend " + (Object)((Object)ChatFormatting.RED) + name + (Object)((Object)ChatFormatting.WHITE) + " successfully added to your friend list!", 4, NotificationType.SUCCESS);
                        }
                    }
                    if (arguments[1].equalsIgnoreCase("del") && Celestial.instance.friendManager.isFriend(name = arguments[2])) {
                        Celestial.instance.friendManager.removeFriend(name);
                        ChatHelper.addChatMessage("Friend " + (Object)((Object)ChatFormatting.RED) + name + (Object)((Object)ChatFormatting.WHITE) + " deleted from your friend list!");
                        NotificationManager.publicity("Friend Manager", "Friend " + (Object)((Object)ChatFormatting.RED) + name + (Object)((Object)ChatFormatting.WHITE) + " deleted from your friend list!", 4, NotificationType.SUCCESS);
                    }
                    if (arguments[1].equalsIgnoreCase("clear")) {
                        if (Celestial.instance.friendManager.getFriends().isEmpty()) {
                            ChatHelper.addChatMessage((Object)((Object)ChatFormatting.RED) + "Your friend list is empty!");
                            NotificationManager.publicity("Friend Manager", "Your friend list is empty!", 4, NotificationType.ERROR);
                            return;
                        }
                        Celestial.instance.friendManager.getFriends().clear();
                        ChatHelper.addChatMessage("Your " + (Object)((Object)ChatFormatting.GREEN) + "friend list " + (Object)((Object)ChatFormatting.WHITE) + "was cleared!");
                        NotificationManager.publicity("Friend Manager", "Your " + (Object)((Object)ChatFormatting.GREEN) + "friend list " + (Object)((Object)ChatFormatting.WHITE) + "was cleared!", 4, NotificationType.SUCCESS);
                    }
                    if (arguments[1].equalsIgnoreCase("list")) {
                        if (Celestial.instance.friendManager.getFriends().isEmpty()) {
                            ChatHelper.addChatMessage((Object)((Object)ChatFormatting.RED) + "Your friend list is empty!");
                            NotificationManager.publicity("Friend Manager", "Your friend list is empty!", 4, NotificationType.ERROR);
                            return;
                        }
                        Celestial.instance.friendManager.getFriends().forEach(friend -> ChatHelper.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "Friend list: " + (Object)((Object)ChatFormatting.RED) + friend.getName()));
                    }
                }
            } else {
                ChatHelper.addChatMessage(this.getUsage());
            }
        }
        catch (Exception e) {
            ChatHelper.addChatMessage("\u00a7cNo, no, no. Usage: " + this.getUsage());
        }
    }
}

