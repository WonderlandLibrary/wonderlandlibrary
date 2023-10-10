/*
 * Decompiled with CFR 0.152.
 */
package joptsimple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import joptsimple.ArgumentList;
import joptsimple.OptionArgumentConversionException;
import joptsimple.OptionDescriptor;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import joptsimple.ValueConversionException;
import joptsimple.ValueConverter;
import joptsimple.internal.Reflection;
import joptsimple.internal.ReflectionException;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
abstract class AbstractOptionSpec<V>
implements OptionSpec<V>,
OptionDescriptor {
    private final List<String> options = new ArrayList<String>();
    private final String description;
    private boolean forHelp;

    protected AbstractOptionSpec(String option) {
        this(Collections.singletonList(option), "");
    }

    protected AbstractOptionSpec(List<String> options, String description) {
        this.arrangeOptions(options);
        this.description = description;
    }

    @Override
    public final List<String> options() {
        return Collections.unmodifiableList(this.options);
    }

    @Override
    public final List<V> values(OptionSet detectedOptions) {
        return detectedOptions.valuesOf(this);
    }

    @Override
    public final V value(OptionSet detectedOptions) {
        return detectedOptions.valueOf(this);
    }

    @Override
    public String description() {
        return this.description;
    }

    public final AbstractOptionSpec<V> forHelp() {
        this.forHelp = true;
        return this;
    }

    @Override
    public final boolean isForHelp() {
        return this.forHelp;
    }

    @Override
    public boolean representsNonOptions() {
        return false;
    }

    protected abstract V convert(String var1);

    protected V convertWith(ValueConverter<V> converter, String argument) {
        try {
            return Reflection.convertWith(converter, argument);
        }
        catch (ReflectionException ex) {
            throw new OptionArgumentConversionException(this, argument, ex);
        }
        catch (ValueConversionException ex) {
            throw new OptionArgumentConversionException(this, argument, ex);
        }
    }

    protected String argumentTypeIndicatorFrom(ValueConverter<V> converter) {
        if (converter == null) {
            return null;
        }
        String pattern = converter.valuePattern();
        return pattern == null ? converter.valueType().getName() : pattern;
    }

    abstract void handleOption(OptionParser var1, ArgumentList var2, OptionSet var3, String var4);

    private void arrangeOptions(List<String> unarranged) {
        if (unarranged.size() == 1) {
            this.options.addAll(unarranged);
            return;
        }
        ArrayList<String> shortOptions = new ArrayList<String>();
        ArrayList<String> longOptions = new ArrayList<String>();
        for (String each : unarranged) {
            if (each.length() == 1) {
                shortOptions.add(each);
                continue;
            }
            longOptions.add(each);
        }
        Collections.sort(shortOptions);
        Collections.sort(longOptions);
        this.options.addAll(shortOptions);
        this.options.addAll(longOptions);
    }

    public boolean equals(Object that) {
        if (!(that instanceof AbstractOptionSpec)) {
            return false;
        }
        AbstractOptionSpec other = (AbstractOptionSpec)that;
        return this.options.equals(other.options);
    }

    public int hashCode() {
        return this.options.hashCode();
    }

    public String toString() {
        return this.options.toString();
    }
}

