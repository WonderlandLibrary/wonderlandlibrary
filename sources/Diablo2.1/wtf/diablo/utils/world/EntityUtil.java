package wtf.diablo.utils.world;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.MathHelper;
import wtf.diablo.events.impl.UpdateEvent;

import java.util.ArrayList;

public class EntityUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static EntityLivingBase getClosestEntity(double range) {
        double dist = range;
        EntityLivingBase target = null;
        for (Entity entity : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase player = (EntityLivingBase) entity;
                if (canAttack(player)) {
                    double currentDist = mc.thePlayer.getDistanceToEntity(player);
                    if (currentDist <= dist) {
                        dist = currentDist;
                        target = player;
                    }
                }
            }
        }
        return target;
    }

    public static float getYawChangeToEntity(Entity entity) {
        double deltaX = entity.posX - mc.thePlayer.posX;
        double deltaZ = entity.posZ - mc.thePlayer.posZ;
        double yawToEntity;
        if (deltaZ < 0.0 && deltaX < 0.0) {
            yawToEntity = 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
        } else if (deltaZ < 0.0 && deltaX > 0.0) {
            yawToEntity = -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
        } else {
            yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
        }
        return Double.isNaN(mc.thePlayer.rotationYaw - yawToEntity) ? 0.0f : MathHelper.wrapAngleTo180_float(-(mc.thePlayer.rotationYaw - (float) yawToEntity));
    }


    public static float getPitchChangeToEntity(Entity entity) {
        double deltaX = entity.posX - mc.thePlayer.posX;
        double deltaZ = entity.posZ - mc.thePlayer.posZ;
        double deltaY = entity.posY - 1.6 + entity.getEyeHeight() - mc.thePlayer.posY;
        double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
        return Double.isNaN(mc.thePlayer.rotationPitch - pitchToEntity) ? 0.0f : -MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationPitch - (float) pitchToEntity);
    }

    public static void setRotations(UpdateEvent e, float yaw, float pitch) {
        e.setYaw(yaw);
        e.setPitch(pitch);
        mc.thePlayer.rotationYawHead = yaw;
        mc.thePlayer.rotationPitchHead = pitch;
        mc.thePlayer.renderYawOffset = yaw;
    }

    public static float[] getAngles(Entity e) {
        return new float[] { getYawChangeToEntity(e) + mc.thePlayer.rotationYaw, getPitchChangeToEntity(e) + mc.thePlayer.rotationPitch };
    }
    public static boolean canAttack(Entity entity) {
        if ((!entity.isInvisible())) {
            return entity != mc.thePlayer && entity.isEntityAlive() && mc.thePlayer != null && Minecraft.getMinecraft().theWorld != null && mc.thePlayer.ticksExisted > 30 && entity.ticksExisted > 15;
        } else {
            return false;
        }
    }

    public static void damageVerus() {
        mc.getNetHandler().getNetworkManager().sendPacket(
                new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));

        double val1 = 0;

        for (int i = 0; i <= 6; i++) {
            val1 += 0.5;
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                    mc.thePlayer.posY + val1, mc.thePlayer.posZ, true));
        }

        double val2 = mc.thePlayer.posY + val1;

        ArrayList<Float> vals = new ArrayList<>();

        vals.add(0.07840000152587834f);
        vals.add(0.07840000152587834f);
        vals.add(0.23052736891295922f);
        vals.add(0.30431682745754074f);
        vals.add(0.37663049823865435f);
        vals.add(0.44749789698342113f);
        vals.add(0.5169479491049742f);
        vals.add(0.5850090015087517f);
        vals.add(0.6517088341626192f);
        vals.add(0.1537296175885956f);

        for (float value : vals) {
            val2 -= value;
        }
        mc.thePlayer.sendQueue.addToSendQueue(
                new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, val2, mc.thePlayer.posZ, false));
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));

        mc.getNetHandler().getNetworkManager().sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
        mc.thePlayer.jump();
    }
}
