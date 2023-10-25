/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemBlock
extends Item {
    protected final Block block;

    @Override
    public String getUnlocalizedName() {
        return this.block.getUnlocalizedName();
    }

    @Override
    public CreativeTabs getCreativeTab() {
        return this.block.getCreativeTabToDisplayOn();
    }

    public boolean canPlaceBlockOnSide(World world, BlockPos blockPos, EnumFacing enumFacing, EntityPlayer entityPlayer, ItemStack itemStack) {
        Block block = world.getBlockState(blockPos).getBlock();
        if (block == Blocks.snow_layer) {
            enumFacing = EnumFacing.UP;
        } else if (!block.isReplaceable(world, blockPos)) {
            blockPos = blockPos.offset(enumFacing);
        }
        return world.canBlockBePlaced(this.block, blockPos, false, enumFacing, null, itemStack);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return this.block.getUnlocalizedName();
    }

    public static boolean setTileEntityNBT(World world, EntityPlayer entityPlayer, BlockPos blockPos, ItemStack itemStack) {
        TileEntity tileEntity;
        MinecraftServer minecraftServer = MinecraftServer.getServer();
        if (minecraftServer == null) {
            return false;
        }
        if (itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("BlockEntityTag", 10) && (tileEntity = world.getTileEntity(blockPos)) != null) {
            if (!world.isRemote && tileEntity.func_183000_F() && !minecraftServer.getConfigurationManager().canSendCommands(entityPlayer.getGameProfile())) {
                return false;
            }
            NBTTagCompound nBTTagCompound = new NBTTagCompound();
            NBTTagCompound nBTTagCompound2 = (NBTTagCompound)nBTTagCompound.copy();
            tileEntity.writeToNBT(nBTTagCompound);
            NBTTagCompound nBTTagCompound3 = (NBTTagCompound)itemStack.getTagCompound().getTag("BlockEntityTag");
            nBTTagCompound.merge(nBTTagCompound3);
            nBTTagCompound.setInteger("x", blockPos.getX());
            nBTTagCompound.setInteger("y", blockPos.getY());
            nBTTagCompound.setInteger("z", blockPos.getZ());
            if (!nBTTagCompound.equals(nBTTagCompound2)) {
                tileEntity.readFromNBT(nBTTagCompound);
                tileEntity.markDirty();
                return true;
            }
        }
        return false;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs creativeTabs, List<ItemStack> list) {
        this.block.getSubBlocks(item, creativeTabs, list);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3) {
        IBlockState iBlockState = world.getBlockState(blockPos);
        Block block = iBlockState.getBlock();
        if (!block.isReplaceable(world, blockPos)) {
            blockPos = blockPos.offset(enumFacing);
        }
        if (itemStack.stackSize == 0) {
            return false;
        }
        if (!entityPlayer.canPlayerEdit(blockPos, enumFacing, itemStack)) {
            return false;
        }
        if (world.canBlockBePlaced(this.block, blockPos, false, enumFacing, null, itemStack)) {
            int n = this.getMetadata(itemStack.getMetadata());
            IBlockState iBlockState2 = this.block.onBlockPlaced(world, blockPos, enumFacing, f, f2, f3, n, entityPlayer);
            if (world.setBlockState(blockPos, iBlockState2, 3)) {
                iBlockState2 = world.getBlockState(blockPos);
                if (iBlockState2.getBlock() == this.block) {
                    ItemBlock.setTileEntityNBT(world, entityPlayer, blockPos, itemStack);
                    this.block.onBlockPlacedBy(world, blockPos, iBlockState2, entityPlayer, itemStack);
                }
                world.playSoundEffect((float)blockPos.getX() + 0.5f, (float)blockPos.getY() + 0.5f, (float)blockPos.getZ() + 0.5f, this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume() + 1.0f) / 2.0f, this.block.stepSound.getFrequency() * 0.8f);
                --itemStack.stackSize;
            }
            return true;
        }
        return false;
    }

    public ItemBlock(Block block) {
        this.block = block;
    }

    public Block getBlock() {
        return this.block;
    }

    @Override
    public ItemBlock setUnlocalizedName(String string) {
        super.setUnlocalizedName(string);
        return this;
    }
}

