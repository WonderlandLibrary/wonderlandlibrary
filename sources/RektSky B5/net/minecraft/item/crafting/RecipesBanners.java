/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.world.World;

public class RecipesBanners {
    void addRecipes(CraftingManager p_179534_1_) {
        for (EnumDyeColor enumdyecolor : EnumDyeColor.values()) {
            p_179534_1_.addRecipe(new ItemStack(Items.banner, 1, enumdyecolor.getDyeDamage()), "###", "###", " | ", Character.valueOf('#'), new ItemStack(Blocks.wool, 1, enumdyecolor.getMetadata()), Character.valueOf('|'), Items.stick);
        }
        p_179534_1_.addRecipe(new RecipeDuplicatePattern());
        p_179534_1_.addRecipe(new RecipeAddPattern());
    }

    static class RecipeDuplicatePattern
    implements IRecipe {
        private RecipeDuplicatePattern() {
        }

        @Override
        public boolean matches(InventoryCrafting inv, World worldIn) {
            ItemStack itemstack = null;
            ItemStack itemstack1 = null;
            for (int i2 = 0; i2 < inv.getSizeInventory(); ++i2) {
                boolean flag;
                ItemStack itemstack2 = inv.getStackInSlot(i2);
                if (itemstack2 == null) continue;
                if (itemstack2.getItem() != Items.banner) {
                    return false;
                }
                if (itemstack != null && itemstack1 != null) {
                    return false;
                }
                int j2 = TileEntityBanner.getBaseColor(itemstack2);
                boolean bl = flag = TileEntityBanner.getPatterns(itemstack2) > 0;
                if (itemstack != null) {
                    if (flag) {
                        return false;
                    }
                    if (j2 != TileEntityBanner.getBaseColor(itemstack)) {
                        return false;
                    }
                    itemstack1 = itemstack2;
                    continue;
                }
                if (itemstack1 != null) {
                    if (!flag) {
                        return false;
                    }
                    if (j2 != TileEntityBanner.getBaseColor(itemstack1)) {
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
            return itemstack != null && itemstack1 != null;
        }

        @Override
        public ItemStack getCraftingResult(InventoryCrafting inv) {
            for (int i2 = 0; i2 < inv.getSizeInventory(); ++i2) {
                ItemStack itemstack = inv.getStackInSlot(i2);
                if (itemstack == null || TileEntityBanner.getPatterns(itemstack) <= 0) continue;
                ItemStack itemstack1 = itemstack.copy();
                itemstack1.stackSize = 1;
                return itemstack1;
            }
            return null;
        }

        @Override
        public int getRecipeSize() {
            return 2;
        }

        @Override
        public ItemStack getRecipeOutput() {
            return null;
        }

        @Override
        public ItemStack[] getRemainingItems(InventoryCrafting inv) {
            ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];
            for (int i2 = 0; i2 < aitemstack.length; ++i2) {
                ItemStack itemstack = inv.getStackInSlot(i2);
                if (itemstack == null) continue;
                if (itemstack.getItem().hasContainerItem()) {
                    aitemstack[i2] = new ItemStack(itemstack.getItem().getContainerItem());
                    continue;
                }
                if (!itemstack.hasTagCompound() || TileEntityBanner.getPatterns(itemstack) <= 0) continue;
                aitemstack[i2] = itemstack.copy();
                aitemstack[i2].stackSize = 1;
            }
            return aitemstack;
        }
    }

    static class RecipeAddPattern
    implements IRecipe {
        private RecipeAddPattern() {
        }

        @Override
        public boolean matches(InventoryCrafting inv, World worldIn) {
            boolean flag = false;
            for (int i2 = 0; i2 < inv.getSizeInventory(); ++i2) {
                ItemStack itemstack = inv.getStackInSlot(i2);
                if (itemstack == null || itemstack.getItem() != Items.banner) continue;
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
            return this.func_179533_c(inv) != null;
        }

        @Override
        public ItemStack getCraftingResult(InventoryCrafting inv) {
            TileEntityBanner.EnumBannerPattern tileentitybanner$enumbannerpattern;
            ItemStack itemstack = null;
            for (int i2 = 0; i2 < inv.getSizeInventory(); ++i2) {
                ItemStack itemstack1 = inv.getStackInSlot(i2);
                if (itemstack1 == null || itemstack1.getItem() != Items.banner) continue;
                itemstack = itemstack1.copy();
                itemstack.stackSize = 1;
                break;
            }
            if ((tileentitybanner$enumbannerpattern = this.func_179533_c(inv)) != null) {
                int k2 = 0;
                for (int j2 = 0; j2 < inv.getSizeInventory(); ++j2) {
                    ItemStack itemstack2 = inv.getStackInSlot(j2);
                    if (itemstack2 == null || itemstack2.getItem() != Items.dye) continue;
                    k2 = itemstack2.getMetadata();
                    break;
                }
                NBTTagCompound nbttagcompound1 = itemstack.getSubCompound("BlockEntityTag", true);
                NBTTagList nbttaglist = null;
                if (nbttagcompound1.hasKey("Patterns", 9)) {
                    nbttaglist = nbttagcompound1.getTagList("Patterns", 10);
                } else {
                    nbttaglist = new NBTTagList();
                    nbttagcompound1.setTag("Patterns", nbttaglist);
                }
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setString("Pattern", tileentitybanner$enumbannerpattern.getPatternID());
                nbttagcompound.setInteger("Color", k2);
                nbttaglist.appendTag(nbttagcompound);
            }
            return itemstack;
        }

        @Override
        public int getRecipeSize() {
            return 10;
        }

        @Override
        public ItemStack getRecipeOutput() {
            return null;
        }

        @Override
        public ItemStack[] getRemainingItems(InventoryCrafting inv) {
            ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];
            for (int i2 = 0; i2 < aitemstack.length; ++i2) {
                ItemStack itemstack = inv.getStackInSlot(i2);
                if (itemstack == null || !itemstack.getItem().hasContainerItem()) continue;
                aitemstack[i2] = new ItemStack(itemstack.getItem().getContainerItem());
            }
            return aitemstack;
        }

        /*
         * Enabled aggressive block sorting
         */
        private TileEntityBanner.EnumBannerPattern func_179533_c(InventoryCrafting p_179533_1_) {
            TileEntityBanner.EnumBannerPattern[] enumBannerPatternArray = TileEntityBanner.EnumBannerPattern.values();
            int n2 = enumBannerPatternArray.length;
            int n3 = 0;
            while (true) {
                block12: {
                    boolean flag;
                    TileEntityBanner.EnumBannerPattern tileentitybanner$enumbannerpattern;
                    block17: {
                        int j2;
                        block16: {
                            boolean flag2;
                            boolean flag1;
                            block14: {
                                block15: {
                                    block13: {
                                        if (n3 >= n2) {
                                            return null;
                                        }
                                        tileentitybanner$enumbannerpattern = enumBannerPatternArray[n3];
                                        if (!tileentitybanner$enumbannerpattern.hasValidCrafting()) break block12;
                                        flag = true;
                                        if (!tileentitybanner$enumbannerpattern.hasCraftingStack()) break block13;
                                        flag1 = false;
                                        flag2 = false;
                                        break block14;
                                    }
                                    if (p_179533_1_.getSizeInventory() != tileentitybanner$enumbannerpattern.getCraftingLayers().length * tileentitybanner$enumbannerpattern.getCraftingLayers()[0].length()) break block15;
                                    j2 = -1;
                                    break block16;
                                }
                                flag = false;
                                break block17;
                            }
                            for (int i2 = 0; i2 < p_179533_1_.getSizeInventory() && flag; ++i2) {
                                ItemStack itemstack = p_179533_1_.getStackInSlot(i2);
                                if (itemstack == null || itemstack.getItem() == Items.banner) continue;
                                if (itemstack.getItem() == Items.dye) {
                                    if (flag2) {
                                        flag = false;
                                        break;
                                    }
                                    flag2 = true;
                                    continue;
                                }
                                if (flag1 || !itemstack.isItemEqual(tileentitybanner$enumbannerpattern.getCraftingStack())) {
                                    flag = false;
                                    break;
                                }
                                flag1 = true;
                            }
                            if (flag1) break block17;
                            flag = false;
                            break block17;
                        }
                        for (int k2 = 0; k2 < p_179533_1_.getSizeInventory() && flag; ++k2) {
                            int l2 = k2 / 3;
                            int i1 = k2 % 3;
                            ItemStack itemstack1 = p_179533_1_.getStackInSlot(k2);
                            if (itemstack1 != null && itemstack1.getItem() != Items.banner) {
                                if (itemstack1.getItem() != Items.dye) {
                                    flag = false;
                                    break;
                                }
                                if (j2 != -1 && j2 != itemstack1.getMetadata()) {
                                    flag = false;
                                    break;
                                }
                                if (tileentitybanner$enumbannerpattern.getCraftingLayers()[l2].charAt(i1) == ' ') {
                                    flag = false;
                                    break;
                                }
                                j2 = itemstack1.getMetadata();
                                continue;
                            }
                            if (tileentitybanner$enumbannerpattern.getCraftingLayers()[l2].charAt(i1) == ' ') continue;
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        return tileentitybanner$enumbannerpattern;
                    }
                }
                ++n3;
            }
        }
    }
}

