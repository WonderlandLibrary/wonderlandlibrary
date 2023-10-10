// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.examination;

import org.jetbrains.annotations.Nullable;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public interface Examiner<R>
{
    @NotNull
    default R examine(@NotNull final Examinable examinable) {
        return this.examine(examinable.examinableName(), examinable.examinableProperties());
    }
    
    @NotNull
    R examine(@NotNull final String name, @NotNull final Stream<? extends ExaminableProperty> properties);
    
    @NotNull
    R examine(@Nullable final Object value);
    
    @NotNull
    R examine(final boolean value);
    
    @NotNull
    R examine(final boolean[] values);
    
    @NotNull
    R examine(final byte value);
    
    @NotNull
    R examine(final byte[] values);
    
    @NotNull
    R examine(final char value);
    
    @NotNull
    R examine(final char[] values);
    
    @NotNull
    R examine(final double value);
    
    @NotNull
    R examine(final double[] values);
    
    @NotNull
    R examine(final float value);
    
    @NotNull
    R examine(final float[] values);
    
    @NotNull
    R examine(final int value);
    
    @NotNull
    R examine(final int[] values);
    
    @NotNull
    R examine(final long value);
    
    @NotNull
    R examine(final long[] values);
    
    @NotNull
    R examine(final short value);
    
    @NotNull
    R examine(final short[] values);
    
    @NotNull
    R examine(@Nullable final String value);
}
