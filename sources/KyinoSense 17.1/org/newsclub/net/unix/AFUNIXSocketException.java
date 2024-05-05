/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.net.SocketException;

public class AFUNIXSocketException
extends SocketException {
    private static final long serialVersionUID = 1L;
    private final String socketFile;

    public AFUNIXSocketException(String reason) {
        this(reason, (String)null);
    }

    public AFUNIXSocketException(String reason, Throwable cause) {
        this(reason, (String)null);
        this.initCause(cause);
    }

    public AFUNIXSocketException(String reason, String socketFile) {
        super(reason);
        this.socketFile = socketFile;
    }

    @Override
    public String toString() {
        if (this.socketFile == null) {
            return super.toString();
        }
        return super.toString() + " (socket: " + this.socketFile + ")";
    }
}

