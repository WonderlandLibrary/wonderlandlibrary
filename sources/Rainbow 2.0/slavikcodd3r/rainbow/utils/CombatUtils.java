package slavikcodd3r.rainbow.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import slavikcodd3r.rainbow.friend.FriendManager;
import slavikcodd3r.rainbow.module.modules.combat.AimAssist;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class CombatUtils
{
    public static void tpToEnt(final Entity entity) {
        double curX = ClientUtils.player().posX;
        double curY = ClientUtils.player().posY;
        double curZ = ClientUtils.player().posZ;
        final double endX = entity.getPosition().getX();
        final double endY = entity.getPosition().getY();
        final double endZ = entity.getPosition().getZ();
        double distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
        int count = 0;
        while (distance > 0.0) {
            distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
            if (count > 120) {
                break;
            }
            final boolean next = false;
            final double diffX = curX - endX;
            final double diffY = curY - endY;
            final double diffZ = curZ - endZ;
            final double offset = ((count & 0x1) == 0x0) ? 0.4 : 0.1;
            if (diffX < 0.0) {
                if (Math.abs(diffX) > offset) {
                    curX += offset;
                }
                else {
                    curX += Math.abs(diffX);
                }
            }
            if (diffX > 0.0) {
                if (Math.abs(diffX) > offset) {
                    curX -= offset;
                }
                else {
                    curX -= Math.abs(diffX);
                }
            }
            if (diffY < 0.0) {
                if (Math.abs(diffY) > 0.25) {
                    curY += 0.25;
                }
                else {
                    curY += Math.abs(diffY);
                }
            }
            if (diffY > 0.0) {
                if (Math.abs(diffY) > 0.25) {
                    curY -= 0.25;
                }
                else {
                    curY -= Math.abs(diffY);
                }
            }
            if (diffZ < 0.0) {
                if (Math.abs(diffZ) > offset) {
                    curZ += offset;
                }
                else {
                    curZ += Math.abs(diffZ);
                }
            }
            if (diffZ > 0.0) {
                if (Math.abs(diffZ) > offset) {
                    curZ -= offset;
                }
                else {
                    curZ -= Math.abs(diffZ);
                }
            }
            Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY, curZ, true));
            ++count;
        }
    }
    
    public static float getPitchChange(final Entity local_01) {
        final double local_2 = local_01.posX - ClientUtils.mc().thePlayer.posX;
        final double local_3 = local_01.posZ - ClientUtils.mc().thePlayer.posZ;
        final double local_4 = local_01.posY - 2.2 + local_01.getEyeHeight() - ClientUtils.mc().thePlayer.posY;
        final double local_5 = MathHelper.sqrt_double(local_2 * local_2 + local_3 * local_3);
        final double local_6 = -Math.toDegrees(Math.atan(local_4 / local_5));
        return -MathHelper.wrapAngleTo180_float(ClientUtils.mc().thePlayer.rotationPitch - (float)local_6) - 2.5f;
    }
    
    public static float getYawChange(final Entity local_01) {
        final double local_2 = local_01.posX - ClientUtils.mc().thePlayer.posX;
        final double local_3 = local_01.posZ - ClientUtils.mc().thePlayer.posZ;
        double local_4 = 0.0;
        if (local_3 < 0.0 && local_2 < 0.0) {
            local_4 = 90.0 + Math.toDegrees(Math.atan(local_3 / local_2));
        }
        else if (local_3 < 0.0 && local_2 > 0.0) {
            local_4 = -90.0 + Math.toDegrees(Math.atan(local_3 / local_2));
        }
        else {
            local_4 = Math.toDegrees(-Math.atan(local_2 / local_3));
        }
        return MathHelper.wrapAngleTo180_float(-(ClientUtils.mc().thePlayer.rotationYaw - (float)local_4));
    }
    
    public static boolean isEntityValid(final Entity local_01) {
        if (local_01 instanceof EntityLivingBase) {
            if (!ClientUtils.player().isEntityAlive() || !((EntityLivingBase)local_01).isEntityAlive() || local_01.getDistanceToEntity(ClientUtils.player()) > (ClientUtils.player().canEntityBeSeen(local_01) ? AimAssist.range : 3.0)) {
                return false;
            }
            final double local_2 = local_01.posX - ClientUtils.player().posX;
            final double local_3 = local_01.posZ - ClientUtils.player().posZ;
            final double local_4 = ClientUtils.player().posY + ClientUtils.player().getEyeHeight() - (local_01.posY + local_01.getEyeHeight());
            final double local_5 = Math.sqrt(local_2 * local_2 + local_3 * local_3);
            final float local_6 = (float)(Math.atan2(local_3, local_2) * 180.0 / 3.141592653589793) - 90.0f;
            final float local_7 = (float)(Math.atan2(local_4, local_5) * 180.0 / 3.141592653589793);
            final double local_8 = RotationUtils.getDistanceBetweenAngles(local_6, ClientUtils.player().rotationYaw % 360.0f);
            final double local_9 = RotationUtils.getDistanceBetweenAngles(local_7, ClientUtils.player().rotationPitch % 360.0f);
            final double local_10 = Math.sqrt(local_8 * local_8 + local_9 * local_9);
            if (local_10 > AimAssist.degrees) {
                return false;
            }
            if (local_01 instanceof EntityPlayer) {
                return !FriendManager.isFriend(((EntityPlayer)local_01).getName());
            }
        }
        return false;
    }
}
