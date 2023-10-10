// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.util;

import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;

public interface HSVLike extends Examinable
{
    @NotNull
    default HSVLike of(final float h, final float s, final float v) {
        return new HSVLikeImpl(h, s, v);
    }
    
    @NotNull
    default HSVLike fromRGB(final int red, final int green, final int blue) {
        final float r = red / 255.0f;
        final float g = green / 255.0f;
        final float b = blue / 255.0f;
        final float min = Math.min(r, Math.min(g, b));
        final float max = Math.max(r, Math.max(g, b));
        final float delta = max - min;
        float s;
        if (max != 0.0f) {
            s = delta / max;
        }
        else {
            s = 0.0f;
        }
        if (s == 0.0f) {
            return new HSVLikeImpl(0.0f, s, max);
        }
        float h;
        if (r == max) {
            h = (g - b) / delta;
        }
        else if (g == max) {
            h = 2.0f + (b - r) / delta;
        }
        else {
            h = 4.0f + (r - g) / delta;
        }
        h *= 60.0f;
        if (h < 0.0f) {
            h += 360.0f;
        }
        return new HSVLikeImpl(h / 360.0f, s, max);
    }
    
    float h();
    
    float s();
    
    float v();
    
    @NotNull
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of((ExaminableProperty[])new ExaminableProperty[] { ExaminableProperty.of("h", this.h()), ExaminableProperty.of("s", this.s()), ExaminableProperty.of("v", this.v()) });
    }
}
