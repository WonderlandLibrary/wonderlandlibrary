/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.player;

import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventMotion;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class NoFall
extends Module {
    public NoFall(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events={EventMotion.class})
    public void onEvent(Event event) {
        EventMotion em;
        if (event instanceof EventMotion && (em = (EventMotion)event).isPre() && NoFall.mc.thePlayer.fallDistance >= 1.0f) {
            em.setGround(true);
        }
    }
}

