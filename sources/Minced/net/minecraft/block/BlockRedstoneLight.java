// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.material.Material;

public class BlockRedstoneLight extends Block
{
    private final boolean isOn;
    
    public BlockRedstoneLight(final boolean isOn) {
        super(Material.REDSTONE_LIGHT);
        this.isOn = isOn;
        if (isOn) {
            this.setLightLevel(1.0f);
        }
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!worldIn.isRemote) {
            if (this.isOn && !worldIn.isBlockPowered(pos)) {
                worldIn.setBlockState(pos, Blocks.REDSTONE_LAMP.getDefaultState(), 2);
            }
            else if (!this.isOn && worldIn.isBlockPowered(pos)) {
                worldIn.setBlockState(pos, Blocks.LIT_REDSTONE_LAMP.getDefaultState(), 2);
            }
        }
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        if (!worldIn.isRemote) {
            if (this.isOn && !worldIn.isBlockPowered(pos)) {
                worldIn.scheduleUpdate(pos, this, 4);
            }
            else if (!this.isOn && worldIn.isBlockPowered(pos)) {
                worldIn.setBlockState(pos, Blocks.LIT_REDSTONE_LAMP.getDefaultState(), 2);
            }
        }
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!worldIn.isRemote && this.isOn && !worldIn.isBlockPowered(pos)) {
            worldIn.setBlockState(pos, Blocks.REDSTONE_LAMP.getDefaultState(), 2);
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Item.getItemFromBlock(Blocks.REDSTONE_LAMP);
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new ItemStack(Blocks.REDSTONE_LAMP);
    }
    
    @Override
    protected ItemStack getSilkTouchDrop(final IBlockState state) {
        return new ItemStack(Blocks.REDSTONE_LAMP);
    }
}
