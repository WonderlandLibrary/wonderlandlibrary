package org.dreamcore.client.feature.impl.movement;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.MathHelper;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventPreMotion;
import org.dreamcore.client.event.events.impl.player.EventUpdate;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.player.MovementHelper;
import org.dreamcore.client.settings.impl.ListSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

public class Flight extends Feature {

    public static ListSetting flyMode;
    public static NumberSetting speed;

    public Flight() {
        super("Flight", "Позволяет вам летать без креатив режима", Type.Movement);
        flyMode = new ListSetting("Flight Mode", "Vanilla", () -> true, "Vanilla", "MineLand", "Matrix 6.3.0", "WellMore", "Matrix Boost 6.2.2", "Matrix Glide", "Matrix Web");
        speed = new NumberSetting("Flight Speed", 5F, 0.1F, 15F, 0.1F, () -> flyMode.currentMode.equals("Vanilla") || flyMode.currentMode.equals("WellMore"));
        //   TPAmount = new NumberSetting("TPAmount", 25, 5, 100, 1, () -> flyMode.currentMode.equals("ReallyWorld"));
        addSettings(flyMode, speed);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.player.speedInAir = 0.02f;
        mc.timer.timerSpeed = 1.0f;
        mc.player.capabilities.isFlying = false;
        if (flyMode.getOptions().equalsIgnoreCase("WellMore")) {
            mc.player.motionY = 0;
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        String mode = flyMode.getCurrentMode();
        if (mode.equalsIgnoreCase("Matrix Glide")) {
            if (!mc.player.onGround && mc.player.fallDistance >= 1) {
                mc.timer.timerSpeed = mc.player.ticksExisted % 4 == 0 ? 0.08f : 0.5F;
                mc.player.motionY *= 0.007;
                mc.player.fallDistance = 0F;
            } else {
                mc.timer.timerSpeed = 1;
            }
        }
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        String mode = flyMode.getCurrentMode();

        setSuffix(flyMode.getCurrentMode());

        if (mode.equalsIgnoreCase("Matrix 6.3.0")) {
            double yMotion = 1.0E-12;
            mc.player.fallDistance = (float) yMotion;
            mc.player.motionY = 0.4;
            mc.player.onGround = false;
            double f = Math.toRadians(mc.player.rotationYaw);
            mc.player.motionX -= MathHelper.sin((float) f) * 0.35f;
            mc.player.motionZ += MathHelper.cos((float) f) * 0.35f;
            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.player.motionY -= speed.getNumberValue();
            } else if (mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.player.motionY += speed.getNumberValue();
            }
        } else if (mode.equals("MineLand")) {
            if (mc.gameSettings.keyBindForward.isKeyDown()) {
                float yaw = (float) Math.toRadians(mc.player.rotationYaw);
                double x = -MathHelper.sin(yaw) * 0.25;
                double z = MathHelper.cos(yaw) * 0.25;
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX + x, mc.player.posY, mc.player.posZ + z, false));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX + x, mc.player.posY - 490, mc.player.posZ + z, true));
                mc.player.motionY = 0;
            }
        } else if (mode.equalsIgnoreCase("Wellmore")) {
            if (mc.player.onGround) {
                mc.player.jump();
            } else {
                mc.player.motionX = 0;
                mc.player.motionZ = 0;
                mc.player.motionY = -0.01;
                MovementHelper.setSpeed(speed.getNumberValue());
                mc.player.speedInAir = 0.3f;
                if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.player.motionY -= speed.getNumberValue();
                } else if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.player.motionY += speed.getNumberValue();
                }
            }
        } else if (mode.equalsIgnoreCase("Matrix Boost 6.2.2")) {
            mc.player.motionY = 0.6;
            if (mc.gameSettings.keyBindForward.pressed && !mc.player.onGround) {
                double f = Math.toRadians(mc.player.rotationYaw);
                mc.player.motionX -= MathHelper.sin((float) f) * 0.27f;
                mc.player.motionZ += MathHelper.cos((float) f) * 0.27f;
            }
        } else if (mode.equalsIgnoreCase("Vanilla")) {
            mc.player.capabilities.isFlying = true;
            MovementHelper.setSpeed(speed.getNumberValue());
            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.player.motionY -= speed.getNumberValue();
            } else if (mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.player.motionY += speed.getNumberValue();
            }

        } else if (mode.equalsIgnoreCase("Matrix Web")) {
            if (mc.player.isInWeb) {
                mc.player.isInWeb = false;
                mc.player.motionY *= mc.player.ticksExisted % 2 == 0 ? -100 : -0.05;
            }
        }
    }
}