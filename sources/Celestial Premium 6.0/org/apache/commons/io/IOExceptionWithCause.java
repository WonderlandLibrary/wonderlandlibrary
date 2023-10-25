/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.io;

import java.io.IOException;

@Deprecated
public class IOExceptionWithCause
extends IOException {
    private static final long serialVersionUID = 1L;

    public IOExceptionWithCause(String message, Throwable cause) {
        super(message, cause);
    }

    public IOExceptionWithCause(Throwable cause) {
        super(cause);
    }
}

