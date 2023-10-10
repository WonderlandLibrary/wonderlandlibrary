// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.velocity.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.viaversion.viaversion.update.UpdateUtil;
import com.viaversion.viaversion.api.Via;
import com.velocitypowered.api.event.connection.PostLoginEvent;

public class UpdateListener
{
    @Subscribe
    public void onJoin(final PostLoginEvent e) {
        if (e.getPlayer().hasPermission("viaversion.update") && Via.getConfig().isCheckForUpdates()) {
            UpdateUtil.sendUpdateMessage(e.getPlayer().getUniqueId());
        }
    }
}
