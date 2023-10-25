/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.NonNull
 */
package com.viaversion.viaversion.libs.kyori.examination;

import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import com.viaversion.viaversion.libs.kyori.examination.Examiner;
import java.util.stream.Stream;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Examinable {
    default public @NonNull String examinableName() {
        return this.getClass().getSimpleName();
    }

    default public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.empty();
    }

    default public <R> @NonNull R examine(@NonNull Examiner<R> examiner) {
        return examiner.examine(this);
    }
}

