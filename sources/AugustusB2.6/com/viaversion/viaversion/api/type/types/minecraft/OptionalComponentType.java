// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types.minecraft;

import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.type.Type;

public class OptionalComponentType extends Type<JsonElement>
{
    public OptionalComponentType() {
        super(JsonElement.class);
    }
    
    @Override
    public JsonElement read(final ByteBuf buffer) throws Exception {
        final boolean present = buffer.readBoolean();
        return present ? Type.COMPONENT.read(buffer) : null;
    }
    
    @Override
    public void write(final ByteBuf buffer, final JsonElement object) throws Exception {
        if (object == null) {
            buffer.writeBoolean(false);
        }
        else {
            buffer.writeBoolean(true);
            Type.COMPONENT.write(buffer, object);
        }
    }
}
