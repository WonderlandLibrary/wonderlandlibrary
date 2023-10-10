// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types.minecraft;

import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.type.Type;

public class OptPositionType extends Type<Position>
{
    public OptPositionType() {
        super(Position.class);
    }
    
    @Override
    public Position read(final ByteBuf buffer) throws Exception {
        final boolean present = buffer.readBoolean();
        if (!present) {
            return null;
        }
        return Type.POSITION.read(buffer);
    }
    
    @Override
    public void write(final ByteBuf buffer, final Position object) throws Exception {
        buffer.writeBoolean(object != null);
        if (object != null) {
            Type.POSITION.write(buffer, object);
        }
    }
}
