/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import java.util.List;
import java.util.Random;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Rotations;
import net.minecraft.world.World;

public class ItemArmorStand
extends Item {
    public ItemArmorStand() {
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY) {
        double d2;
        double d1;
        ItemStack itemstack;
        if (hand == EnumFacing.DOWN) {
            return EnumActionResult.FAIL;
        }
        boolean flag = playerIn.getBlockState(worldIn).getBlock().isReplaceable(playerIn, worldIn);
        BlockPos blockpos = flag ? worldIn : worldIn.offset(hand);
        if (!stack.canPlayerEdit(blockpos, hand, itemstack = stack.getHeldItem(pos))) {
            return EnumActionResult.FAIL;
        }
        BlockPos blockpos1 = blockpos.up();
        boolean flag1 = !playerIn.isAirBlock(blockpos) && !playerIn.getBlockState(blockpos).getBlock().isReplaceable(playerIn, blockpos);
        if (flag1 |= !playerIn.isAirBlock(blockpos1) && !playerIn.getBlockState(blockpos1).getBlock().isReplaceable(playerIn, blockpos1)) {
            return EnumActionResult.FAIL;
        }
        double d0 = blockpos.getX();
        List<Entity> list = playerIn.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(d0, d1 = (double)blockpos.getY(), d2 = (double)blockpos.getZ(), d0 + 1.0, d1 + 2.0, d2 + 1.0));
        if (!list.isEmpty()) {
            return EnumActionResult.FAIL;
        }
        if (!playerIn.isRemote) {
            playerIn.setBlockToAir(blockpos);
            playerIn.setBlockToAir(blockpos1);
            EntityArmorStand entityarmorstand = new EntityArmorStand(playerIn, d0 + 0.5, d1, d2 + 0.5);
            float f = (float)MathHelper.floor((MathHelper.wrapDegrees(stack.rotationYaw - 180.0f) + 22.5f) / 45.0f) * 45.0f;
            entityarmorstand.setLocationAndAngles(d0 + 0.5, d1, d2 + 0.5, f, 0.0f);
            this.applyRandomRotations(entityarmorstand, playerIn.rand);
            ItemMonsterPlacer.applyItemEntityDataToEntity(playerIn, stack, itemstack, entityarmorstand);
            playerIn.spawnEntityInWorld(entityarmorstand);
            playerIn.playSound(null, entityarmorstand.posX, entityarmorstand.posY, entityarmorstand.posZ, SoundEvents.ENTITY_ARMORSTAND_PLACE, SoundCategory.BLOCKS, 0.75f, 0.8f);
        }
        itemstack.func_190918_g(1);
        return EnumActionResult.SUCCESS;
    }

    private void applyRandomRotations(EntityArmorStand armorStand, Random rand) {
        Rotations rotations = armorStand.getHeadRotation();
        float f = rand.nextFloat() * 5.0f;
        float f1 = rand.nextFloat() * 20.0f - 10.0f;
        Rotations rotations1 = new Rotations(rotations.getX() + f, rotations.getY() + f1, rotations.getZ());
        armorStand.setHeadRotation(rotations1);
        rotations = armorStand.getBodyRotation();
        f = rand.nextFloat() * 10.0f - 5.0f;
        rotations1 = new Rotations(rotations.getX(), rotations.getY() + f, rotations.getZ());
        armorStand.setBodyRotation(rotations1);
    }
}

