// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.login;

import net.minecraft.network.login.server.SPacketEnableCompression;
import net.minecraft.network.login.server.SPacketDisconnect;
import net.minecraft.network.login.server.SPacketLoginSuccess;
import net.minecraft.network.login.server.SPacketEncryptionRequest;
import net.minecraft.network.INetHandler;

public interface INetHandlerLoginClient extends INetHandler
{
    void handleEncryptionRequest(final SPacketEncryptionRequest p0);
    
    void handleLoginSuccess(final SPacketLoginSuccess p0);
    
    void handleDisconnect(final SPacketDisconnect p0);
    
    void handleEnableCompression(final SPacketEnableCompression p0);
}
