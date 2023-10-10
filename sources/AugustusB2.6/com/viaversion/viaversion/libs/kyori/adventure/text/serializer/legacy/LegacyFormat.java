// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.legacy;

import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.Objects;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextColor;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.Nullable;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.NamedTextColor;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;

public final class LegacyFormat implements Examinable
{
    static final LegacyFormat RESET;
    @Nullable
    private final NamedTextColor color;
    @Nullable
    private final TextDecoration decoration;
    private final boolean reset;
    
    LegacyFormat(@Nullable final NamedTextColor color) {
        this.color = color;
        this.decoration = null;
        this.reset = false;
    }
    
    LegacyFormat(@Nullable final TextDecoration decoration) {
        this.color = null;
        this.decoration = decoration;
        this.reset = false;
    }
    
    private LegacyFormat(final boolean reset) {
        this.color = null;
        this.decoration = null;
        this.reset = reset;
    }
    
    @Nullable
    public TextColor color() {
        return this.color;
    }
    
    @Nullable
    public TextDecoration decoration() {
        return this.decoration;
    }
    
    public boolean reset() {
        return this.reset;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        final LegacyFormat that = (LegacyFormat)other;
        return this.color == that.color && this.decoration == that.decoration && this.reset == that.reset;
    }
    
    @Override
    public int hashCode() {
        int result = Objects.hashCode(this.color);
        result = 31 * result + Objects.hashCode(this.decoration);
        result = 31 * result + Boolean.hashCode(this.reset);
        return result;
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of((ExaminableProperty[])new ExaminableProperty[] { ExaminableProperty.of("color", this.color), ExaminableProperty.of("decoration", this.decoration), ExaminableProperty.of("reset", this.reset) });
    }
    
    static {
        RESET = new LegacyFormat(true);
    }
}
