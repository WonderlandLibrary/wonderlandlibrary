// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.init.Blocks;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.stats.StatList;
import net.minecraft.world.IInteractionObject;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;

public class BlockWorkbench extends Block
{
    protected BlockWorkbench() {
        super(Material.WOOD);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        playerIn.displayGui(new InterfaceCraftingTable(worldIn, pos));
        playerIn.addStat(StatList.CRAFTING_TABLE_INTERACTION);
        return true;
    }
    
    public static class InterfaceCraftingTable implements IInteractionObject
    {
        private final World world;
        private final BlockPos position;
        
        public InterfaceCraftingTable(final World worldIn, final BlockPos pos) {
            this.world = worldIn;
            this.position = pos;
        }
        
        @Override
        public String getName() {
            return "crafting_table";
        }
        
        @Override
        public boolean hasCustomName() {
            return false;
        }
        
        @Override
        public ITextComponent getDisplayName() {
            return new TextComponentTranslation(Blocks.CRAFTING_TABLE.getTranslationKey() + ".name", new Object[0]);
        }
        
        @Override
        public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
            return new ContainerWorkbench(playerInventory, this.world, this.position);
        }
        
        @Override
        public String getGuiID() {
            return "minecraft:crafting_table";
        }
    }
}
