/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.other;

import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventTick;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import net.minecraft.client.Minecraft;

public class Timer
extends Module {
    private String GS = "GAMESPEED";

    public Timer(ModuleData data) {
        super(data);
        this.settings.put(this.GS, new Setting<Double>(this.GS, 0.3, "The value the mc timer will be set to.", 0.05, 0.1, 5.0));
    }

    @Override
    public void onEnable() {
        Timer.mc.timer.timerSpeed = 1.0f;
    }

    @Override
    public void onDisable() {
        Timer.mc.timer.timerSpeed = 1.0f;
    }

    @RegisterEvent(events={EventTick.class})
    public void onEvent(Event event) {
        Timer.mc.timer.timerSpeed = ((Number)((Setting)this.settings.get(this.GS)).getValue()).floatValue();
    }
}

