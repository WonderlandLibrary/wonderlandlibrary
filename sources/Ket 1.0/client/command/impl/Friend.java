package client.command.impl;

import client.Client;
import client.command.Command;
import client.util.ChatUtil;

import java.util.List;

public final class Friend extends Command {

    public Friend() {
        super("Adds player to a friends list", "friend", "f");
    }

    @Override
    public void execute(final String[] args) {
        final List<String> friends = Client.INSTANCE.getBotManager().getFriends();
        switch (args.length) {
            case 2: {
                switch (args[1].toLowerCase()) {
                    case "list": {
                        ChatUtil.display("Friend list:");
                        friends.forEach(ChatUtil::displayNoPrefix);
                        ChatUtil.display("Count: %s", friends.size());
                        break;
                    }
                    case "clear": {
                        friends.clear();
                        ChatUtil.display("Friend list has cleared");
                        break;
                    }
                }
                break;
            }
            case 3: {
                switch (args[1].toLowerCase()) {
                    case "add": {
                        if (!friends.contains(args[2])) {
                            friends.add(args[2]);
                            ChatUtil.display("Added %s to friends list", args[2]);
                        }
                        break;
                    }
                    case "remove": {
                        if (friends.contains(args[2])) {
                            friends.remove(args[2]);
                            ChatUtil.display("Removed %s from friends list", args[2]);
                        }
                        break;
                    }
                }
                break;
            }
            default: {
                error(String.format(".%s <list/clear/add/remove> [player]", args[0]));
                break;
            }
        }
    }
}