/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.pattern.ArrayPatternConverter;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.util.OptionConverter;
import org.apache.logging.log4j.util.PerformanceSensitive;

@PerformanceSensitive(value={"allocation"})
abstract class SimpleLiteralPatternConverter
extends LogEventPatternConverter
implements ArrayPatternConverter {
    private SimpleLiteralPatternConverter() {
        super("SimpleLiteral", "literal");
    }

    static LogEventPatternConverter of(String literal, boolean convertBackslashes) {
        String value = convertBackslashes ? OptionConverter.convertSpecialChars(literal) : literal;
        return SimpleLiteralPatternConverter.of(value);
    }

    static LogEventPatternConverter of(String literal) {
        if (literal == null || literal.isEmpty()) {
            return Noop.INSTANCE;
        }
        if (" ".equals(literal)) {
            return Space.INSTANCE;
        }
        return new StringValue(literal);
    }

    @Override
    public final void format(LogEvent ignored, StringBuilder output) {
        this.format(output);
    }

    @Override
    public final void format(Object ignored, StringBuilder output) {
        this.format(output);
    }

    @Override
    public final void format(StringBuilder output, Object ... args) {
        this.format(output);
    }

    abstract void format(StringBuilder var1);

    public final boolean isVariable() {
        return false;
    }

    @Override
    public final boolean handlesThrowable() {
        return false;
    }

    private static final class StringValue
    extends SimpleLiteralPatternConverter {
        private final String literal;

        StringValue(String literal) {
            this.literal = literal;
        }

        @Override
        void format(StringBuilder output) {
            output.append(this.literal);
        }
    }

    private static final class Space
    extends SimpleLiteralPatternConverter {
        private static final Space INSTANCE = new Space();

        private Space() {
        }

        @Override
        void format(StringBuilder output) {
            output.append(' ');
        }
    }

    private static final class Noop
    extends SimpleLiteralPatternConverter {
        private static final Noop INSTANCE = new Noop();

        private Noop() {
        }

        @Override
        void format(StringBuilder output) {
        }
    }
}

