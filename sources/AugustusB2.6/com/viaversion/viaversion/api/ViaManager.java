// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api;

import java.util.Set;
import com.viaversion.viaversion.api.debug.DebugHandler;
import com.viaversion.viaversion.api.platform.ViaPlatformLoader;
import com.viaversion.viaversion.api.command.ViaVersionCommand;
import com.viaversion.viaversion.api.platform.ViaInjector;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.viaversion.viaversion.api.connection.ConnectionManager;
import com.viaversion.viaversion.api.platform.ViaPlatform;
import com.viaversion.viaversion.api.protocol.ProtocolManager;

public interface ViaManager
{
    ProtocolManager getProtocolManager();
    
    ViaPlatform<?> getPlatform();
    
    ConnectionManager getConnectionManager();
    
    ViaProviders getProviders();
    
    ViaInjector getInjector();
    
    ViaVersionCommand getCommandHandler();
    
    ViaPlatformLoader getLoader();
    
    @Deprecated
    default boolean isDebug() {
        return this.debugHandler().enabled();
    }
    
    @Deprecated
    default void setDebug(final boolean debug) {
        this.debugHandler().setEnabled(debug);
    }
    
    DebugHandler debugHandler();
    
    Set<String> getSubPlatforms();
    
    void addEnableListener(final Runnable p0);
}
