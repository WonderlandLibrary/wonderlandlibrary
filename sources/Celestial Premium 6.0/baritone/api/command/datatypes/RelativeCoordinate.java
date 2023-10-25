/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.command.datatypes;

import baritone.api.command.argument.IArgConsumer;
import baritone.api.command.datatypes.IDatatypeContext;
import baritone.api.command.datatypes.IDatatypePost;
import baritone.api.command.exception.CommandException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public enum RelativeCoordinate implements IDatatypePost<Double, Double>
{
    INSTANCE;

    private static Pattern PATTERN;

    @Override
    public Double apply(IDatatypeContext ctx, Double origin) throws CommandException {
        double offset;
        Matcher matcher;
        if (origin == null) {
            origin = 0.0;
        }
        if (!(matcher = PATTERN.matcher(ctx.getConsumer().getString())).matches()) {
            throw new IllegalArgumentException("pattern doesn't match");
        }
        boolean isRelative = !matcher.group(1).isEmpty();
        double d = offset = matcher.group(2).isEmpty() ? 0.0 : Double.parseDouble(matcher.group(2).replaceAll("k", ""));
        if (matcher.group(2).contains("k")) {
            offset *= 1000.0;
        }
        if (isRelative) {
            return origin + offset;
        }
        return offset;
    }

    @Override
    public Stream<String> tabComplete(IDatatypeContext ctx) throws CommandException {
        IArgConsumer consumer = ctx.getConsumer();
        if (!consumer.has(2) && consumer.getString().matches("^(~|$)")) {
            return Stream.of("~");
        }
        return Stream.empty();
    }

    static {
        PATTERN = Pattern.compile("^(~?)([+-]?(?:\\d+(?:\\.\\d*)?|\\.\\d+)([k-k]?)|)$");
    }
}

