package best.azura.client.impl.command.impl;

import best.azura.client.impl.Client;
import best.azura.client.api.command.ACommand;
import best.azura.client.api.friend.Friend;
import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import com.mojang.realmsclient.gui.ChatFormatting;

public class FriendCommand extends ACommand {
    @Override
    public String getName() {
        return "Friend";
    }

    @Override
    public String getDescription() {
        return "Add or remove a friend";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"f", "friend", "friends"};
    }

    @Override
    public void handleCommand(String[] args) {
        if (args.length < 1) {
            msg(Client.PREFIX + "§cPlease use .friend <add, remove, list>");
        }
        switch (args[0].toLowerCase()) {
            case "add": {
                Client.INSTANCE.getFriendManager().addFriend(args[1]);
                Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Friends", "Added " + args[1] + " as a friend!", 3000, Type.SUCCESS));
            }
            break;
            case "remove": {
                try {
                    Client.INSTANCE.getFriendManager().removeFriend(args[1]);
                    Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Friends", "Removed " + args[1] + " as a friend", 3000, Type.SUCCESS));

                } catch (Exception e) {
                    Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Friends", "Can not remove " + args[1] + " since he does not exist", 3000, Type.WARNING));
                }
            }
            break;
            case "list": {
                for (Friend friend : Client.INSTANCE.getFriendManager().getFriendList()) {
                    try {
                        msg(ChatFormatting.GREEN + friend.getName() + "§r is your friend");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case "clear": {
                Client.INSTANCE.getFriendManager().getFriendList().clear();
                break;
            }
        }
    }
}
