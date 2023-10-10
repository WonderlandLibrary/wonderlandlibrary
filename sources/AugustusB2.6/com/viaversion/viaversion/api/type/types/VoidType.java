// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types;

import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.type.TypeConverter;
import com.viaversion.viaversion.api.type.Type;

public class VoidType extends Type<Void> implements TypeConverter<Void>
{
    public VoidType() {
        super(Void.class);
    }
    
    @Override
    public Void read(final ByteBuf buffer) {
        return null;
    }
    
    @Override
    public void write(final ByteBuf buffer, final Void object) {
    }
    
    @Override
    public Void from(final Object o) {
        return null;
    }
}
