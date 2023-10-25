/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorse;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class EntityFeatures
extends Feature {
    public static BooleanSetting entityControl;
    public static BooleanSetting entitySpeed;
    public static NumberSetting entitySpeedValue;

    public EntityFeatures() {
        super("EntityFeatures", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u043a\u043e\u043d\u0442\u0440\u043e\u043b\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0436\u0438\u0432\u043e\u0442\u043d\u044b\u0445", Type.Misc);
        entityControl = new BooleanSetting("Entity Control", true, () -> true);
        entitySpeed = new BooleanSetting("Entity Speed", true, entityControl::getCurrentValue);
        entitySpeedValue = new NumberSetting("Entity Speed Multiplier", 1.0f, 0.0f, 2.0f, 0.1f, () -> entityControl.getCurrentValue() && entitySpeed.getCurrentValue());
        this.addSettings(entityControl, entitySpeed, entitySpeedValue);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (entityControl.getCurrentValue()) {
            Entity ridingEntity = EntityFeatures.mc.player.getRidingEntity();
            assert (ridingEntity != null);
            if (ridingEntity instanceof AbstractHorse) {
                EntityFeatures.mc.player.horseJumpPowerCounter = 9;
                EntityFeatures.mc.player.horseJumpPower = 1.0f;
            }
        }
        if (entitySpeed.getCurrentValue() && EntityFeatures.mc.player != null && EntityFeatures.mc.player.getRidingEntity() != null) {
            double forward = EntityFeatures.mc.player.movementInput.moveForward;
            double strafe = EntityFeatures.mc.player.movementInput.moveStrafe;
            float yaw = EntityFeatures.mc.player.rotationYaw;
            if (forward == 0.0 && strafe == 0.0) {
                EntityFeatures.mc.player.getRidingEntity().motionX = 0.0;
                EntityFeatures.mc.player.getRidingEntity().motionZ = 0.0;
            } else {
                if (forward != 0.0) {
                    if (strafe > 0.0) {
                        yaw += (float)(forward > 0.0 ? -45 : 45);
                    } else if (strafe < 0.0) {
                        yaw += (float)(forward > 0.0 ? 45 : -45);
                    }
                    strafe = 0.0;
                    if (forward > 0.0) {
                        forward = 1.0;
                    } else if (forward < 0.0) {
                        forward = -1.0;
                    }
                }
                double cos = Math.cos(Math.toRadians(yaw + 90.0f));
                double sin = Math.sin(Math.toRadians(yaw + 90.0f));
                EntityFeatures.mc.player.getRidingEntity().motionX = forward * (double)entitySpeedValue.getCurrentValue() * cos + strafe * (double)entitySpeedValue.getCurrentValue() * sin;
                EntityFeatures.mc.player.getRidingEntity().motionZ = forward * (double)entitySpeedValue.getCurrentValue() * sin - strafe * (double)entitySpeedValue.getCurrentValue() * cos;
            }
        }
    }
}

