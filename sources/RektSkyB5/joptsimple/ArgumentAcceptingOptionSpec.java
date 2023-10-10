/*
 * Decompiled with CFR 0.152.
 */
package joptsimple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import joptsimple.AbstractOptionSpec;
import joptsimple.ArgumentList;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.ValueConverter;
import joptsimple.internal.Objects;
import joptsimple.internal.Reflection;
import joptsimple.internal.Strings;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class ArgumentAcceptingOptionSpec<V>
extends AbstractOptionSpec<V> {
    private static final char NIL_VALUE_SEPARATOR = '\u0000';
    private boolean optionRequired;
    private final boolean argumentRequired;
    private ValueConverter<V> converter;
    private String argumentDescription = "";
    private String valueSeparator = String.valueOf('\u0000');
    private final List<V> defaultValues = new ArrayList<V>();

    ArgumentAcceptingOptionSpec(String option, boolean argumentRequired) {
        super(option);
        this.argumentRequired = argumentRequired;
    }

    ArgumentAcceptingOptionSpec(List<String> options, boolean argumentRequired, String description) {
        super(options, description);
        this.argumentRequired = argumentRequired;
    }

    public final <T> ArgumentAcceptingOptionSpec<T> ofType(Class<T> argumentType) {
        return this.withValuesConvertedBy(Reflection.findConverter(argumentType));
    }

    public final <T> ArgumentAcceptingOptionSpec<T> withValuesConvertedBy(ValueConverter<T> aConverter) {
        if (aConverter == null) {
            throw new NullPointerException("illegal null converter");
        }
        this.converter = aConverter;
        return this;
    }

    public final ArgumentAcceptingOptionSpec<V> describedAs(String description) {
        this.argumentDescription = description;
        return this;
    }

    public final ArgumentAcceptingOptionSpec<V> withValuesSeparatedBy(char separator) {
        if (separator == '\u0000') {
            throw new IllegalArgumentException("cannot use U+0000 as separator");
        }
        this.valueSeparator = String.valueOf(separator);
        return this;
    }

    public final ArgumentAcceptingOptionSpec<V> withValuesSeparatedBy(String separator) {
        if (separator.indexOf(0) != -1) {
            throw new IllegalArgumentException("cannot use U+0000 in separator");
        }
        this.valueSeparator = separator;
        return this;
    }

    public ArgumentAcceptingOptionSpec<V> defaultsTo(V value, V ... values) {
        this.addDefaultValue(value);
        this.defaultsTo(values);
        return this;
    }

    public ArgumentAcceptingOptionSpec<V> defaultsTo(V[] values) {
        for (V each : values) {
            this.addDefaultValue(each);
        }
        return this;
    }

    public ArgumentAcceptingOptionSpec<V> required() {
        this.optionRequired = true;
        return this;
    }

    @Override
    public boolean isRequired() {
        return this.optionRequired;
    }

    private void addDefaultValue(V value) {
        Objects.ensureNotNull(value);
        this.defaultValues.add(value);
    }

    @Override
    final void handleOption(OptionParser parser, ArgumentList arguments, OptionSet detectedOptions, String detectedArgument) {
        if (Strings.isNullOrEmpty(detectedArgument)) {
            this.detectOptionArgument(parser, arguments, detectedOptions);
        } else {
            this.addArguments(detectedOptions, detectedArgument);
        }
    }

    protected void addArguments(OptionSet detectedOptions, String detectedArgument) {
        StringTokenizer lexer = new StringTokenizer(detectedArgument, this.valueSeparator);
        if (!lexer.hasMoreTokens()) {
            detectedOptions.addWithArgument(this, detectedArgument);
        } else {
            while (lexer.hasMoreTokens()) {
                detectedOptions.addWithArgument(this, lexer.nextToken());
            }
        }
    }

    protected abstract void detectOptionArgument(OptionParser var1, ArgumentList var2, OptionSet var3);

    @Override
    protected final V convert(String argument) {
        return this.convertWith(this.converter, argument);
    }

    protected boolean canConvertArgument(String argument) {
        StringTokenizer lexer = new StringTokenizer(argument, this.valueSeparator);
        try {
            while (lexer.hasMoreTokens()) {
                this.convert(lexer.nextToken());
            }
            return true;
        }
        catch (OptionException ignored) {
            return false;
        }
    }

    protected boolean isArgumentOfNumberType() {
        return this.converter != null && Number.class.isAssignableFrom(this.converter.valueType());
    }

    @Override
    public boolean acceptsArguments() {
        return true;
    }

    @Override
    public boolean requiresArgument() {
        return this.argumentRequired;
    }

    @Override
    public String argumentDescription() {
        return this.argumentDescription;
    }

    @Override
    public String argumentTypeIndicator() {
        return this.argumentTypeIndicatorFrom(this.converter);
    }

    public List<V> defaultValues() {
        return Collections.unmodifiableList(this.defaultValues);
    }

    @Override
    public boolean equals(Object that) {
        if (!super.equals(that)) {
            return false;
        }
        ArgumentAcceptingOptionSpec other = (ArgumentAcceptingOptionSpec)that;
        return this.requiresArgument() == other.requiresArgument();
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ (this.argumentRequired ? 0 : 1);
    }
}

