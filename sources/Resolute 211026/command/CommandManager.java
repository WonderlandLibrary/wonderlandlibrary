// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.command;

import java.util.Iterator;
import vip.Resolute.Resolute;
import java.util.Arrays;
import vip.Resolute.events.impl.EventChat;
import vip.Resolute.command.impl.NameProtect;
import vip.Resolute.command.impl.SpectatorAlt;
import vip.Resolute.command.impl.API;
import vip.Resolute.command.impl.HClip;
import vip.Resolute.command.impl.Configure;
import vip.Resolute.command.impl.Clientname;
import vip.Resolute.command.impl.Unhide;
import vip.Resolute.command.impl.Hide;
import vip.Resolute.command.impl.VClip;
import vip.Resolute.command.impl.Bind;
import vip.Resolute.command.impl.Toggle;
import java.util.ArrayList;
import java.util.List;

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
        this.commands.add(new Toggle());
        this.commands.add(new Bind());
        this.commands.add(new VClip());
        this.commands.add(new Hide());
        this.commands.add(new Unhide());
        this.commands.add(new Clientname());
        this.commands.add(new Configure());
        this.commands.add(new HClip());
        this.commands.add(new API());
        this.commands.add(new SpectatorAlt());
        this.commands.add(new NameProtect());
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
            Resolute.addChatMessage("Could not find command");
        }
    }
}
