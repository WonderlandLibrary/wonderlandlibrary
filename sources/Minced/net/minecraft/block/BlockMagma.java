// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.WorldServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import java.util.Random;
import net.minecraft.util.DamageSource;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.block.material.MapColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;

public class BlockMagma extends Block
{
    public BlockMagma() {
        super(Material.ROCK);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setLightLevel(0.2f);
        this.setTickRandomly(true);
    }
    
    @Override
    @Deprecated
    public MapColor getMapColor(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return MapColor.NETHERRACK;
    }
    
    @Override
    public void onEntityWalk(final World worldIn, final BlockPos pos, final Entity entityIn) {
        if (!entityIn.isImmuneToFire() && entityIn instanceof EntityLivingBase && !EnchantmentHelper.hasFrostWalkerEnchantment((EntityLivingBase)entityIn)) {
            entityIn.attackEntityFrom(DamageSource.HOT_FLOOR, 1.0f);
        }
        super.onEntityWalk(worldIn, pos, entityIn);
    }
    
    @Override
    @Deprecated
    public int getPackedLightmapCoords(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        return 15728880;
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        final BlockPos blockpos = pos.up();
        final IBlockState iblockstate = worldIn.getBlockState(blockpos);
        if (iblockstate.getBlock() == Blocks.WATER || iblockstate.getBlock() == Blocks.FLOWING_WATER) {
            worldIn.setBlockToAir(blockpos);
            worldIn.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5f, 2.6f + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8f);
            if (worldIn instanceof WorldServer) {
                ((WorldServer)worldIn).spawnParticle(EnumParticleTypes.SMOKE_LARGE, blockpos.getX() + 0.5, blockpos.getY() + 0.25, blockpos.getZ() + 0.5, 8, 0.5, 0.25, 0.5, 0.0, new int[0]);
            }
        }
    }
    
    @Override
    @Deprecated
    public boolean canEntitySpawn(final IBlockState state, final Entity entityIn) {
        return entityIn.isImmuneToFire();
    }
}
