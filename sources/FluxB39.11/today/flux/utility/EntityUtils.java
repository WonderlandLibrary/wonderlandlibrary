package today.flux.utility;

import com.soterdev.SoterObfuscator;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import today.flux.Flux;

public class EntityUtils {
    public static Minecraft mc = Minecraft.getMinecraft();

    public static boolean hasFakeInvisible(EntityLivingBase entity) {
        for (PotionEffect effect : entity.getActivePotionEffects()) {
            if (effect.getPotionID() <= 0)
                continue;

            if (effect.getPotionID() == Potion.invisibility.getId() && !effect.getIsShowParticles()) {
                return true;
            }
        }

        return false;
    }

    // @SoterObfuscator.Obfuscation(flags = "+native")
    public static void damagePlayer(int amount) {
        for (int index = 0; index < amount; ++index) {
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.06D, mc.thePlayer.posZ, false));
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
        }

        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.1D, mc.thePlayer.posZ, false));
    }

    public static double[] interpolate(Entity entity) {
        double partialTicks = mc.timer.renderPartialTicks;
        double[] pos = new double[]{entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks};
        return pos;
    }

    public static void blinkToPos(double[] startPos, BlockPos endPos, double slack, double[] pOffset) {
        double curX = startPos[0];
        double curY = startPos[1];
        double curZ = startPos[2];
        double endX = endPos.getX();
        double endY = endPos.getY();
        double endZ = endPos.getZ();
        double distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
        int count = 0;
        while (distance > slack) {
            distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
            if (count > 120) {
                break;
            }
            boolean next = false;
            double diffX = curX - endX;
            double diffY = curY - endY;
            double diffZ = curZ - endZ;
            double offset = ((count & 0x1) == 0x0) ? pOffset[0] : pOffset[1];
            if (diffX < 0.0) {
                curX += Math.min(Math.abs(diffX), offset);
            }
            if (diffX > 0.0) {
                curX -= Math.min(Math.abs(diffX), offset);
            }
            if (diffY < 0.0) {
                curY += Math.min(Math.abs(diffY), 0.25);
            }
            if (diffY > 0.0) {
                curY -= Math.min(Math.abs(diffY), 0.25);
            }
            if (diffZ < 0.0) {
                curZ += Math.min(Math.abs(diffZ), offset);
            }
            if (diffZ > 0.0) {
                curZ -= Math.min(Math.abs(diffZ), offset);
            }
            mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(curX, curY, curZ, true));
            ++count;
        }
    }

    public static float[] getEntityRotations(final Entity target) {
        final double var4 = target.posX - mc.thePlayer.posX;
        final double var5 = target.posZ - mc.thePlayer.posZ;
        final double var6 = target.posY + target.getEyeHeight() / 1.3 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        final double var7 = MathHelper.sqrt_double(var4 * var4 + var5 * var5);
        final float yaw = (float) (Math.atan2(var5, var4) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float) (-(Math.atan2(var6, var7) * 180.0 / 3.141592653589793));
        return new float[]{yaw, pitch};
    }

    public static EntityLivingBase findClosestEntity() {
        double distance = Double.MAX_VALUE;
        EntityLivingBase entity = null;
        for (final Object object : mc.theWorld.loadedEntityList) {
            if (object instanceof EntityLivingBase) {
                final EntityLivingBase e = (EntityLivingBase) object;
                if (e.getDistanceToEntity(mc.thePlayer) >= distance || !isValid(e)) {
                    continue;
                }
                entity = e;
                distance = e.getDistanceToEntity(mc.thePlayer);
            }
        }
        return entity;
    }

    public static float limitAngleChange(final float current, final float intended, final float maxChange) {
        float change = intended - current;
        if (change > maxChange)
            change = maxChange;
        else if (change < -maxChange)
            change = -maxChange;
        return current + change;
    }

    public static boolean isValid(final EntityLivingBase entity) {
        return entity != null && entity != mc.thePlayer && entity.getDistanceToEntity(mc.thePlayer) <= 10000 && entity.isEntityAlive() && (!entity.isInvisible() || entity.getTotalArmorValue() != 0) && !Flux.INSTANCE.getFriendManager().isFriend(entity.getName());
    }

}
