// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.viamcp.loader;

import dev.tenacity.viamcp.ViaMCP;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.protocols.base.BaseVersionProvider;
import com.viaversion.viaversion.api.protocol.version.VersionProvider;
import com.viaversion.viaversion.api.platform.providers.Provider;
import com.viaversion.viaversion.bungee.providers.BungeeMovementTransmitter;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.MovementTransmitterProvider;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.platform.ViaPlatformLoader;

public class MCPViaLoader implements ViaPlatformLoader
{
    public void load() {
        Via.getManager().getProviders().use((Class)MovementTransmitterProvider.class, (Provider)new BungeeMovementTransmitter());
        Via.getManager().getProviders().use((Class)VersionProvider.class, (Provider)new BaseVersionProvider() {
            public int getClosestServerProtocol(final UserConnection connection) throws Exception {
                if (connection.isClientSide()) {
                    return ViaMCP.getInstance().getVersion();
                }
                return super.getClosestServerProtocol(connection);
            }
        });
    }
    
    public void unload() {
    }
}
