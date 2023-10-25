/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.NonNull
 *  org.jetbrains.annotations.ApiStatus$NonExtendable
 */
package com.viaversion.viaversion.libs.kyori.adventure.text.format;

import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.StyleBuilderApplicable;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextDecoration;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface TextDecorationAndState
extends Examinable,
StyleBuilderApplicable {
    public @NonNull TextDecoration decoration();

    public @NonNull TextDecoration.State state();

    @Override
    default public void styleApply(@NonNull Style.Builder style) {
        style.decoration(this.decoration(), this.state());
    }

    @Override
    default public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("decoration", this.decoration()), ExaminableProperty.of("state", (Object)this.state()));
    }
}

