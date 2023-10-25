/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.command.argument;

import baritone.api.command.exception.CommandInvalidTypeException;

public interface ICommandArgument {
    public int getIndex();

    public String getValue();

    public String getRawRest();

    public <E extends Enum<?>> E getEnum(Class<E> var1) throws CommandInvalidTypeException;

    public <T> T getAs(Class<T> var1) throws CommandInvalidTypeException;

    public <T> boolean is(Class<T> var1);

    public <T, S> T getAs(Class<T> var1, Class<S> var2, S var3) throws CommandInvalidTypeException;

    public <T, S> boolean is(Class<T> var1, Class<S> var2, S var3);
}

