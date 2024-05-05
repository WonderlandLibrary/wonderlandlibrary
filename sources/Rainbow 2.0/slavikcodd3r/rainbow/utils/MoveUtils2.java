package slavikcodd3r.rainbow.utils;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;

public class MoveUtils2
{
    private static Minecraft mc;
    
    static {
        MoveUtils2.mc = Minecraft.getMinecraft();
    }
    
    public static float getDirection() {
        float yaw = MoveUtils2.mc.thePlayer.rotationYawHead;
        final float forward = MoveUtils2.mc.thePlayer.moveForward;
        final float strafe = MoveUtils2.mc.thePlayer.moveStrafing;
        yaw += ((forward < 0.0f) ? 180 : 0);
        if (strafe < 0.0f) {
            yaw += ((forward < 0.0f) ? -45 : ((forward == 0.0f) ? 90 : 45));
        }
        if (strafe > 0.0f) {
            yaw -= ((forward < 0.0f) ? -45 : ((forward == 0.0f) ? 90 : 45));
        }
        return yaw * 0.017453292f;
    }
    
    public static boolean isMoving(final Entity e) {
        return e.motionX != 0.0 && e.motionZ != 0.0 && (e.motionY != 0.0 || e.motionY > 0.0);
    }
    
    public static boolean isMoving() {
        return MoveUtils2.mc.thePlayer.lastTickPosX != MoveUtils2.mc.thePlayer.posX && MoveUtils2.mc.thePlayer.lastTickPosZ != MoveUtils2.mc.thePlayer.posZ;
    }
    
    public static void setSpeed(final double speed) {
        final EntityPlayerSP player = ClientUtils.player();
        double yaw = player.rotationYaw;
        final boolean isMoving = player.moveForward != 0.0 || player.moveStrafing != 0.0;
        final boolean isMovingForward = player.moveForward > 0.0;
        final boolean isMovingBackward = player.moveForward < 0.0;
        final boolean isMovingRight = player.moveStrafing > 0.0;
        final boolean isMovingLeft = player.moveStrafing < 0.0;
        final boolean isMovingSideways = isMovingLeft || isMovingRight;
        final boolean isMovingStraight = isMovingForward || isMovingBackward;
        if (isMoving) {
            if (isMovingForward && !isMovingSideways) {
                yaw += 0.0;
            }
            else if (isMovingBackward && !isMovingSideways) {
                yaw += 180.0;
            }
            else if (isMovingForward && isMovingLeft) {
                yaw += 45.0;
            }
            else if (isMovingForward) {
                yaw -= 45.0;
            }
            else if (!isMovingStraight && isMovingLeft) {
                yaw += 90.0;
            }
            else if (!isMovingStraight && isMovingRight) {
                yaw -= 90.0;
            }
            else if (isMovingBackward && isMovingLeft) {
                yaw += 135.0;
            }
            else if (isMovingBackward) {
                yaw -= 135.0;
            }
            yaw = Math.toRadians(yaw);
            player.motionX = -Math.sin(yaw) * speed;
            player.motionZ = Math.cos(yaw) * speed;
        }
    }
    
    public static void multiplySpeed(final Float multiplier) {
        final EntityPlayerSP player3;
        final EntityPlayerSP player2;
        final EntityPlayerSP entityPlayerSP6;
        final EntityPlayerSP entityPlayerSP10;
        final EntityPlayerSP entityPlayerSP5 = entityPlayerSP10 = (entityPlayerSP6 = (player2 = (player3 = ClientUtils.player())));
        entityPlayerSP10.motionX *= multiplier;
        final EntityPlayerSP entityPlayerSP9;
        final EntityPlayerSP entityPlayerSP8;
        final EntityPlayerSP entityPlayerSP11;
        final EntityPlayerSP entityPlayerSP7 = entityPlayerSP11 = (entityPlayerSP8 = (entityPlayerSP9 = player3));
        entityPlayerSP11.motionZ *= multiplier;
    }
    
    public static void toFwd(final double amount) {
        final EntityPlayerSP player = ClientUtils.player();
        double yaw = player.rotationYaw;
        yaw = Math.toRadians(yaw);
        final double dX = -Math.sin(yaw) * amount;
        final double dZ = Math.cos(yaw) * amount;
        ClientUtils.player().setPosition(ClientUtils.player().posX + dX, ClientUtils.player().posY, ClientUtils.player().posZ + dZ);
    }
}
