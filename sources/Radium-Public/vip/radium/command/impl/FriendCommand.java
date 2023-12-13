package vip.radium.command.impl;

import vip.radium.RadiumClient;
import vip.radium.command.Command;
import vip.radium.command.CommandExecutionException;
import vip.radium.notification.Notification;
import vip.radium.notification.NotificationType;

public final class FriendCommand implements Command {
    @Override
    public String[] getAliases() {
        return new String[]{"friend", "f"};
    }

    @Override
    public void execute(String[] arguments) throws CommandExecutionException {
        friend:
        {
            if (arguments.length <= 1)
                break friend;
            final String usernameOrAction = arguments[1].toUpperCase();
            String username = usernameOrAction;
            switch (usernameOrAction) {
                case "ADD":
                    if (arguments.length <= 2)
                        break friend;
                    username = arguments[2];
                default:
                    final String alias;
                    if (arguments.length >= 4) {
                        alias = arguments[3];
                    } else {
                        alias = null;
                    }
                    final String friendAdded = RadiumClient.getInstance().getPlayerManager()
                            .add(username, alias, false);
                    if (friendAdded != null) {
                        RadiumClient.getInstance().getNotificationManager().add(new Notification(
                                "Friend Added",
                                String.format("%s is now friended.", friendAdded),
                                1500L,
                                NotificationType.SUCCESS));
                    } else {
                        RadiumClient.getInstance().getNotificationManager().add(new Notification(
                                String.format("%s is not a player.", username), 1000L, NotificationType.ERROR));
                    }
                    return;
                case "DEL":
                case "REMOVE":
                    if (arguments.length != 3)
                        break friend;
                    username = arguments[2];

                    final String friendRemoved = RadiumClient.getInstance().getPlayerManager()
                            .remove(username);
                    if (friendRemoved != null) {
                        RadiumClient.getInstance().getNotificationManager().add(new Notification(
                                "Friend Removed",
                                String.format("%s is no longer friended.", friendRemoved),
                                1500L,
                                NotificationType.SUCCESS));
                    } else {
                        RadiumClient.getInstance().getNotificationManager().add(new Notification(
                                String.format("%s is not friended.", username), 1000L, NotificationType.ERROR));
                    }

            }
        }

        throw new CommandExecutionException(getUsage());
    }

    @Override
    public String getUsage() {
        return "friend/f <(optional)add/remove/del> <username> <(optional)alias>";
    }
}
