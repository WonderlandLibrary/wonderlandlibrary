// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.util;

import com.elementars.eclient.event.EventTarget;
import com.google.common.base.Strings;
import java.util.function.Predicate;
import java.util.Objects;
import com.elementars.eclient.event.events.EventReceivePacket;
import com.elementars.eclient.event.events.EventPlayerConnect;
import java.util.UUID;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import com.elementars.eclient.Xulu;

public class ConnectionUtil
{
    public static ConnectionUtil INSTANCE;
    
    public ConnectionUtil() {
        Xulu.EVENT_MANAGER.register(this);
    }
    
    private void fireEvents(final SPacketPlayerListItem.Action action, final UUID uuid, final String name) {
        if (uuid == null) {
            return;
        }
        switch (action) {
            case ADD_PLAYER: {
                final EventPlayerConnect.Join eventPlayerConnect = new EventPlayerConnect.Join(uuid, name);
                eventPlayerConnect.call();
                break;
            }
            case REMOVE_PLAYER: {
                final EventPlayerConnect.Leave eventPlayerConnect2 = new EventPlayerConnect.Leave(uuid, name);
                eventPlayerConnect2.call();
                break;
            }
        }
    }
    
    @EventTarget
    public void onScoreboardEvent(final EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketPlayerListItem) {
            final SPacketPlayerListItem packet = (SPacketPlayerListItem)event.getPacket();
            if (!SPacketPlayerListItem.Action.ADD_PLAYER.equals((Object)packet.getAction()) && !SPacketPlayerListItem.Action.REMOVE_PLAYER.equals((Object)packet.getAction())) {
                return;
            }
            final UUID id;
            final String name;
            final SPacketPlayerListItem sPacketPlayerListItem;
            packet.getEntries().stream().filter(Objects::nonNull).filter(data -> !Strings.isNullOrEmpty(data.getProfile().getName()) || data.getProfile().getId() != null).forEach(data -> {
                id = data.getProfile().getId();
                name = data.getProfile().getName();
                this.fireEvents(sPacketPlayerListItem.getAction(), id, name);
            });
        }
    }
}
