/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command;

import net.minecraft.command.CommandException;

public class EntityNotFoundException
extends CommandException {
    public EntityNotFoundException(String string, Object ... objectArray) {
        super(string, objectArray);
    }

    public EntityNotFoundException() {
        this("commands.generic.entity.notFound", new Object[0]);
    }
}

