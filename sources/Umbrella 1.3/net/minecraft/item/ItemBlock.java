/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemBlock
extends Item {
    protected final Block block;
    private static final String __OBFID = "CL_00001772";

    public ItemBlock(Block block) {
        this.block = block;
    }

    @Override
    public ItemBlock setUnlocalizedName(String unlocalizedName) {
        super.setUnlocalizedName(unlocalizedName);
        return this;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        IBlockState var9 = worldIn.getBlockState(pos);
        Block var10 = var9.getBlock();
        if (var10 == Blocks.snow_layer && (Integer)var9.getValue(BlockSnow.LAYERS_PROP) < 1) {
            side = EnumFacing.UP;
        } else if (!var10.isReplaceable(worldIn, pos)) {
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
            int var11 = this.getMetadata(stack.getMetadata());
            IBlockState var12 = this.block.onBlockPlaced(worldIn, pos, side, hitX, hitY, hitZ, var11, playerIn);
            if (worldIn.setBlockState(pos, var12, 3)) {
                var12 = worldIn.getBlockState(pos);
                if (var12.getBlock() == this.block) {
                    ItemBlock.setTileEntityNBT(worldIn, pos, stack);
                    this.block.onBlockPlacedBy(worldIn, pos, var12, playerIn, stack);
                }
                worldIn.playSoundEffect((float)pos.getX() + 0.5f, (float)pos.getY() + 0.5f, (float)pos.getZ() + 0.5f, this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume() + 1.0f) / 2.0f, this.block.stepSound.getFrequency() * 0.8f);
                --stack.stackSize;
            }
            return true;
        }
        return false;
    }

    public static boolean setTileEntityNBT(World worldIn, BlockPos p_179224_1_, ItemStack p_179224_2_) {
        TileEntity var3;
        if (p_179224_2_.hasTagCompound() && p_179224_2_.getTagCompound().hasKey("BlockEntityTag", 10) && (var3 = worldIn.getTileEntity(p_179224_1_)) != null) {
            NBTTagCompound var4 = new NBTTagCompound();
            NBTTagCompound var5 = (NBTTagCompound)var4.copy();
            var3.writeToNBT(var4);
            NBTTagCompound var6 = (NBTTagCompound)p_179224_2_.getTagCompound().getTag("BlockEntityTag");
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
        return false;
    }

    public boolean canPlaceBlockOnSide(World worldIn, BlockPos p_179222_2_, EnumFacing p_179222_3_, EntityPlayer p_179222_4_, ItemStack p_179222_5_) {
        Block var6 = worldIn.getBlockState(p_179222_2_).getBlock();
        if (var6 == Blocks.snow_layer) {
            p_179222_3_ = EnumFacing.UP;
        } else if (!var6.isReplaceable(worldIn, p_179222_2_)) {
            p_179222_2_ = p_179222_2_.offset(p_179222_3_);
        }
        return worldIn.canBlockBePlaced(this.block, p_179222_2_, false, p_179222_3_, null, p_179222_5_);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
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
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
        this.block.getSubBlocks(itemIn, tab, subItems);
    }

    public Block getBlock() {
        return this.block;
    }
}

