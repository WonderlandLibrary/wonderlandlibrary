/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemMonsterPlacer
extends Item {
    public ItemMonsterPlacer() {
        this.setCreativeTab(CreativeTabs.MISC);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String s = ("" + I18n.translateToLocal(this.getUnlocalizedName() + ".name")).trim();
        String s1 = EntityList.func_191302_a(ItemMonsterPlacer.func_190908_h(stack));
        if (s1 != null) {
            s = s + " " + I18n.translateToLocal("entity." + s1 + ".name");
        }
        return s;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY) {
        TileEntity tileentity;
        ItemStack itemstack = stack.getHeldItem(pos);
        if (playerIn.isRemote) {
            return EnumActionResult.SUCCESS;
        }
        if (!stack.canPlayerEdit(worldIn.offset(hand), hand, itemstack)) {
            return EnumActionResult.FAIL;
        }
        IBlockState iblockstate = playerIn.getBlockState(worldIn);
        Block block = iblockstate.getBlock();
        if (block == Blocks.MOB_SPAWNER && (tileentity = playerIn.getTileEntity(worldIn)) instanceof TileEntityMobSpawner) {
            MobSpawnerBaseLogic mobspawnerbaselogic = ((TileEntityMobSpawner)tileentity).getSpawnerBaseLogic();
            mobspawnerbaselogic.func_190894_a(ItemMonsterPlacer.func_190908_h(itemstack));
            tileentity.markDirty();
            playerIn.notifyBlockUpdate(worldIn, iblockstate, iblockstate, 3);
            if (!stack.capabilities.isCreativeMode) {
                itemstack.func_190918_g(1);
            }
            return EnumActionResult.SUCCESS;
        }
        BlockPos blockpos = worldIn.offset(hand);
        double d0 = this.func_190909_a(playerIn, blockpos);
        Entity entity = ItemMonsterPlacer.spawnCreature(playerIn, ItemMonsterPlacer.func_190908_h(itemstack), (double)blockpos.getX() + 0.5, (double)blockpos.getY() + d0, (double)blockpos.getZ() + 0.5);
        if (entity != null) {
            if (entity instanceof EntityLivingBase && itemstack.hasDisplayName()) {
                entity.setCustomNameTag(itemstack.getDisplayName());
            }
            ItemMonsterPlacer.applyItemEntityDataToEntity(playerIn, stack, itemstack, entity);
            if (!stack.capabilities.isCreativeMode) {
                itemstack.func_190918_g(1);
            }
        }
        return EnumActionResult.SUCCESS;
    }

    protected double func_190909_a(World p_190909_1_, BlockPos p_190909_2_) {
        AxisAlignedBB axisalignedbb = new AxisAlignedBB(p_190909_2_).addCoord(0.0, -1.0, 0.0);
        List<AxisAlignedBB> list = p_190909_1_.getCollisionBoxes(null, axisalignedbb);
        if (list.isEmpty()) {
            return 0.0;
        }
        double d0 = axisalignedbb.minY;
        for (AxisAlignedBB axisalignedbb1 : list) {
            d0 = Math.max(axisalignedbb1.maxY, d0);
        }
        return d0 - (double)p_190909_2_.getY();
    }

    public static void applyItemEntityDataToEntity(World entityWorld, @Nullable EntityPlayer player, ItemStack stack, @Nullable Entity targetEntity) {
        NBTTagCompound nbttagcompound;
        MinecraftServer minecraftserver = entityWorld.getInstanceServer();
        if (minecraftserver != null && targetEntity != null && (nbttagcompound = stack.getTagCompound()) != null && nbttagcompound.hasKey("EntityTag", 10)) {
            if (!(entityWorld.isRemote || !targetEntity.ignoreItemEntityData() || player != null && minecraftserver.getPlayerList().canSendCommands(player.getGameProfile()))) {
                return;
            }
            NBTTagCompound nbttagcompound1 = targetEntity.writeToNBT(new NBTTagCompound());
            UUID uuid = targetEntity.getUniqueID();
            nbttagcompound1.merge(nbttagcompound.getCompoundTag("EntityTag"));
            targetEntity.setUniqueId(uuid);
            targetEntity.readFromNBT(nbttagcompound1);
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World itemStackIn, EntityPlayer worldIn, EnumHand playerIn) {
        ItemStack itemstack = worldIn.getHeldItem(playerIn);
        if (itemStackIn.isRemote) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        RayTraceResult raytraceresult = this.rayTrace(itemStackIn, worldIn, true);
        if (raytraceresult != null && raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos blockpos = raytraceresult.getBlockPos();
            if (!(itemStackIn.getBlockState(blockpos).getBlock() instanceof BlockLiquid)) {
                return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
            }
            if (itemStackIn.isBlockModifiable(worldIn, blockpos) && worldIn.canPlayerEdit(blockpos, raytraceresult.sideHit, itemstack)) {
                Entity entity = ItemMonsterPlacer.spawnCreature(itemStackIn, ItemMonsterPlacer.func_190908_h(itemstack), (double)blockpos.getX() + 0.5, (double)blockpos.getY() + 0.5, (double)blockpos.getZ() + 0.5);
                if (entity == null) {
                    return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
                }
                if (entity instanceof EntityLivingBase && itemstack.hasDisplayName()) {
                    entity.setCustomNameTag(itemstack.getDisplayName());
                }
                ItemMonsterPlacer.applyItemEntityDataToEntity(itemStackIn, worldIn, itemstack, entity);
                if (!worldIn.capabilities.isCreativeMode) {
                    itemstack.func_190918_g(1);
                }
                worldIn.addStat(StatList.getObjectUseStats(this));
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
            }
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        }
        return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
    }

    @Nullable
    public static Entity spawnCreature(World worldIn, @Nullable ResourceLocation entityID, double x, double y, double z) {
        if (entityID != null && EntityList.ENTITY_EGGS.containsKey(entityID)) {
            Entity entity = null;
            for (int i = 0; i < 1; ++i) {
                entity = EntityList.createEntityByIDFromName(entityID, worldIn);
                if (!(entity instanceof EntityLiving)) continue;
                EntityLiving entityliving = (EntityLiving)entity;
                entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0f), 0.0f);
                entityliving.rotationYawHead = entityliving.rotationYaw;
                entityliving.renderYawOffset = entityliving.rotationYaw;
                entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), null);
                worldIn.spawnEntityInWorld(entity);
                entityliving.playLivingSound();
            }
            return entity;
        }
        return null;
    }

    @Override
    public void getSubItems(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
        if (this.func_194125_a(itemIn)) {
            for (EntityList.EntityEggInfo entitylist$entityegginfo : EntityList.ENTITY_EGGS.values()) {
                ItemStack itemstack = new ItemStack(this, 1);
                ItemMonsterPlacer.applyEntityIdToItemStack(itemstack, entitylist$entityegginfo.spawnedID);
                tab.add(itemstack);
            }
        }
    }

    public static void applyEntityIdToItemStack(ItemStack stack, ResourceLocation entityId) {
        NBTTagCompound nbttagcompound = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        nbttagcompound1.setString("id", entityId.toString());
        nbttagcompound.setTag("EntityTag", nbttagcompound1);
        stack.setTagCompound(nbttagcompound);
    }

    @Nullable
    public static ResourceLocation func_190908_h(ItemStack p_190908_0_) {
        NBTTagCompound nbttagcompound = p_190908_0_.getTagCompound();
        if (nbttagcompound == null) {
            return null;
        }
        if (!nbttagcompound.hasKey("EntityTag", 10)) {
            return null;
        }
        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("EntityTag");
        if (!nbttagcompound1.hasKey("id", 8)) {
            return null;
        }
        String s = nbttagcompound1.getString("id");
        ResourceLocation resourcelocation = new ResourceLocation(s);
        if (!s.contains(":")) {
            nbttagcompound1.setString("id", resourcelocation.toString());
        }
        return resourcelocation;
    }
}

