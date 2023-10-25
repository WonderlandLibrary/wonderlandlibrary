/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.parser;

public class ParseException
extends Exception {
    private static final long serialVersionUID = -2739649998196663857L;

    public ParseException(String message) {
        super(message);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParseException(Throwable cause) {
        super(cause);
    }
}

