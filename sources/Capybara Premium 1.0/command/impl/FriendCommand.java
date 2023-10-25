package fun.expensive.client.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import fun.rich.client.Rich;
import fun.rich.client.command.CommandAbstract;
import fun.rich.client.utils.other.ChatUtils;
import net.minecraft.client.Minecraft;


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
                        if (!Rich.instance.friendManager.isFriend(name)) {
                            Rich.instance.friendManager.addFriend(name);
                            ChatUtils.addChatMessage("Friend " + ChatFormatting.GREEN + name + ChatFormatting.WHITE + " successfully added to your friend list!");
                        }
                    }
                    if (arguments[1].equalsIgnoreCase("del")) {
                        String name = arguments[2];
                        if (Rich.instance.friendManager.isFriend(name)) {
                            Rich.instance.friendManager.removeFriend(name);
                            ChatUtils.addChatMessage("Friend " + ChatFormatting.RED + name + ChatFormatting.WHITE + " deleted from your friend list!");
                        }
                    }
                    if (arguments[1].equalsIgnoreCase("clear")) {
                        if (Rich.instance.friendManager.getFriends().isEmpty()) {
                            ChatUtils.addChatMessage(ChatFormatting.RED + "Your friend list is empty!");
                            return;
                        }
                        Rich.instance.friendManager.getFriends().clear();
                        ChatUtils.addChatMessage("Your " + ChatFormatting.GREEN + "friend list " + ChatFormatting.WHITE + "was cleared!");
                    }
                    if (arguments[1].equalsIgnoreCase("list")) {
                        if (Rich.instance.friendManager.getFriends().isEmpty()) {
                            ChatUtils.addChatMessage(ChatFormatting.RED + "Your friend list is empty!");
                            return;
                        }
                        Rich.instance.friendManager.getFriends().forEach(friend -> ChatUtils.addChatMessage(ChatFormatting.GREEN + "Friend list: " + ChatFormatting.RED + friend.getName()));
                    }
                }
            } else {
                ChatUtils.addChatMessage(getUsage());
            }
        } catch (Exception e) {
            ChatUtils.addChatMessage("§cNo, no, no. Usage: " + getUsage());
        }
    }
}
