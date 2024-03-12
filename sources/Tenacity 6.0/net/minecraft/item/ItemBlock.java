// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import dev.tenacity.viamcp.utils.FixedSoundEngine;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;

public class ItemBlock extends Item
{
    protected final Block block;
    
    public ItemBlock(final Block block) {
        this.block = block;
    }
    
    @Override
    public ItemBlock setUnlocalizedName(final String unlocalizedName) {
        super.setUnlocalizedName(unlocalizedName);
        return this;
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, final BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        return FixedSoundEngine.onItemUse(this, stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
    }
    
    public static boolean setTileEntityNBT(final World worldIn, final EntityPlayer pos, final BlockPos stack, final ItemStack p_179224_3_) {
        final MinecraftServer minecraftserver = MinecraftServer.getServer();
        if (minecraftserver != null && p_179224_3_.hasTagCompound() && p_179224_3_.getTagCompound().hasKey("BlockEntityTag", 10)) {
            final TileEntity tileentity = worldIn.getTileEntity(stack);
            if (tileentity != null) {
                if (!worldIn.isRemote && tileentity.func_183000_F() && !minecraftserver.getConfigurationManager().canSendCommands(pos.getGameProfile())) {
                    return false;
                }
                final NBTTagCompound nbttagcompound = new NBTTagCompound();
                final NBTTagCompound nbttagcompound2 = (NBTTagCompound)nbttagcompound.copy();
                tileentity.writeToNBT(nbttagcompound);
                final NBTTagCompound nbttagcompound3 = (NBTTagCompound)p_179224_3_.getTagCompound().getTag("BlockEntityTag");
                nbttagcompound.merge(nbttagcompound3);
                nbttagcompound.setInteger("x", stack.getX());
                nbttagcompound.setInteger("y", stack.getY());
                nbttagcompound.setInteger("z", stack.getZ());
                if (!nbttagcompound.equals(nbttagcompound2)) {
                    tileentity.readFromNBT(nbttagcompound);
                    tileentity.markDirty();
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean canPlaceBlockOnSide(final World worldIn, BlockPos pos, EnumFacing side, final EntityPlayer player, final ItemStack stack) {
        final Block block = worldIn.getBlockState(pos).getBlock();
        if (block == Blocks.snow_layer) {
            side = EnumFacing.UP;
        }
        else if (!block.isReplaceable(worldIn, pos)) {
            pos = pos.offset(side);
        }
        return worldIn.canBlockBePlaced(this.block, pos, false, side, null, stack);
    }
    
    @Override
    public String getUnlocalizedName(final ItemStack stack) {
        return this.block.getUnlocalizedName();
    }
    
    @Override
    public String getUnlocalizedName() {
        return this.block.getUnlocalizedName();
    }
    
    @Override
    public CreativeTabs getCreativeTab() {
        return this.block.getCreativeTabToDisplayOn();
    }
    
    @Override
    public void getSubItems(final Item itemIn, final CreativeTabs tab, final List<ItemStack> subItems) {
        this.block.getSubBlocks(itemIn, tab, subItems);
    }
    
    public Block getBlock() {
        return this.block;
    }
}
