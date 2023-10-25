/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.misc;

import net.minecraft.network.play.client.CPacketConfirmTeleport;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.packet.EventSendPacket;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;

public class PortalGodMode
extends Feature {
    public PortalGodMode() {
        super("PortalGodMode", "\u0414\u0435\u043b\u0430\u0435\u0442 \u0432\u0430\u0441 \u043d\u0435\u0443\u044f\u0437\u0432\u0438\u043c\u044b\u043c \u0432 \u043f\u043e\u0440\u0442\u0430\u043b\u0435", Type.Misc);
    }

    @EventTarget
    public void onSend(EventSendPacket event) {
        if (event.getPacket() instanceof CPacketConfirmTeleport) {
            event.setCancelled(true);
        }
    }
}

