/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command;

import net.minecraft.command.CommandException;

public class PlayerNotFoundException
extends CommandException {
    public PlayerNotFoundException(String p_i47330_1_) {
        super(p_i47330_1_, new Object[0]);
    }

    public PlayerNotFoundException(String message, Object ... replacements) {
        super(message, replacements);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}

