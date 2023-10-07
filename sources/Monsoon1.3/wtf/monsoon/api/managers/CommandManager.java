package wtf.monsoon.api.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wtf.monsoon.Monsoon;
import wtf.monsoon.api.command.Command;
import wtf.monsoon.api.event.impl.EventChat;
import wtf.monsoon.impl.commands.yanchop.YanchopCommand;
import wtf.monsoon.impl.ui.notification.Notification;
import wtf.monsoon.impl.ui.notification.NotificationManager;
import wtf.monsoon.impl.ui.notification.NotificationType;
import wtf.monsoon.impl.commands.BedlessTP;
import wtf.monsoon.impl.commands.Bind;

public class CommandManager {

    public List<Command> commands = new ArrayList<Command>();
    public String prefix = ".";

    public CommandManager() {
    	
    	commands.add(new Bind());
    	commands.add(new BedlessTP());
    	commands.add(new YanchopCommand());
    	
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
