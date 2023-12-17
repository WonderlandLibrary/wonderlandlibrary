// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.world.IBlockAccess;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import java.util.Random;
import ru.tuskevich.event.events.Event;
import ru.tuskevich.event.EventManager;
import ru.tuskevich.event.events.impl.ObsidianPlaceEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;

public class BlockObsidian extends Block
{
    public BlockObsidian() {
        super(Material.ROCK);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        final ObsidianPlaceEvent event = new ObsidianPlaceEvent(this, pos);
        EventManager.call(event);
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Item.getItemFromBlock(Blocks.OBSIDIAN);
    }
    
    @Override
    @Deprecated
    public MapColor getMapColor(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return MapColor.BLACK;
    }
}
