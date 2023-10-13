/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemHoe
extends Item {
    protected Item.ToolMaterial theToolMaterial;
    private static final String __OBFID = "CL_00000039";

    public ItemHoe(Item.ToolMaterial p_i45343_1_) {
        this.theToolMaterial = p_i45343_1_;
        this.maxStackSize = 1;
        this.setMaxDamage(p_i45343_1_.getMaxUses());
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!playerIn.func_175151_a(pos.offset(side), side, stack)) {
            return false;
        }
        IBlockState var9 = worldIn.getBlockState(pos);
        Block var10 = var9.getBlock();
        if (side != EnumFacing.DOWN && worldIn.getBlockState(pos.offsetUp()).getBlock().getMaterial() == Material.air) {
            if (var10 == Blocks.grass) {
                return this.func_179232_a(stack, playerIn, worldIn, pos, Blocks.farmland.getDefaultState());
            }
            if (var10 == Blocks.dirt) {
                switch (SwitchDirtType.field_179590_a[((BlockDirt.DirtType)((Object)var9.getValue(BlockDirt.VARIANT))).ordinal()]) {
                    case 1: {
                        return this.func_179232_a(stack, playerIn, worldIn, pos, Blocks.farmland.getDefaultState());
                    }
                    case 2: {
                        return this.func_179232_a(stack, playerIn, worldIn, pos, Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, (Comparable)((Object)BlockDirt.DirtType.DIRT)));
                    }
                }
            }
        }
        return false;
    }

    protected boolean func_179232_a(ItemStack p_179232_1_, EntityPlayer p_179232_2_, World worldIn, BlockPos p_179232_4_, IBlockState p_179232_5_) {
        worldIn.playSoundEffect((float)p_179232_4_.getX() + 0.5f, (float)p_179232_4_.getY() + 0.5f, (float)p_179232_4_.getZ() + 0.5f, p_179232_5_.getBlock().stepSound.getStepSound(), (p_179232_5_.getBlock().stepSound.getVolume() + 1.0f) / 2.0f, p_179232_5_.getBlock().stepSound.getFrequency() * 0.8f);
        if (worldIn.isRemote) {
            return true;
        }
        worldIn.setBlockState(p_179232_4_, p_179232_5_);
        p_179232_1_.damageItem(1, p_179232_2_);
        return true;
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    public String getMaterialName() {
        return this.theToolMaterial.toString();
    }

    static final class SwitchDirtType {
        static final int[] field_179590_a = new int[BlockDirt.DirtType.values().length];
        private static final String __OBFID = "CL_00002179";

        static {
            try {
                SwitchDirtType.field_179590_a[BlockDirt.DirtType.DIRT.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchDirtType.field_179590_a[BlockDirt.DirtType.COARSE_DIRT.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
        }

        SwitchDirtType() {
        }
    }
}

