/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlock
extends Item {
    protected final Block block;

    public ItemBlock(Block block) {
        this.block = block;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY) {
        ItemStack itemstack;
        IBlockState iblockstate = playerIn.getBlockState(worldIn);
        Block block = iblockstate.getBlock();
        if (!block.isReplaceable(playerIn, worldIn)) {
            worldIn = worldIn.offset(hand);
        }
        if (!(itemstack = stack.getHeldItem(pos)).isEmpty() && stack.canPlayerEdit(worldIn, hand, itemstack) && playerIn.mayPlace(this.block, worldIn, false, hand, null)) {
            int i = this.getMetadata(itemstack.getMetadata());
            IBlockState iblockstate1 = this.block.getStateForPlacement(playerIn, worldIn, hand, facing, hitX, hitY, i, stack);
            if (playerIn.setBlockState(worldIn, iblockstate1, 11)) {
                iblockstate1 = playerIn.getBlockState(worldIn);
                if (iblockstate1.getBlock() == this.block) {
                    ItemBlock.setTileEntityNBT(playerIn, stack, worldIn, itemstack);
                    this.block.onBlockPlacedBy(playerIn, worldIn, iblockstate1, stack, itemstack);
                    if (stack instanceof EntityPlayerMP) {
                        CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP)stack, worldIn, itemstack);
                    }
                }
                SoundType soundtype = this.block.getSoundType();
                playerIn.playSound(stack, worldIn, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0f) / 2.0f, soundtype.getPitch() * 0.8f);
                itemstack.func_190918_g(1);
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }

    public static boolean setTileEntityNBT(World worldIn, @Nullable EntityPlayer player, BlockPos pos, ItemStack stackIn) {
        TileEntity tileentity;
        MinecraftServer minecraftserver = worldIn.getInstanceServer();
        if (minecraftserver == null) {
            return false;
        }
        NBTTagCompound nbttagcompound = stackIn.getSubCompound("BlockEntityTag");
        if (nbttagcompound != null && (tileentity = worldIn.getTileEntity(pos)) != null) {
            if (!(worldIn.isRemote || !tileentity.onlyOpsCanSetNbt() || player != null && player.canUseCommandBlock())) {
                return false;
            }
            NBTTagCompound nbttagcompound1 = tileentity.writeToNBT(new NBTTagCompound());
            NBTTagCompound nbttagcompound2 = nbttagcompound1.copy();
            nbttagcompound1.merge(nbttagcompound);
            nbttagcompound1.setInteger("x", pos.getX());
            nbttagcompound1.setInteger("y", pos.getY());
            nbttagcompound1.setInteger("z", pos.getZ());
            if (!nbttagcompound1.equals(nbttagcompound2)) {
                tileentity.readFromNBT(nbttagcompound1);
                tileentity.markDirty();
                return true;
            }
        }
        return false;
    }

    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack) {
        Block block = worldIn.getBlockState(pos).getBlock();
        if (block == Blocks.SNOW_LAYER) {
            side = EnumFacing.UP;
        } else if (!block.isReplaceable(worldIn, pos)) {
            pos = pos.offset(side);
        }
        return worldIn.mayPlace(this.block, pos, false, side, null);
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
    public void getSubItems(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
        if (this.func_194125_a(itemIn)) {
            this.block.getSubBlocks(itemIn, tab);
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        this.block.func_190948_a(stack, playerIn, tooltip, advanced);
    }

    public Block getBlock() {
        return this.block;
    }
}

