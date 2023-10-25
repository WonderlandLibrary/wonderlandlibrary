package ru.smertnix.celestial.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.Minecraft;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.command.CommandAbstract;
import ru.smertnix.celestial.utils.other.ChatUtils;


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
                        if (name.equals(Minecraft.getMinecraft().player.getName())) {
                            ChatUtils.addChatMessage(ChatFormatting.RED + "You can't add yourself!");
                            return;
                        }
                        if (!Celestial.instance.friendManager.isFriend(name)) {
                            Celestial.instance.friendManager.addFriend(name);
                            ChatUtils.addChatMessage("Friend " + ChatFormatting.GREEN + name + ChatFormatting.WHITE + " successfully added to your friend list!");
                        }
                    }
                    if (arguments[1].equalsIgnoreCase("del")) {
                        String name = arguments[2];
                        if (Celestial.instance.friendManager.isFriend(name)) {
                            Celestial.instance.friendManager.removeFriend(name);
                            ChatUtils.addChatMessage("Friend " + ChatFormatting.RED + name + ChatFormatting.WHITE + " deleted from your friend list!");
                        }
                    }
                    if (arguments[1].equalsIgnoreCase("clear")) {
                        if (Celestial.instance.friendManager.getFriends().isEmpty()) {
                            ChatUtils.addChatMessage(ChatFormatting.RED + "Your friend list is empty!");
                            return;
                        }
                        Celestial.instance.friendManager.getFriends().clear();
                        ChatUtils.addChatMessage("Your " + ChatFormatting.GREEN + "friend list " + ChatFormatting.WHITE + "was cleared!");
                    }
                    if (arguments[1].equalsIgnoreCase("list")) {
                        if (Celestial.instance.friendManager.getFriends().isEmpty()) {
                            ChatUtils.addChatMessage(ChatFormatting.RED + "Your friend list is empty!");
                            return;
                        }
                        Celestial.instance.friendManager.getFriends().forEach(friend -> ChatUtils.addChatMessage(ChatFormatting.GREEN + "Friend list: " + ChatFormatting.RED + friend.getName()));
                    }
                }
            } else {
                ChatUtils.addChatMessage("Доступные команды для изпользования:");
            	ChatUtils.addChatMessage("friend add <name> (Добавить в друзья)");
            	ChatUtils.addChatMessage("friend delete <name> (Удалить из друзей)");
            	ChatUtils.addChatMessage("friend list (Список друзей)");
            	ChatUtils.addChatMessage("friend clear (Очистить друзей)");
            }
        } catch (Exception e) {
            ChatUtils.addChatMessage("No, no, no. Usage: " + getUsage());
        }
    }
}
