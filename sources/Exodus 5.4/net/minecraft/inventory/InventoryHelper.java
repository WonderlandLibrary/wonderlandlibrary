/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.inventory;

import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class InventoryHelper {
    private static final Random RANDOM = new Random();

    public static void dropInventoryItems(World world, BlockPos blockPos, IInventory iInventory) {
        InventoryHelper.func_180174_a(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), iInventory);
    }

    public static void func_180176_a(World world, Entity entity, IInventory iInventory) {
        InventoryHelper.func_180174_a(world, entity.posX, entity.posY, entity.posZ, iInventory);
    }

    private static void spawnItemStack(World world, double d, double d2, double d3, ItemStack itemStack) {
        float f = RANDOM.nextFloat() * 0.8f + 0.1f;
        float f2 = RANDOM.nextFloat() * 0.8f + 0.1f;
        float f3 = RANDOM.nextFloat() * 0.8f + 0.1f;
        while (itemStack.stackSize > 0) {
            int n = RANDOM.nextInt(21) + 10;
            if (n > itemStack.stackSize) {
                n = itemStack.stackSize;
            }
            itemStack.stackSize -= n;
            EntityItem entityItem = new EntityItem(world, d + (double)f, d2 + (double)f2, d3 + (double)f3, new ItemStack(itemStack.getItem(), n, itemStack.getMetadata()));
            if (itemStack.hasTagCompound()) {
                entityItem.getEntityItem().setTagCompound((NBTTagCompound)itemStack.getTagCompound().copy());
            }
            float f4 = 0.05f;
            entityItem.motionX = RANDOM.nextGaussian() * (double)f4;
            entityItem.motionY = RANDOM.nextGaussian() * (double)f4 + (double)0.2f;
            entityItem.motionZ = RANDOM.nextGaussian() * (double)f4;
            world.spawnEntityInWorld(entityItem);
        }
    }

    private static void func_180174_a(World world, double d, double d2, double d3, IInventory iInventory) {
        int n = 0;
        while (n < iInventory.getSizeInventory()) {
            ItemStack itemStack = iInventory.getStackInSlot(n);
            if (itemStack != null) {
                InventoryHelper.spawnItemStack(world, d, d2, d3, itemStack);
            }
            ++n;
        }
    }
}

