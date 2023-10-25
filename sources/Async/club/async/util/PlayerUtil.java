package club.async.util;

import club.async.interfaces.MinecraftInterface;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public final class PlayerUtil implements MinecraftInterface {

    public static double getEffectiveHealth(EntityLivingBase entity) {
        return entity.getHealth() * (entity.getMaxHealth() / entity.getTotalArmorValue());
    }

    public static double getHorizontalDistanceToEntity(Entity entity) {
        float f = (float)(mc.thePlayer.posX - entity.posX);
        float f2 = (float)(mc.thePlayer.posZ - entity.posZ);
        return MathHelper.sqrt_float(f * f + f2 * f2);
    }

    public static boolean checkVoid() {
        for(int i = (int) mc.thePlayer.posY; i > 0; i--) {
            if(!(WorldUtil.getBlock(new BlockPos(mc.thePlayer.posX, i, mc.thePlayer.posZ)) instanceof BlockAir)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isChestEmpty() {
        if(mc.thePlayer.openContainer instanceof ContainerChest) {
            ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;
            for (int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); ++i) {
                if (chest.getLowerChestInventory().getStackInSlot(i) != null) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}
