/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.movement;

import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventPacket;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class KeepSprint
extends Module {
    public KeepSprint(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events={EventPacket.class})
    public void onEvent(Event event) {
        C0BPacketEntityAction packet;
        EventPacket e = (EventPacket)event;
        if (e.isIncoming() && e.getPacket() instanceof C0BPacketEntityAction && (packet = (C0BPacketEntityAction)e.getPacket()).func_180764_b() == C0BPacketEntityAction.Action.STOP_SPRINTING) {
            e.setCancelled(true);
        }
    }
}

