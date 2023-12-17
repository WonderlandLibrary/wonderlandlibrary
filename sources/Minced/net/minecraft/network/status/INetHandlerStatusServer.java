// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.status;

import net.minecraft.network.status.client.CPacketServerQuery;
import net.minecraft.network.status.client.CPacketPing;
import net.minecraft.network.INetHandler;

public interface INetHandlerStatusServer extends INetHandler
{
    void processPing(final CPacketPing p0);
    
    void processServerQuery(final CPacketServerQuery p0);
}
