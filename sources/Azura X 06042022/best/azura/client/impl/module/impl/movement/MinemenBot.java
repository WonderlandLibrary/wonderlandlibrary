package best.azura.client.impl.module.impl.movement;

import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventReceivedPacket;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.other.DelayUtil;
import best.azura.client.util.player.RotationUtil;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

@ModuleInfo(name = "Minemen Bot",
        description = "haker man minemen",
        category = Category.MOVEMENT)
public class MinemenBot extends Module {

    private final DelayUtil lagBackDelay = new DelayUtil();

    @EventHandler
    public final Listener<EventReceivedPacket> packetInboundEventListener = e -> {
        if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            lagBackDelay.reset();
        }
    };

    @EventHandler
    public final Listener<EventMotion> playerUpdateEventListener = e -> {
        if (e.isPre() || e.isUpdate()) {
            EntityLivingBase target = null;
            double lastDistance = Double.MAX_VALUE, lastArmor = Double.MAX_VALUE, lastHealth = Double.MAX_VALUE;
            boolean lastVisible = false;
            for (final Entity entity : mc.theWorld.loadedEntityList) {
                if (!(entity instanceof EntityPlayer)) continue;
                if (entity == mc.thePlayer) continue;
                if (entity.isInvisibleToPlayer(mc.thePlayer)) continue;
                if (mc.thePlayer.getDistanceToEntity(entity) <= lastDistance &&
                        (((EntityLivingBase) entity).getTotalArmorValue() <= lastArmor ||
                                ((EntityLivingBase) entity).getHealth() <= lastHealth) && (mc.thePlayer.canEntityBeSeen(entity) || !lastVisible)) {
                    target = (EntityLivingBase) entity;
                    lastDistance = mc.thePlayer.getDistanceToEntity(entity);
                    lastArmor = ((EntityLivingBase) entity).getTotalArmorValue();
                    lastHealth = ((EntityLivingBase) entity).getHealth();
                    lastVisible = mc.thePlayer.canEntityBeSeen(entity);
                }
            }
            if (target == null)
                return;
            if (e.isPre()) {
                final float[] rotation = RotationUtil.faceVector(RotationUtil.getClosestPoint(mc.thePlayer.getPositionEyes(1.0f), target.getEntityBoundingBox()));
                mc.thePlayer.rotationYaw = rotation[0];
                mc.thePlayer.rotationPitch = rotation[1];

            }
            if (e.isUpdate()) {
                mc.gameSettings.keyBindLeft.pressed = false;
                mc.gameSettings.keyBindRight.pressed = false;
                Vec3 playerVector = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
                AxisAlignedBB aa = target.getEntityBoundingBox();
                Vec3 entityVector = new Vec3(aa.minX + (aa.maxX - aa.minX) * 0.5, aa.minY + (aa.maxY - aa.minY) * 0.5, aa.minZ + (aa.maxZ - aa.minZ) * 0.5);
                double x = playerVector.xCoord - entityVector.xCoord;
                double z = playerVector.zCoord - entityVector.zCoord;
                float yaw = MathHelper.wrapAngleTo180_float((float) (Math.toDegrees(Math.atan2(z, x)) - 90.0f));
                float diffYaw = MathUtil.getDifference(MathHelper.wrapAngleTo180_float(target.rotationYaw), yaw);
                if (lastDistance < 6 && mc.thePlayer.onGround && diffYaw < 45) {
                    if (yaw > MathHelper.wrapAngleTo180_float(target.rotationYaw)) mc.gameSettings.keyBindLeft.pressed = true;
                    else mc.gameSettings.keyBindRight.pressed = true;
                }
                mc.gameSettings.keyBindForward.pressed = true;
                mc.gameSettings.keyBindUse.pressed = diffYaw < 90 && lastDistance < 3.0 && mc.thePlayer.ticksExisted % 5 != 0 && mc.thePlayer.ticksExisted % 5 != 4;
                if (lastDistance < 6.0 && mc.thePlayer.ticksExisted % MathUtil.getRandom_int(2, 5) == 0)
                    mc.clickMouse();
                if (target.hurtTime >= 8)
                    mc.gameSettings.keyBindForward.pressed = false;
                if (mc.thePlayer.onGround && lagBackDelay.hasReached(500) && (lastDistance > 8 || mc.thePlayer.isCollidedHorizontally) && !mc.gameSettings.keyBindJump.pressed)
                    mc.thePlayer.jump();
            }
        }
    };

}