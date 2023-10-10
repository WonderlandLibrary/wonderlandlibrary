// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.format;

import java.util.Collections;
import java.util.Arrays;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.viaversion.viaversion.libs.kyori.adventure.util.HSVLike;
import com.viaversion.viaversion.libs.kyori.adventure.util.Index;
import java.util.List;

public final class NamedTextColor implements TextColor
{
    private static final int BLACK_VALUE = 0;
    private static final int DARK_BLUE_VALUE = 170;
    private static final int DARK_GREEN_VALUE = 43520;
    private static final int DARK_AQUA_VALUE = 43690;
    private static final int DARK_RED_VALUE = 11141120;
    private static final int DARK_PURPLE_VALUE = 11141290;
    private static final int GOLD_VALUE = 16755200;
    private static final int GRAY_VALUE = 11184810;
    private static final int DARK_GRAY_VALUE = 5592405;
    private static final int BLUE_VALUE = 5592575;
    private static final int GREEN_VALUE = 5635925;
    private static final int AQUA_VALUE = 5636095;
    private static final int RED_VALUE = 16733525;
    private static final int LIGHT_PURPLE_VALUE = 16733695;
    private static final int YELLOW_VALUE = 16777045;
    private static final int WHITE_VALUE = 16777215;
    public static final NamedTextColor BLACK;
    public static final NamedTextColor DARK_BLUE;
    public static final NamedTextColor DARK_GREEN;
    public static final NamedTextColor DARK_AQUA;
    public static final NamedTextColor DARK_RED;
    public static final NamedTextColor DARK_PURPLE;
    public static final NamedTextColor GOLD;
    public static final NamedTextColor GRAY;
    public static final NamedTextColor DARK_GRAY;
    public static final NamedTextColor BLUE;
    public static final NamedTextColor GREEN;
    public static final NamedTextColor AQUA;
    public static final NamedTextColor RED;
    public static final NamedTextColor LIGHT_PURPLE;
    public static final NamedTextColor YELLOW;
    public static final NamedTextColor WHITE;
    private static final List<NamedTextColor> VALUES;
    public static final Index<String, NamedTextColor> NAMES;
    private final String name;
    private final int value;
    private final HSVLike hsv;
    
    @Nullable
    public static NamedTextColor ofExact(final int value) {
        if (value == 0) {
            return NamedTextColor.BLACK;
        }
        if (value == 170) {
            return NamedTextColor.DARK_BLUE;
        }
        if (value == 43520) {
            return NamedTextColor.DARK_GREEN;
        }
        if (value == 43690) {
            return NamedTextColor.DARK_AQUA;
        }
        if (value == 11141120) {
            return NamedTextColor.DARK_RED;
        }
        if (value == 11141290) {
            return NamedTextColor.DARK_PURPLE;
        }
        if (value == 16755200) {
            return NamedTextColor.GOLD;
        }
        if (value == 11184810) {
            return NamedTextColor.GRAY;
        }
        if (value == 5592405) {
            return NamedTextColor.DARK_GRAY;
        }
        if (value == 5592575) {
            return NamedTextColor.BLUE;
        }
        if (value == 5635925) {
            return NamedTextColor.GREEN;
        }
        if (value == 5636095) {
            return NamedTextColor.AQUA;
        }
        if (value == 16733525) {
            return NamedTextColor.RED;
        }
        if (value == 16733695) {
            return NamedTextColor.LIGHT_PURPLE;
        }
        if (value == 16777045) {
            return NamedTextColor.YELLOW;
        }
        if (value == 16777215) {
            return NamedTextColor.WHITE;
        }
        return null;
    }
    
    @NotNull
    public static NamedTextColor nearestTo(@NotNull final TextColor any) {
        if (any instanceof NamedTextColor) {
            return (NamedTextColor)any;
        }
        Objects.requireNonNull(any, "color");
        float matchedDistance = Float.MAX_VALUE;
        NamedTextColor match = NamedTextColor.VALUES.get(0);
        for (int i = 0, length = NamedTextColor.VALUES.size(); i < length; ++i) {
            final NamedTextColor potential = NamedTextColor.VALUES.get(i);
            final float distance = distance(any.asHSV(), potential.asHSV());
            if (distance < matchedDistance) {
                match = potential;
                matchedDistance = distance;
            }
            if (distance == 0.0f) {
                break;
            }
        }
        return match;
    }
    
    private static float distance(@NotNull final HSVLike self, @NotNull final HSVLike other) {
        final float hueDistance = 3.0f * Math.min(Math.abs(self.h() - other.h()), 1.0f - Math.abs(self.h() - other.h()));
        final float saturationDiff = self.s() - other.s();
        final float valueDiff = self.v() - other.v();
        return hueDistance * hueDistance + saturationDiff * saturationDiff + valueDiff * valueDiff;
    }
    
    private NamedTextColor(final String name, final int value) {
        this.name = name;
        this.value = value;
        this.hsv = HSVLike.fromRGB(this.red(), this.green(), this.blue());
    }
    
    @Override
    public int value() {
        return this.value;
    }
    
    @NotNull
    @Override
    public HSVLike asHSV() {
        return this.hsv;
    }
    
    @NotNull
    @Override
    public String toString() {
        return this.name;
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.concat((Stream<? extends ExaminableProperty>)Stream.of((T)ExaminableProperty.of("name", this.name)), super.examinableProperties());
    }
    
    static {
        BLACK = new NamedTextColor("black", 0);
        DARK_BLUE = new NamedTextColor("dark_blue", 170);
        DARK_GREEN = new NamedTextColor("dark_green", 43520);
        DARK_AQUA = new NamedTextColor("dark_aqua", 43690);
        DARK_RED = new NamedTextColor("dark_red", 11141120);
        DARK_PURPLE = new NamedTextColor("dark_purple", 11141290);
        GOLD = new NamedTextColor("gold", 16755200);
        GRAY = new NamedTextColor("gray", 11184810);
        DARK_GRAY = new NamedTextColor("dark_gray", 5592405);
        BLUE = new NamedTextColor("blue", 5592575);
        GREEN = new NamedTextColor("green", 5635925);
        AQUA = new NamedTextColor("aqua", 5636095);
        RED = new NamedTextColor("red", 16733525);
        LIGHT_PURPLE = new NamedTextColor("light_purple", 16733695);
        YELLOW = new NamedTextColor("yellow", 16777045);
        WHITE = new NamedTextColor("white", 16777215);
        VALUES = Collections.unmodifiableList((List<? extends NamedTextColor>)Arrays.asList(NamedTextColor.BLACK, NamedTextColor.DARK_BLUE, NamedTextColor.DARK_GREEN, NamedTextColor.DARK_AQUA, NamedTextColor.DARK_RED, NamedTextColor.DARK_PURPLE, NamedTextColor.GOLD, NamedTextColor.GRAY, NamedTextColor.DARK_GRAY, NamedTextColor.BLUE, NamedTextColor.GREEN, NamedTextColor.AQUA, NamedTextColor.RED, NamedTextColor.LIGHT_PURPLE, NamedTextColor.YELLOW, NamedTextColor.WHITE));
        NAMES = Index.create(constant -> constant.name, NamedTextColor.VALUES);
    }
}
