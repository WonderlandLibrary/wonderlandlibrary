/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item.crafting;

import javax.annotation.Nullable;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class RecipesBanners {

    public static class RecipeDuplicatePattern
    implements IRecipe {
        @Override
        public boolean matches(InventoryCrafting inv, World worldIn) {
            ItemStack itemstack = ItemStack.field_190927_a;
            ItemStack itemstack1 = ItemStack.field_190927_a;
            for (int i = 0; i < inv.getSizeInventory(); ++i) {
                boolean flag;
                ItemStack itemstack2 = inv.getStackInSlot(i);
                if (itemstack2.isEmpty()) continue;
                if (itemstack2.getItem() != Items.BANNER) {
                    return false;
                }
                if (!itemstack.isEmpty() && !itemstack1.isEmpty()) {
                    return false;
                }
                EnumDyeColor enumdyecolor = ItemBanner.getBaseColor(itemstack2);
                boolean bl = flag = TileEntityBanner.getPatterns(itemstack2) > 0;
                if (!itemstack.isEmpty()) {
                    if (flag) {
                        return false;
                    }
                    if (enumdyecolor != ItemBanner.getBaseColor(itemstack)) {
                        return false;
                    }
                    itemstack1 = itemstack2;
                    continue;
                }
                if (!itemstack1.isEmpty()) {
                    if (!flag) {
                        return false;
                    }
                    if (enumdyecolor != ItemBanner.getBaseColor(itemstack1)) {
                        return false;
                    }
                    itemstack = itemstack2;
                    continue;
                }
                if (flag) {
                    itemstack = itemstack2;
                    continue;
                }
                itemstack1 = itemstack2;
            }
            return !itemstack.isEmpty() && !itemstack1.isEmpty();
        }

        @Override
        public ItemStack getCraftingResult(InventoryCrafting inv) {
            for (int i = 0; i < inv.getSizeInventory(); ++i) {
                ItemStack itemstack = inv.getStackInSlot(i);
                if (itemstack.isEmpty() || TileEntityBanner.getPatterns(itemstack) <= 0) continue;
                ItemStack itemstack1 = itemstack.copy();
                itemstack1.func_190920_e(1);
                return itemstack1;
            }
            return ItemStack.field_190927_a;
        }

        @Override
        public ItemStack getRecipeOutput() {
            return ItemStack.field_190927_a;
        }

        @Override
        public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
            NonNullList<ItemStack> nonnulllist = NonNullList.func_191197_a(inv.getSizeInventory(), ItemStack.field_190927_a);
            for (int i = 0; i < nonnulllist.size(); ++i) {
                ItemStack itemstack = inv.getStackInSlot(i);
                if (itemstack.isEmpty()) continue;
                if (itemstack.getItem().hasContainerItem()) {
                    nonnulllist.set(i, new ItemStack(itemstack.getItem().getContainerItem()));
                    continue;
                }
                if (!itemstack.hasTagCompound() || TileEntityBanner.getPatterns(itemstack) <= 0) continue;
                ItemStack itemstack1 = itemstack.copy();
                itemstack1.func_190920_e(1);
                nonnulllist.set(i, itemstack1);
            }
            return nonnulllist;
        }

        @Override
        public boolean func_192399_d() {
            return true;
        }

        @Override
        public boolean func_194133_a(int p_194133_1_, int p_194133_2_) {
            return p_194133_1_ * p_194133_2_ >= 2;
        }
    }

    public static class RecipeAddPattern
    implements IRecipe {
        @Override
        public boolean matches(InventoryCrafting inv, World worldIn) {
            boolean flag = false;
            for (int i = 0; i < inv.getSizeInventory(); ++i) {
                ItemStack itemstack = inv.getStackInSlot(i);
                if (itemstack.getItem() != Items.BANNER) continue;
                if (flag) {
                    return false;
                }
                if (TileEntityBanner.getPatterns(itemstack) >= 6) {
                    return false;
                }
                flag = true;
            }
            if (!flag) {
                return false;
            }
            return this.func_190933_c(inv) != null;
        }

        @Override
        public ItemStack getCraftingResult(InventoryCrafting inv) {
            BannerPattern bannerpattern;
            ItemStack itemstack = ItemStack.field_190927_a;
            for (int i = 0; i < inv.getSizeInventory(); ++i) {
                ItemStack itemstack1 = inv.getStackInSlot(i);
                if (itemstack1.isEmpty() || itemstack1.getItem() != Items.BANNER) continue;
                itemstack = itemstack1.copy();
                itemstack.func_190920_e(1);
                break;
            }
            if ((bannerpattern = this.func_190933_c(inv)) != null) {
                NBTTagList nbttaglist;
                NBTTagCompound nbttagcompound1;
                int k = 0;
                for (int j = 0; j < inv.getSizeInventory(); ++j) {
                    ItemStack itemstack2 = inv.getStackInSlot(j);
                    if (itemstack2.getItem() != Items.DYE) continue;
                    k = itemstack2.getMetadata();
                    break;
                }
                if ((nbttagcompound1 = itemstack.func_190925_c("BlockEntityTag")).hasKey("Patterns", 9)) {
                    nbttaglist = nbttagcompound1.getTagList("Patterns", 10);
                } else {
                    nbttaglist = new NBTTagList();
                    nbttagcompound1.setTag("Patterns", nbttaglist);
                }
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setString("Pattern", bannerpattern.func_190993_b());
                nbttagcompound.setInteger("Color", k);
                nbttaglist.appendTag(nbttagcompound);
            }
            return itemstack;
        }

        @Override
        public ItemStack getRecipeOutput() {
            return ItemStack.field_190927_a;
        }

        @Override
        public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
            NonNullList<ItemStack> nonnulllist = NonNullList.func_191197_a(inv.getSizeInventory(), ItemStack.field_190927_a);
            for (int i = 0; i < nonnulllist.size(); ++i) {
                ItemStack itemstack = inv.getStackInSlot(i);
                if (!itemstack.getItem().hasContainerItem()) continue;
                nonnulllist.set(i, new ItemStack(itemstack.getItem().getContainerItem()));
            }
            return nonnulllist;
        }

        /*
         * Enabled aggressive block sorting
         */
        @Nullable
        private BannerPattern func_190933_c(InventoryCrafting p_190933_1_) {
            BannerPattern[] arrbannerPattern = BannerPattern.values();
            int n = arrbannerPattern.length;
            int n2 = 0;
            while (true) {
                block12: {
                    boolean flag;
                    BannerPattern bannerpattern;
                    block17: {
                        int j;
                        block16: {
                            boolean flag2;
                            boolean flag1;
                            block14: {
                                block15: {
                                    block13: {
                                        if (n2 >= n) {
                                            return null;
                                        }
                                        bannerpattern = arrbannerPattern[n2];
                                        if (!bannerpattern.func_191000_d()) break block12;
                                        flag = true;
                                        if (!bannerpattern.func_190999_e()) break block13;
                                        flag1 = false;
                                        flag2 = false;
                                        break block14;
                                    }
                                    if (p_190933_1_.getSizeInventory() != bannerpattern.func_190996_c().length * bannerpattern.func_190996_c()[0].length()) break block15;
                                    j = -1;
                                    break block16;
                                }
                                flag = false;
                                break block17;
                            }
                            for (int i = 0; i < p_190933_1_.getSizeInventory() && flag; ++i) {
                                ItemStack itemstack = p_190933_1_.getStackInSlot(i);
                                if (itemstack.isEmpty() || itemstack.getItem() == Items.BANNER) continue;
                                if (itemstack.getItem() == Items.DYE) {
                                    if (flag2) {
                                        flag = false;
                                        break;
                                    }
                                    flag2 = true;
                                    continue;
                                }
                                if (flag1 || !itemstack.isItemEqual(bannerpattern.func_190998_f())) {
                                    flag = false;
                                    break;
                                }
                                flag1 = true;
                            }
                            if (flag1 && flag2) break block17;
                            flag = false;
                            break block17;
                        }
                        for (int k = 0; k < p_190933_1_.getSizeInventory() && flag; ++k) {
                            int l = k / 3;
                            int i1 = k % 3;
                            ItemStack itemstack1 = p_190933_1_.getStackInSlot(k);
                            if (!itemstack1.isEmpty() && itemstack1.getItem() != Items.BANNER) {
                                if (itemstack1.getItem() != Items.DYE) {
                                    flag = false;
                                    break;
                                }
                                if (j != -1 && j != itemstack1.getMetadata()) {
                                    flag = false;
                                    break;
                                }
                                if (bannerpattern.func_190996_c()[l].charAt(i1) == ' ') {
                                    flag = false;
                                    break;
                                }
                                j = itemstack1.getMetadata();
                                continue;
                            }
                            if (bannerpattern.func_190996_c()[l].charAt(i1) == ' ') continue;
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        return bannerpattern;
                    }
                }
                ++n2;
            }
        }

        @Override
        public boolean func_192399_d() {
            return true;
        }

        @Override
        public boolean func_194133_a(int p_194133_1_, int p_194133_2_) {
            return p_194133_1_ >= 3 && p_194133_2_ >= 3;
        }
    }
}

