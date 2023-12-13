/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

@Beta
public final class HashingInputStream
extends FilterInputStream {
    private final Hasher hasher;

    public HashingInputStream(HashFunction hashFunction, InputStream in) {
        super(Preconditions.checkNotNull(in));
        this.hasher = Preconditions.checkNotNull(hashFunction.newHasher());
    }

    @Override
    public int read() throws IOException {
        int b2 = this.in.read();
        if (b2 != -1) {
            this.hasher.putByte((byte)b2);
        }
        return b2;
    }

    @Override
    public int read(byte[] bytes, int off, int len) throws IOException {
        int numOfBytesRead = this.in.read(bytes, off, len);
        if (numOfBytesRead != -1) {
            this.hasher.putBytes(bytes, off, numOfBytesRead);
        }
        return numOfBytesRead;
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public void mark(int readlimit) {
    }

    @Override
    public void reset() throws IOException {
        throw new IOException("reset not supported");
    }

    public HashCode hash() {
        return this.hasher.hash();
    }
}

