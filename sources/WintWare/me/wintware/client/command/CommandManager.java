/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.command;

import java.util.ArrayList;
import java.util.List;
import me.wintware.client.command.AbstractCommand;
import me.wintware.client.command.CommandHandler;
import me.wintware.client.command.impl.BindCommand;
import me.wintware.client.command.impl.ClientNameCommand;
import me.wintware.client.command.impl.FakeHackCommand;
import me.wintware.client.event.EventManager;

public final class CommandManager {
    private final List commands = new ArrayList();

    public CommandManager() {
        EventManager.register(new CommandHandler(this));
        this.commands.add(new ClientNameCommand());
        this.commands.add(new FakeHackCommand());
        this.commands.add(new BindCommand());
    }

    public List getCommands() {
        return this.commands;
    }

    public final boolean execute(String args) {
        String noPrefix = args.substring(1);
        String[] split = noPrefix.split(" ");
        if (split.length > 0) {
            List commands = this.commands;
            int commandsSize = commands.size();
            for (int i = 0; i < commandsSize; ++i) {
                AbstractCommand command = (AbstractCommand)commands.get(i);
                for (String alias : command.getAliases()) {
                    if (!split[0].equalsIgnoreCase(alias)) continue;
                    command.execute(split);
                    return true;
                }
            }
        }
        return false;
    }
}

