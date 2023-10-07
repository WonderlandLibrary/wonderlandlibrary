// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;

public class BlockDeadBush extends BlockBush
{
    private static final String __OBFID = "CL_00000224";
    
    protected BlockDeadBush() {
        super(Material.vine);
        final float var1 = 0.4f;
        this.setBlockBounds(0.5f - var1, 0.0f, 0.5f - var1, 0.5f + var1, 0.8f, 0.5f + var1);
    }
    
    @Override
    protected boolean canPlaceBlockOn(final Block ground) {
        return ground == Blocks.sand || ground == Blocks.hardened_clay || ground == Blocks.stained_hardened_clay || ground == Blocks.dirt;
    }
    
    @Override
    public boolean isReplaceable(final World worldIn, final BlockPos pos) {
        return true;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return null;
    }
    
    @Override
    public void harvestBlock(final World worldIn, final EntityPlayer playerIn, final BlockPos pos, final IBlockState state, final TileEntity te) {
        if (!worldIn.isRemote && playerIn.getCurrentEquippedItem() != null && playerIn.getCurrentEquippedItem().getItem() == Items.shears) {
            playerIn.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
            Block.spawnAsEntity(worldIn, pos, new ItemStack(Blocks.deadbush, 1, 0));
        }
        else {
            super.harvestBlock(worldIn, playerIn, pos, state, te);
        }
    }
}
