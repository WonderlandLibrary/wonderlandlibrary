package me.nyan.flush.command.impl;

import me.nyan.flush.Flush;
import me.nyan.flush.command.Command;
import me.nyan.flush.utils.other.ChatUtils;

public class Friend extends Command {
    public Friend() {
        super("Friend", "Whitelists a player to the aura.", "friend add <playerName> | friend remove <playerName> | friend list | friend clear", "f");
    }

    @Override
    public void onCommand(String[] args, String message) {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "add":
                    if (args.length == 2) {
                        String playername = args[1];
                        if (flush.getFriendManager().addFriend(playername)) {
                            flush.getFriendManager().save();
                            ChatUtils.println("\"" + playername + "\" is now your friend.");
                            break;
                        }

                        ChatUtils.println("\"" + playername + "\" is already your friend!");
                        break;
                    }

                    sendSyntaxHelpMessage();
                    break;

                case "remove":
                    if (args.length == 2) {
                        String playername = args[1];
                        if (flush.getFriendManager().removeFriend(playername)) {
                            flush.getFriendManager().save();
                            ChatUtils.println("\"" + playername + "\" is no longer your friend.");
                            break;
                        }

                        ChatUtils.println("\"" + playername + "\" is not a friend.");
                        break;
                    }

                    sendSyntaxHelpMessage();
                    break;

                case "list":
                    flush.getFriendManager().load();
                    if (flush.getFriendManager().getFriends().size() != 0) {
                        ChatUtils.println("§9Current friends:");
                        flush.getFriendManager().getFriends().forEach(ChatUtils::println);
                        break;
                    }

                    ChatUtils.println("§9You currently don't have any friends.");
                    break;

                case "clear":
                    flush.getFriendManager().getFriends().clear();
                    flush.getFriendManager().save();
                    ChatUtils.println("§9Cleared all friends.");
                    break;

                default:
                    sendSyntaxHelpMessage();
                    break;
            }
            return;
        }

        sendSyntaxHelpMessage();
    }
}