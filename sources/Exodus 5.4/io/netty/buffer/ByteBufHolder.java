/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCounted;

public interface ByteBufHolder
extends ReferenceCounted {
    public ByteBufHolder duplicate();

    public ByteBuf content();

    public ByteBufHolder copy();

    @Override
    public ByteBufHolder retain(int var1);

    @Override
    public ByteBufHolder retain();
}

