/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.module.modules.misc;

import com.wallhacks.losebypass.systems.module.Module;
import com.wallhacks.losebypass.systems.module.modules.misc.Freecam;
import com.wallhacks.losebypass.systems.setting.settings.BooleanSetting;
import com.wallhacks.losebypass.systems.setting.settings.ModeSetting;
import java.util.Arrays;
import net.minecraft.util.MathHelper;

@Module.Registration(name="FreeLook", description="Allows you to look around freely", hold=true, allowHold=false)
public class FreeLook
extends Module {
    ModeSetting mode = this.modeSetting("Perspective", "First", Arrays.asList("Front", "Back", "First")).description("Perspective to use when in FreeLook");
    BooleanSetting invertPitch = this.booleanSetting("InvertPitch", false);
    BooleanSetting invertYaw = this.booleanSetting("InvertYaw", false);
    private static FreeLook instance;
    private static float yaw;
    private static float pitch;
    private static float prevYaw;
    private static float prevPitch;

    public FreeLook() {
        instance = this;
    }

    @Override
    public void onEnable() {
        yaw = FreeLook.mc.thePlayer.rotationYaw;
        pitch = FreeLook.mc.thePlayer.rotationPitch;
    }

    public static boolean enabled() {
        return instance.isEnabled();
    }

    public static void updateAngle(float yawChange, float pitchChange) {
        prevPitch = pitch;
        prevYaw = yaw;
        if (((Boolean)FreeLook.instance.invertPitch.getValue()).booleanValue()) {
            pitchChange *= -1.0f;
        }
        if (((Boolean)FreeLook.instance.invertYaw.getValue()).booleanValue()) {
            yawChange *= -1.0f;
        }
        yaw = (float)((double)yaw + (double)yawChange * 0.15);
        pitch = (float)((double)pitch - (double)pitchChange * 0.15);
        pitch = MathHelper.clamp_float(pitch, -90.0f, 90.0f);
    }

    public static float getYaw() {
        if (!instance.isEnabled()) return FreeLook.mc.getRenderViewEntity().rotationYaw;
        return yaw;
    }

    public static float getPrevYaw() {
        if (!instance.isEnabled()) return FreeLook.mc.getRenderViewEntity().prevRotationYaw;
        return prevYaw;
    }

    public static float getPrevPitch() {
        if (!instance.isEnabled()) return FreeLook.mc.getRenderViewEntity().prevRotationPitch;
        return prevPitch;
    }

    public static float getPitch() {
        if (!instance.isEnabled()) return FreeLook.mc.getRenderViewEntity().rotationPitch;
        return pitch;
    }

    public static int getPerspective() {
        if (Freecam.fakePlayer != null) {
            return 0;
        }
        if (!instance.isEnabled()) {
            return FreeLook.mc.gameSettings.thirdPersonView;
        }
        switch ((String)FreeLook.instance.mode.getValue()) {
            case "First": {
                return 0;
            }
            case "Back": {
                return 1;
            }
        }
        return 2;
    }
}

