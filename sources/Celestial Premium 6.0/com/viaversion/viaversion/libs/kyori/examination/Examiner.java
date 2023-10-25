/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.NonNull
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.viaversion.viaversion.libs.kyori.examination;

import com.viaversion.viaversion.libs.kyori.examination.Examinable;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface Examiner<R> {
    default public @NonNull R examine(@NonNull Examinable examinable) {
        return this.examine(examinable.examinableName(), examinable.examinableProperties());
    }

    public @NonNull R examine(@NonNull String var1, @NonNull Stream<? extends ExaminableProperty> var2);

    public @NonNull R examine(@Nullable Object var1);

    public @NonNull R examine(boolean var1);

    public @NonNull R examine(boolean @Nullable [] var1);

    public @NonNull R examine(byte var1);

    public @NonNull R examine(byte @Nullable [] var1);

    public @NonNull R examine(char var1);

    public @NonNull R examine(char @Nullable [] var1);

    public @NonNull R examine(double var1);

    public @NonNull R examine(double @Nullable [] var1);

    public @NonNull R examine(float var1);

    public @NonNull R examine(float @Nullable [] var1);

    public @NonNull R examine(int var1);

    public @NonNull R examine(int @Nullable [] var1);

    public @NonNull R examine(long var1);

    public @NonNull R examine(long @Nullable [] var1);

    public @NonNull R examine(short var1);

    public @NonNull R examine(short @Nullable [] var1);

    public @NonNull R examine(@Nullable String var1);
}

