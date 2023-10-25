/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.player;

import net.minecraft.network.play.client.CPacketCloseWindow;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.packet.EventSendPacket;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;

public class XCarry
extends Feature {
    public XCarry() {
        super("XCarry", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0445\u0440\u0430\u043d\u0438\u0442\u044c \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u044b \u0432 \u0441\u043b\u043e\u0442\u0430\u0445 \u0434\u043b\u044f \u043a\u0440\u0430\u0444\u0442\u0430", Type.Player);
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof CPacketCloseWindow) {
            event.setCancelled(true);
        }
    }
}

