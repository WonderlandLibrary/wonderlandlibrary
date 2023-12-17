// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.status;

import net.minecraft.network.status.server.SPacketPong;
import net.minecraft.network.status.server.SPacketServerInfo;
import net.minecraft.network.INetHandler;

public interface INetHandlerStatusClient extends INetHandler
{
    void handleServerInfo(final SPacketServerInfo p0);
    
    void handlePong(final SPacketPong p0);
}
