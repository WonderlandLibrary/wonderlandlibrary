// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.sponge.listeners;

import org.spongepowered.api.event.Listener;
import com.viaversion.viaversion.update.UpdateUtil;
import com.viaversion.viaversion.api.Via;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;

public class UpdateListener
{
    @Listener
    public void onJoin(final ServerSideConnectionEvent.Join join) {
        if (join.player().hasPermission("viaversion.update") && Via.getConfig().isCheckForUpdates()) {
            UpdateUtil.sendUpdateMessage(join.player().uniqueId());
        }
    }
}
