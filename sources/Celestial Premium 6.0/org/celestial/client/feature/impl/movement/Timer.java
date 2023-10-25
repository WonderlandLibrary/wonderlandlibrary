/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.movement;

import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class Timer
extends Feature {
    public BooleanSetting sunriseBypass;
    public BooleanSetting autoDisable;
    public NumberSetting disableTicks;
    public NumberSetting timer = new NumberSetting("Timer", 2.0f, 0.1f, 20.0f, 0.1f, () -> true);

    public Timer() {
        super("Timer", "\u0423\u0432\u0435\u043b\u0438\u0447\u0438\u0432\u0430\u0435\u0442 \u0441\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u0438\u0433\u0440\u044b", Type.Movement);
        this.autoDisable = new BooleanSetting("Auto Disable", false, () -> true);
        this.disableTicks = new NumberSetting("Disable Ticks", 50.0f, 2.0f, 100.0f, 1.0f, this.autoDisable::getCurrentValue);
        this.sunriseBypass = new BooleanSetting("Old Sunrise Bypass", false, () -> true);
        this.addSettings(this.autoDisable, this.disableTicks, this.sunriseBypass, this.timer);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!this.getState()) {
            return;
        }
        this.setSuffix("" + this.timer.getCurrentValue());
        if (this.autoDisable.getCurrentValue() && (float)Timer.mc.player.ticksExisted % this.disableTicks.getCurrentValue() == 0.0f) {
            if (this.getState()) {
                this.toggle();
            }
            return;
        }
        if (this.sunriseBypass.getCurrentValue()) {
            Timer.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        }
        Timer.mc.timer.timerSpeed = this.timer.getCurrentValue();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Timer.mc.timer.timerSpeed = 1.0f;
    }
}

