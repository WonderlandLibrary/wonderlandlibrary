package slavikcodd3r.rainbow.utils;

import net.minecraft.util.AxisAlignedBB;
import java.util.List;
import net.minecraft.util.Vec3;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.client.Minecraft;

public class RayCastUtil
{
    private static Minecraft mc;
    
    static {
        RayCastUtil.mc = Minecraft.getMinecraft();
    }
    
    public static MovingObjectPosition getMouseOver(final float yaw, final float pitch, final float range) {
        final float partialTicks = 1.0f;
        final Entity var2 = RayCastUtil.mc.getRenderViewEntity();
        if (var2 != null && RayCastUtil.mc.theWorld != null) {
            double var3 = RayCastUtil.mc.playerController.getBlockReachDistance();
            MovingObjectPosition objectMouseOver = var2.func_174822_a(var3, 1.0f);
            double var4 = var3;
            final Vec3 var5 = var2.func_174824_e(1.0f);
            var3 = range;
            var4 = range;
            if (objectMouseOver != null) {
                var4 = objectMouseOver.hitVec.distanceTo(var5);
            }
            final Vec3 var6 = var2.getLook(1.0f);
            final Vec3 var7 = var5.addVector(var6.xCoord * var3, var6.yCoord * var3, var6.zCoord * var3);
            Entity pointedEntity = null;
            Vec3 var8 = null;
            final float var9 = 1.0f;
            final List var10 = RayCastUtil.mc.theWorld.getEntitiesWithinAABBExcludingEntity(var2, var2.getEntityBoundingBox().addCoord(var6.xCoord * var3, var6.yCoord * var3, var6.zCoord * var3).expand(1.0, 1.0, 1.0));
            double var11 = var4;
            for (int var12 = 0; var12 < var10.size(); ++var12) {
                final Entity var13 = (Entity) var10.get(var12);
                if (var13.canBeCollidedWith()) {
                    final float var14 = var13.getCollisionBorderSize();
                    final AxisAlignedBB var15 = var13.getEntityBoundingBox().expand(var14, var14, var14);
                    final MovingObjectPosition var16 = var15.calculateIntercept(var5, var7);
                    if (var15.isVecInside(var5)) {
                        if (0.0 < var11 || var11 == 0.0) {
                            pointedEntity = var13;
                            var8 = ((var16 == null) ? var5 : var16.hitVec);
                            var11 = 0.0;
                        }
                    }
                    else if (var16 != null) {
                        final double var17 = var5.distanceTo(var16.hitVec);
                        if (var17 < var11 || var11 == 0.0) {
                            final boolean canRiderInteract = false;
                            if (var13 == var2.ridingEntity && !canRiderInteract) {
                                if (var11 == 0.0) {
                                    pointedEntity = var13;
                                    var8 = var16.hitVec;
                                }
                            }
                            else {
                                pointedEntity = var13;
                                var8 = var16.hitVec;
                                var11 = var17;
                            }
                        }
                    }
                }
            }
            if (pointedEntity != null && (var11 < var4 || RayCastUtil.mc.objectMouseOver == null)) {
                objectMouseOver = new MovingObjectPosition(pointedEntity, var8);
                if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
                    return objectMouseOver;
                }
            }
            return objectMouseOver;
        }
        return null;
    }
}
