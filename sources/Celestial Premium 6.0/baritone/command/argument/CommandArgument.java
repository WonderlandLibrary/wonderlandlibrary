/*
 * Decompiled with CFR 0.150.
 */
package baritone.command.argument;

import baritone.api.command.argument.ICommandArgument;
import baritone.api.command.exception.CommandInvalidTypeException;
import baritone.command.argparser.ArgParserManager;
import java.util.stream.Stream;

class CommandArgument
implements ICommandArgument {
    private final int index;
    private final String value;
    private final String rawRest;

    CommandArgument(int index, String value, String rawRest) {
        this.index = index;
        this.value = value;
        this.rawRest = rawRest;
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String getRawRest() {
        return this.rawRest;
    }

    @Override
    public <E extends Enum<?>> E getEnum(Class<E> enumClass) throws CommandInvalidTypeException {
        return (E)Stream.of(enumClass.getEnumConstants()).filter(e -> e.name().equalsIgnoreCase(this.value)).findFirst().orElseThrow(() -> new CommandInvalidTypeException(this, enumClass.getSimpleName()));
    }

    @Override
    public <T> T getAs(Class<T> type) throws CommandInvalidTypeException {
        return ArgParserManager.INSTANCE.parseStateless(type, this);
    }

    @Override
    public <T> boolean is(Class<T> type) {
        try {
            this.getAs(type);
            return true;
        }
        catch (Throwable t) {
            return false;
        }
    }

    @Override
    public <T, S> T getAs(Class<T> type, Class<S> stateType, S state) throws CommandInvalidTypeException {
        return ArgParserManager.INSTANCE.parseStated(type, stateType, this, state);
    }

    @Override
    public <T, S> boolean is(Class<T> type, Class<S> stateType, S state) {
        try {
            this.getAs(type, stateType, state);
            return true;
        }
        catch (Throwable t) {
            return false;
        }
    }
}

