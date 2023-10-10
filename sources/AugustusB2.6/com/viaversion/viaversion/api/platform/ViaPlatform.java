// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.platform;

import java.util.Collections;
import java.util.Collection;
import com.viaversion.viaversion.libs.gson.JsonObject;
import java.io.File;
import com.viaversion.viaversion.api.configuration.ConfigurationProvider;
import com.viaversion.viaversion.api.configuration.ViaVersionConfig;
import com.viaversion.viaversion.api.ViaAPI;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.UUID;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import java.util.logging.Logger;

public interface ViaPlatform<T>
{
    Logger getLogger();
    
    String getPlatformName();
    
    String getPlatformVersion();
    
    default boolean isProxy() {
        return false;
    }
    
    String getPluginVersion();
    
    PlatformTask runAsync(final Runnable p0);
    
    PlatformTask runSync(final Runnable p0);
    
    PlatformTask runSync(final Runnable p0, final long p1);
    
    PlatformTask runRepeatingSync(final Runnable p0, final long p1);
    
    ViaCommandSender[] getOnlinePlayers();
    
    void sendMessage(final UUID p0, final String p1);
    
    boolean kickPlayer(final UUID p0, final String p1);
    
    default boolean disconnect(final UserConnection connection, final String message) {
        if (connection.isClientSide()) {
            return false;
        }
        final UUID uuid = connection.getProtocolInfo().getUuid();
        return uuid != null && this.kickPlayer(uuid, message);
    }
    
    boolean isPluginEnabled();
    
    ViaAPI<T> getApi();
    
    ViaVersionConfig getConf();
    
    ConfigurationProvider getConfigurationProvider();
    
    File getDataFolder();
    
    void onReload();
    
    JsonObject getDump();
    
    boolean isOldClientsAllowed();
    
    default Collection<UnsupportedSoftware> getUnsupportedSoftwareClasses() {
        return (Collection<UnsupportedSoftware>)Collections.emptyList();
    }
}
