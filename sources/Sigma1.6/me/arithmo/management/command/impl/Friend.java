/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.management.command.impl;

import me.arithmo.event.Event;
import me.arithmo.management.command.Command;
import me.arithmo.management.friend.FriendManager;
import me.arithmo.util.misc.ChatUtil;
import net.minecraft.util.EnumChatFormatting;

public class Friend
extends Command {
    public Friend(String[] names, String description) {
        super(names, description);
    }

    @Override
    public void fire(String[] args) {
        if (args == null || args.length < 2) {
            this.printUsage();
            return;
        }
        try {
            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("a")) {
                if (FriendManager.isFriend(args[1])) {
                    ChatUtil.printChat("\u00a7c[\u00a7fS\u00a7c]\u00a77 " + String.valueOf(args[1]) + " is already your friend.");
                    return;
                }
                FriendManager.removeFriend(args[1]);
                FriendManager.addFriend(args[1], args.length == 3 ? args[2] : args[1]);
                ChatUtil.printChat("\u00a7c[\u00a7fS\u00a7c]\u00a77 Added " + args[1]);
                return;
            }
            if (args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("d")) {
                if (FriendManager.isFriend(args[1])) {
                    FriendManager.removeFriend(args[1]);
                    ChatUtil.printChat("\u00a7c[\u00a7fS\u00a7c]\u00a77 Removed friend: " + args[1]);
                    return;
                }
                ChatUtil.printChat("\u00a7c[\u00a7fS\u00a7c]\u00a77 " + String.valueOf(args[1]) + " is not your friend.");
                return;
            }
        }
        catch (NullPointerException e) {
            this.printUsage();
        }
        this.printUsage();
    }

    @Override
    public String getUsage() {
        return "friend <add/remove> " + (Object)((Object)EnumChatFormatting.RESET) + "<name> " + (Object)((Object)EnumChatFormatting.RESET) + "<alias>";
    }

    public void onEvent(Event event) {
    }
}

