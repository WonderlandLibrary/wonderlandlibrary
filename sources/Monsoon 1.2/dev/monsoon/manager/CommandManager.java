package dev.monsoon.manager;

import dev.monsoon.Monsoon;
import dev.monsoon.command.Command;
import dev.monsoon.command.impl.*;
import dev.monsoon.command.impl.dos.DDoS;
import dev.monsoon.command.impl.yanchop.YanchopCommand;
import dev.monsoon.event.listeners.EventChat;
import dev.monsoon.notification.Notification;
import dev.monsoon.notification.NotificationManager;
import dev.monsoon.notification.NotificationType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {

    public List<Command> commands = new ArrayList<Command>();
    public String prefix = ".";

    public CommandManager() {
        commands.add(new Toggle());
        commands.add(new Bind());
        commands.add(new Prefix());
        commands.add(new Config());
        commands.add(new Info());
        commands.add(new IGN());
        commands.add(new Help());
        commands.add(new BedlessTP());
        commands.add(new YanchopCommand());
        commands.add(new DDoS());
        //.add(new WebhookSpammerCommand());
    }

    public void handleChat(EventChat e) {
        if(Monsoon.authorized) {
            String message = e.getMessage();

            if (!message.startsWith(prefix))
                return;

            e.setCancelled(true);

            message = message.substring(prefix.length());

            boolean foundCommand = false;

            if (message.split(" ").length > 0) {
                String commandName = message.split(" ")[0];

                for (Command c : commands) {
                    if (c.aliases.contains(commandName) || c.name.equalsIgnoreCase(commandName)) {
                        c.onCommand(Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length), message);
                        foundCommand = true;
                        break;
                    }
                }
            }
            if (!foundCommand) {
                NotificationManager.show(new Notification(NotificationType.ERROR, "Invalid Command", "Could not find command!", 3));
            }
        }
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
