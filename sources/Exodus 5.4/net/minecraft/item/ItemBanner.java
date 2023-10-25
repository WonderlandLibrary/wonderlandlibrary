/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import java.util.List;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockWallSign;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemBanner
extends ItemBlock {
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> list, boolean bl) {
        NBTTagCompound nBTTagCompound = itemStack.getSubCompound("BlockEntityTag", false);
        if (nBTTagCompound != null && nBTTagCompound.hasKey("Patterns")) {
            NBTTagList nBTTagList = nBTTagCompound.getTagList("Patterns", 10);
            int n = 0;
            while (n < nBTTagList.tagCount() && n < 6) {
                NBTTagCompound nBTTagCompound2 = nBTTagList.getCompoundTagAt(n);
                EnumDyeColor enumDyeColor = EnumDyeColor.byDyeDamage(nBTTagCompound2.getInteger("Color"));
                TileEntityBanner.EnumBannerPattern enumBannerPattern = TileEntityBanner.EnumBannerPattern.getPatternByID(nBTTagCompound2.getString("Pattern"));
                if (enumBannerPattern != null) {
                    list.add(StatCollector.translateToLocal("item.banner." + enumBannerPattern.getPatternName() + "." + enumDyeColor.getUnlocalizedName()));
                }
                ++n;
            }
        }
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3) {
        if (enumFacing == EnumFacing.DOWN) {
            return false;
        }
        if (!world.getBlockState(blockPos).getBlock().getMaterial().isSolid()) {
            return false;
        }
        if (!entityPlayer.canPlayerEdit(blockPos = blockPos.offset(enumFacing), enumFacing, itemStack)) {
            return false;
        }
        if (!Blocks.standing_banner.canPlaceBlockAt(world, blockPos)) {
            return false;
        }
        if (world.isRemote) {
            return true;
        }
        if (enumFacing == EnumFacing.UP) {
            int n = MathHelper.floor_double((double)((entityPlayer.rotationYaw + 180.0f) * 16.0f / 360.0f) + 0.5) & 0xF;
            world.setBlockState(blockPos, Blocks.standing_banner.getDefaultState().withProperty(BlockStandingSign.ROTATION, n), 3);
        } else {
            world.setBlockState(blockPos, Blocks.wall_banner.getDefaultState().withProperty(BlockWallSign.FACING, enumFacing), 3);
        }
        --itemStack.stackSize;
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (tileEntity instanceof TileEntityBanner) {
            ((TileEntityBanner)tileEntity).setItemValues(itemStack);
        }
        return true;
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        String string = "item.banner.";
        EnumDyeColor enumDyeColor = this.getBaseColor(itemStack);
        string = String.valueOf(string) + enumDyeColor.getUnlocalizedName() + ".name";
        return StatCollector.translateToLocal(string);
    }

    @Override
    public int getColorFromItemStack(ItemStack itemStack, int n) {
        if (n == 0) {
            return 0xFFFFFF;
        }
        EnumDyeColor enumDyeColor = this.getBaseColor(itemStack);
        return enumDyeColor.getMapColor().colorValue;
    }

    private EnumDyeColor getBaseColor(ItemStack itemStack) {
        NBTTagCompound nBTTagCompound = itemStack.getSubCompound("BlockEntityTag", false);
        EnumDyeColor enumDyeColor = null;
        enumDyeColor = nBTTagCompound != null && nBTTagCompound.hasKey("Base") ? EnumDyeColor.byDyeDamage(nBTTagCompound.getInteger("Base")) : EnumDyeColor.byDyeDamage(itemStack.getMetadata());
        return enumDyeColor;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs creativeTabs, List<ItemStack> list) {
        EnumDyeColor[] enumDyeColorArray = EnumDyeColor.values();
        int n = enumDyeColorArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumDyeColor enumDyeColor = enumDyeColorArray[n2];
            NBTTagCompound nBTTagCompound = new NBTTagCompound();
            TileEntityBanner.func_181020_a(nBTTagCompound, enumDyeColor.getDyeDamage(), null);
            NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
            nBTTagCompound2.setTag("BlockEntityTag", nBTTagCompound);
            ItemStack itemStack = new ItemStack(item, 1, enumDyeColor.getDyeDamage());
            itemStack.setTagCompound(nBTTagCompound2);
            list.add(itemStack);
            ++n2;
        }
    }

    @Override
    public CreativeTabs getCreativeTab() {
        return CreativeTabs.tabDecorations;
    }

    public ItemBanner() {
        super(Blocks.standing_banner);
        this.maxStackSize = 16;
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }
}

