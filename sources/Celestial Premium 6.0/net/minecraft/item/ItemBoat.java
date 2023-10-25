/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemBoat
extends Item {
    private final EntityBoat.Type type;

    public ItemBoat(EntityBoat.Type typeIn) {
        this.type = typeIn;
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.TRANSPORTATION);
        this.setUnlocalizedName("boat." + typeIn.getName());
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World itemStackIn, EntityPlayer worldIn, EnumHand playerIn) {
        ItemStack itemstack = worldIn.getHeldItem(playerIn);
        float f = 1.0f;
        float f1 = worldIn.prevRotationPitch + (worldIn.rotationPitch - worldIn.prevRotationPitch) * 1.0f;
        float f2 = worldIn.prevRotationYaw + (worldIn.rotationYaw - worldIn.prevRotationYaw) * 1.0f;
        double d0 = worldIn.prevPosX + (worldIn.posX - worldIn.prevPosX) * 1.0;
        double d1 = worldIn.prevPosY + (worldIn.posY - worldIn.prevPosY) * 1.0 + (double)worldIn.getEyeHeight();
        double d2 = worldIn.prevPosZ + (worldIn.posZ - worldIn.prevPosZ) * 1.0;
        Vec3d vec3d = new Vec3d(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * ((float)Math.PI / 180) - (float)Math.PI);
        float f4 = MathHelper.sin(-f2 * ((float)Math.PI / 180) - (float)Math.PI);
        float f5 = -MathHelper.cos(-f1 * ((float)Math.PI / 180));
        float f6 = MathHelper.sin(-f1 * ((float)Math.PI / 180));
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = 5.0;
        Vec3d vec3d1 = vec3d.add((double)f7 * 5.0, (double)f6 * 5.0, (double)f8 * 5.0);
        RayTraceResult raytraceresult = itemStackIn.rayTraceBlocks(vec3d, vec3d1, true);
        if (raytraceresult == null) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        Vec3d vec3d2 = worldIn.getLook(1.0f);
        boolean flag = false;
        List<Entity> list = itemStackIn.getEntitiesWithinAABBExcludingEntity(worldIn, worldIn.getEntityBoundingBox().addCoord(vec3d2.x * 5.0, vec3d2.y * 5.0, vec3d2.z * 5.0).expandXyz(1.0));
        for (int i = 0; i < list.size(); ++i) {
            AxisAlignedBB axisalignedbb;
            Entity entity = list.get(i);
            if (!entity.canBeCollidedWith() || !(axisalignedbb = entity.getEntityBoundingBox().expandXyz(entity.getCollisionBorderSize())).isVecInside(vec3d)) continue;
            flag = true;
        }
        if (flag) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        Block block = itemStackIn.getBlockState(raytraceresult.getBlockPos()).getBlock();
        boolean flag1 = block == Blocks.WATER || block == Blocks.FLOWING_WATER;
        EntityBoat entityboat = new EntityBoat(itemStackIn, raytraceresult.hitVec.x, flag1 ? raytraceresult.hitVec.y - 0.12 : raytraceresult.hitVec.y, raytraceresult.hitVec.z);
        entityboat.setBoatType(this.type);
        entityboat.rotationYaw = worldIn.rotationYaw;
        if (!itemStackIn.getCollisionBoxes(entityboat, entityboat.getEntityBoundingBox().expandXyz(-0.1)).isEmpty()) {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        }
        if (!itemStackIn.isRemote) {
            itemStackIn.spawnEntityInWorld(entityboat);
        }
        if (!worldIn.capabilities.isCreativeMode) {
            itemstack.func_190918_g(1);
        }
        worldIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }
}

