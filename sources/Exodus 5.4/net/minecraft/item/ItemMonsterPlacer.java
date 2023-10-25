/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import java.util.List;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemMonsterPlacer
extends Item {
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        if (world.isRemote) {
            return itemStack;
        }
        MovingObjectPosition movingObjectPosition = this.getMovingObjectPositionFromPlayer(world, entityPlayer, true);
        if (movingObjectPosition == null) {
            return itemStack;
        }
        if (movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            Entity entity;
            BlockPos blockPos = movingObjectPosition.getBlockPos();
            if (!world.isBlockModifiable(entityPlayer, blockPos)) {
                return itemStack;
            }
            if (!entityPlayer.canPlayerEdit(blockPos, movingObjectPosition.sideHit, itemStack)) {
                return itemStack;
            }
            if (world.getBlockState(blockPos).getBlock() instanceof BlockLiquid && (entity = ItemMonsterPlacer.spawnCreature(world, itemStack.getMetadata(), (double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5)) != null) {
                if (entity instanceof EntityLivingBase && itemStack.hasDisplayName()) {
                    ((EntityLiving)entity).setCustomNameTag(itemStack.getDisplayName());
                }
                if (!entityPlayer.capabilities.isCreativeMode) {
                    --itemStack.stackSize;
                }
                entityPlayer.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
            }
        }
        return itemStack;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs creativeTabs, List<ItemStack> list) {
        for (EntityList.EntityEggInfo entityEggInfo : EntityList.entityEggs.values()) {
            list.add(new ItemStack(item, 1, entityEggInfo.spawnedID));
        }
    }

    public ItemMonsterPlacer() {
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        String string = StatCollector.translateToLocal(String.valueOf(this.getUnlocalizedName()) + ".name").trim();
        String string2 = EntityList.getStringFromID(itemStack.getMetadata());
        if (string2 != null) {
            string = String.valueOf(string) + " " + StatCollector.translateToLocal("entity." + string2 + ".name");
        }
        return string;
    }

    @Override
    public int getColorFromItemStack(ItemStack itemStack, int n) {
        EntityList.EntityEggInfo entityEggInfo = EntityList.entityEggs.get(itemStack.getMetadata());
        return entityEggInfo != null ? (n == 0 ? entityEggInfo.primaryColor : entityEggInfo.secondaryColor) : 0xFFFFFF;
    }

    public static Entity spawnCreature(World world, int n, double d, double d2, double d3) {
        if (!EntityList.entityEggs.containsKey(n)) {
            return null;
        }
        Entity entity = null;
        int n2 = 0;
        while (n2 < 1) {
            entity = EntityList.createEntityByID(n, world);
            if (entity instanceof EntityLivingBase) {
                EntityLiving entityLiving = (EntityLiving)entity;
                entity.setLocationAndAngles(d, d2, d3, MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360.0f), 0.0f);
                entityLiving.rotationYawHead = entityLiving.rotationYaw;
                entityLiving.renderYawOffset = entityLiving.rotationYaw;
                entityLiving.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entityLiving)), null);
                world.spawnEntityInWorld(entity);
                entityLiving.playLivingSound();
            }
            ++n2;
        }
        return entity;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3) {
        Entity entity;
        TileEntity tileEntity;
        if (world.isRemote) {
            return true;
        }
        if (!entityPlayer.canPlayerEdit(blockPos.offset(enumFacing), enumFacing, itemStack)) {
            return false;
        }
        IBlockState iBlockState = world.getBlockState(blockPos);
        if (iBlockState.getBlock() == Blocks.mob_spawner && (tileEntity = world.getTileEntity(blockPos)) instanceof TileEntityMobSpawner) {
            MobSpawnerBaseLogic mobSpawnerBaseLogic = ((TileEntityMobSpawner)tileEntity).getSpawnerBaseLogic();
            mobSpawnerBaseLogic.setEntityName(EntityList.getStringFromID(itemStack.getMetadata()));
            tileEntity.markDirty();
            world.markBlockForUpdate(blockPos);
            if (!entityPlayer.capabilities.isCreativeMode) {
                --itemStack.stackSize;
            }
            return true;
        }
        blockPos = blockPos.offset(enumFacing);
        double d = 0.0;
        if (enumFacing == EnumFacing.UP && iBlockState instanceof BlockFence) {
            d = 0.5;
        }
        if ((entity = ItemMonsterPlacer.spawnCreature(world, itemStack.getMetadata(), (double)blockPos.getX() + 0.5, (double)blockPos.getY() + d, (double)blockPos.getZ() + 0.5)) != null) {
            if (entity instanceof EntityLivingBase && itemStack.hasDisplayName()) {
                entity.setCustomNameTag(itemStack.getDisplayName());
            }
            if (!entityPlayer.capabilities.isCreativeMode) {
                --itemStack.stackSize;
            }
        }
        return true;
    }
}

