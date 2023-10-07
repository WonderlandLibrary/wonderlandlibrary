// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.bukkit.listeners;

import org.jetbrains.annotations.Nullable;
import org.bukkit.plugin.Plugin;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.bukkit.platform.BukkitViaInjector;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.Listener;

public class ProtocolLibEnableListener implements Listener
{
    @EventHandler
    public void onPluginEnable(final PluginEnableEvent e) {
        if (e.getPlugin().getName().equals("ProtocolLib")) {
            checkCompat(e.getPlugin());
        }
    }
    
    @EventHandler
    public void onPluginDisable(final PluginDisableEvent e) {
        if (e.getPlugin().getName().equals("ProtocolLib")) {
            ((BukkitViaInjector)Via.getManager().getInjector()).setProtocolLib(false);
        }
    }
    
    public static void checkCompat(@Nullable final Plugin protocolLib) {
        if (protocolLib != null) {
            final String version = protocolLib.getDescription().getVersion();
            final String majorVersion = version.split("\\.", 2)[0];
            try {
                if (Integer.parseInt(majorVersion) < 5) {
                    ((BukkitViaInjector)Via.getManager().getInjector()).setProtocolLib(true);
                    return;
                }
            }
            catch (final NumberFormatException ignored) {
                Via.getPlatform().getLogger().warning("ProtocolLib version check failed for version " + version);
            }
        }
        ((BukkitViaInjector)Via.getManager().getInjector()).setProtocolLib(false);
    }
}
