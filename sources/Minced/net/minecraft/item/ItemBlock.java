// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.client.util.ITooltipFlag;
import java.util.List;
import net.minecraft.util.NonNullList;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.nbt.NBTTagCompound;
import javax.annotation.Nullable;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.SoundCategory;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
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
    public EnumActionResult onItemUse(final EntityPlayer player, final World worldIn, BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final Block block = iblockstate.getBlock();
        if (!block.isReplaceable(worldIn, pos)) {
            pos = pos.offset(facing);
        }
        final ItemStack itemstack = player.getHeldItem(hand);
        if (!itemstack.isEmpty() && player.canPlayerEdit(pos, facing, itemstack) && worldIn.mayPlace(this.block, pos, false, facing, null)) {
            final int i = this.getMetadata(itemstack.getMetadata());
            IBlockState iblockstate2 = this.block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, i, player);
            if (worldIn.setBlockState(pos, iblockstate2, 11)) {
                iblockstate2 = worldIn.getBlockState(pos);
                if (iblockstate2.getBlock() == this.block) {
                    setTileEntityNBT(worldIn, player, pos, itemstack);
                    this.block.onBlockPlacedBy(worldIn, pos, iblockstate2, player, itemstack);
                    if (player instanceof EntityPlayerMP) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, itemstack);
                    }
                }
                final SoundType soundtype = this.block.getSoundType();
                worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0f) / 2.0f, soundtype.getPitch() * 0.8f);
                itemstack.shrink(1);
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }
    
    public static boolean setTileEntityNBT(final World worldIn, @Nullable final EntityPlayer player, final BlockPos pos, final ItemStack stackIn) {
        final MinecraftServer minecraftserver = worldIn.getMinecraftServer();
        if (minecraftserver == null) {
            return false;
        }
        final NBTTagCompound nbttagcompound = stackIn.getSubCompound("BlockEntityTag");
        if (nbttagcompound != null) {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity != null) {
                if (!worldIn.isRemote && tileentity.onlyOpsCanSetNbt() && (player == null || !player.canUseCommandBlock())) {
                    return false;
                }
                final NBTTagCompound nbttagcompound2 = tileentity.writeToNBT(new NBTTagCompound());
                final NBTTagCompound nbttagcompound3 = nbttagcompound2.copy();
                nbttagcompound2.merge(nbttagcompound);
                nbttagcompound2.setInteger("x", pos.getX());
                nbttagcompound2.setInteger("y", pos.getY());
                nbttagcompound2.setInteger("z", pos.getZ());
                if (!nbttagcompound2.equals(nbttagcompound3)) {
                    tileentity.readFromNBT(nbttagcompound2);
                    tileentity.markDirty();
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean canPlaceBlockOnSide(final World worldIn, BlockPos pos, EnumFacing side, final EntityPlayer player, final ItemStack stack) {
        final Block block = worldIn.getBlockState(pos).getBlock();
        if (block == Blocks.SNOW_LAYER) {
            side = EnumFacing.UP;
        }
        else if (!block.isReplaceable(worldIn, pos)) {
            pos = pos.offset(side);
        }
        return worldIn.mayPlace(this.block, pos, false, side, null);
    }
    
    @Override
    public String getTranslationKey(final ItemStack stack) {
        return this.block.getTranslationKey();
    }
    
    @Override
    public String getTranslationKey() {
        return this.block.getTranslationKey();
    }
    
    @Override
    public CreativeTabs getCreativeTab() {
        return this.block.getCreativeTab();
    }
    
    @Override
    public void getSubItems(final CreativeTabs tab, final NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            this.block.getSubBlocks(tab, items);
        }
    }
    
    @Override
    public void addInformation(final ItemStack stack, @Nullable final World worldIn, final List<String> tooltip, final ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        this.block.addInformation(stack, worldIn, tooltip, flagIn);
    }
    
    public Block getBlock() {
        return this.block;
    }
}
