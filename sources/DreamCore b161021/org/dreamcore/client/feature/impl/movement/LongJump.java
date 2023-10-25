package org.dreamcore.client.feature.impl.movement;

import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventPreMotion;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.ListSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

public class LongJump extends Feature {

    public ListSetting mode;
    public NumberSetting boostMultiplier;
    public NumberSetting motionBoost;
    public BooleanSetting motionYBoost = new BooleanSetting("MotionY boost", false, () -> true);

    public LongJump() {
        super("Long Jump", "Позволяет прыгать на большую длинну", Type.Movement);
        mode = new ListSetting("LongJump Mode", "Matrix Pearle", () -> true, "Redesky", "Matrix Pearle");
        boostMultiplier = new NumberSetting("Boost Speed", 0.3F, 0.1F, 1F, 0.1F, () -> mode.currentMode.equals("Matrix Pearle"));
        motionBoost = new NumberSetting("Motion Boost", 0.6F, 0.1F, 8F, 0.1F, () -> mode.currentMode.equals("Matrix Pearle") && motionYBoost.getBoolValue());
        addSettings(mode, boostMultiplier, motionYBoost, motionBoost);
    }

    @EventTarget
    public void onPreUpdate(EventPreMotion event) {
        String longMode = mode.getOptions();
        this.setSuffix(longMode);
        if (!this.getState())
            return;
        if (longMode.equalsIgnoreCase("Redesky")) {
            if (mc.player.hurtTime > 0) {
                mc.timer.timerSpeed = 1F;
                if (mc.player.fallDistance != 0.0f) {
                    mc.player.motionY += 0.039;
                }
                if (mc.player.onGround) {
                    mc.player.jump();
                } else {
                    mc.timer.timerSpeed = 0.2f;
                    mc.player.motionY += 0.075;
                    mc.player.motionX *= 1.065f;
                    mc.player.motionZ *= 1.065f;
                }
            }
        } else if (longMode.equalsIgnoreCase("Matrix Pearle")) {
            if (mc.player.hurtTime > 0) {
                mc.player.isAirBorne = true;
                if (motionYBoost.getBoolValue()) {
                    mc.player.motionY = motionBoost.getNumberValue();
                }
                mc.player.jumpMovementFactor = boostMultiplier.getNumberValue();
            }
        }
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1;
        super.onDisable();
    }
}
