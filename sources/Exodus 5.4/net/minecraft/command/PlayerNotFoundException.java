/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command;

import net.minecraft.command.CommandException;

public class PlayerNotFoundException
extends CommandException {
    public PlayerNotFoundException(String string, Object ... objectArray) {
        super(string, objectArray);
    }

    public PlayerNotFoundException() {
        this("commands.generic.player.notFound", new Object[0]);
    }
}

