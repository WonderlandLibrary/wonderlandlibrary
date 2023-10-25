/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.ssl;

import javax.net.ssl.SSLException;

public class NotSslRecordException
extends SSLException {
    private static final long serialVersionUID = -4316784434770656841L;

    public NotSslRecordException(String string) {
        super(string);
    }

    public NotSslRecordException() {
        super("");
    }

    public NotSslRecordException(Throwable throwable) {
        super(throwable);
    }

    public NotSslRecordException(String string, Throwable throwable) {
        super(string, throwable);
    }
}

