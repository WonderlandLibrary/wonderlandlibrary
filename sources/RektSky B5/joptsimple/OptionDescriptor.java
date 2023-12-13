/*
 * Decompiled with CFR 0.152.
 */
package joptsimple;

import java.util.List;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public interface OptionDescriptor {
    public List<String> options();

    public String description();

    public List<?> defaultValues();

    public boolean isRequired();

    public boolean acceptsArguments();

    public boolean requiresArgument();

    public String argumentDescription();

    public String argumentTypeIndicator();

    public boolean representsNonOptions();
}

