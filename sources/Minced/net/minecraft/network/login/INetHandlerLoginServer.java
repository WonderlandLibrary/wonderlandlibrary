// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.login;

import net.minecraft.network.login.client.CPacketEncryptionResponse;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.network.INetHandler;

public interface INetHandlerLoginServer extends INetHandler
{
    void processLoginStart(final CPacketLoginStart p0);
    
    void processEncryptionResponse(final CPacketEncryptionResponse p0);
}
