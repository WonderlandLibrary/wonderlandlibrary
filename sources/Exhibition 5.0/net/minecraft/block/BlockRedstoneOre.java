// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.material.Material;

public class BlockRedstoneOre extends Block
{
    private final boolean isOn;
    private static final String __OBFID = "CL_00000294";
    
    public BlockRedstoneOre(final boolean p_i45420_1_) {
        super(Material.rock);
        if (p_i45420_1_) {
            this.setTickRandomly(true);
        }
        this.isOn = p_i45420_1_;
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return 30;
    }
    
    @Override
    public void onBlockClicked(final World worldIn, final BlockPos pos, final EntityPlayer playerIn) {
        this.setOn(worldIn, pos);
        super.onBlockClicked(worldIn, pos, playerIn);
    }
    
    @Override
    public void onEntityCollidedWithBlock(final World worldIn, final BlockPos pos, final Entity entityIn) {
        this.setOn(worldIn, pos);
        super.onEntityCollidedWithBlock(worldIn, pos, entityIn);
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        this.setOn(worldIn, pos);
        return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
    }
    
    private void setOn(final World worldIn, final BlockPos p_176352_2_) {
        this.spawnRedstoneParticles(worldIn, p_176352_2_);
        if (this == Blocks.redstone_ore) {
            worldIn.setBlockState(p_176352_2_, Blocks.lit_redstone_ore.getDefaultState());
        }
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (this == Blocks.lit_redstone_ore) {
            worldIn.setBlockState(pos, Blocks.redstone_ore.getDefaultState());
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.redstone;
    }
    
    @Override
    public int quantityDroppedWithBonus(final int fortune, final Random random) {
        return this.quantityDropped(random) + random.nextInt(fortune + 1);
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 4 + random.nextInt(2);
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
        if (this.getItemDropped(state, worldIn.rand, fortune) != Item.getItemFromBlock(this)) {
            final int var6 = 1 + worldIn.rand.nextInt(5);
            this.dropXpOnBlockBreak(worldIn, pos, var6);
        }
    }
    
    @Override
    public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (this.isOn) {
            this.spawnRedstoneParticles(worldIn, pos);
        }
    }
    
    private void spawnRedstoneParticles(final World worldIn, final BlockPos p_180691_2_) {
        final Random var3 = worldIn.rand;
        final double var4 = 0.0625;
        for (int var5 = 0; var5 < 6; ++var5) {
            double var6 = p_180691_2_.getX() + var3.nextFloat();
            double var7 = p_180691_2_.getY() + var3.nextFloat();
            double var8 = p_180691_2_.getZ() + var3.nextFloat();
            if (var5 == 0 && !worldIn.getBlockState(p_180691_2_.offsetUp()).getBlock().isOpaqueCube()) {
                var7 = p_180691_2_.getY() + var4 + 1.0;
            }
            if (var5 == 1 && !worldIn.getBlockState(p_180691_2_.offsetDown()).getBlock().isOpaqueCube()) {
                var7 = p_180691_2_.getY() - var4;
            }
            if (var5 == 2 && !worldIn.getBlockState(p_180691_2_.offsetSouth()).getBlock().isOpaqueCube()) {
                var8 = p_180691_2_.getZ() + var4 + 1.0;
            }
            if (var5 == 3 && !worldIn.getBlockState(p_180691_2_.offsetNorth()).getBlock().isOpaqueCube()) {
                var8 = p_180691_2_.getZ() - var4;
            }
            if (var5 == 4 && !worldIn.getBlockState(p_180691_2_.offsetEast()).getBlock().isOpaqueCube()) {
                var6 = p_180691_2_.getX() + var4 + 1.0;
            }
            if (var5 == 5 && !worldIn.getBlockState(p_180691_2_.offsetWest()).getBlock().isOpaqueCube()) {
                var6 = p_180691_2_.getX() - var4;
            }
            if (var6 < p_180691_2_.getX() || var6 > p_180691_2_.getX() + 1 || var7 < 0.0 || var7 > p_180691_2_.getY() + 1 || var8 < p_180691_2_.getZ() || var8 > p_180691_2_.getZ() + 1) {
                worldIn.spawnParticle(EnumParticleTypes.REDSTONE, var6, var7, var8, 0.0, 0.0, 0.0, new int[0]);
            }
        }
    }
    
    @Override
    protected ItemStack createStackedBlock(final IBlockState state) {
        return new ItemStack(Blocks.redstone_ore);
    }
}
