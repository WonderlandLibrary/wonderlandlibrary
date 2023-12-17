// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.init.Enchantments;
import net.minecraft.block.state.IBlockState;
import java.util.Iterator;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.init.Blocks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentFrostWalker extends Enchantment
{
    public EnchantmentFrostWalker(final Rarity rarityIn, final EntityEquipmentSlot... slots) {
        super(rarityIn, EnumEnchantmentType.ARMOR_FEET, slots);
        this.setName("frostWalker");
    }
    
    @Override
    public int getMinEnchantability(final int enchantmentLevel) {
        return enchantmentLevel * 10;
    }
    
    @Override
    public int getMaxEnchantability(final int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 15;
    }
    
    @Override
    public boolean isTreasureEnchantment() {
        return true;
    }
    
    @Override
    public int getMaxLevel() {
        return 2;
    }
    
    public static void freezeNearby(final EntityLivingBase living, final World worldIn, final BlockPos pos, final int level) {
        if (living.onGround) {
            final float f = (float)Math.min(16, 2 + level);
            final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(0, 0, 0);
            for (final BlockPos.MutableBlockPos blockpos$mutableblockpos2 : BlockPos.getAllInBoxMutable(pos.add(-f, -1.0, -f), pos.add(f, -1.0, f))) {
                if (blockpos$mutableblockpos2.distanceSqToCenter(living.posX, living.posY, living.posZ) <= f * f) {
                    blockpos$mutableblockpos.setPos(blockpos$mutableblockpos2.getX(), blockpos$mutableblockpos2.getY() + 1, blockpos$mutableblockpos2.getZ());
                    final IBlockState iblockstate = worldIn.getBlockState(blockpos$mutableblockpos);
                    if (iblockstate.getMaterial() != Material.AIR) {
                        continue;
                    }
                    final IBlockState iblockstate2 = worldIn.getBlockState(blockpos$mutableblockpos2);
                    if (iblockstate2.getMaterial() != Material.WATER || iblockstate2.getValue((IProperty<Integer>)BlockLiquid.LEVEL) != 0 || !worldIn.mayPlace(Blocks.FROSTED_ICE, blockpos$mutableblockpos2, false, EnumFacing.DOWN, null)) {
                        continue;
                    }
                    worldIn.setBlockState(blockpos$mutableblockpos2, Blocks.FROSTED_ICE.getDefaultState());
                    worldIn.scheduleUpdate(blockpos$mutableblockpos2.toImmutable(), Blocks.FROSTED_ICE, MathHelper.getInt(living.getRNG(), 60, 120));
                }
            }
        }
    }
    
    public boolean canApplyTogether(final Enchantment ench) {
        return super.canApplyTogether(ench) && ench != Enchantments.DEPTH_STRIDER;
    }
}
