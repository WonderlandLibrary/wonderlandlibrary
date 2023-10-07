// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.type.types.minecraft;

import com.viaversion.viaversion.api.type.OptionalType;
import com.viaversion.viaversion.api.minecraft.Position;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.minecraft.GlobalPosition;
import com.viaversion.viaversion.api.type.Type;

public class GlobalPositionType extends Type<GlobalPosition>
{
    public GlobalPositionType() {
        super(GlobalPosition.class);
    }
    
    @Override
    public GlobalPosition read(final ByteBuf buffer) throws Exception {
        final String dimension = Type.STRING.read(buffer);
        return Type.POSITION1_14.read(buffer).withDimension(dimension);
    }
    
    @Override
    public void write(final ByteBuf buffer, final GlobalPosition object) throws Exception {
        Type.STRING.write(buffer, object.dimension());
        Type.POSITION1_14.write(buffer, object);
    }
    
    public static final class OptionalGlobalPositionType extends OptionalType<GlobalPosition>
    {
        public OptionalGlobalPositionType() {
            super(Type.GLOBAL_POSITION);
        }
    }
}
