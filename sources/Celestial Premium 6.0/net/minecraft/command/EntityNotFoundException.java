/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command;

import net.minecraft.command.CommandException;

public class EntityNotFoundException
extends CommandException {
    public EntityNotFoundException(String p_i47332_1_) {
        this("commands.generic.entity.notFound", p_i47332_1_);
    }

    public EntityNotFoundException(String message, Object ... args) {
        super(message, args);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}

