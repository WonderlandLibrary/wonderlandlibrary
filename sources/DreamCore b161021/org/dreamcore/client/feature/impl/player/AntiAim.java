package org.dreamcore.client.feature.impl.player;

import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventPreMotion;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.math.GCDCalcHelper;
import org.dreamcore.client.helpers.math.MathematicHelper;
import org.dreamcore.client.settings.impl.ListSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

public class AntiAim extends Feature {

    public float rot = 0;
    public NumberSetting spinSpeed;
    public ListSetting pitchMode;
    public ListSetting mode;
    public ListSetting degreeMode;

    public AntiAim() {
        super("AntiAim", "АнтиАим как в CSGO", Type.Player);
        mode = new ListSetting("Yaw Mode", "Jitter", () -> true, "Freestanding", "Spin", "Jitter");
        spinSpeed = new NumberSetting("Spin Speed", 1, 0, 10, 0.1f, () -> degreeMode.currentMode.equals("Spin"));
        pitchMode = new ListSetting("Custom Pitch", "Down", () -> true, "None", "Down", "Up", "Fake-Down", "Fake-Up");
        degreeMode = new ListSetting("Degree Mode", "Spin", () -> true, "Random", "Spin");
        addSettings(mode, spinSpeed, pitchMode, degreeMode);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        String antiAimMode = mode.getCurrentMode();
        this.setSuffix(antiAimMode);
        float speed = spinSpeed.getNumberValue() * 10;
        switch (pitchMode.currentMode) {
            case "Down":
                event.setPitch(90);
                mc.player.rotationPitchHead = 90;
                break;
            case "Up":
                event.setPitch(-90);
                mc.player.rotationPitchHead = -90;
                break;
            case "Fake-Down":
                mc.player.rotationPitchHead = 90;
                break;
            case "Fake-Up":
                mc.player.rotationPitchHead = -90;
                break;
        }
        if (mode.currentMode.equals("Jitter")) {
            float yaw = mc.player.rotationYaw + 180 + (mc.player.ticksExisted % 8 < 4 ? MathematicHelper.randomizeFloat(-90, 90) : -MathematicHelper.randomizeFloat(90, -90));
            event.setYaw(GCDCalcHelper.getFixedRotation(yaw));
            mc.player.renderYawOffset = yaw;
            mc.player.rotationYawHead = yaw;
        } else if (antiAimMode.equals("Freestanding")) {
            float yaw = (float) (mc.player.rotationYaw + 5 + Math.random() * 175);
            event.setYaw(GCDCalcHelper.getFixedRotation(yaw));
            mc.player.renderYawOffset = yaw;
            mc.player.rotationYawHead = yaw;
        } else if (antiAimMode.equalsIgnoreCase("Spin")) {
            float yaw = GCDCalcHelper.getFixedRotation((float) (Math.floor(spinAim(speed)) + MathematicHelper.randomizeFloat(-4, 1)));
            event.setYaw(yaw);
            mc.player.renderYawOffset = yaw;
            mc.player.rotationYawHead = yaw;
        }

        if (mc.player.isSneaking()) {
            if (degreeMode.currentMode.equals("Spin")) {
                float yaw = GCDCalcHelper.getFixedRotation((float) (Math.floor(spinAim(speed)) + MathematicHelper.randomizeFloat(-4, 1)));
                event.setYaw(yaw);
                mc.player.renderYawOffset = yaw;
                mc.player.rotationYawHead = yaw;
            } else if (degreeMode.currentMode.equals("Random")) {
                float yaw = (float) (mc.player.rotationYaw + Math.floor(spinAim(speed) + (mc.player.ticksExisted % 8 < 4 ? MathematicHelper.randomizeFloat(33, 22) : -MathematicHelper.randomizeFloat(33, 22))));
                event.setYaw(yaw);
                mc.player.renderYawOffset = yaw;
                mc.player.rotationYawHead = yaw;
            }
        }
    }

    public float spinAim(float rots) {
        rot += rots;
        return rot;
    }
}