// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.command.commands;

import com.elementars.eclient.friend.Friends;
import com.elementars.eclient.friend.Friend;
import com.elementars.eclient.command.Command;

public class FriendCommand extends Command
{
    public FriendCommand() {
        super("friend", "adds or deletes friends", new String[] { "add", "del" });
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length < 2) {
            Command.sendChatMessage("Try .friend add or .friend del");
            return;
        }
        if (args[1].equalsIgnoreCase("help")) {
            this.showSyntax(args[0]);
        }
        if (args.length < 3) {
            Command.sendChatMessage("Specify a username");
            return;
        }
        final Friend friend = new Friend(args[2]);
        if (args[1].equalsIgnoreCase("add")) {
            if (!Friends.getFriends().contains((Object)friend)) {
                Friends.addFriend(friend.getUsername());
                Command.sendChatMessage(friend.getUsername() + " has been friended");
            }
            else {
                Command.sendChatMessage(friend.getUsername() + " is already friended!");
            }
        }
        else if (args[1].equalsIgnoreCase("del")) {
            if (Friends.getFriendByName(friend.getUsername()) != null) {
                Friends.delFriend(friend.getUsername());
                Command.sendChatMessage(friend.getUsername() + " has been unfriended");
            }
            else {
                Command.sendChatMessage(friend.getUsername() + " isn't a friend");
            }
        }
        else {
            Command.sendChatMessage("Unknown attribute '" + args[1] + "'");
        }
    }
}
