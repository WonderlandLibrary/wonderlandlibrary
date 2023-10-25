/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.command.datatypes;

import baritone.api.command.datatypes.IDatatypeContext;
import baritone.api.command.exception.CommandException;
import java.util.stream.Stream;

public interface IDatatype {
    public Stream<String> tabComplete(IDatatypeContext var1) throws CommandException;
}

