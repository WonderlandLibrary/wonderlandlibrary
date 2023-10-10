// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.hash;

import java.util.Arrays;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.Immutable;

@Immutable
@ElementTypesAreNonnullByDefault
abstract class AbstractNonStreamingHashFunction extends AbstractHashFunction
{
    @Override
    public Hasher newHasher() {
        return this.newHasher(32);
    }
    
    @Override
    public Hasher newHasher(final int expectedInputSize) {
        Preconditions.checkArgument(expectedInputSize >= 0);
        return new BufferingHasher(expectedInputSize);
    }
    
    @Override
    public HashCode hashInt(final int input) {
        return this.hashBytes(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(input).array());
    }
    
    @Override
    public HashCode hashLong(final long input) {
        return this.hashBytes(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(input).array());
    }
    
    @Override
    public HashCode hashUnencodedChars(final CharSequence input) {
        final int len = input.length();
        final ByteBuffer buffer = ByteBuffer.allocate(len * 2).order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < len; ++i) {
            buffer.putChar(input.charAt(i));
        }
        return this.hashBytes(buffer.array());
    }
    
    @Override
    public HashCode hashString(final CharSequence input, final Charset charset) {
        return this.hashBytes(input.toString().getBytes(charset));
    }
    
    @Override
    public abstract HashCode hashBytes(final byte[] p0, final int p1, final int p2);
    
    @Override
    public HashCode hashBytes(final ByteBuffer input) {
        return this.newHasher(input.remaining()).putBytes(input).hash();
    }
    
    private final class BufferingHasher extends AbstractHasher
    {
        final ExposedByteArrayOutputStream stream;
        
        BufferingHasher(final int expectedInputSize) {
            this.stream = new ExposedByteArrayOutputStream(expectedInputSize);
        }
        
        @Override
        public Hasher putByte(final byte b) {
            this.stream.write(b);
            return this;
        }
        
        @Override
        public Hasher putBytes(final byte[] bytes, final int off, final int len) {
            this.stream.write(bytes, off, len);
            return this;
        }
        
        @Override
        public Hasher putBytes(final ByteBuffer bytes) {
            this.stream.write(bytes);
            return this;
        }
        
        @Override
        public HashCode hash() {
            return AbstractNonStreamingHashFunction.this.hashBytes(this.stream.byteArray(), 0, this.stream.length());
        }
    }
    
    private static final class ExposedByteArrayOutputStream extends ByteArrayOutputStream
    {
        ExposedByteArrayOutputStream(final int expectedInputSize) {
            super(expectedInputSize);
        }
        
        void write(final ByteBuffer input) {
            final int remaining = input.remaining();
            if (this.count + remaining > this.buf.length) {
                this.buf = Arrays.copyOf(this.buf, this.count + remaining);
            }
            input.get(this.buf, this.count, remaining);
            this.count += remaining;
        }
        
        byte[] byteArray() {
            return this.buf;
        }
        
        int length() {
            return this.count;
        }
    }
}
