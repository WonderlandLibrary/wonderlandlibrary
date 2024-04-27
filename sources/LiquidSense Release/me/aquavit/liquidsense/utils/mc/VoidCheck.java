package me.aquavit.liquidsense.utils.mc;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.modules.movement.Fly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;

public class VoidCheck extends MinecraftInstance{

    public static boolean checkVoid(EntityLivingBase entity) {
        for(int x = -1; x <= 0; ++x) {
            for(int z = -1; z <= 0; ++z) {
                if (isVoid(x, z, entity)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isVoid(int X, int Z, EntityLivingBase entity){
        Fly fly = (Fly) LiquidSense.moduleManager.getModule(Fly.class);

        if (fly.getState()) {
            return false;
        }
        if (mc.thePlayer.posY < 0.0) {
            return true;
        }
        for (int off = 0; off < entity.posY + 2; off += 2) {
            AxisAlignedBB bb = entity.getEntityBoundingBox().offset(X,-off,Z);
            if (mc.theWorld.getCollidingBoundingBoxes(entity, bb).isEmpty()) {
                continue;
            }
            return false;
        }

        return true;
    }

    public static boolean isBlockUnder() {
        if (mc.thePlayer.posY < 0.0) {
            return false;
        } else {
            int off = 0;

            while(true) {
                if (off >= (int) mc.thePlayer.posY + 2) {
                    return false;
                }

                AxisAlignedBB bb = mc.thePlayer.getEntityBoundingBox().offset(0.0, -off, 0.0);
                if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()) {
                    return true;
                }

                off += 2;
            }
        }
    }

    public static boolean inAir(double height, double plus) {
        if (mc.thePlayer.posY < 0)
            return false;
        for (int off = 0; off < height; off += plus) {
            AxisAlignedBB bb = new AxisAlignedBB(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.posX, mc.thePlayer.posY - off, mc.thePlayer.posZ);
            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()) {
                return true;
            }
        }

        return false;
    }
}
