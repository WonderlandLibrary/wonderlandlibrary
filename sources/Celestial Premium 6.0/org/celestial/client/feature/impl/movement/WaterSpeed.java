/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.movement;

import net.minecraft.init.MobEffects;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.player.MovementHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class WaterSpeed
extends Feature {
    public static NumberSetting speed;
    private final BooleanSetting speedCheck;

    public WaterSpeed() {
        super("WaterSpeed", "\u0414\u0435\u043b\u0430\u0435\u0442 \u0432\u0430\u0441 \u0431\u044b\u0441\u0442\u0440\u0435\u0435 \u0432 \u0432\u043e\u0434\u0435", Type.Movement);
        speed = new NumberSetting("Speed Amount", 1.0f, 0.1f, 4.0f, 0.01f, () -> true);
        this.speedCheck = new BooleanSetting("Speed Potion Check", false, () -> true);
        this.addSettings(this.speedCheck, speed);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!WaterSpeed.mc.player.isPotionActive(MobEffects.SPEED) && this.speedCheck.getCurrentValue()) {
            return;
        }
        if (WaterSpeed.mc.player.isInLiquid()) {
            MovementHelper.setSpeed(speed.getCurrentValue());
        }
    }
}

