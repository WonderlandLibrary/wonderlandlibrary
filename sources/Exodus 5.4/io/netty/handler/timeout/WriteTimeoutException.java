/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.timeout;

import io.netty.handler.timeout.TimeoutException;

public final class WriteTimeoutException
extends TimeoutException {
    public static final WriteTimeoutException INSTANCE = new WriteTimeoutException();
    private static final long serialVersionUID = -144786655770296065L;

    private WriteTimeoutException() {
    }
}

