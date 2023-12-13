/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.render;

import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventPacket;
import me.arithmo.event.impl.EventRender3D;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S06PacketUpdateHealth;

public class HitIndicator
extends Module {
    public HitIndicator(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events={EventRender3D.class, EventPacket.class})
    public void onEvent(Event event) {
        EventPacket ep;
        if (event instanceof EventPacket && (ep = (EventPacket)event).isIncoming() && ep.getPacket() instanceof S06PacketUpdateHealth) {
            S06PacketUpdateHealth s06PacketUpdateHealth = (S06PacketUpdateHealth)ep.getPacket();
        }
        if (event instanceof EventRender3D) {
            // empty if block
        }
    }
}

