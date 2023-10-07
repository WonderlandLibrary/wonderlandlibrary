// 
// Decompiled by Procyon v0.6.0
// 

package de.gerrygames.viarewind.utils;

import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;

public interface ServerSender
{
    void sendToServer(final PacketWrapper p0, final Class<? extends Protocol> p1, final boolean p2, final boolean p3) throws Exception;
}
