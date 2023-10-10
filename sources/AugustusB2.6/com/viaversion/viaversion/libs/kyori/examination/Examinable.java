// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.examination;

import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public interface Examinable
{
    @NotNull
    default String examinableName() {
        return this.getClass().getSimpleName();
    }
    
    @NotNull
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.empty();
    }
    
    @NotNull
    default <R> R examine(@NotNull final Examiner<R> examiner) {
        return examiner.examine(this);
    }
}
