/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command;

import net.minecraft.command.CommandException;

public class NumberInvalidException
extends CommandException {
    public NumberInvalidException() {
        this("commands.generic.num.invalid", new Object[0]);
    }

    public NumberInvalidException(String string, Object ... objectArray) {
        super(string, objectArray);
    }
}

