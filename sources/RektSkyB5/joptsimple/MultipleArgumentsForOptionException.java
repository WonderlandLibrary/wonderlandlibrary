/*
 * Decompiled with CFR 0.152.
 */
package joptsimple;

import java.util.Collections;
import joptsimple.OptionException;
import joptsimple.OptionSpec;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class MultipleArgumentsForOptionException
extends OptionException {
    private static final long serialVersionUID = -1L;

    MultipleArgumentsForOptionException(OptionSpec<?> options) {
        super(Collections.singleton(options));
    }

    @Override
    Object[] messageArguments() {
        return new Object[]{this.singleOptionString()};
    }
}

