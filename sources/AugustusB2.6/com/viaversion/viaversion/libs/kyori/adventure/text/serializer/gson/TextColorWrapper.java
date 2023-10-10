// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson;

import java.io.IOException;
import com.viaversion.viaversion.libs.gson.JsonParseException;
import com.viaversion.viaversion.libs.gson.stream.JsonReader;
import com.viaversion.viaversion.libs.gson.JsonSyntaxException;
import com.viaversion.viaversion.libs.gson.stream.JsonWriter;
import com.viaversion.viaversion.libs.gson.TypeAdapter;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.Nullable;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextColor;

final class TextColorWrapper
{
    @Nullable
    final TextColor color;
    @Nullable
    final TextDecoration decoration;
    final boolean reset;
    
    TextColorWrapper(@Nullable final TextColor color, @Nullable final TextDecoration decoration, final boolean reset) {
        this.color = color;
        this.decoration = decoration;
        this.reset = reset;
    }
    
    static final class Serializer extends TypeAdapter<TextColorWrapper>
    {
        static final Serializer INSTANCE;
        
        private Serializer() {
        }
        
        @Override
        public void write(final JsonWriter out, final TextColorWrapper value) {
            throw new JsonSyntaxException("Cannot write TextColorWrapper instances");
        }
        
        @Override
        public TextColorWrapper read(final JsonReader in) throws IOException {
            final String input = in.nextString();
            final TextColor color = TextColorSerializer.fromString(input);
            final TextDecoration decoration = TextDecoration.NAMES.value(input);
            final boolean reset = decoration == null && input.equals("reset");
            if (color == null && decoration == null && !reset) {
                throw new JsonParseException("Don't know how to parse " + input + " at " + in.getPath());
            }
            return new TextColorWrapper(color, decoration, reset);
        }
        
        static {
            INSTANCE = new Serializer();
        }
    }
}
