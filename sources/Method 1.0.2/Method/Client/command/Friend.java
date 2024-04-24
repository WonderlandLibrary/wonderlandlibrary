package Method.Client.command;

import Method.Client.managers.FriendManager;
import Method.Client.utils.visual.ChatUtils;

@Command.CommandFeatures(description = "Friend manager.", Syntax = "friend <add/remove/list/clear> <nick>")

public class Friend extends Command {

    @Override
    public void runCommand(String s, String[] args) {
        if (args[0].equalsIgnoreCase("add")) {
            FriendManager.addFriend(args[1]);
        }
        if (args[0].equalsIgnoreCase("list")) {
            ChatUtils.message(FriendManager.friendsList.toString());
        } else if (args[0].equalsIgnoreCase("remove")) {
            FriendManager.removeFriend(args[1]);
        } else if (args[0].equalsIgnoreCase("clear")) {
            FriendManager.clear();
        }

    }
}