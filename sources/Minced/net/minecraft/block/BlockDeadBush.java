// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.stats.StatList;
import net.minecraft.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.MapColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;

public class BlockDeadBush extends BlockBush
{
    protected static final AxisAlignedBB DEAD_BUSH_AABB;
    
    protected BlockDeadBush() {
        super(Material.VINE);
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        return BlockDeadBush.DEAD_BUSH_AABB;
    }
    
    @Override
    @Deprecated
    public MapColor getMapColor(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return MapColor.WOOD;
    }
    
    @Override
    protected boolean canSustainBush(final IBlockState state) {
        return state.getBlock() == Blocks.SAND || state.getBlock() == Blocks.HARDENED_CLAY || state.getBlock() == Blocks.STAINED_HARDENED_CLAY || state.getBlock() == Blocks.DIRT;
    }
    
    @Override
    public boolean isReplaceable(final IBlockAccess worldIn, final BlockPos pos) {
        return true;
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return random.nextInt(3);
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.STICK;
    }
    
    @Override
    public void harvestBlock(final World worldIn, final EntityPlayer player, final BlockPos pos, final IBlockState state, @Nullable final TileEntity te, final ItemStack stack) {
        if (!worldIn.isRemote && stack.getItem() == Items.SHEARS) {
            player.addStat(StatList.getBlockStats(this));
            Block.spawnAsEntity(worldIn, pos, new ItemStack(Blocks.DEADBUSH, 1, 0));
        }
        else {
            super.harvestBlock(worldIn, player, pos, state, te, stack);
        }
    }
    
    static {
        DEAD_BUSH_AABB = new AxisAlignedBB(0.09999999403953552, 0.0, 0.09999999403953552, 0.8999999761581421, 0.800000011920929, 0.8999999761581421);
    }
}
