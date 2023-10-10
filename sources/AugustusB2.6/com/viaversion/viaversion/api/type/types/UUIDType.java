// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types;

import io.netty.buffer.ByteBuf;
import java.util.UUID;
import com.viaversion.viaversion.api.type.Type;

public class UUIDType extends Type<UUID>
{
    public UUIDType() {
        super(UUID.class);
    }
    
    @Override
    public UUID read(final ByteBuf buffer) {
        return new UUID(buffer.readLong(), buffer.readLong());
    }
    
    @Override
    public void write(final ByteBuf buffer, final UUID object) {
        buffer.writeLong(object.getMostSignificantBits());
        buffer.writeLong(object.getLeastSignificantBits());
    }
}
