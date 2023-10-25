/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util;

public interface ReferenceCounted {
    public ReferenceCounted retain();

    public ReferenceCounted retain(int var1);

    public boolean release(int var1);

    public boolean release();

    public int refCnt();
}

