// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.hash;

import java.nio.charset.Charset;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;

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
}
