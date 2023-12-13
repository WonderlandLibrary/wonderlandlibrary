/*
 * Decompiled with CFR 0.152.
 */
package joptsimple;

import java.util.Collection;
import java.util.List;
import joptsimple.OptionException;
import joptsimple.OptionSpec;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class MissingRequiredOptionsException
extends OptionException {
    private static final long serialVersionUID = -1L;

    protected MissingRequiredOptionsException(List<? extends OptionSpec<?>> missingRequiredOptions) {
        super((Collection<? extends OptionSpec<?>>)missingRequiredOptions);
    }

    @Override
    Object[] messageArguments() {
        return new Object[]{this.multipleOptionString()};
    }
}

