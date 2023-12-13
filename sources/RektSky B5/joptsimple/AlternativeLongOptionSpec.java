/*
 * Decompiled with CFR 0.152.
 */
package joptsimple;

import java.util.Collections;
import java.util.Locale;
import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.ArgumentList;
import joptsimple.OptionMissingRequiredArgumentException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.internal.Messages;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class AlternativeLongOptionSpec
extends ArgumentAcceptingOptionSpec<String> {
    AlternativeLongOptionSpec() {
        super(Collections.singletonList("W"), true, Messages.message(Locale.getDefault(), "joptsimple.HelpFormatterMessages", AlternativeLongOptionSpec.class, "description", new Object[0]));
        this.describedAs(Messages.message(Locale.getDefault(), "joptsimple.HelpFormatterMessages", AlternativeLongOptionSpec.class, "arg.description", new Object[0]));
    }

    @Override
    protected void detectOptionArgument(OptionParser parser, ArgumentList arguments, OptionSet detectedOptions) {
        if (!arguments.hasMore()) {
            throw new OptionMissingRequiredArgumentException(this);
        }
        arguments.treatNextAsLongOption();
    }
}

