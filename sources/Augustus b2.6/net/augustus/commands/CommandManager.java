// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.commands;

import net.lenni0451.eventapi.reflection.EventTarget;
import java.util.Iterator;
import net.augustus.Augustus;
import net.augustus.events.EventChat;
import net.augustus.commands.commands.CommandClicker;
import net.augustus.commands.commands.CommandTest;
import net.augustus.commands.commands.CommandKillauraWhitelist;
import net.augustus.commands.commands.CommandFucker;
import net.augustus.commands.commands.CommandBlockESP;
import net.augustus.commands.commands.CommandToggle;
import net.augustus.commands.commands.CommandInGameName;
import net.augustus.commands.commands.CommandHelp;
import net.augustus.commands.commands.CommandBind;
import net.lenni0451.eventapi.manager.EventManager;
import java.util.ArrayList;
import java.util.List;

public class CommandManager
{
    private List<Command> commands;
    
    public CommandManager() {
        this.commands = new ArrayList<Command>();
        EventManager.register((Object)this);
        this.commands.add(new CommandBind());
        this.commands.add(new CommandHelp());
        this.commands.add(new CommandInGameName());
        this.commands.add(new CommandToggle());
        this.commands.add(new CommandBlockESP());
        this.commands.add(new CommandFucker());
        this.commands.add(new CommandKillauraWhitelist());
        this.commands.add(new CommandTest());
        this.commands.add(new CommandClicker());
    }
    
    @EventTarget
    public void onEventChat(final EventChat eventChat) {
        final String message = eventChat.getMessage();
        for (final Command command : Augustus.getInstance().getCommandManager().getCommands()) {
            final String[] strings = message.split(" ");
            if (strings.length > 0 && strings[0].equalsIgnoreCase(command.getCommand())) {
                command.commandAction(strings);
                eventChat.setCanceled(true);
            }
        }
    }
    
    public List<Command> getCommands() {
        return this.commands;
    }
    
    public void setCommands(final List<Command> commands) {
        this.commands = commands;
    }
}
