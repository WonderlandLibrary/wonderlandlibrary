/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.movement;

import net.minecraft.network.play.client.CPacketEntityAction;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.player.MovementHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class LongJump
extends Feature {
    public static ListSetting mode;
    private final BooleanSetting noBurnDamage;
    private final NumberSetting boostMultiplier;
    private final NumberSetting minHurtTime;
    private final NumberSetting maxHurtTime;

    public LongJump() {
        super("LongJump", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u043f\u0440\u044b\u0433\u0430\u0442\u044c \u043d\u0430 \u0431\u043e\u043b\u044c\u0448\u0443\u044e \u0434\u043b\u0438\u043d\u043d\u0443", Type.Movement);
        mode = new ListSetting("LongJump Mode", "Matrix Hurt", () -> true, "Old Redesky", "Matrix Hurt", "Sunrise Hurt");
        this.noBurnDamage = new BooleanSetting("No Burn Damage", true, () -> !LongJump.mode.currentMode.equals("Old Redesky"));
        this.boostMultiplier = new NumberSetting("Boost Multiplier", 1.0f, 0.1f, 1.0f, 0.01f, () -> !LongJump.mode.currentMode.equals("Old Redesky"));
        this.minHurtTime = new NumberSetting("Min HurtTime", 0.0f, 0.0f, 8.0f, 1.0f, () -> !LongJump.mode.currentMode.equals("Old Redesky"));
        this.maxHurtTime = new NumberSetting("Max HurtTime", 9.0f, 2.0f, 9.0f, 1.0f, () -> !LongJump.mode.currentMode.equals("Old Redesky"));
        this.addSettings(mode, this.noBurnDamage, this.minHurtTime, this.maxHurtTime, this.boostMultiplier);
    }

    @Override
    public void onDisable() {
        LongJump.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }

    @EventTarget
    public void onPreUpdate(EventPreMotion event) {
        String longMode = mode.getOptions();
        this.setSuffix(longMode);
        if (!this.getState()) {
            return;
        }
        if (longMode.equalsIgnoreCase("Old Redesky")) {
            if (LongJump.mc.player.fallDistance != 0.0f) {
                LongJump.mc.player.motionY += 0.039;
            }
            if (LongJump.mc.player.onGround) {
                LongJump.mc.player.jump();
            } else {
                LongJump.mc.timer.timerSpeed = 0.2f;
                LongJump.mc.player.motionY += 0.075;
                LongJump.mc.player.motionX *= (double)1.065f;
                LongJump.mc.player.motionZ *= (double)1.065f;
            }
        } else if (longMode.equalsIgnoreCase("Matrix Hurt")) {
            double yaw = (double)LongJump.mc.player.rotationYaw * 0.017453292;
            if (LongJump.mc.player.isBurning() && LongJump.mc.player.hurtTime > 0 && this.noBurnDamage.getCurrentValue()) {
                return;
            }
            if ((float)LongJump.mc.player.hurtTime > this.minHurtTime.getCurrentValue() && (float)LongJump.mc.player.hurtTime <= this.maxHurtTime.getCurrentValue()) {
                LongJump.mc.player.motionY += 0.009;
                if (LongJump.mc.player.onGround) {
                    LongJump.mc.player.jump();
                } else {
                    MovementHelper.strafePlayer();
                    LongJump.mc.player.motionX -= Math.sin(yaw) * (double)this.boostMultiplier.getCurrentValue();
                    LongJump.mc.player.motionZ += Math.cos(yaw) * (double)this.boostMultiplier.getCurrentValue();
                }
            }
        } else if (longMode.equalsIgnoreCase("Sunrise Hurt")) {
            if (LongJump.mc.player.isBurning() && LongJump.mc.player.hurtTime > 0 && this.noBurnDamage.getCurrentValue()) {
                return;
            }
            if ((float)LongJump.mc.player.hurtTime > this.minHurtTime.getCurrentValue() && (float)LongJump.mc.player.hurtTime <= this.maxHurtTime.getCurrentValue()) {
                LongJump.mc.player.connection.sendPacket(new CPacketEntityAction(LongJump.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                if (LongJump.mc.player.onGround) {
                    LongJump.mc.player.posY += 2.742;
                    LongJump.mc.player.jump();
                }
                if (LongJump.mc.player.motionY == 0.0030162615090425808) {
                    LongJump.mc.player.jumpMovementFactor = this.boostMultiplier.getCurrentValue() * 5.0f;
                }
            }
        }
    }
}

