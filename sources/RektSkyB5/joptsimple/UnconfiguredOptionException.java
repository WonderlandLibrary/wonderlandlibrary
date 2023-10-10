/*
 * Decompiled with CFR 0.152.
 */
package joptsimple;

import java.util.Collections;
import java.util.List;
import joptsimple.OptionException;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class UnconfiguredOptionException
extends OptionException {
    private static final long serialVersionUID = -1L;

    UnconfiguredOptionException(String option) {
        this(Collections.singletonList(option));
    }

    UnconfiguredOptionException(List<String> options) {
        super(options);
    }

    @Override
    Object[] messageArguments() {
        return new Object[]{this.multipleOptionString()};
    }
}

