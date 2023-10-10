// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.viaversion.viaversion.libs.gson.stream.JsonReader;
import java.io.IOException;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.NamedTextColor;
import com.viaversion.viaversion.libs.gson.stream.JsonWriter;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextColor;
import com.viaversion.viaversion.libs.gson.TypeAdapter;

final class TextColorSerializer extends TypeAdapter<TextColor>
{
    static final TypeAdapter<TextColor> INSTANCE;
    static final TypeAdapter<TextColor> DOWNSAMPLE_COLOR;
    private final boolean downsampleColor;
    
    private TextColorSerializer(final boolean downsampleColor) {
        this.downsampleColor = downsampleColor;
    }
    
    @Override
    public void write(final JsonWriter out, final TextColor value) throws IOException {
        if (value instanceof NamedTextColor) {
            out.value(NamedTextColor.NAMES.key((NamedTextColor)value));
        }
        else if (this.downsampleColor) {
            out.value(NamedTextColor.NAMES.key(NamedTextColor.nearestTo(value)));
        }
        else {
            out.value(value.asHexString());
        }
    }
    
    @Nullable
    @Override
    public TextColor read(final JsonReader in) throws IOException {
        final TextColor color = fromString(in.nextString());
        if (color == null) {
            return null;
        }
        return this.downsampleColor ? NamedTextColor.nearestTo(color) : color;
    }
    
    @Nullable
    static TextColor fromString(@NotNull final String value) {
        if (value.startsWith("#")) {
            return TextColor.fromHexString(value);
        }
        return NamedTextColor.NAMES.value(value);
    }
    
    static {
        INSTANCE = new TextColorSerializer(false).nullSafe();
        DOWNSAMPLE_COLOR = new TextColorSerializer(true).nullSafe();
    }
}
