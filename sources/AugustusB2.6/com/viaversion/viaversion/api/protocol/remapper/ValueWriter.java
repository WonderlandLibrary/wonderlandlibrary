// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.protocol.remapper;

import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;

@FunctionalInterface
public interface ValueWriter<T>
{
    void write(final PacketWrapper p0, final T p1) throws Exception;
}
