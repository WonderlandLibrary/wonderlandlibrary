package com.initial.commands;

import com.initial.commands.impl.*;
import com.initial.events.impl.*;
import com.initial.*;
import java.util.*;

public class CommandManager
{
    public List<Command> commands;
    public String prefix;
    
    public CommandManager() {
        this.commands = new ArrayList<Command>();
        this.prefix = ".";
        this.setup();
    }
    
    public void setup() {
        this.commands.add(new Hide());
        this.commands.add(new Help());
        this.commands.add(new Rename());
        this.commands.add(new VClip());
        this.commands.add(new Toggle());
        this.commands.add(new Bind());
        this.commands.add(new HClip());
        this.commands.add(new Name());
        this.commands.add(new Config());
    }
    
    public void handleChat(final EventChat event) {
        String message = event.getMessage();
        if (!message.startsWith(this.prefix)) {
            return;
        }
        event.setCancelled(true);
        message = message.substring(this.prefix.length());
        boolean foundCommand = false;
        if (message.split(" ").length > 0) {
            final String commandName = message.split(" ")[0];
            for (final Command c : this.commands) {
                if (c.aliases.contains(commandName) || c.name.equalsIgnoreCase(commandName)) {
                    c.onCommand(Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length), message);
                    foundCommand = true;
                    break;
                }
            }
        }
        if (!foundCommand) {
            Astomero.addChatMessage("Could find the command.");
        }
    }
}
