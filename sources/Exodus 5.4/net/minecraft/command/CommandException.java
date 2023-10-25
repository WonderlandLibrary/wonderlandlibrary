/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command;

public class CommandException
extends Exception {
    private final Object[] errorObjects;

    public CommandException(String string, Object ... objectArray) {
        super(string);
        this.errorObjects = objectArray;
    }

    public Object[] getErrorObjects() {
        return this.errorObjects;
    }
}

