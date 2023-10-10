/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.io;

import com.google.common.annotations.Beta;
import java.io.IOException;

@Beta
public interface ByteProcessor<T> {
    public boolean processBytes(byte[] var1, int var2, int var3) throws IOException;

    public T getResult();
}

