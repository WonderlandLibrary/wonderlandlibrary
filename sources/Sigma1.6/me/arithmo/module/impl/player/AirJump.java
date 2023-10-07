/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.player;

import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventTick;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.FoodStats;

public class AirJump
extends Module {
    Timer timer = new Timer();

    public AirJump(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events={EventTick.class})
    public void onEvent(Event event) {
        AirJump.mc.thePlayer.onGround = true;
    }
}