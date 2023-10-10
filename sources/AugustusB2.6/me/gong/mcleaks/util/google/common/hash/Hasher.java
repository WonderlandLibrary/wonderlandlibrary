// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.hash;

import java.nio.charset.Charset;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
@CanIgnoreReturnValue
public interface Hasher extends PrimitiveSink
{
    Hasher putByte(final byte p0);
    
    Hasher putBytes(final byte[] p0);
    
    Hasher putBytes(final byte[] p0, final int p1, final int p2);
    
    Hasher putShort(final short p0);
    
    Hasher putInt(final int p0);
    
    Hasher putLong(final long p0);
    
    Hasher putFloat(final float p0);
    
    Hasher putDouble(final double p0);
    
    Hasher putBoolean(final boolean p0);
    
    Hasher putChar(final char p0);
    
    Hasher putUnencodedChars(final CharSequence p0);
    
    Hasher putString(final CharSequence p0, final Charset p1);
    
     <T> Hasher putObject(final T p0, final Funnel<? super T> p1);
    
    HashCode hash();
    
    @Deprecated
    int hashCode();
}
