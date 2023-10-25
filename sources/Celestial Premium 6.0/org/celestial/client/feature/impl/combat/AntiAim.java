/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.combat;

import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.math.GCDCalcHelper;
import org.celestial.client.helpers.math.MathematicHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class AntiAim
extends Feature {
    public float rot = 0.0f;
    public NumberSetting spinSpeed;
    public BooleanSetting pitch;
    public NumberSetting pitchValue;
    public BooleanSetting sendPacket;
    public ListSetting mode = new ListSetting("AntiAim Mode", "Spin", () -> true, "Spin", "Freestanding", "OneTap");

    public AntiAim() {
        super("AntiAim", "\u0410\u043d\u0442\u0438\u0410\u0438\u043c \u043a\u0430\u043a \u0432 CSGO", Type.Player);
        this.spinSpeed = new NumberSetting("Spin Speed", 1.0f, 0.0f, 10.0f, 0.1f, () -> this.mode.currentMode.equals("Spin"));
        this.sendPacket = new BooleanSetting("Send Packet", false, () -> true);
        this.pitch = new BooleanSetting("Custom Pitch", false, () -> true);
        this.pitchValue = new NumberSetting("Custom Pitch Value", 90.0f, -90.0f, 90.0f, 5.0f, this.pitch::getCurrentValue);
        this.addSettings(this.mode, this.spinSpeed, this.sendPacket, this.pitch, this.pitchValue);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        String antiAimMode = this.mode.getCurrentMode();
        this.setSuffix(antiAimMode);
        float speed = this.spinSpeed.getCurrentValue() * 10.0f;
        if (this.pitch.getCurrentValue()) {
            if (this.sendPacket.getCurrentValue()) {
                event.setPitch(this.pitchValue.getCurrentValue());
            }
            AntiAim.mc.player.rotationPitchHead = this.pitchValue.getCurrentValue();
        }
        if (antiAimMode.equalsIgnoreCase("Spin")) {
            float yaw = GCDCalcHelper.getFixedRotation((float)(Math.floor(this.spinAim(speed)) + (double)MathematicHelper.randomizeFloat(-4.0f, 1.0f)));
            if (this.sendPacket.getCurrentValue()) {
                event.setYaw(yaw);
            }
            AntiAim.mc.player.renderYawOffset = GCDCalcHelper.getFixedRotation((float)Math.floor(this.spinAim(speed)));
            AntiAim.mc.player.rotationYawHead = GCDCalcHelper.getFixedRotation((float)Math.floor(this.spinAim(speed)));
        } else if (antiAimMode.equalsIgnoreCase("OneTap")) {
            float yaw = AntiAim.mc.player.rotationYaw + 180.0f + (AntiAim.mc.player.ticksExisted % 8 < 4 ? MathematicHelper.randomizeFloat(33.0f, 22.0f) : -MathematicHelper.randomizeFloat(33.0f, 22.0f));
            if (this.sendPacket.getCurrentValue()) {
                event.setYaw(GCDCalcHelper.getFixedRotation(yaw));
            }
            AntiAim.mc.player.renderYawOffset = yaw;
            AntiAim.mc.player.rotationYawHead = yaw;
        } else if (antiAimMode.equalsIgnoreCase("Freestanding")) {
            float yaw = (float)((double)(AntiAim.mc.player.rotationYaw + 5.0f) + Math.random() * 175.0);
            if (this.sendPacket.getCurrentValue()) {
                event.setYaw(GCDCalcHelper.getFixedRotation(yaw));
            }
            AntiAim.mc.player.renderYawOffset = yaw;
            AntiAim.mc.player.rotationYawHead = yaw;
        }
    }

    public float spinAim(float rots) {
        this.rot += rots;
        return this.rot;
    }
}

