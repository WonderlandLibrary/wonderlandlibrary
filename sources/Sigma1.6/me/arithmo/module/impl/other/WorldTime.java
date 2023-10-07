/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.other;

import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventPacket;
import me.arithmo.event.impl.EventTick;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;

public class WorldTime
extends Module {
    public WorldTime(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events={EventTick.class, EventPacket.class})
    public void onEvent(Event event) {
        if (event instanceof EventTick || event instanceof EventPacket) {
            // empty if block
        }
    }
}

