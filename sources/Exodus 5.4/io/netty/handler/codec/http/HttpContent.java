/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.buffer.ByteBufHolder;
import io.netty.handler.codec.http.HttpObject;

public interface HttpContent
extends ByteBufHolder,
HttpObject {
    @Override
    public HttpContent retain();

    @Override
    public HttpContent retain(int var1);

    @Override
    public HttpContent duplicate();

    @Override
    public HttpContent copy();
}

