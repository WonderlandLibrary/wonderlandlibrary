/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.velocitypowered.api.event.Subscribe
 *  com.velocitypowered.api.event.connection.PostLoginEvent
 */
package com.viaversion.viaversion.velocity.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.update.UpdateUtil;

public class UpdateListener {
    @Subscribe
    public void onJoin(PostLoginEvent e2) {
        if (e2.getPlayer().hasPermission("viaversion.update") && Via.getConfig().isCheckForUpdates()) {
            UpdateUtil.sendUpdateMessage(e2.getPlayer().getUniqueId());
        }
    }
}

