// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bukkit.listeners;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.EventHandler;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.bukkit.platform.BukkitViaInjector;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.Listener;

public class ProtocolLibEnableListener implements Listener
{
    @EventHandler
    public void onPluginEnable(final PluginEnableEvent e) {
        if (e.getPlugin().getName().equals("ProtocolLib")) {
            ((BukkitViaInjector)Via.getManager().getInjector()).setProtocolLib(true);
        }
    }
    
    @EventHandler
    public void onPluginDisable(final PluginDisableEvent e) {
        if (e.getPlugin().getName().equals("ProtocolLib")) {
            ((BukkitViaInjector)Via.getManager().getInjector()).setProtocolLib(false);
        }
    }
}
