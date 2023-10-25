package org.dreamcore.client.cmd.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.cmd.CommandAbstract;
import org.dreamcore.client.helpers.misc.ChatHelper;
import org.dreamcore.client.ui.notification.NotificationManager;
import org.dreamcore.client.ui.notification.NotificationType;

public class FriendCommand extends CommandAbstract {

    public FriendCommand() {
        super("friend", "friend list", "§6.friend" + ChatFormatting.LIGHT_PURPLE + " add " + "§3<nickname> | §6.friend" + ChatFormatting.LIGHT_PURPLE + " del " + "§3<nickname> | §6.friend" + ChatFormatting.LIGHT_PURPLE + " list " + "| §6.friend" + ChatFormatting.LIGHT_PURPLE + " clear", "friend");
    }

    @Override
    public void execute(String... arguments) {
        try {
            if (arguments.length > 1) {
                if (arguments[0].equalsIgnoreCase("friend")) {
                    if (arguments[1].equalsIgnoreCase("add")) {
                        String name = arguments[2];
                        if (name.equals(Minecraft.getInstance().player.getName())) {
                            ChatHelper.addChatMessage(ChatFormatting.RED + "You can't add yourself!");
                            NotificationManager.publicity("Friend Manager", "You can't add yourself!", 4, NotificationType.ERROR);
                            return;
                        }
                        if (!dreamcore.instance.friendManager.isFriend(name)) {
                            dreamcore.instance.friendManager.addFriend(name);
                            ChatHelper.addChatMessage("Friend " + ChatFormatting.GREEN + name + ChatFormatting.WHITE + " successfully added to your friend list!");
                            NotificationManager.publicity("Friend Manager", "Friend " + ChatFormatting.RED + name + ChatFormatting.WHITE + " deleted from your friend list!", 4, NotificationType.SUCCESS);
                        }
                    }
                    if (arguments[1].equalsIgnoreCase("del")) {
                        String name = arguments[2];
                        if (dreamcore.instance.friendManager.isFriend(name)) {
                            dreamcore.instance.friendManager.removeFriend(name);
                            ChatHelper.addChatMessage("Friend " + ChatFormatting.RED + name + ChatFormatting.WHITE + " deleted from your friend list!");
                            NotificationManager.publicity("Friend Manager", "Friend " + ChatFormatting.RED + name + ChatFormatting.WHITE + " deleted from your friend list!", 4, NotificationType.SUCCESS);
                        }
                    }
                    if (arguments[1].equalsIgnoreCase("clear")) {
                        if (dreamcore.instance.friendManager.getFriends().isEmpty()) {
                            ChatHelper.addChatMessage(ChatFormatting.RED + "Your friend list is empty!");
                            NotificationManager.publicity("Friend Manager", "Your friend list is empty!", 4, NotificationType.ERROR);
                            return;
                        }
                        dreamcore.instance.friendManager.getFriends().clear();
                        ChatHelper.addChatMessage("Your " + ChatFormatting.GREEN + "friend list " + ChatFormatting.WHITE + "was cleared!");
                        NotificationManager.publicity("Friend Manager", "Your " + ChatFormatting.GREEN + "friend list " + ChatFormatting.WHITE + "was cleared!", 4, NotificationType.SUCCESS);
                    }
                    if (arguments[1].equalsIgnoreCase("list")) {
                        if (dreamcore.instance.friendManager.getFriends().isEmpty()) {
                            ChatHelper.addChatMessage(ChatFormatting.RED + "Your friend list is empty!");
                            NotificationManager.publicity("Friend Manager", "Your friend list is empty!", 4, NotificationType.ERROR);
                            return;
                        }
                        dreamcore.instance.friendManager.getFriends().forEach(friend -> ChatHelper.addChatMessage(ChatFormatting.GREEN + "Friend list: " + ChatFormatting.RED + friend.getName()));
                    }
                }
            } else {
                ChatHelper.addChatMessage(getUsage());
            }
        } catch (Exception e) {
            ChatHelper.addChatMessage("§cNo, no, no. Usage: " + getUsage());
        }
    }
}
