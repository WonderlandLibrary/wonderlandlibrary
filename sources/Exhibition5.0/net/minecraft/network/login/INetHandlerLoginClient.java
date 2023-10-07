// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.login;

import net.minecraft.network.login.server.S03PacketEnableCompression;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.network.login.server.S02PacketLoginSuccess;
import net.minecraft.network.login.server.S01PacketEncryptionRequest;
import net.minecraft.network.INetHandler;

public interface INetHandlerLoginClient extends INetHandler
{
    void handleEncryptionRequest(final S01PacketEncryptionRequest p0);
    
    void handleLoginSuccess(final S02PacketLoginSuccess p0);
    
    void handleDisconnect(final S00PacketDisconnect p0);
    
    void func_180464_a(final S03PacketEnableCompression p0);
}
