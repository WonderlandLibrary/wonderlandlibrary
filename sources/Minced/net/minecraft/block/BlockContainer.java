// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.stats.StatList;
import net.minecraft.world.IWorldNameable;
import net.minecraft.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public abstract class BlockContainer extends Block implements ITileEntityProvider
{
    protected BlockContainer(final Material materialIn) {
        this(materialIn, materialIn.getMaterialMapColor());
    }
    
    protected BlockContainer(final Material materialIn, final MapColor color) {
        super(materialIn, color);
        this.hasTileEntity = true;
    }
    
    protected boolean isInvalidNeighbor(final World worldIn, final BlockPos pos, final EnumFacing facing) {
        return worldIn.getBlockState(pos.offset(facing)).getMaterial() == Material.CACTUS;
    }
    
    protected boolean hasInvalidNeighbor(final World worldIn, final BlockPos pos) {
        return this.isInvalidNeighbor(worldIn, pos, EnumFacing.NORTH) || this.isInvalidNeighbor(worldIn, pos, EnumFacing.SOUTH) || this.isInvalidNeighbor(worldIn, pos, EnumFacing.WEST) || this.isInvalidNeighbor(worldIn, pos, EnumFacing.EAST);
    }
    
    @Override
    @Deprecated
    public EnumBlockRenderType getRenderType(final IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }
    
    @Override
    public void harvestBlock(final World worldIn, final EntityPlayer player, final BlockPos pos, final IBlockState state, @Nullable final TileEntity te, final ItemStack stack) {
        if (te instanceof IWorldNameable && ((IWorldNameable)te).hasCustomName()) {
            player.addStat(StatList.getBlockStats(this));
            player.addExhaustion(0.005f);
            if (worldIn.isRemote) {
                return;
            }
            final int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
            final Item item = this.getItemDropped(state, worldIn.rand, i);
            if (item == Items.AIR) {
                return;
            }
            final ItemStack itemstack = new ItemStack(item, this.quantityDropped(worldIn.rand));
            itemstack.setStackDisplayName(((IWorldNameable)te).getName());
            Block.spawnAsEntity(worldIn, pos, itemstack);
        }
        else {
            super.harvestBlock(worldIn, player, pos, state, null, stack);
        }
    }
    
    @Override
    @Deprecated
    public boolean eventReceived(final IBlockState state, final World worldIn, final BlockPos pos, final int id, final int param) {
        super.eventReceived(state, worldIn, pos, id, param);
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(id, param);
    }
}
