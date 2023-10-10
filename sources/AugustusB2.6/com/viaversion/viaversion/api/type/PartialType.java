// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type;

import io.netty.buffer.ByteBuf;

public abstract class PartialType<T, X> extends Type<T>
{
    private final X param;
    
    protected PartialType(final X param, final Class<T> type) {
        super(type);
        this.param = param;
    }
    
    protected PartialType(final X param, final String name, final Class<T> type) {
        super(name, type);
        this.param = param;
    }
    
    public abstract T read(final ByteBuf p0, final X p1) throws Exception;
    
    public abstract void write(final ByteBuf p0, final X p1, final T p2) throws Exception;
    
    @Override
    public final T read(final ByteBuf buffer) throws Exception {
        return this.read(buffer, this.param);
    }
    
    @Override
    public final void write(final ByteBuf buffer, final T object) throws Exception {
        this.write(buffer, this.param, object);
    }
}
