/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command;

import net.minecraft.command.CommandException;

public class SyntaxErrorException
extends CommandException {
    public SyntaxErrorException(String string, Object ... objectArray) {
        super(string, objectArray);
    }

    public SyntaxErrorException() {
        this("commands.generic.snytax", new Object[0]);
    }
}

