package net.caffeinemc.phosphor.module.modules.combat;

import net.caffeinemc.phosphor.module.setting.settings.BooleanSetting;
import net.caffeinemc.phosphor.module.setting.settings.ModeSetting;
import net.caffeinemc.phosphor.module.setting.settings.NumberSetting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.caffeinemc.phosphor.api.event.events.MouseUpdateEvent;
import net.caffeinemc.phosphor.api.event.orbit.EventHandler;
import net.caffeinemc.phosphor.api.util.PlayerUtils;
import net.caffeinemc.phosphor.api.rotation.RotationUtils;
import net.caffeinemc.phosphor.module.Module;

public class AimAssistModule extends Module {
    public final ModeSetting aimAt = new ModeSetting("Aim at", this, "Head", "Chest", "Legs", "Head");
    public final BooleanSetting lookAtNearest = new BooleanSetting("Look At Corner", this, false);
    public final BooleanSetting yawAssist = new BooleanSetting("Horizontal", this, true);
    public final NumberSetting yawSpeed = new NumberSetting("Horizontal Speed", this, 1d, 0.1d, 10d, 0.1d);
    public final BooleanSetting pitchAssist = new BooleanSetting("Vertical", this, true);
    public final NumberSetting pitchSpeed = new NumberSetting("Vertical Speed", this, 0.5d, 0.1d, 10d, 0.1d);
    public final NumberSetting distance = new NumberSetting("Distance", this, 6d, 3d, 10d, 0.1d);
    public final NumberSetting fov = new NumberSetting("FOV", this, 180d, 1d, 360d, 1d);
    public final BooleanSetting seeOnly = new BooleanSetting("See Only", this, true);

    public AimAssistModule() {
        super("AimAssist", "Automatically Aims at players for you.", Category.COMBAT);
    }

    @EventHandler
    public void onMouseUpdate(MouseUpdateEvent event) {
        if (mc.currentScreen == null) {
            PlayerEntity targetPlayer = PlayerUtils.findNearestPlayer(mc.player, distance.getFValue(), seeOnly.isEnabled());

            if (targetPlayer == null)
                return;

            Vec3d targetPlayerPos = targetPlayer.getPos();

            switch (aimAt.getMode()) {
                case "Chest" -> {
                    targetPlayerPos = targetPlayerPos.add(0, -0.5, 0);
                }
                case "Legs" -> {
                    targetPlayerPos = targetPlayerPos.add(0, -1.2, 0);
                }
            }

            if (lookAtNearest.isEnabled()) {
                double offsetX;

                if (mc.player.getX() - targetPlayer.getX() > 0) {
                    offsetX = 0.29;
                } else {
                    offsetX = -0.29;
                }

                double offsetZ;

                if (mc.player.getZ() - targetPlayer.getZ() > 0) {
                    offsetZ = 0.29;
                } else {
                    offsetZ = -0.29;
                }

                targetPlayerPos = targetPlayerPos.add(offsetX, 0, offsetZ);
            }

            RotationUtils.Rotation targetRot = RotationUtils.getDirection(mc.player, targetPlayerPos);

            if (RotationUtils.getAngleToRotation(targetRot) > fov.getValue() / 2)
                return;

            float yawStrength = yawSpeed.getFValue() / 50;
            float pitchStrength = pitchSpeed.getFValue() / 50;

            float yaw = MathHelper.lerpAngleDegrees(yawStrength, mc.player.getYaw(), (float) targetRot.yaw());
            float pitch = MathHelper.lerpAngleDegrees(pitchStrength, mc.player.getPitch(), (float) targetRot.pitch());

            if (yawAssist.isEnabled()) mc.player.setYaw(yaw);
            if (pitchAssist.isEnabled()) mc.player.setPitch(pitch);
        }
    }
}
