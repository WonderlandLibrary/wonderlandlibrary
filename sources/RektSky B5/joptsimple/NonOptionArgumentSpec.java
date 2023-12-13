/*
 * Decompiled with CFR 0.152.
 */
package joptsimple;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import joptsimple.AbstractOptionSpec;
import joptsimple.ArgumentList;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.ValueConverter;
import joptsimple.internal.Reflection;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class NonOptionArgumentSpec<V>
extends AbstractOptionSpec<V> {
    static final String NAME = "[arguments]";
    private ValueConverter<V> converter;
    private String argumentDescription = "";

    NonOptionArgumentSpec() {
        this("");
    }

    NonOptionArgumentSpec(String description) {
        super(Arrays.asList(NAME), description);
    }

    public <T> NonOptionArgumentSpec<T> ofType(Class<T> argumentType) {
        this.converter = Reflection.findConverter(argumentType);
        return this;
    }

    public final <T> NonOptionArgumentSpec<T> withValuesConvertedBy(ValueConverter<T> aConverter) {
        if (aConverter == null) {
            throw new NullPointerException("illegal null converter");
        }
        this.converter = aConverter;
        return this;
    }

    public NonOptionArgumentSpec<V> describedAs(String description) {
        this.argumentDescription = description;
        return this;
    }

    @Override
    protected final V convert(String argument) {
        return this.convertWith(this.converter, argument);
    }

    @Override
    void handleOption(OptionParser parser, ArgumentList arguments, OptionSet detectedOptions, String detectedArgument) {
        detectedOptions.addWithArgument(this, detectedArgument);
    }

    @Override
    public List<?> defaultValues() {
        return Collections.emptyList();
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public boolean acceptsArguments() {
        return false;
    }

    @Override
    public boolean requiresArgument() {
        return false;
    }

    @Override
    public String argumentDescription() {
        return this.argumentDescription;
    }

    @Override
    public String argumentTypeIndicator() {
        return this.argumentTypeIndicatorFrom(this.converter);
    }

    @Override
    public boolean representsNonOptions() {
        return true;
    }
}

