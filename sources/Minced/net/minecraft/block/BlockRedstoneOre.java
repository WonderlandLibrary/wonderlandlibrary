// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.material.Material;

public class BlockRedstoneOre extends Block
{
    private final boolean isOn;
    
    public BlockRedstoneOre(final boolean isOn) {
        super(Material.ROCK);
        if (isOn) {
            this.setTickRandomly(true);
        }
        this.isOn = isOn;
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return 30;
    }
    
    @Override
    public void onBlockClicked(final World worldIn, final BlockPos pos, final EntityPlayer playerIn) {
        this.activate(worldIn, pos);
        super.onBlockClicked(worldIn, pos, playerIn);
    }
    
    @Override
    public void onEntityWalk(final World worldIn, final BlockPos pos, final Entity entityIn) {
        this.activate(worldIn, pos);
        super.onEntityWalk(worldIn, pos, entityIn);
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        this.activate(worldIn, pos);
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }
    
    private void activate(final World worldIn, final BlockPos pos) {
        this.spawnParticles(worldIn, pos);
        if (this == Blocks.REDSTONE_ORE) {
            worldIn.setBlockState(pos, Blocks.LIT_REDSTONE_ORE.getDefaultState());
        }
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (this == Blocks.LIT_REDSTONE_ORE) {
            worldIn.setBlockState(pos, Blocks.REDSTONE_ORE.getDefaultState());
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.REDSTONE;
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
            final int i = 1 + worldIn.rand.nextInt(5);
            this.dropXpOnBlockBreak(worldIn, pos, i);
        }
    }
    
    @Override
    public void randomDisplayTick(final IBlockState stateIn, final World worldIn, final BlockPos pos, final Random rand) {
        if (this.isOn) {
            this.spawnParticles(worldIn, pos);
        }
    }
    
    private void spawnParticles(final World worldIn, final BlockPos pos) {
        final Random random = worldIn.rand;
        final double d0 = 0.0625;
        for (int i = 0; i < 6; ++i) {
            double d2 = pos.getX() + random.nextFloat();
            double d3 = pos.getY() + random.nextFloat();
            double d4 = pos.getZ() + random.nextFloat();
            if (i == 0 && !worldIn.getBlockState(pos.up()).isOpaqueCube()) {
                d3 = pos.getY() + 0.0625 + 1.0;
            }
            if (i == 1 && !worldIn.getBlockState(pos.down()).isOpaqueCube()) {
                d3 = pos.getY() - 0.0625;
            }
            if (i == 2 && !worldIn.getBlockState(pos.south()).isOpaqueCube()) {
                d4 = pos.getZ() + 0.0625 + 1.0;
            }
            if (i == 3 && !worldIn.getBlockState(pos.north()).isOpaqueCube()) {
                d4 = pos.getZ() - 0.0625;
            }
            if (i == 4 && !worldIn.getBlockState(pos.east()).isOpaqueCube()) {
                d2 = pos.getX() + 0.0625 + 1.0;
            }
            if (i == 5 && !worldIn.getBlockState(pos.west()).isOpaqueCube()) {
                d2 = pos.getX() - 0.0625;
            }
            if (d2 < pos.getX() || d2 > pos.getX() + 1 || d3 < 0.0 || d3 > pos.getY() + 1 || d4 < pos.getZ() || d4 > pos.getZ() + 1) {
                worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d2, d3, d4, 0.0, 0.0, 0.0, new int[0]);
            }
        }
    }
    
    @Override
    protected ItemStack getSilkTouchDrop(final IBlockState state) {
        return new ItemStack(Blocks.REDSTONE_ORE);
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(Blocks.REDSTONE_ORE), 1, this.damageDropped(state));
    }
}
