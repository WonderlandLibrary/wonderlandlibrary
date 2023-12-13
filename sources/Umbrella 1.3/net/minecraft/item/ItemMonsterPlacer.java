/*
 * Decompiled with CFR 0.150.
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
    private static final String __OBFID = "CL_00000070";

    public ItemMonsterPlacer() {
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String var2 = StatCollector.translateToLocal(String.valueOf(this.getUnlocalizedName()) + ".name").trim();
        String var3 = EntityList.getStringFromID(stack.getMetadata());
        if (var3 != null) {
            var2 = String.valueOf(var2) + " " + StatCollector.translateToLocal("entity." + var3 + ".name");
        }
        return var2;
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        EntityList.EntityEggInfo var3 = (EntityList.EntityEggInfo)EntityList.entityEggs.get(stack.getMetadata());
        return var3 != null ? (renderPass == 0 ? var3.primaryColor : var3.secondaryColor) : 0xFFFFFF;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        Entity var12;
        TileEntity var10;
        if (worldIn.isRemote) {
            return true;
        }
        if (!playerIn.func_175151_a(pos.offset(side), side, stack)) {
            return false;
        }
        IBlockState var9 = worldIn.getBlockState(pos);
        if (var9.getBlock() == Blocks.mob_spawner && (var10 = worldIn.getTileEntity(pos)) instanceof TileEntityMobSpawner) {
            MobSpawnerBaseLogic var11 = ((TileEntityMobSpawner)var10).getSpawnerBaseLogic();
            var11.setEntityName(EntityList.getStringFromID(stack.getMetadata()));
            var10.markDirty();
            worldIn.markBlockForUpdate(pos);
            if (!playerIn.capabilities.isCreativeMode) {
                --stack.stackSize;
            }
            return true;
        }
        pos = pos.offset(side);
        double var13 = 0.0;
        if (side == EnumFacing.UP && var9 instanceof BlockFence) {
            var13 = 0.5;
        }
        if ((var12 = ItemMonsterPlacer.spawnCreature(worldIn, stack.getMetadata(), (double)pos.getX() + 0.5, (double)pos.getY() + var13, (double)pos.getZ() + 0.5)) != null) {
            if (var12 instanceof EntityLivingBase && stack.hasDisplayName()) {
                var12.setCustomNameTag(stack.getDisplayName());
            }
            if (!playerIn.capabilities.isCreativeMode) {
                --stack.stackSize;
            }
        }
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        if (worldIn.isRemote) {
            return itemStackIn;
        }
        MovingObjectPosition var4 = this.getMovingObjectPositionFromPlayer(worldIn, playerIn, true);
        if (var4 == null) {
            return itemStackIn;
        }
        if (var4.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            Entity var6;
            BlockPos var5 = var4.func_178782_a();
            if (!worldIn.isBlockModifiable(playerIn, var5)) {
                return itemStackIn;
            }
            if (!playerIn.func_175151_a(var5, var4.field_178784_b, itemStackIn)) {
                return itemStackIn;
            }
            if (worldIn.getBlockState(var5).getBlock() instanceof BlockLiquid && (var6 = ItemMonsterPlacer.spawnCreature(worldIn, itemStackIn.getMetadata(), (double)var5.getX() + 0.5, (double)var5.getY() + 0.5, (double)var5.getZ() + 0.5)) != null) {
                if (var6 instanceof EntityLivingBase && itemStackIn.hasDisplayName()) {
                    ((EntityLiving)var6).setCustomNameTag(itemStackIn.getDisplayName());
                }
                if (!playerIn.capabilities.isCreativeMode) {
                    --itemStackIn.stackSize;
                }
                playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
            }
        }
        return itemStackIn;
    }

    public static Entity spawnCreature(World worldIn, int p_77840_1_, double p_77840_2_, double p_77840_4_, double p_77840_6_) {
        if (!EntityList.entityEggs.containsKey(p_77840_1_)) {
            return null;
        }
        Entity var8 = null;
        for (int var9 = 0; var9 < 1; ++var9) {
            var8 = EntityList.createEntityByID(p_77840_1_, worldIn);
            if (!(var8 instanceof EntityLivingBase)) continue;
            EntityLiving var10 = (EntityLiving)var8;
            var8.setLocationAndAngles(p_77840_2_, p_77840_4_, p_77840_6_, MathHelper.wrapAngleTo180_float(worldIn.rand.nextFloat() * 360.0f), 0.0f);
            var10.rotationYawHead = var10.rotationYaw;
            var10.renderYawOffset = var10.rotationYaw;
            var10.func_180482_a(worldIn.getDifficultyForLocation(new BlockPos(var10)), null);
            worldIn.spawnEntityInWorld(var8);
            var10.playLivingSound();
        }
        return var8;
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
        for (EntityList.EntityEggInfo var5 : EntityList.entityEggs.values()) {
            subItems.add(new ItemStack(itemIn, 1, var5.spawnedID));
        }
    }
}

