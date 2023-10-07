// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.protocol.remapper;

import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;

@FunctionalInterface
public interface PacketHandler
{
    void handle(final PacketWrapper p0) throws Exception;
}
