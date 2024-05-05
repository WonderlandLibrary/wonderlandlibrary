// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.cmd.impl;

import ru.fluger.client.friend.Friend;
import ru.fluger.client.Fluger;
import ru.fluger.client.helpers.misc.ChatHelper;
import com.mojang.realmsclient.gui.ChatFormatting;
import ru.fluger.client.cmd.CommandAbstract;

public class FriendCommand extends CommandAbstract
{
    public FriendCommand() {
        super("friend", "friend list", "§6.friend" + ChatFormatting.WHITE + " add §3<nickname>" + ChatFormatting.WHITE + " | §6.friend" + ChatFormatting.WHITE + " del §3<nickname>" + ChatFormatting.WHITE + " | §6.friend" + ChatFormatting.WHITE + " list " + ChatFormatting.WHITE + "| §6.friend" + ChatFormatting.WHITE + " clear", new String[] { "friend" });
    }
    
    @Override
    public void execute(final String... arguments) {
        try {
            if (arguments.length > 1) {
                if (arguments[0].equalsIgnoreCase("friend")) {
                    if (arguments[1].equalsIgnoreCase("add")) {
                        final String name = arguments[2];
                        if (name.equals(bib.z().h.h_())) {
                            ChatHelper.addChatMessage(ChatFormatting.RED + "You can't add yourself!");
                            return;
                        }
                        if (!Fluger.instance.friendManager.isFriend(name)) {
                            Fluger.instance.friendManager.addFriend(name);
                            ChatHelper.addChatMessage("Friend " + ChatFormatting.GREEN + name + ChatFormatting.WHITE + " successfully added to your friend list!");
                        }
                    }
                    if (arguments[1].equalsIgnoreCase("del")) {
                        final String name = arguments[2];
                        if (Fluger.instance.friendManager.isFriend(name)) {
                            Fluger.instance.friendManager.removeFriend(name);
                            ChatHelper.addChatMessage("Friend " + ChatFormatting.RED + name + ChatFormatting.WHITE + " deleted from your friend list!");
                        }
                    }
                    if (arguments[1].equalsIgnoreCase("clear")) {
                        if (Fluger.instance.friendManager.getFriends().isEmpty()) {
                            ChatHelper.addChatMessage(ChatFormatting.RED + "Your friend list is empty!");
                            return;
                        }
                        Fluger.instance.friendManager.getFriends().clear();
                        ChatHelper.addChatMessage("Your " + ChatFormatting.GREEN + "friend list " + ChatFormatting.WHITE + "was cleared!");
                    }
                    if (arguments[1].equalsIgnoreCase("list")) {
                        if (Fluger.instance.friendManager.getFriends().isEmpty()) {
                            ChatHelper.addChatMessage(ChatFormatting.RED + "Your friend list is empty!");
                            return;
                        }
                        Fluger.instance.friendManager.getFriends().forEach(friend -> ChatHelper.addChatMessage(ChatFormatting.GREEN + "Friend list: " + ChatFormatting.RED + friend.getName()));
                    }
                }
            }
            else {
                ChatHelper.addChatMessage(this.getUsage());
            }
        }
        catch (Exception e) {
            ChatHelper.addChatMessage("§cNo, no, no. Usage: " + this.getUsage());
        }
    }
}
