/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.movement;

import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventMotion;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.FoodStats;

public class Sprint
extends Module {
    public Sprint(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events={EventMotion.class})
    public void onEvent(Event event) {
        EventMotion em = (EventMotion)event;
        if (em.isPre() && this.canSprint()) {
            Sprint.mc.thePlayer.setSprinting(true);
        }
    }

    private boolean canSprint() {
        return PlayerUtil.isMoving() && Sprint.mc.thePlayer.getFoodStats().getFoodLevel() > 6;
    }
}

