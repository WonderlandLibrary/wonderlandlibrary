// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.io;

import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
@GwtIncompatible
public interface ByteProcessor<T>
{
    @CanIgnoreReturnValue
    boolean processBytes(final byte[] p0, final int p1, final int p2) throws IOException;
    
    T getResult();
}
