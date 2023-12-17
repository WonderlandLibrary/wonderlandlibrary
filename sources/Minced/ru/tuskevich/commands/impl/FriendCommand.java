// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.commands.impl;

import ru.tuskevich.managers.Friend;
import com.mojang.realmsclient.gui.ChatFormatting;
import ru.tuskevich.Minced;
import net.minecraft.client.Minecraft;
import ru.tuskevich.commands.Command;
import ru.tuskevich.commands.CommandAbstract;

@Command(name = "friend", description = "Allows you to add friends")
public class FriendCommand extends CommandAbstract
{
    @Override
    public void execute(final String[] args) throws Exception {
        final String s = args[1];
        switch (s) {
            case "add": {
                if (args[2].equalsIgnoreCase(Minecraft.getMinecraft().session.getUsername())) {
                    this.sendMessage("Player not founded");
                    break;
                }
                Minced.getInstance().friendManager.addFriend(args[2]);
                this.sendMessage("Player " + ChatFormatting.BLUE + "successfully" + ChatFormatting.WHITE + " added to friend");
                break;
            }
            case "remove": {
                if (Minced.getInstance().friendManager.isFriend(args[2])) {
                    Minced.getInstance().friendManager.removeFriend(args[2]);
                    this.sendMessage("Module " + ChatFormatting.BLUE + "successfully" + ChatFormatting.WHITE + " removed from friend");
                    break;
                }
                this.sendMessage("Friend not founded");
                break;
            }
            case "clear": {
                if (Minced.getInstance().friendManager.getFriends().isEmpty()) {
                    this.sendMessage("Friends not founded");
                    break;
                }
                Minced.getInstance().friendManager.clearFriend();
                this.sendMessage("Friends " + ChatFormatting.BLUE + "successfully" + ChatFormatting.WHITE + " cleared");
                break;
            }
            case "list": {
                if (Minced.getInstance().friendManager.getFriends().isEmpty()) {
                    this.sendMessage("Friends not founded");
                    return;
                }
                this.sendMessage(ChatFormatting.GRAY + "Friend list: ");
                Minced.getInstance().friendManager.getFriends().forEach(friend -> this.sendMessage(friend.getName()));
                break;
            }
        }
    }
    
    @Override
    public void error() {
        this.sendMessage(ChatFormatting.GRAY + "command use" + ChatFormatting.WHITE + ":");
        this.sendMessage(ChatFormatting.WHITE + ".friend add " + ChatFormatting.BLUE + "<name>");
        this.sendMessage(ChatFormatting.WHITE + ".friend remove " + ChatFormatting.BLUE + "<name>");
        this.sendMessage(ChatFormatting.WHITE + ".friend list");
        this.sendMessage(ChatFormatting.WHITE + ".friend clear");
    }
}
