/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.commands.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import tk.rektsky.Client;
import tk.rektsky.commands.Command;
import tk.rektsky.module.impl.render.Notification;
import tk.rektsky.utils.display.ColorUtil;

public class FriendCommand
extends Command {
    public static List<String> friends = new ArrayList<String>();

    public FriendCommand() {
        super("friend", new String[]{"f"}, "<add | remove | list> [player]", "Add friends to Killaura whitelist");
    }

    @Override
    public void onCommand(String label, String[] args) {
        if (args.length == 0) {
            Client.addClientChat("Incorrect Usage! Please use .help");
            return;
        }
        if (args[0].equalsIgnoreCase("add")) {
            if (args.length != 2) {
                Client.addClientChat("Incorrect Usage! Please use .help");
                return;
            }
            friends.add(args[1].toLowerCase(Locale.ROOT));
            Client.notify(new Notification.PopupMessage("Friend", "Added " + args[1] + " to your friend list!", ColorUtil.NotificationColors.GREEN, 20));
        }
        if (args[0].equalsIgnoreCase("remove")) {
            if (args.length != 2) {
                Client.addClientChat("Incorrect Usage! Please use .help");
                return;
            }
            for (String friend : friends) {
                if (!friend.equalsIgnoreCase(args[0])) continue;
                friends.remove(friend);
                Client.notify(new Notification.PopupMessage("Friend", "Removed " + args[1] + " from your friend list!", ColorUtil.NotificationColors.RED, 20));
                return;
            }
        }
        if (args[0].equalsIgnoreCase("list")) {
            Client.addClientChat("===== Friends =====");
            for (String friend : friends) {
                Client.addClientChat(" - " + friend);
            }
        }
    }
}

