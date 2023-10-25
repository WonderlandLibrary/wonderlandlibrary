/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.command.datatypes;

import baritone.api.command.exception.CommandException;

public interface IDatatypePostFunction<T, O> {
    public T apply(O var1) throws CommandException;
}

