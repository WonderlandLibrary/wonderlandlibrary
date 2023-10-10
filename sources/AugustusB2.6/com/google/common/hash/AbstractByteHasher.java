// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.nio.Buffer;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

@ElementTypesAreNonnullByDefault
@CanIgnoreReturnValue
abstract class AbstractByteHasher extends AbstractHasher
{
    private final ByteBuffer scratch;
    
    AbstractByteHasher() {
        this.scratch = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
    }
    
    protected abstract void update(final byte p0);
    
    protected void update(final byte[] b) {
        this.update(b, 0, b.length);
    }
    
    protected void update(final byte[] b, final int off, final int len) {
        for (int i = off; i < off + len; ++i) {
            this.update(b[i]);
        }
    }
    
    protected void update(final ByteBuffer b) {
        if (b.hasArray()) {
            this.update(b.array(), b.arrayOffset() + b.position(), b.remaining());
            Java8Compatibility.position(b, b.limit());
        }
        else {
            for (int remaining = b.remaining(); remaining > 0; --remaining) {
                this.update(b.get());
            }
        }
    }
    
    private Hasher update(final int bytes) {
        try {
            this.update(this.scratch.array(), 0, bytes);
        }
        finally {
            Java8Compatibility.clear(this.scratch);
        }
        return this;
    }
    
    @Override
    public Hasher putByte(final byte b) {
        this.update(b);
        return this;
    }
    
    @Override
    public Hasher putBytes(final byte[] bytes) {
        Preconditions.checkNotNull(bytes);
        this.update(bytes);
        return this;
    }
    
    @Override
    public Hasher putBytes(final byte[] bytes, final int off, final int len) {
        Preconditions.checkPositionIndexes(off, off + len, bytes.length);
        this.update(bytes, off, len);
        return this;
    }
    
    @Override
    public Hasher putBytes(final ByteBuffer bytes) {
        this.update(bytes);
        return this;
    }
    
    @Override
    public Hasher putShort(final short s) {
        this.scratch.putShort(s);
        return this.update(2);
    }
    
    @Override
    public Hasher putInt(final int i) {
        this.scratch.putInt(i);
        return this.update(4);
    }
    
    @Override
    public Hasher putLong(final long l) {
        this.scratch.putLong(l);
        return this.update(8);
    }
    
    @Override
    public Hasher putChar(final char c) {
        this.scratch.putChar(c);
        return this.update(2);
    }
}
