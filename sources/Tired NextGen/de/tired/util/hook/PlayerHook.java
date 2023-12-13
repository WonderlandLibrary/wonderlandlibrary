package de.tired.util.hook;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.experimental.UtilityClass;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

@UtilityClass
public class PlayerHook {

    private final Minecraft mc = Minecraft.getMinecraft();

    public boolean isMoving() {
        return mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0;
    }

    public void stop() {
        mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
    }

    public double getPredictedMotionY(final double motionY) {
        return (motionY - 0.08) * 0.98F;
    }

    public void increaseSpeedWithStrafe(final double speed) {
        mc.thePlayer.motionX = -MathHelper.sin(getDirection()) * speed;
        mc.thePlayer.motionZ = MathHelper.cos(getDirection()) * speed;
    }

    public void increaseSpeedWithStrafe(final double speed, float yaw) {
        mc.thePlayer.motionX = -MathHelper.sin(getDirection(yaw)) * speed;
        mc.thePlayer.motionZ = MathHelper.cos(getDirection(yaw)) * speed;
    }


    public Block getBlockRelativeToPlayer(final double offsetX, final double offsetY, final double offsetZ) {
        return mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX + offsetX, mc.thePlayer.posY + offsetY, mc.thePlayer.posZ + offsetZ)).getBlock();
    }

    public Block getBlock(final double offsetX, final double offsetY, final double offsetZ) {
        return mc.theWorld.getBlockState(new BlockPos(offsetX, offsetY, offsetZ)).getBlock();
    }

    public void setupStrafe() {
        mc.thePlayer.motionX = -MathHelper.sin(getDirection()) * getSpeed();
        mc.thePlayer.motionZ = MathHelper.cos(getDirection()) * getSpeed();
    }
    public float getDirection() {
        float yaw = mc.thePlayer.rotationYaw;
        final float forward = mc.thePlayer.moveForward;
        final float strafe = mc.thePlayer.moveStrafing;
        yaw += ((forward < 0.0f) ? 180 : 0);
        final int i = (forward < 0.0f) ? -45 : ((forward == 0.0f) ? 90 : 45);
        if (strafe < 0.0f) {
            yaw += i;
        }
        if (strafe > 0.0f) {
            yaw -= i;
        }
        return yaw * 0.017453292f;
    }

    public float getDirection(float yaw) {
        final float forward = mc.thePlayer.moveForward;
        final float strafe = mc.thePlayer.moveStrafing;
        yaw += ((forward < 0.0f) ? 180 : 0);
        final int i = (forward < 0.0f) ? -45 : ((forward == 0.0f) ? 90 : 45);
        if (strafe < 0.0f) {
            yaw += i;
        }
        if (strafe > 0.0f) {
            yaw -= i;
        }
        return yaw * 0.017453292f;
    }

    public float getSpeed() {
        if (mc.thePlayer == null || mc.theWorld == null) return 0;
        return (float) Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
    }

}
