// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.util.math.AxisAlignedBB;
import java.util.List;
import net.minecraft.stats.StatList;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityBoat;

public class ItemBoat extends Item
{
    private final EntityBoat.Type type;
    
    public ItemBoat(final EntityBoat.Type typeIn) {
        this.type = typeIn;
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.TRANSPORTATION);
        this.setTranslationKey("boat." + typeIn.getName());
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand handIn) {
        final ItemStack itemstack = playerIn.getHeldItem(handIn);
        final float f = 1.0f;
        final float f2 = playerIn.prevRotationPitch + (playerIn.rotationPitch - playerIn.prevRotationPitch) * 1.0f;
        final float f3 = playerIn.prevRotationYaw + (playerIn.rotationYaw - playerIn.prevRotationYaw) * 1.0f;
        final double d0 = playerIn.prevPosX + (playerIn.posX - playerIn.prevPosX) * 1.0;
        final double d2 = playerIn.prevPosY + (playerIn.posY - playerIn.prevPosY) * 1.0 + playerIn.getEyeHeight();
        final double d3 = playerIn.prevPosZ + (playerIn.posZ - playerIn.prevPosZ) * 1.0;
        final Vec3d vec3d = new Vec3d(d0, d2, d3);
        final float f4 = MathHelper.cos(-f3 * 0.017453292f - 3.1415927f);
        final float f5 = MathHelper.sin(-f3 * 0.017453292f - 3.1415927f);
        final float f6 = -MathHelper.cos(-f2 * 0.017453292f);
        final float f7 = MathHelper.sin(-f2 * 0.017453292f);
        final float f8 = f5 * f6;
        final float f9 = f4 * f6;
        final double d4 = 5.0;
        final Vec3d vec3d2 = vec3d.add(f8 * 5.0, f7 * 5.0, f9 * 5.0);
        final RayTraceResult raytraceresult = worldIn.rayTraceBlocks(vec3d, vec3d2, true);
        if (raytraceresult == null) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        final Vec3d vec3d3 = playerIn.getLook(1.0f);
        boolean flag = false;
        final List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(playerIn, playerIn.getEntityBoundingBox().expand(vec3d3.x * 5.0, vec3d3.y * 5.0, vec3d3.z * 5.0).grow(1.0));
        for (int i = 0; i < list.size(); ++i) {
            final Entity entity = list.get(i);
            if (entity.canBeCollidedWith()) {
                final AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().grow(entity.getCollisionBorderSize());
                if (axisalignedbb.contains(vec3d)) {
                    flag = true;
                }
            }
        }
        if (flag) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        final Block block = worldIn.getBlockState(raytraceresult.getBlockPos()).getBlock();
        final boolean flag2 = block == Blocks.WATER || block == Blocks.FLOWING_WATER;
        final EntityBoat entityboat = new EntityBoat(worldIn, raytraceresult.hitVec.x, flag2 ? (raytraceresult.hitVec.y - 0.12) : raytraceresult.hitVec.y, raytraceresult.hitVec.z);
        entityboat.setBoatType(this.type);
        entityboat.rotationYaw = playerIn.rotationYaw;
        if (!worldIn.getCollisionBoxes(entityboat, entityboat.getEntityBoundingBox().grow(-0.1)).isEmpty()) {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        }
        if (!worldIn.isRemote) {
            worldIn.spawnEntity(entityboat);
        }
        if (!playerIn.capabilities.isCreativeMode) {
            itemstack.shrink(1);
        }
        playerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }
}
