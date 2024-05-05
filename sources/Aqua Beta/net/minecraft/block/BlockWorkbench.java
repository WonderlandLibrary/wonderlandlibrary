package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;

public class BlockWorkbench
extends Block {
    protected BlockWorkbench() {
        super(Material.wood);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        playerIn.displayGui((IInteractionObject)new InterfaceCraftingTable(worldIn, pos));
        playerIn.triggerAchievement(StatList.field_181742_Z);
        return true;
    }
}
