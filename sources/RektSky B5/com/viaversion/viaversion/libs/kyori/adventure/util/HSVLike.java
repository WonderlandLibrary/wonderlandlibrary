/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Range
 */
package com.viaversion.viaversion.libs.kyori.adventure.util;

import com.viaversion.viaversion.libs.kyori.adventure.util.HSVLikeImpl;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public interface HSVLike
extends Examinable {
    @NotNull
    public static HSVLike of(float h2, float s2, float v2) {
        return new HSVLikeImpl(h2, s2, v2);
    }

    @NotNull
    public static HSVLike fromRGB(@Range(from=0L, to=255L) int red, @Range(from=0L, to=255L) int green, @Range(from=0L, to=255L) int blue) {
        float r2 = (float)red / 255.0f;
        float g2 = (float)green / 255.0f;
        float b2 = (float)blue / 255.0f;
        float min = Math.min(r2, Math.min(g2, b2));
        float max = Math.max(r2, Math.max(g2, b2));
        float delta = max - min;
        float s2 = max != 0.0f ? delta / max : 0.0f;
        if (s2 == 0.0f) {
            return new HSVLikeImpl(0.0f, s2, max);
        }
        float h2 = r2 == max ? (g2 - b2) / delta : (g2 == max ? 2.0f + (b2 - r2) / delta : 4.0f + (r2 - g2) / delta);
        if ((h2 *= 60.0f) < 0.0f) {
            h2 += 360.0f;
        }
        return new HSVLikeImpl(h2 / 360.0f, s2, max);
    }

    public float h();

    public float s();

    public float v();

    @Override
    @NotNull
    default public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("h", this.h()), ExaminableProperty.of("s", this.s()), ExaminableProperty.of("v", this.v()));
    }
}

