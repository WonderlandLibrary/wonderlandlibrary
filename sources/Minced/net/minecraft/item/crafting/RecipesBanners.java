// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item.crafting;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBanner;
import javax.annotation.Nullable;
import net.minecraft.util.NonNullList;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.init.Items;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;

public class RecipesBanners
{
    public static class RecipeAddPattern implements IRecipe
    {
        @Override
        public boolean matches(final InventoryCrafting inv, final World worldIn) {
            boolean flag = false;
            for (int i = 0; i < inv.getSizeInventory(); ++i) {
                final ItemStack itemstack = inv.getStackInSlot(i);
                if (itemstack.getItem() == Items.BANNER) {
                    if (flag) {
                        return false;
                    }
                    if (TileEntityBanner.getPatterns(itemstack) >= 6) {
                        return false;
                    }
                    flag = true;
                }
            }
            return flag && this.matchPatterns(inv) != null;
        }
        
        @Override
        public ItemStack getCraftingResult(final InventoryCrafting inv) {
            ItemStack itemstack = ItemStack.EMPTY;
            for (int i = 0; i < inv.getSizeInventory(); ++i) {
                final ItemStack itemstack2 = inv.getStackInSlot(i);
                if (!itemstack2.isEmpty() && itemstack2.getItem() == Items.BANNER) {
                    itemstack = itemstack2.copy();
                    itemstack.setCount(1);
                    break;
                }
            }
            final BannerPattern bannerpattern = this.matchPatterns(inv);
            if (bannerpattern != null) {
                int k = 0;
                for (int j = 0; j < inv.getSizeInventory(); ++j) {
                    final ItemStack itemstack3 = inv.getStackInSlot(j);
                    if (itemstack3.getItem() == Items.DYE) {
                        k = itemstack3.getMetadata();
                        break;
                    }
                }
                final NBTTagCompound nbttagcompound1 = itemstack.getOrCreateSubCompound("BlockEntityTag");
                NBTTagList nbttaglist;
                if (nbttagcompound1.hasKey("Patterns", 9)) {
                    nbttaglist = nbttagcompound1.getTagList("Patterns", 10);
                }
                else {
                    nbttaglist = new NBTTagList();
                    nbttagcompound1.setTag("Patterns", nbttaglist);
                }
                final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                nbttagcompound2.setString("Pattern", bannerpattern.getHashname());
                nbttagcompound2.setInteger("Color", k);
                nbttaglist.appendTag(nbttagcompound2);
            }
            return itemstack;
        }
        
        @Override
        public ItemStack getRecipeOutput() {
            return ItemStack.EMPTY;
        }
        
        @Override
        public NonNullList<ItemStack> getRemainingItems(final InventoryCrafting inv) {
            final NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
            for (int i = 0; i < nonnulllist.size(); ++i) {
                final ItemStack itemstack = inv.getStackInSlot(i);
                if (itemstack.getItem().hasContainerItem()) {
                    nonnulllist.set(i, new ItemStack(itemstack.getItem().getContainerItem()));
                }
            }
            return nonnulllist;
        }
        
        @Nullable
        private BannerPattern matchPatterns(final InventoryCrafting p_190933_1_) {
            for (final BannerPattern bannerpattern : BannerPattern.values()) {
                if (bannerpattern.hasPattern()) {
                    boolean flag = true;
                    if (bannerpattern.hasPatternItem()) {
                        boolean flag2 = false;
                        boolean flag3 = false;
                        for (int i = 0; i < p_190933_1_.getSizeInventory() && flag; ++i) {
                            final ItemStack itemstack = p_190933_1_.getStackInSlot(i);
                            if (!itemstack.isEmpty() && itemstack.getItem() != Items.BANNER) {
                                if (itemstack.getItem() == Items.DYE) {
                                    if (flag3) {
                                        flag = false;
                                        break;
                                    }
                                    flag3 = true;
                                }
                                else {
                                    if (flag2 || !itemstack.isItemEqual(bannerpattern.getPatternItem())) {
                                        flag = false;
                                        break;
                                    }
                                    flag2 = true;
                                }
                            }
                        }
                        if (!flag2 || !flag3) {
                            flag = false;
                        }
                    }
                    else if (p_190933_1_.getSizeInventory() == bannerpattern.getPatterns().length * bannerpattern.getPatterns()[0].length()) {
                        int j = -1;
                        for (int k = 0; k < p_190933_1_.getSizeInventory() && flag; ++k) {
                            final int l = k / 3;
                            final int i2 = k % 3;
                            final ItemStack itemstack2 = p_190933_1_.getStackInSlot(k);
                            if (!itemstack2.isEmpty() && itemstack2.getItem() != Items.BANNER) {
                                if (itemstack2.getItem() != Items.DYE) {
                                    flag = false;
                                    break;
                                }
                                if (j != -1 && j != itemstack2.getMetadata()) {
                                    flag = false;
                                    break;
                                }
                                if (bannerpattern.getPatterns()[l].charAt(i2) == ' ') {
                                    flag = false;
                                    break;
                                }
                                j = itemstack2.getMetadata();
                            }
                            else if (bannerpattern.getPatterns()[l].charAt(i2) != ' ') {
                                flag = false;
                                break;
                            }
                        }
                    }
                    else {
                        flag = false;
                    }
                    if (flag) {
                        return bannerpattern;
                    }
                }
            }
            return null;
        }
        
        @Override
        public boolean isDynamic() {
            return true;
        }
        
        @Override
        public boolean canFit(final int width, final int height) {
            return width >= 3 && height >= 3;
        }
    }
    
    public static class RecipeDuplicatePattern implements IRecipe
    {
        @Override
        public boolean matches(final InventoryCrafting inv, final World worldIn) {
            ItemStack itemstack = ItemStack.EMPTY;
            ItemStack itemstack2 = ItemStack.EMPTY;
            for (int i = 0; i < inv.getSizeInventory(); ++i) {
                final ItemStack itemstack3 = inv.getStackInSlot(i);
                if (!itemstack3.isEmpty()) {
                    if (itemstack3.getItem() != Items.BANNER) {
                        return false;
                    }
                    if (!itemstack.isEmpty() && !itemstack2.isEmpty()) {
                        return false;
                    }
                    final EnumDyeColor enumdyecolor = ItemBanner.getBaseColor(itemstack3);
                    final boolean flag = TileEntityBanner.getPatterns(itemstack3) > 0;
                    if (!itemstack.isEmpty()) {
                        if (flag) {
                            return false;
                        }
                        if (enumdyecolor != ItemBanner.getBaseColor(itemstack)) {
                            return false;
                        }
                        itemstack2 = itemstack3;
                    }
                    else if (!itemstack2.isEmpty()) {
                        if (!flag) {
                            return false;
                        }
                        if (enumdyecolor != ItemBanner.getBaseColor(itemstack2)) {
                            return false;
                        }
                        itemstack = itemstack3;
                    }
                    else if (flag) {
                        itemstack = itemstack3;
                    }
                    else {
                        itemstack2 = itemstack3;
                    }
                }
            }
            return !itemstack.isEmpty() && !itemstack2.isEmpty();
        }
        
        @Override
        public ItemStack getCraftingResult(final InventoryCrafting inv) {
            for (int i = 0; i < inv.getSizeInventory(); ++i) {
                final ItemStack itemstack = inv.getStackInSlot(i);
                if (!itemstack.isEmpty() && TileEntityBanner.getPatterns(itemstack) > 0) {
                    final ItemStack itemstack2 = itemstack.copy();
                    itemstack2.setCount(1);
                    return itemstack2;
                }
            }
            return ItemStack.EMPTY;
        }
        
        @Override
        public ItemStack getRecipeOutput() {
            return ItemStack.EMPTY;
        }
        
        @Override
        public NonNullList<ItemStack> getRemainingItems(final InventoryCrafting inv) {
            final NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
            for (int i = 0; i < nonnulllist.size(); ++i) {
                final ItemStack itemstack = inv.getStackInSlot(i);
                if (!itemstack.isEmpty()) {
                    if (itemstack.getItem().hasContainerItem()) {
                        nonnulllist.set(i, new ItemStack(itemstack.getItem().getContainerItem()));
                    }
                    else if (itemstack.hasTagCompound() && TileEntityBanner.getPatterns(itemstack) > 0) {
                        final ItemStack itemstack2 = itemstack.copy();
                        itemstack2.setCount(1);
                        nonnulllist.set(i, itemstack2);
                    }
                }
            }
            return nonnulllist;
        }
        
        @Override
        public boolean isDynamic() {
            return true;
        }
        
        @Override
        public boolean canFit(final int width, final int height) {
            return width * height >= 2;
        }
    }
}
