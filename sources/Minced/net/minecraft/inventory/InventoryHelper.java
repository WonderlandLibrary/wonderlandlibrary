// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.Random;

public class InventoryHelper
{
    private static final Random RANDOM;
    
    public static void dropInventoryItems(final World worldIn, final BlockPos pos, final IInventory inventory) {
        dropInventoryItems(worldIn, pos.getX(), pos.getY(), pos.getZ(), inventory);
    }
    
    public static void dropInventoryItems(final World worldIn, final Entity entityAt, final IInventory inventory) {
        dropInventoryItems(worldIn, entityAt.posX, entityAt.posY, entityAt.posZ, inventory);
    }
    
    private static void dropInventoryItems(final World worldIn, final double x, final double y, final double z, final IInventory inventory) {
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            final ItemStack itemstack = inventory.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                spawnItemStack(worldIn, x, y, z, itemstack);
            }
        }
    }
    
    public static void spawnItemStack(final World worldIn, final double x, final double y, final double z, final ItemStack stack) {
        final float f = InventoryHelper.RANDOM.nextFloat() * 0.8f + 0.1f;
        final float f2 = InventoryHelper.RANDOM.nextFloat() * 0.8f + 0.1f;
        final float f3 = InventoryHelper.RANDOM.nextFloat() * 0.8f + 0.1f;
        while (!stack.isEmpty()) {
            final EntityItem entityitem = new EntityItem(worldIn, x + f, y + f2, z + f3, stack.splitStack(InventoryHelper.RANDOM.nextInt(21) + 10));
            final float f4 = 0.05f;
            entityitem.motionX = InventoryHelper.RANDOM.nextGaussian() * 0.05000000074505806;
            entityitem.motionY = InventoryHelper.RANDOM.nextGaussian() * 0.05000000074505806 + 0.20000000298023224;
            entityitem.motionZ = InventoryHelper.RANDOM.nextGaussian() * 0.05000000074505806;
            worldIn.spawnEntity(entityitem);
        }
    }
    
    static {
        RANDOM = new Random();
    }
}
