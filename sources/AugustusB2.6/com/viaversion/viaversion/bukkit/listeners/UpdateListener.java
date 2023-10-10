// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bukkit.listeners;

import org.bukkit.event.EventHandler;
import com.viaversion.viaversion.update.UpdateUtil;
import com.viaversion.viaversion.api.Via;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.Listener;

public class UpdateListener implements Listener
{
    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        if (e.getPlayer().hasPermission("viaversion.update") && Via.getConfig().isCheckForUpdates()) {
            UpdateUtil.sendUpdateMessage(e.getPlayer().getUniqueId());
        }
    }
}
