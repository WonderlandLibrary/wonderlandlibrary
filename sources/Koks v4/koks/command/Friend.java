package koks.command;

import de.liquiddev.ircclient.client.IrcPlayer;
import koks.api.registry.command.Command;
import koks.api.manager.friend.FriendManager;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Command.Info(name = "friend", aliases = {"fr", "friends"}, description = "add or remove friends")
public class Friend extends Command {

    @Override
    public boolean execute(String[] args) {
        final FriendManager friendManager = FriendManager.getInstance();
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("rem") || args[0].equalsIgnoreCase("r")) {
                if (friendManager.getFriends().containsValue(args[1])) {
                    for (int i = 0; i < friendManager.getFriends().values().size(); i++) {
                        final String nick = (String) friendManager.getFriends().keySet().toArray()[i];
                        final String alias = (String) friendManager.getFriends().values().toArray()[i];
                        if (alias.equalsIgnoreCase(args[1])) {
                            friendManager.getFriends().remove(nick);
                        }
                    }
                } else {
                    friendManager.removeFriend(args[1]);
                }
                sendMessage("Removed friend §e" + args[1]);
            } else if (args[0].equalsIgnoreCase("add")) {
                IrcPlayer ircPlayer = IrcPlayer.getByIrcNickname(args[1]);
                if (ircPlayer == null) {
                    for (Object o : IrcPlayer.listPlayers()) {
                        if (o instanceof IrcPlayer) {
                            final IrcPlayer player = (IrcPlayer) o;
                            if (player.getClientName().equalsIgnoreCase("Koks") && player.getExtra().equalsIgnoreCase(args[1])) {
                                ircPlayer = player;
                            }
                        }
                    }
                }
                if (ircPlayer != null) {
                    if (!friendManager.isFriend(ircPlayer.getIngameName())) {
                        friendManager.addFriend(ircPlayer.getIngameName(), ircPlayer.getClientName().equalsIgnoreCase("Koks") ? ircPlayer.getExtra() : ircPlayer.getIrcNick());
                        sendMessage("Added friend §e" + args[1]);
                    } else {
                        sendError("Existing", "This friend already exist");
                    }
                }
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("add")) {
                if (!friendManager.isFriend(args[1])) {
                    friendManager.addFriend(args[1], args[2]);
                    sendMessage("Added friend §e" + args[2]);
                } else {
                    sendError("Existing", "This friend already exist");
                }
            }
        } else if (args.length == 0) {
            sendHelp(this, "add [IRC-Name]", "add [Name] [Alias]", "remove [Name|Alias]");
        } else {
            return false;
        }
        return true;
    }
}
