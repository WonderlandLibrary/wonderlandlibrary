// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockSnow;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;

public class ItemBlock extends Item
{
    protected final Block block;
    private static final String __OBFID = "CL_00001772";
    
    public ItemBlock(final Block block) {
        this.block = block;
    }
    
    @Override
    public ItemBlock setUnlocalizedName(final String unlocalizedName) {
        super.setUnlocalizedName(unlocalizedName);
        return this;
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, BlockPos pos, EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        final IBlockState var9 = worldIn.getBlockState(pos);
        final Block var10 = var9.getBlock();
        if (var10 == Blocks.snow_layer && (int)var9.getValue(BlockSnow.LAYERS_PROP) < 1) {
            side = EnumFacing.UP;
        }
        else if (!var10.isReplaceable(worldIn, pos)) {
            pos = pos.offset(side);
        }
        if (stack.stackSize == 0) {
            return false;
        }
        if (!playerIn.func_175151_a(pos, side, stack)) {
            return false;
        }
        if (pos.getY() == 255 && this.block.getMaterial().isSolid()) {
            return false;
        }
        if (worldIn.canBlockBePlaced(this.block, pos, false, side, null, stack)) {
            final int var11 = this.getMetadata(stack.getMetadata());
            IBlockState var12 = this.block.onBlockPlaced(worldIn, pos, side, hitX, hitY, hitZ, var11, playerIn);
            if (worldIn.setBlockState(pos, var12, 3)) {
                var12 = worldIn.getBlockState(pos);
                if (var12.getBlock() == this.block) {
                    setTileEntityNBT(worldIn, pos, stack);
                    this.block.onBlockPlacedBy(worldIn, pos, var12, playerIn, stack);
                }
                worldIn.playSoundEffect(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume() + 1.0f) / 2.0f, this.block.stepSound.getFrequency() * 0.8f);
                --stack.stackSize;
            }
            return true;
        }
        return false;
    }
    
    public static boolean setTileEntityNBT(final World worldIn, final BlockPos p_179224_1_, final ItemStack p_179224_2_) {
        if (p_179224_2_.hasTagCompound() && p_179224_2_.getTagCompound().hasKey("BlockEntityTag", 10)) {
            final TileEntity var3 = worldIn.getTileEntity(p_179224_1_);
            if (var3 != null) {
                final NBTTagCompound var4 = new NBTTagCompound();
                final NBTTagCompound var5 = (NBTTagCompound)var4.copy();
                var3.writeToNBT(var4);
                final NBTTagCompound var6 = (NBTTagCompound)p_179224_2_.getTagCompound().getTag("BlockEntityTag");
                var4.merge(var6);
                var4.setInteger("x", p_179224_1_.getX());
                var4.setInteger("y", p_179224_1_.getY());
                var4.setInteger("z", p_179224_1_.getZ());
                if (!var4.equals(var5)) {
                    var3.readFromNBT(var4);
                    var3.markDirty();
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean canPlaceBlockOnSide(final World worldIn, BlockPos p_179222_2_, EnumFacing p_179222_3_, final EntityPlayer p_179222_4_, final ItemStack p_179222_5_) {
        final Block var6 = worldIn.getBlockState(p_179222_2_).getBlock();
        if (var6 == Blocks.snow_layer) {
            p_179222_3_ = EnumFacing.UP;
        }
        else if (!var6.isReplaceable(worldIn, p_179222_2_)) {
            p_179222_2_ = p_179222_2_.offset(p_179222_3_);
        }
        return worldIn.canBlockBePlaced(this.block, p_179222_2_, false, p_179222_3_, null, p_179222_5_);
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
    public void getSubItems(final Item itemIn, final CreativeTabs tab, final List subItems) {
        this.block.getSubBlocks(itemIn, tab, subItems);
    }
    
    public Block getBlock() {
        return this.block;
    }
}
