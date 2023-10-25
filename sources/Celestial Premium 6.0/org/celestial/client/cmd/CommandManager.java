/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.cmd;

import java.util.ArrayList;
import java.util.List;
import org.celestial.client.cmd.Command;
import org.celestial.client.cmd.CommandAbstract;
import org.celestial.client.cmd.CommandHandler;
import org.celestial.client.cmd.impl.BindCommand;
import org.celestial.client.cmd.impl.ClipCommand;
import org.celestial.client.cmd.impl.ConfigCommand;
import org.celestial.client.cmd.impl.FakeHackCommand;
import org.celestial.client.cmd.impl.FakeNameCommand;
import org.celestial.client.cmd.impl.FriendCommand;
import org.celestial.client.cmd.impl.HelpCommand;
import org.celestial.client.cmd.impl.MacroCommand;
import org.celestial.client.cmd.impl.PanicCommand;
import org.celestial.client.cmd.impl.ParseCommand;
import org.celestial.client.cmd.impl.ResCommand;
import org.celestial.client.cmd.impl.SettingCommand;
import org.celestial.client.cmd.impl.TPCommand;
import org.celestial.client.cmd.impl.XrayCommand;
import org.celestial.client.event.EventManager;

public class CommandManager {
    private final ArrayList<Command> commands = new ArrayList();

    public CommandManager() {
        EventManager.register(new CommandHandler(this));
        this.commands.add(new FakeHackCommand());
        this.commands.add(new BindCommand());
        this.commands.add(new ClipCommand());
        this.commands.add(new FriendCommand());
        this.commands.add(new MacroCommand());
        this.commands.add(new ConfigCommand());
        this.commands.add(new ResCommand());
        this.commands.add(new HelpCommand());
        this.commands.add(new XrayCommand());
        this.commands.add(new SettingCommand());
        this.commands.add(new FakeNameCommand());
        this.commands.add(new PanicCommand());
        this.commands.add(new TPCommand());
        this.commands.add(new ParseCommand());
    }

    public List<Command> getCommands() {
        return this.commands;
    }

    public boolean execute(String args) {
        String noPrefix = args.substring(1);
        String[] split = noPrefix.split(" ");
        if (split.length > 0) {
            for (Command command : this.commands) {
                String[] commandAliases;
                CommandAbstract abstractCommand = (CommandAbstract)command;
                for (String alias : commandAliases = abstractCommand.getAliases()) {
                    if (!split[0].equalsIgnoreCase(alias)) continue;
                    abstractCommand.execute(split);
                    return true;
                }
            }
        }
        return false;
    }
}

