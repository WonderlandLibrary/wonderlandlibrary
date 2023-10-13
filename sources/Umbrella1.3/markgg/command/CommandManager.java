/*
 * Decompiled with CFR 0.150.
 */
package markgg.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import markgg.Client;
import markgg.command.Command;
import markgg.command.impl.Bind;
import markgg.command.impl.Flip;
import markgg.command.impl.Help;
import markgg.command.impl.Say;
import markgg.command.impl.Toggle;
import markgg.command.impl.VClip;
import markgg.events.listeners.EventChat;

public class CommandManager {
    public List<Command> commands = new ArrayList<Command>();
    public String prefix = ".";

    public CommandManager() {
        this.setup();
    }

    public void setup() {
        this.commands.add(new Toggle());
        this.commands.add(new VClip());
        this.commands.add(new Bind());
        this.commands.add(new Help());
        this.commands.add(new Say());
        this.commands.add(new Flip());
    }

    public void handleChat(EventChat event) {
        String message = event.getMessage();
        if (!message.startsWith(this.prefix)) {
            return;
        }
        event.setCancelled(true);
        message = message.substring(this.prefix.length());
        boolean foundCommand = false;
        if (message.split(" ").length > 0) {
            String commandName = message.split(" ")[0];
            for (Command c : this.commands) {
                if (!c.aliases.contains(commandName) && !c.name.equalsIgnoreCase(commandName)) continue;
                c.onCommand(Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length), message);
                foundCommand = true;
                break;
            }
        }
        if (!foundCommand) {
            Client.addChatMessage("Could not find command!");
        }
    }
}

