/*
 * Decompiled with CFR 0.150.
 */
package baritone.command.argparser;

import baritone.api.command.argparser.IArgParser;
import baritone.api.command.argument.ICommandArgument;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DefaultArgParsers {
    public static final List<IArgParser<?>> ALL = Arrays.asList(IntArgumentParser.INSTANCE, LongArgumentParser.INSTANCE, FloatArgumentParser.INSTANCE, DoubleArgumentParser.INSTANCE, BooleanArgumentParser.INSTANCE);

    public static class BooleanArgumentParser
    implements IArgParser.Stateless<Boolean> {
        public static final BooleanArgumentParser INSTANCE = new BooleanArgumentParser();
        public static final List<String> TRUTHY_VALUES = Arrays.asList("1", "true", "yes", "t", "y", "on", "enable");
        public static final List<String> FALSY_VALUES = Arrays.asList("0", "false", "no", "f", "n", "off", "disable");

        @Override
        public Class<Boolean> getTarget() {
            return Boolean.class;
        }

        @Override
        public Boolean parseArg(ICommandArgument arg) throws RuntimeException {
            String value = arg.getValue();
            if (TRUTHY_VALUES.contains(value.toLowerCase(Locale.US))) {
                return true;
            }
            if (FALSY_VALUES.contains(value.toLowerCase(Locale.US))) {
                return false;
            }
            throw new IllegalArgumentException("invalid boolean");
        }
    }

    public static enum DoubleArgumentParser implements IArgParser.Stateless<Double>
    {
        INSTANCE;


        @Override
        public Class<Double> getTarget() {
            return Double.class;
        }

        @Override
        public Double parseArg(ICommandArgument arg) throws RuntimeException {
            String value = arg.getValue();
            if (!value.matches("^([+-]?(?:\\d+(?:\\.\\d*)?|\\.\\d+)|)$")) {
                throw new IllegalArgumentException("failed double format check");
            }
            return Double.parseDouble(value);
        }
    }

    public static enum FloatArgumentParser implements IArgParser.Stateless<Float>
    {
        INSTANCE;


        @Override
        public Class<Float> getTarget() {
            return Float.class;
        }

        @Override
        public Float parseArg(ICommandArgument arg) throws RuntimeException {
            String value = arg.getValue();
            if (!value.matches("^([+-]?(?:\\d+(?:\\.\\d*)?|\\.\\d+)|)$")) {
                throw new IllegalArgumentException("failed float format check");
            }
            return Float.valueOf(Float.parseFloat(value));
        }
    }

    public static enum LongArgumentParser implements IArgParser.Stateless<Long>
    {
        INSTANCE;


        @Override
        public Class<Long> getTarget() {
            return Long.class;
        }

        @Override
        public Long parseArg(ICommandArgument arg) throws RuntimeException {
            return Long.parseLong(arg.getValue());
        }
    }

    public static enum IntArgumentParser implements IArgParser.Stateless<Integer>
    {
        INSTANCE;


        @Override
        public Class<Integer> getTarget() {
            return Integer.class;
        }

        @Override
        public Integer parseArg(ICommandArgument arg) throws RuntimeException {
            return Integer.parseInt(arg.getValue());
        }
    }
}

