/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command;

import net.minecraft.command.CommandException;

public class CommandNotFoundException
extends CommandException {
    public CommandNotFoundException(String string, Object ... objectArray) {
        super(string, objectArray);
    }

    public CommandNotFoundException() {
        this("commands.generic.notFound", new Object[0]);
    }
}

