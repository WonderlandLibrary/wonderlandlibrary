/*
 * Decompiled with CFR 0.152.
 */
package joptsimple;

import java.util.List;
import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.ArgumentList;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class OptionalArgumentOptionSpec<V>
extends ArgumentAcceptingOptionSpec<V> {
    OptionalArgumentOptionSpec(String option) {
        super(option, false);
    }

    OptionalArgumentOptionSpec(List<String> options, String description) {
        super(options, false, description);
    }

    @Override
    protected void detectOptionArgument(OptionParser parser, ArgumentList arguments, OptionSet detectedOptions) {
        if (arguments.hasMore()) {
            String nextArgument = arguments.peek();
            if (!parser.looksLikeAnOption(nextArgument) && this.canConvertArgument(nextArgument)) {
                this.handleOptionArgument(parser, detectedOptions, arguments);
            } else if (this.isArgumentOfNumberType() && this.canConvertArgument(nextArgument)) {
                this.addArguments(detectedOptions, arguments.next());
            } else {
                detectedOptions.add(this);
            }
        } else {
            detectedOptions.add(this);
        }
    }

    private void handleOptionArgument(OptionParser parser, OptionSet detectedOptions, ArgumentList arguments) {
        if (parser.posixlyCorrect()) {
            detectedOptions.add(this);
            parser.noMoreOptions();
        } else {
            this.addArguments(detectedOptions, arguments.next());
        }
    }
}

