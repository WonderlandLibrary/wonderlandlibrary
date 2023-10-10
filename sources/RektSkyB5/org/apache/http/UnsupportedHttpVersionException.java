/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http;

import org.apache.http.ProtocolException;

public class UnsupportedHttpVersionException
extends ProtocolException {
    private static final long serialVersionUID = -1348448090193107031L;

    public UnsupportedHttpVersionException() {
    }

    public UnsupportedHttpVersionException(String message) {
        super(message);
    }
}

