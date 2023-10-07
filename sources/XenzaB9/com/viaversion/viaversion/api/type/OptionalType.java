// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.type;

import io.netty.buffer.ByteBuf;

public abstract class OptionalType<T> extends Type<T>
{
    private final Type<T> type;
    
    protected OptionalType(final Type<T> type) {
        super(type.getOutputClass());
        this.type = type;
    }
    
    @Override
    public T read(final ByteBuf buffer) throws Exception {
        return buffer.readBoolean() ? this.type.read(buffer) : null;
    }
    
    @Override
    public void write(final ByteBuf buffer, final T value) throws Exception {
        if (value == null) {
            buffer.writeBoolean(false);
        }
        else {
            buffer.writeBoolean(true);
            this.type.write(buffer, value);
        }
    }
}
