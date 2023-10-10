/*
 * Decompiled with CFR 0.152.
 */
package joptsimple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.NoArgumentOptionSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSpec;
import joptsimple.OptionalArgumentOptionSpec;
import joptsimple.RequiredArgumentOptionSpec;
import joptsimple.UnconfiguredOptionException;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class OptionSpecBuilder
extends NoArgumentOptionSpec {
    private final OptionParser parser;

    OptionSpecBuilder(OptionParser parser, List<String> options, String description) {
        super(options, description);
        this.parser = parser;
        this.attachToParser();
    }

    private void attachToParser() {
        this.parser.recognize(this);
    }

    public ArgumentAcceptingOptionSpec<String> withRequiredArg() {
        RequiredArgumentOptionSpec<String> newSpec = new RequiredArgumentOptionSpec<String>(this.options(), this.description());
        this.parser.recognize(newSpec);
        return newSpec;
    }

    public ArgumentAcceptingOptionSpec<String> withOptionalArg() {
        OptionalArgumentOptionSpec<String> newSpec = new OptionalArgumentOptionSpec<String>(this.options(), this.description());
        this.parser.recognize(newSpec);
        return newSpec;
    }

    public OptionSpecBuilder requiredIf(String dependent, String ... otherDependents) {
        List<String> dependents = this.validatedDependents(dependent, otherDependents);
        for (String each : dependents) {
            this.parser.requiredIf(this.options(), each);
        }
        return this;
    }

    public OptionSpecBuilder requiredIf(OptionSpec<?> dependent, OptionSpec<?> ... otherDependents) {
        this.parser.requiredIf(this.options(), dependent);
        for (OptionSpec<?> each : otherDependents) {
            this.parser.requiredIf(this.options(), each);
        }
        return this;
    }

    public OptionSpecBuilder requiredUnless(String dependent, String ... otherDependents) {
        List<String> dependents = this.validatedDependents(dependent, otherDependents);
        for (String each : dependents) {
            this.parser.requiredUnless(this.options(), each);
        }
        return this;
    }

    public OptionSpecBuilder requiredUnless(OptionSpec<?> dependent, OptionSpec<?> ... otherDependents) {
        this.parser.requiredUnless(this.options(), dependent);
        for (OptionSpec<?> each : otherDependents) {
            this.parser.requiredUnless(this.options(), each);
        }
        return this;
    }

    private List<String> validatedDependents(String dependent, String ... otherDependents) {
        ArrayList<String> dependents = new ArrayList<String>();
        dependents.add(dependent);
        Collections.addAll(dependents, otherDependents);
        for (String each : dependents) {
            if (this.parser.isRecognized(each)) continue;
            throw new UnconfiguredOptionException(each);
        }
        return dependents;
    }
}

