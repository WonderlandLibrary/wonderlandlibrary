/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.spongepowered.api.event.Listener
 *  org.spongepowered.api.event.network.ServerSideConnectionEvent$Join
 */
package com.viaversion.viaversion.sponge.listeners;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.update.UpdateUtil;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;

public class UpdateListener {
    @Listener
    public void onJoin(ServerSideConnectionEvent.Join join) {
        if (join.player().hasPermission("viaversion.update") && Via.getConfig().isCheckForUpdates()) {
            UpdateUtil.sendUpdateMessage(join.player().uniqueId());
        }
    }
}

