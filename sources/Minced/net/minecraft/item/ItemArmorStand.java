// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.util.math.Rotations;
import java.util.Random;
import java.util.List;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemArmorStand extends Item
{
    public ItemArmorStand() {
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer player, final World worldIn, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        if (facing == EnumFacing.DOWN) {
            return EnumActionResult.FAIL;
        }
        final boolean flag = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
        final BlockPos blockpos = flag ? pos : pos.offset(facing);
        final ItemStack itemstack = player.getHeldItem(hand);
        if (!player.canPlayerEdit(blockpos, facing, itemstack)) {
            return EnumActionResult.FAIL;
        }
        final BlockPos blockpos2 = blockpos.up();
        boolean flag2 = !worldIn.isAirBlock(blockpos) && !worldIn.getBlockState(blockpos).getBlock().isReplaceable(worldIn, blockpos);
        flag2 |= (!worldIn.isAirBlock(blockpos2) && !worldIn.getBlockState(blockpos2).getBlock().isReplaceable(worldIn, blockpos2));
        if (flag2) {
            return EnumActionResult.FAIL;
        }
        final double d0 = blockpos.getX();
        final double d2 = blockpos.getY();
        final double d3 = blockpos.getZ();
        final List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(d0, d2, d3, d0 + 1.0, d2 + 2.0, d3 + 1.0));
        if (!list.isEmpty()) {
            return EnumActionResult.FAIL;
        }
        if (!worldIn.isRemote) {
            worldIn.setBlockToAir(blockpos);
            worldIn.setBlockToAir(blockpos2);
            final EntityArmorStand entityarmorstand = new EntityArmorStand(worldIn, d0 + 0.5, d2, d3 + 0.5);
            final float f = MathHelper.floor((MathHelper.wrapDegrees(player.rotationYaw - 180.0f) + 22.5f) / 45.0f) * 45.0f;
            entityarmorstand.setLocationAndAngles(d0 + 0.5, d2, d3 + 0.5, f, 0.0f);
            this.applyRandomRotations(entityarmorstand, worldIn.rand);
            ItemMonsterPlacer.applyItemEntityDataToEntity(worldIn, player, itemstack, entityarmorstand);
            worldIn.spawnEntity(entityarmorstand);
            worldIn.playSound(null, entityarmorstand.posX, entityarmorstand.posY, entityarmorstand.posZ, SoundEvents.ENTITY_ARMORSTAND_PLACE, SoundCategory.BLOCKS, 0.75f, 0.8f);
        }
        itemstack.shrink(1);
        return EnumActionResult.SUCCESS;
    }
    
    private void applyRandomRotations(final EntityArmorStand armorStand, final Random rand) {
        Rotations rotations = armorStand.getHeadRotation();
        float f = rand.nextFloat() * 5.0f;
        final float f2 = rand.nextFloat() * 20.0f - 10.0f;
        Rotations rotations2 = new Rotations(rotations.getX() + f, rotations.getY() + f2, rotations.getZ());
        armorStand.setHeadRotation(rotations2);
        rotations = armorStand.getBodyRotation();
        f = rand.nextFloat() * 10.0f - 5.0f;
        rotations2 = new Rotations(rotations.getX(), rotations.getY() + f, rotations.getZ());
        armorStand.setBodyRotation(rotations2);
    }
}
