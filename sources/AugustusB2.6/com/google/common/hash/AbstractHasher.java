// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.hash;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import com.google.common.base.Preconditions;
import java.nio.charset.Charset;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

@ElementTypesAreNonnullByDefault
@CanIgnoreReturnValue
abstract class AbstractHasher implements Hasher
{
    @Override
    public final Hasher putBoolean(final boolean b) {
        return this.putByte((byte)(byte)(b ? 1 : 0));
    }
    
    @Override
    public final Hasher putDouble(final double d) {
        return this.putLong(Double.doubleToRawLongBits(d));
    }
    
    @Override
    public final Hasher putFloat(final float f) {
        return this.putInt(Float.floatToRawIntBits(f));
    }
    
    @Override
    public Hasher putUnencodedChars(final CharSequence charSequence) {
        for (int i = 0, len = charSequence.length(); i < len; ++i) {
            this.putChar(charSequence.charAt(i));
        }
        return this;
    }
    
    @Override
    public Hasher putString(final CharSequence charSequence, final Charset charset) {
        return this.putBytes(charSequence.toString().getBytes(charset));
    }
    
    @Override
    public Hasher putBytes(final byte[] bytes) {
        return this.putBytes(bytes, 0, bytes.length);
    }
    
    @Override
    public Hasher putBytes(final byte[] bytes, final int off, final int len) {
        Preconditions.checkPositionIndexes(off, off + len, bytes.length);
        for (int i = 0; i < len; ++i) {
            this.putByte(bytes[off + i]);
        }
        return this;
    }
    
    @Override
    public Hasher putBytes(final ByteBuffer b) {
        if (b.hasArray()) {
            this.putBytes(b.array(), b.arrayOffset() + b.position(), b.remaining());
            Java8Compatibility.position(b, b.limit());
        }
        else {
            for (int remaining = b.remaining(); remaining > 0; --remaining) {
                this.putByte(b.get());
            }
        }
        return this;
    }
    
    @Override
    public Hasher putShort(final short s) {
        this.putByte((byte)s);
        this.putByte((byte)(s >>> 8));
        return this;
    }
    
    @Override
    public Hasher putInt(final int i) {
        this.putByte((byte)i);
        this.putByte((byte)(i >>> 8));
        this.putByte((byte)(i >>> 16));
        this.putByte((byte)(i >>> 24));
        return this;
    }
    
    @Override
    public Hasher putLong(final long l) {
        for (int i = 0; i < 64; i += 8) {
            this.putByte((byte)(l >>> i));
        }
        return this;
    }
    
    @Override
    public Hasher putChar(final char c) {
        this.putByte((byte)c);
        this.putByte((byte)(c >>> 8));
        return this;
    }
    
    @Override
    public <T> Hasher putObject(@ParametricNullness final T instance, final Funnel<? super T> funnel) {
        funnel.funnel((Object)instance, (PrimitiveSink)this);
        return this;
    }
}
