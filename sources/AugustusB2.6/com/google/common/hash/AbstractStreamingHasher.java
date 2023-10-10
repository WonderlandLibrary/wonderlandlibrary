// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.hash;

import java.nio.Buffer;
import java.nio.ByteOrder;
import com.google.common.base.Preconditions;
import java.nio.ByteBuffer;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

@ElementTypesAreNonnullByDefault
@CanIgnoreReturnValue
abstract class AbstractStreamingHasher extends AbstractHasher
{
    private final ByteBuffer buffer;
    private final int bufferSize;
    private final int chunkSize;
    
    protected AbstractStreamingHasher(final int chunkSize) {
        this(chunkSize, chunkSize);
    }
    
    protected AbstractStreamingHasher(final int chunkSize, final int bufferSize) {
        Preconditions.checkArgument(bufferSize % chunkSize == 0);
        this.buffer = ByteBuffer.allocate(bufferSize + 7).order(ByteOrder.LITTLE_ENDIAN);
        this.bufferSize = bufferSize;
        this.chunkSize = chunkSize;
    }
    
    protected abstract void process(final ByteBuffer p0);
    
    protected void processRemaining(final ByteBuffer bb) {
        Java8Compatibility.position(bb, bb.limit());
        Java8Compatibility.limit(bb, this.chunkSize + 7);
        while (bb.position() < this.chunkSize) {
            bb.putLong(0L);
        }
        Java8Compatibility.limit(bb, this.chunkSize);
        Java8Compatibility.flip(bb);
        this.process(bb);
    }
    
    @Override
    public final Hasher putBytes(final byte[] bytes, final int off, final int len) {
        return this.putBytesInternal(ByteBuffer.wrap(bytes, off, len).order(ByteOrder.LITTLE_ENDIAN));
    }
    
    @Override
    public final Hasher putBytes(final ByteBuffer readBuffer) {
        final ByteOrder order = readBuffer.order();
        try {
            readBuffer.order(ByteOrder.LITTLE_ENDIAN);
            return this.putBytesInternal(readBuffer);
        }
        finally {
            readBuffer.order(order);
        }
    }
    
    private Hasher putBytesInternal(final ByteBuffer readBuffer) {
        if (readBuffer.remaining() <= this.buffer.remaining()) {
            this.buffer.put(readBuffer);
            this.munchIfFull();
            return this;
        }
        for (int bytesToCopy = this.bufferSize - this.buffer.position(), i = 0; i < bytesToCopy; ++i) {
            this.buffer.put(readBuffer.get());
        }
        this.munch();
        while (readBuffer.remaining() >= this.chunkSize) {
            this.process(readBuffer);
        }
        this.buffer.put(readBuffer);
        return this;
    }
    
    @Override
    public final Hasher putByte(final byte b) {
        this.buffer.put(b);
        this.munchIfFull();
        return this;
    }
    
    @Override
    public final Hasher putShort(final short s) {
        this.buffer.putShort(s);
        this.munchIfFull();
        return this;
    }
    
    @Override
    public final Hasher putChar(final char c) {
        this.buffer.putChar(c);
        this.munchIfFull();
        return this;
    }
    
    @Override
    public final Hasher putInt(final int i) {
        this.buffer.putInt(i);
        this.munchIfFull();
        return this;
    }
    
    @Override
    public final Hasher putLong(final long l) {
        this.buffer.putLong(l);
        this.munchIfFull();
        return this;
    }
    
    @Override
    public final HashCode hash() {
        this.munch();
        Java8Compatibility.flip(this.buffer);
        if (this.buffer.remaining() > 0) {
            this.processRemaining(this.buffer);
            Java8Compatibility.position(this.buffer, this.buffer.limit());
        }
        return this.makeHash();
    }
    
    protected abstract HashCode makeHash();
    
    private void munchIfFull() {
        if (this.buffer.remaining() < 8) {
            this.munch();
        }
    }
    
    private void munch() {
        Java8Compatibility.flip(this.buffer);
        while (this.buffer.remaining() >= this.chunkSize) {
            this.process(this.buffer);
        }
        this.buffer.compact();
    }
}
