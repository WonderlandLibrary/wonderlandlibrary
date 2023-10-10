// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson;

import com.viaversion.viaversion.libs.gson.stream.JsonWriter;
import java.io.IOException;
import com.viaversion.viaversion.libs.gson.JsonParseException;
import com.viaversion.viaversion.libs.gson.stream.JsonReader;
import com.viaversion.viaversion.libs.kyori.adventure.text.BlockNBTComponent;
import com.viaversion.viaversion.libs.gson.TypeAdapter;

final class BlockNBTComponentPosSerializer extends TypeAdapter<BlockNBTComponent.Pos>
{
    static final TypeAdapter<BlockNBTComponent.Pos> INSTANCE;
    
    private BlockNBTComponentPosSerializer() {
    }
    
    @Override
    public BlockNBTComponent.Pos read(final JsonReader in) throws IOException {
        final String string = in.nextString();
        try {
            return BlockNBTComponent.Pos.fromString(string);
        }
        catch (IllegalArgumentException ex) {
            throw new JsonParseException("Don't know how to turn " + string + " into a Position");
        }
    }
    
    @Override
    public void write(final JsonWriter out, final BlockNBTComponent.Pos value) throws IOException {
        out.value(value.asString());
    }
    
    static {
        INSTANCE = new BlockNBTComponentPosSerializer().nullSafe();
    }
}
