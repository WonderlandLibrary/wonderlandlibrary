/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.command;

import me.wintware.client.command.Command;
import me.wintware.client.utils.other.ChatUtils;

public abstract class AbstractCommand
implements Command {
    private final String name;
    private final String description;
    private final String usage;
    private final String[] aliases;

    public AbstractCommand(String name, String description, String usage, String ... aliases) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
        this.usage = usage;
    }

    public void usage() {
        ChatUtils.addChatMessage("Invalid usage try: " + this.getUsage());
    }

    public String getUsage() {
        return this.usage;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String[] getAliases() {
        return this.aliases;
    }
}

