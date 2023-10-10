package client.util;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.util.vector.Vector2f;

@UtilityClass
public class RotationUtil implements MinecraftInstance {
    @Getter
    private Vector2f previousRotation, rotation;
    public void setRotation(final Vector2f newRotation) {
        previousRotation = rotation != null ? rotation : new Vector2f(mc.thePlayer.rotationPitch, mc.thePlayer.rotationYaw);
        rotation = newRotation;
        if (rotation != null) {
            mc.thePlayer.renderYawOffset = rotation.y;
            mc.thePlayer.rotationYawHead = rotation.y;
        }
        mc.thePlayer.prevRenderYawOffset = previousRotation.y;
        mc.thePlayer.prevRotationYawHead = previousRotation.y;
    }
    public Vector2f get(final Vec3 to) {
        final Vec3 diff = to.subtract(new Vec3(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ));
        return new Vector2f((float) -Math.toDegrees(Math.atan2(diff.yCoord, Math.hypot(diff.xCoord, diff.zCoord))), (float) Math.toDegrees(Math.atan2(diff.zCoord, diff.xCoord)) - 90);
    }
    public Vector2f get(final Entity entity) {
        return get(new Vec3(entity.posX, entity.getEntityBoundingBox().minY + entity.getEyeHeight(), entity.posZ));
    }
    public Vector2f getGCD(final Entity entity) {
        return fixedSensitivity(mc.gameSettings.mouseSensitivity, get(entity));
    }
    public float getFixedAngleDelta(final float sensitivity) {
        return (float) (Math.pow(sensitivity * 0.6f + 0.2f, 3) * 1.2f);
    }
    public float getFixedSensitivityAngle(final float targetAngle, final float startAngle, final float gcd) {
        return startAngle + Math.round((targetAngle - startAngle) / gcd) * gcd;
    }
    public Vector2f fixedSensitivity(final float sensitivity, final Vector2f rotation) {
        final float gcd = getFixedAngleDelta(sensitivity);
        return new Vector2f(getFixedSensitivityAngle(rotation.x, mc.thePlayer.lastReportedPitch, gcd), getFixedSensitivityAngle(rotation.y, mc.thePlayer.lastReportedYaw, gcd));
    }
    public static Vec3 getVectorForRotation(final float[] rotation) {
        float yawCos = MathHelper.cos(-rotation[0] * 0.017453292F - (float) Math.PI);
        float yawSin = MathHelper.sin(-rotation[0] * 0.017453292F - (float) Math.PI);
        float pitchCos = -MathHelper.cos(-rotation[1] * 0.017453292F);
        float pitchSin = MathHelper.sin(-rotation[1] * 0.017453292F);
        return new Vec3(yawSin * pitchCos, pitchSin, yawCos * pitchCos);
    }
}