package me.dev.legacy.features.modules.movement;

import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import me.dev.legacy.util.EntityUtil;
import me.dev.legacy.util.MotionUtil;
import net.minecraft.network.play.client.CPacketPlayer;

import java.text.DecimalFormat;

public class Step extends Module {
    public Step() {
        super("Step", "Allows you to step up blocks", Module.Category.MOVEMENT, true, false, false);
    }

    Setting<Double> height = this.register(new Setting<Double>("Height", 2.5, 0.5, 2.5));
    Setting<Boolean> timer = this.register(new Setting<Boolean>("Timer", false));
    Setting<Boolean> reverse = this.register(new Setting<Boolean>("Reverse", false));
    public Setting<Boolean> vanilla = this.register(new Setting<Boolean>("Vanilla", false));

    private int ticks = 0;

    public void onUpdate() {
        if (mc.world == null || mc.player == null) {
            return;
        }

        if (mc.player.isInWater() || mc.player.isInLava() || mc.player.isOnLadder() || mc.gameSettings.keyBindJump.isKeyDown()) {
            return;
        }
        if (vanilla.getValue()) {
            DecimalFormat df = new DecimalFormat("#");
            mc.player.stepHeight = Float.parseFloat(df.format(height.getValue()));
        } else {
            if (timer.getValue()) {
                if (this.ticks == 0) {
                    EntityUtil.resetTimer();
                } else {
                    this.ticks--;
                }
            }
        }
        if (mc.player != null && mc.player.onGround && !mc.player.isInWater() && !mc.player.isOnLadder() && this.reverse.getValue()) {
            for (double y = 0.0; y < this.height.getValue() + 0.5; y += 0.01) {
                if (!mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, -y, 0.0)).isEmpty()) {
                    mc.player.motionY = -10.0;
                    break;
                }
            }
        }
        double[] dir = MotionUtil.forward(0.1);
        boolean twofive = false;
        boolean two = false;
        boolean onefive = false;
        boolean one = false;
        if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 2.6, dir[1])).isEmpty() && !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 2.4, dir[1])).isEmpty()) {
            twofive = true;
        }
        if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 2.1, dir[1])).isEmpty() && !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 1.9, dir[1])).isEmpty()) {
            two = true;
        }
        if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 1.6, dir[1])).isEmpty() && !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 1.4, dir[1])).isEmpty()) {
            onefive = true;
        }
        if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 1.0, dir[1])).isEmpty() && !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 0.6, dir[1])).isEmpty()) {
            one = true;
        }
        if (mc.player.collidedHorizontally && (mc.player.moveForward != 0.0f || mc.player.moveStrafing != 0.0f) && mc.player.onGround) {
            if (one && this.height.getValue() >= 1.0) {
                final double[] oneOffset = {0.42, 0.753};
                for (int i = 0; i < oneOffset.length; i++) {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + oneOffset[i], mc.player.posZ, mc.player.onGround));
                }
                if (timer.getValue()) {
                    EntityUtil.setTimer(0.6f);
                }
                mc.player.setPosition(mc.player.posX, mc.player.posY + 1.0, mc.player.posZ);
                this.ticks = 1;
            }
            if (onefive && this.height.getValue() >= 1.5) {
                final double[] oneFiveOffset = {0.42, 0.75, 1.0, 1.16, 1.23, 1.2};
                for (int i = 0; i < oneFiveOffset.length; i++) {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + oneFiveOffset[i], mc.player.posZ, mc.player.onGround));
                }
                if (timer.getValue()) {
                    EntityUtil.setTimer(0.35f);
                }
                mc.player.setPosition(mc.player.posX, mc.player.posY + 1.5, mc.player.posZ);
                this.ticks = 1;
            }
            if (two && this.height.getValue() >= 2.0) {
                final double[] twoOffset = {0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43};
                for (int i = 0; i < twoOffset.length; i++) {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + twoOffset[i], mc.player.posZ, mc.player.onGround));
                }
                if (timer.getValue()) {
                    EntityUtil.setTimer(0.25f);
                }
                mc.player.setPosition(mc.player.posX, mc.player.posY + 2.0, mc.player.posZ);
                this.ticks = 2;
            }
            if (twofive && this.height.getValue() >= 2.5) {
                final double[] twoFiveOffset = {0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907};
                for (int i = 0; i < twoFiveOffset.length; i++) {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + twoFiveOffset[i], mc.player.posZ, mc.player.onGround));
                }
                if (timer.getValue()) {
                    EntityUtil.setTimer(0.15f);
                }
                mc.player.setPosition(mc.player.posX, mc.player.posY + 2.5, mc.player.posZ);
                this.ticks = 2;
            }
        }
    }

    public void onDisable() {
        mc.player.stepHeight = 0.6F;
    }
}