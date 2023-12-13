/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.player;

import java.util.ArrayList;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.module.combat.KillAura;
import me.wintware.client.utils.other.MathUtils;
import me.wintware.client.utils.other.TimerHelper;
import net.minecraft.client.Minecraft;

public class AntiAim
extends Module {
    public float yaw;
    public float pitch;
    public Boolean sneak;
    float[] lastAngles;
    public static float rotationPitch;
    private boolean fake;
    private boolean fake1;
    private boolean shouldsneak;
    TimerHelper fakeJitter = new TimerHelper();

    public AntiAim() {
        super("AntiAim", Category.Player);
        ArrayList<String> options = new ArrayList<String>();
        options.add("Reverse");
        options.add("Jittery");
        options.add("XD");
        options.add("ClientSpin");
        options.add("SpinSlow");
        options.add("SpinFast");
        options.add("Aside");
        options.add("Glitchy");
        Main.instance.setmgr.rSetting(new Setting("Spin Mode", this, "SpinSlow", options));
        Main.instance.setmgr.rSetting(new Setting("CustomPitch", this, true));
        Main.instance.setmgr.rSetting(new Setting("CustomPitchValue", this, 90.0, -90.0, 90.0, true));
        Main.instance.setmgr.rSetting(new Setting("SpinSpeed", this, 5.0, 0.0, 50.0, true));
    }

    @EventTarget
    public void onPreMotion(EventPreMotionUpdate event) {
        float sens;
        float yaw8;
        float n;
        String mode = Main.instance.setmgr.getSettingByName("Spin Mode").getValString();
        this.setSuffix(mode);
        if (!AntiAim.mc.gameSettings.keyBindUseItem.pressed && KillAura.target == null) {
            if (this.lastAngles == null) {
                float[] arrf = new float[2];
                arrf[0] = Minecraft.player.rotationYaw;
                arrf[1] = Minecraft.player.rotationPitch;
                this.lastAngles = arrf;
            }
            boolean fake = !this.fake;
            this.fake = fake;
            if (mode.equalsIgnoreCase("Jitter")) {
                this.yaw = n = this.lastAngles[0] + 90.0f;
                this.lastAngles = new float[]{n, this.lastAngles[1]};
                this.updateAngles(n, this.lastAngles[1]);
                Minecraft.player.renderYawOffset = n;
                Minecraft.player.prevRenderYawOffset = n;
                Minecraft.player.rotationYawHead = n;
            }
        }
        if (Main.instance.setmgr.getSettingByName("CustomPitch").getValue()) {
            float pp = Main.instance.setmgr.getSettingByName("CustomPitchValue").getValFloat();
            event.setPitch(pp);
            Minecraft.player.rotationPitchHead = pp;
        }
        float sp = Main.instance.setmgr.getSettingByName("SpinSpeed").getValFloat();
        if (mode.equalsIgnoreCase("SpinSlow")) {
            yaw8 = this.lastAngles[0] + sp;
            this.lastAngles = new float[]{yaw8, this.lastAngles[1]};
            sens = this.getSensitivityMultiplier();
            float yaw = yaw8 + (float)MathUtils.getRandomInRange(1, -3);
            float yawGCD = (float)Math.round(yaw / sens) * sens;
            event.setYaw(yaw);
            this.updateAngles(yaw8, this.lastAngles[1]);
        }
        if (mode.equalsIgnoreCase("Jittery")) {
            this.yaw = n = this.lastAngles[0] + 90.0f;
            this.lastAngles = new float[]{n, this.lastAngles[1]};
            sens = this.getSensitivityMultiplier();
            event.setYaw((float)Math.round(n / sens) * sens);
            this.updateAngles(n, this.lastAngles[1]);
        }
        if (mode.equalsIgnoreCase("ClientSpin")) {
            yaw8 = this.lastAngles[0] + 10.0f;
            this.lastAngles = new float[]{yaw8, this.lastAngles[1]};
            sens = this.getSensitivityMultiplier();
            Minecraft.player.rotationYaw = (float)Math.round(yaw8 / sens) * sens;
            this.updateAngles(yaw8, this.lastAngles[1]);
        }
        if (mode.equalsIgnoreCase("SpinFast")) {
            float yaw7 = this.lastAngles[0] + 45.0f;
            this.lastAngles = new float[]{yaw7, this.lastAngles[1]};
            sens = this.getSensitivityMultiplier();
            event.setYaw((float)Math.round(yaw7 / sens) * sens);
            this.updateAngles(yaw7, this.lastAngles[1]);
        }
        if (mode.equalsIgnoreCase("XD")) {
            float yaw = this.lastAngles[0] + 150000.0f;
            this.lastAngles = new float[]{yaw, this.lastAngles[1]};
            sens = this.getSensitivityMultiplier();
            event.setYaw((float)Math.round(yaw / sens) * sens);
            this.updateAngles(yaw, this.lastAngles[1]);
        }
        if (mode.equalsIgnoreCase("Reverse")) {
            float yaw2 = Minecraft.player.rotationYaw + 180.0f;
            this.lastAngles = new float[]{yaw2, this.lastAngles[1]};
            sens = this.getSensitivityMultiplier();
            event.setYaw((float)Math.round(yaw2 / sens) * sens);
            this.updateAngles(yaw2, this.lastAngles[1]);
        }
        if (mode.equalsIgnoreCase("Aside")) {
            float yaw3 = Minecraft.player.rotationYaw - 90.0f;
            this.lastAngles = new float[]{yaw3, this.lastAngles[1]};
            sens = this.getSensitivityMultiplier();
            event.setYaw((float)Math.round(yaw3 / sens) * sens);
            this.updateAngles(yaw3, this.lastAngles[1]);
        }
        if (mode.equalsIgnoreCase("Glitchy")) {
            float yaw6 = (float)((double)(Minecraft.player.rotationYaw + 5.0f) + Math.random() * 175.0);
            this.lastAngles = new float[]{yaw6, this.lastAngles[1]};
            sens = this.getSensitivityMultiplier();
            event.setYaw((float)Math.round(yaw6 / sens) * sens);
            this.updateAngles(yaw6, this.lastAngles[1]);
        }
    }

    private float getSensitivityMultiplier() {
        float f = AntiAim.mc.gameSettings.mouseSensitivity * 0.6f + 0.2f;
        return f * f * f * 8.0f * 0.15f;
    }

    public void updateAngles(float n, float rotationPitch) {
        if (AntiAim.mc.gameSettings.thirdPersonView != 0) {
            AntiAim.rotationPitch = rotationPitch;
            Minecraft.player.rotationYawHead = n;
            Minecraft.player.renderYawOffset = n;
        }
    }
}

