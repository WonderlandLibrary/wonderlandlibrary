/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockCauldron
extends Block {
    public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 3);

    @Override
    public Item getItem(World world, BlockPos blockPos) {
        return Items.cauldron;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos blockPos, IBlockState iBlockState, Entity entity) {
        int n = iBlockState.getValue(LEVEL);
        float f = (float)blockPos.getY() + (6.0f + (float)(3 * n)) / 16.0f;
        if (!world.isRemote && entity.isBurning() && n > 0 && entity.getEntityBoundingBox().minY <= (double)f) {
            entity.extinguish();
            this.setWaterLevel(world, blockPos, iBlockState, n - 1);
        }
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, LEVEL);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer, EnumFacing enumFacing, float f, float f2, float f3) {
        Object object;
        if (world.isRemote) {
            return true;
        }
        ItemStack itemStack = entityPlayer.inventory.getCurrentItem();
        if (itemStack == null) {
            return true;
        }
        int n = iBlockState.getValue(LEVEL);
        Item item = itemStack.getItem();
        if (item == Items.water_bucket) {
            if (n < 3) {
                if (!entityPlayer.capabilities.isCreativeMode) {
                    entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, new ItemStack(Items.bucket));
                }
                entityPlayer.triggerAchievement(StatList.field_181725_I);
                this.setWaterLevel(world, blockPos, iBlockState, 3);
            }
            return true;
        }
        if (item == Items.glass_bottle) {
            if (n > 0) {
                if (!entityPlayer.capabilities.isCreativeMode) {
                    ItemStack itemStack2 = new ItemStack(Items.potionitem, 1, 0);
                    if (!entityPlayer.inventory.addItemStackToInventory(itemStack2)) {
                        world.spawnEntityInWorld(new EntityItem(world, (double)blockPos.getX() + 0.5, (double)blockPos.getY() + 1.5, (double)blockPos.getZ() + 0.5, itemStack2));
                    } else if (entityPlayer instanceof EntityPlayerMP) {
                        ((EntityPlayerMP)entityPlayer).sendContainerToPlayer(entityPlayer.inventoryContainer);
                    }
                    entityPlayer.triggerAchievement(StatList.field_181726_J);
                    --itemStack.stackSize;
                    if (itemStack.stackSize <= 0) {
                        entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, null);
                    }
                }
                this.setWaterLevel(world, blockPos, iBlockState, n - 1);
            }
            return true;
        }
        if (n > 0 && item instanceof ItemArmor && ((ItemArmor)(object = (ItemArmor)item)).getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER && ((ItemArmor)object).hasColor(itemStack)) {
            ((ItemArmor)object).removeColor(itemStack);
            this.setWaterLevel(world, blockPos, iBlockState, n - 1);
            entityPlayer.triggerAchievement(StatList.field_181727_K);
            return true;
        }
        if (n > 0 && item instanceof ItemBanner && TileEntityBanner.getPatterns(itemStack) > 0) {
            object = itemStack.copy();
            ((ItemStack)object).stackSize = 1;
            TileEntityBanner.removeBannerData((ItemStack)object);
            if (itemStack.stackSize <= 1 && !entityPlayer.capabilities.isCreativeMode) {
                entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, (ItemStack)object);
            } else {
                if (!entityPlayer.inventory.addItemStackToInventory((ItemStack)object)) {
                    world.spawnEntityInWorld(new EntityItem(world, (double)blockPos.getX() + 0.5, (double)blockPos.getY() + 1.5, (double)blockPos.getZ() + 0.5, (ItemStack)object));
                } else if (entityPlayer instanceof EntityPlayerMP) {
                    ((EntityPlayerMP)entityPlayer).sendContainerToPlayer(entityPlayer.inventoryContainer);
                }
                entityPlayer.triggerAchievement(StatList.field_181728_L);
                if (!entityPlayer.capabilities.isCreativeMode) {
                    --itemStack.stackSize;
                }
            }
            if (!entityPlayer.capabilities.isCreativeMode) {
                this.setWaterLevel(world, blockPos, iBlockState, n - 1);
            }
            return true;
        }
        return false;
    }

    @Override
    public int getComparatorInputOverride(World world, BlockPos blockPos) {
        return world.getBlockState(blockPos).getValue(LEVEL);
    }

    @Override
    public void fillWithRain(World world, BlockPos blockPos) {
        IBlockState iBlockState;
        if (world.rand.nextInt(20) == 1 && (iBlockState = world.getBlockState(blockPos)).getValue(LEVEL) < 3) {
            world.setBlockState(blockPos, iBlockState.cycleProperty(LEVEL), 2);
        }
    }

    public void setWaterLevel(World world, BlockPos blockPos, IBlockState iBlockState, int n) {
        world.setBlockState(blockPos, iBlockState.withProperty(LEVEL, MathHelper.clamp_int(n, 0, 3)), 2);
        world.updateComparatorOutputLevel(blockPos, this);
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        return iBlockState.getValue(LEVEL);
    }

    @Override
    public void addCollisionBoxesToList(World world, BlockPos blockPos, IBlockState iBlockState, AxisAlignedBB axisAlignedBB, List<AxisAlignedBB> list, Entity entity) {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.3125f, 1.0f);
        super.addCollisionBoxesToList(world, blockPos, iBlockState, axisAlignedBB, list, entity);
        float f = 0.125f;
        this.setBlockBounds(0.0f, 0.0f, 0.0f, f, 1.0f, 1.0f);
        super.addCollisionBoxesToList(world, blockPos, iBlockState, axisAlignedBB, list, entity);
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, f);
        super.addCollisionBoxesToList(world, blockPos, iBlockState, axisAlignedBB, list, entity);
        this.setBlockBounds(1.0f - f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        super.addCollisionBoxesToList(world, blockPos, iBlockState, axisAlignedBB, list, entity);
        this.setBlockBounds(0.0f, 0.0f, 1.0f - f, 1.0f, 1.0f, 1.0f);
        super.addCollisionBoxesToList(world, blockPos, iBlockState, axisAlignedBB, list, entity);
        this.setBlockBoundsForItemRender();
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(LEVEL, n);
    }

    public BlockCauldron() {
        super(Material.iron, MapColor.stoneColor);
        this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, 0));
    }

    @Override
    public Item getItemDropped(IBlockState iBlockState, Random random, int n) {
        return Items.cauldron;
    }
}

