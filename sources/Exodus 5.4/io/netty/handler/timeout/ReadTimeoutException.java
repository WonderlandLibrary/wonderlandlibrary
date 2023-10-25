/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.timeout;

import io.netty.handler.timeout.TimeoutException;

public final class ReadTimeoutException
extends TimeoutException {
    public static final ReadTimeoutException INSTANCE = new ReadTimeoutException();
    private static final long serialVersionUID = 169287984113283421L;

    private ReadTimeoutException() {
    }
}

