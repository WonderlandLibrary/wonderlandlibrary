// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.protocol.remapper;

import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;

@FunctionalInterface
public interface ValueReader<T>
{
    T read(final PacketWrapper p0) throws Exception;
}
