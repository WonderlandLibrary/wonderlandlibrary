/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.slf4j;

public class SLF4JLoggingException
extends RuntimeException {
    private static final long serialVersionUID = -1618650972455089998L;

    public SLF4JLoggingException(String msg) {
        super(msg);
    }

    public SLF4JLoggingException(String msg, Exception ex) {
        super(msg, ex);
    }

    public SLF4JLoggingException(Exception ex) {
        super(ex);
    }
}

