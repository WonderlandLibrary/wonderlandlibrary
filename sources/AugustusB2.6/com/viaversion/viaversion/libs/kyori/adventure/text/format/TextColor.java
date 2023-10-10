// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.format;

import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import com.viaversion.viaversion.libs.kyori.adventure.util.HSVLike;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.adventure.util.RGBLike;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;

public interface TextColor extends Comparable<TextColor>, Examinable, RGBLike, StyleBuilderApplicable, TextFormat
{
    @NotNull
    default TextColor color(final int value) {
        final int truncatedValue = value & 0xFFFFFF;
        final NamedTextColor named = NamedTextColor.ofExact(truncatedValue);
        return (named != null) ? named : new TextColorImpl(truncatedValue);
    }
    
    @NotNull
    default TextColor color(@NotNull final RGBLike rgb) {
        if (rgb instanceof TextColor) {
            return (TextColor)rgb;
        }
        return color(rgb.red(), rgb.green(), rgb.blue());
    }
    
    @NotNull
    default TextColor color(@NotNull final HSVLike hsv) {
        final float s = hsv.s();
        final float v = hsv.v();
        if (s == 0.0f) {
            return color(v, v, v);
        }
        final float h = hsv.h() * 6.0f;
        final int i = (int)Math.floor(h);
        final float f = h - i;
        final float p = v * (1.0f - s);
        final float q = v * (1.0f - s * f);
        final float t = v * (1.0f - s * (1.0f - f));
        if (i == 0) {
            return color(v, t, p);
        }
        if (i == 1) {
            return color(q, v, p);
        }
        if (i == 2) {
            return color(p, v, t);
        }
        if (i == 3) {
            return color(p, q, v);
        }
        if (i == 4) {
            return color(t, p, v);
        }
        return color(v, p, q);
    }
    
    @NotNull
    default TextColor color(final int r, final int g, final int b) {
        return color((r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF));
    }
    
    @NotNull
    default TextColor color(final float r, final float g, final float b) {
        return color((int)(r * 255.0f), (int)(g * 255.0f), (int)(b * 255.0f));
    }
    
    @Nullable
    default TextColor fromHexString(@NotNull final String string) {
        if (string.startsWith("#")) {
            try {
                final int hex = Integer.parseInt(string.substring(1), 16);
                return color(hex);
            }
            catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    
    @Nullable
    default TextColor fromCSSHexString(@NotNull final String string) {
        if (!string.startsWith("#")) {
            return null;
        }
        final String hexString = string.substring(1);
        if (hexString.length() != 3 && hexString.length() != 6) {
            return null;
        }
        int hex;
        try {
            hex = Integer.parseInt(hexString, 16);
        }
        catch (NumberFormatException e) {
            return null;
        }
        if (hexString.length() == 6) {
            return color(hex);
        }
        final int red = (hex & 0xF00) >> 8 | (hex & 0xF00) >> 4;
        final int green = (hex & 0xF0) >> 4 | (hex & 0xF0);
        final int blue = (hex & 0xF) << 4 | (hex & 0xF);
        return color(red, green, blue);
    }
    
    int value();
    
    @NotNull
    default String asHexString() {
        return String.format("#%06x", this.value());
    }
    
    default int red() {
        return this.value() >> 16 & 0xFF;
    }
    
    default int green() {
        return this.value() >> 8 & 0xFF;
    }
    
    default int blue() {
        return this.value() & 0xFF;
    }
    
    @NotNull
    default TextColor lerp(final float t, @NotNull final RGBLike a, @NotNull final RGBLike b) {
        final float clampedT = Math.min(1.0f, Math.max(0.0f, t));
        final int ar = a.red();
        final int br = b.red();
        final int ag = a.green();
        final int bg = b.green();
        final int ab = a.blue();
        final int bb = b.blue();
        return color(Math.round(ar + clampedT * (br - ar)), Math.round(ag + clampedT * (bg - ag)), Math.round(ab + clampedT * (bb - ab)));
    }
    
    default void styleApply(final Style.Builder style) {
        style.color(this);
    }
    
    default int compareTo(final TextColor that) {
        return Integer.compare(this.value(), that.value());
    }
    
    @NotNull
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("value", this.asHexString()));
    }
}
