/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.LastHttpContent;

public interface FullHttpMessage
extends LastHttpContent,
HttpMessage {
    @Override
    public FullHttpMessage retain();

    @Override
    public FullHttpMessage copy();

    @Override
    public FullHttpMessage retain(int var1);
}

