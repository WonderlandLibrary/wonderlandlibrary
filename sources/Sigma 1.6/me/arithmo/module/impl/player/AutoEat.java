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

public class AutoEat
extends Module {
    Timer timer = new Timer();

    public AutoEat(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events={EventTick.class})
    public void onEvent(Event event) {
        if (AutoEat.mc.thePlayer.getFoodStats().getFoodLevel() < 20 && this.timer.delay(2000.0f)) {
            AutoEat.mc.thePlayer.sendChatMessage("/eat");
            this.timer.reset();
        }
        if (this.timer.delay(60000.0f)) {
            AutoEat.mc.thePlayer.sendChatMessage("/eat");
            this.timer.reset();
        }
    }
}

