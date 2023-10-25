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
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.world.World;

public class RecipesBanners {
    void addRecipes(CraftingManager craftingManager) {
        EnumDyeColor[] enumDyeColorArray = EnumDyeColor.values();
        int n = enumDyeColorArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumDyeColor enumDyeColor = enumDyeColorArray[n2];
            craftingManager.addRecipe(new ItemStack(Items.banner, 1, enumDyeColor.getDyeDamage()), "###", "###", " | ", Character.valueOf('#'), new ItemStack(Blocks.wool, 1, enumDyeColor.getMetadata()), Character.valueOf('|'), Items.stick);
            ++n2;
        }
        craftingManager.addRecipe(new RecipeDuplicatePattern());
        craftingManager.addRecipe(new RecipeAddPattern());
    }

    static class RecipeAddPattern
    implements IRecipe {
        @Override
        public int getRecipeSize() {
            return 10;
        }

        @Override
        public boolean matches(InventoryCrafting inventoryCrafting, World world) {
            boolean bl = false;
            int n = 0;
            while (n < inventoryCrafting.getSizeInventory()) {
                ItemStack itemStack = inventoryCrafting.getStackInSlot(n);
                if (itemStack != null && itemStack.getItem() == Items.banner) {
                    if (bl) {
                        return false;
                    }
                    if (TileEntityBanner.getPatterns(itemStack) >= 6) {
                        return false;
                    }
                    bl = true;
                }
                ++n;
            }
            if (!bl) {
                return false;
            }
            return this.func_179533_c(inventoryCrafting) != null;
        }

        @Override
        public ItemStack[] getRemainingItems(InventoryCrafting inventoryCrafting) {
            ItemStack[] itemStackArray = new ItemStack[inventoryCrafting.getSizeInventory()];
            int n = 0;
            while (n < itemStackArray.length) {
                ItemStack itemStack = inventoryCrafting.getStackInSlot(n);
                if (itemStack != null && itemStack.getItem().hasContainerItem()) {
                    itemStackArray[n] = new ItemStack(itemStack.getItem().getContainerItem());
                }
                ++n;
            }
            return itemStackArray;
        }

        private RecipeAddPattern() {
        }

        @Override
        public ItemStack getRecipeOutput() {
            return null;
        }

        @Override
        public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
            ItemStack itemStack = null;
            int n = 0;
            while (n < inventoryCrafting.getSizeInventory()) {
                ItemStack itemStack2 = inventoryCrafting.getStackInSlot(n);
                if (itemStack2 != null && itemStack2.getItem() == Items.banner) {
                    itemStack = itemStack2.copy();
                    itemStack.stackSize = 1;
                    break;
                }
                ++n;
            }
            TileEntityBanner.EnumBannerPattern enumBannerPattern = this.func_179533_c(inventoryCrafting);
            if (enumBannerPattern != null) {
                Object object;
                int n2 = 0;
                int n3 = 0;
                while (n3 < inventoryCrafting.getSizeInventory()) {
                    object = inventoryCrafting.getStackInSlot(n3);
                    if (object != null && ((ItemStack)object).getItem() == Items.dye) {
                        n2 = ((ItemStack)object).getMetadata();
                        break;
                    }
                    ++n3;
                }
                NBTTagCompound nBTTagCompound = itemStack.getSubCompound("BlockEntityTag", true);
                object = null;
                if (nBTTagCompound.hasKey("Patterns", 9)) {
                    object = nBTTagCompound.getTagList("Patterns", 10);
                } else {
                    object = new NBTTagList();
                    nBTTagCompound.setTag("Patterns", (NBTBase)object);
                }
                NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
                nBTTagCompound2.setString("Pattern", enumBannerPattern.getPatternID());
                nBTTagCompound2.setInteger("Color", n2);
                ((NBTTagList)object).appendTag(nBTTagCompound2);
            }
            return itemStack;
        }

        private TileEntityBanner.EnumBannerPattern func_179533_c(InventoryCrafting inventoryCrafting) {
            TileEntityBanner.EnumBannerPattern[] enumBannerPatternArray = TileEntityBanner.EnumBannerPattern.values();
            int n = enumBannerPatternArray.length;
            int n2 = 0;
            while (n2 < n) {
                TileEntityBanner.EnumBannerPattern enumBannerPattern = enumBannerPatternArray[n2];
                if (enumBannerPattern.hasValidCrafting()) {
                    int n3;
                    int n4;
                    int n5;
                    boolean bl = true;
                    if (enumBannerPattern.hasCraftingStack()) {
                        n5 = 0;
                        n4 = 0;
                        n3 = 0;
                        while (n3 < inventoryCrafting.getSizeInventory() && bl) {
                            ItemStack itemStack = inventoryCrafting.getStackInSlot(n3);
                            if (itemStack != null && itemStack.getItem() != Items.banner) {
                                if (itemStack.getItem() == Items.dye) {
                                    if (n4 != 0) {
                                        bl = false;
                                        break;
                                    }
                                    n4 = 1;
                                } else {
                                    if (n5 != 0 || !itemStack.isItemEqual(enumBannerPattern.getCraftingStack())) {
                                        bl = false;
                                        break;
                                    }
                                    n5 = 1;
                                }
                            }
                            ++n3;
                        }
                        if (n5 == 0) {
                            bl = false;
                        }
                    } else if (inventoryCrafting.getSizeInventory() == enumBannerPattern.getCraftingLayers().length * enumBannerPattern.getCraftingLayers()[0].length()) {
                        n5 = -1;
                        n4 = 0;
                        while (n4 < inventoryCrafting.getSizeInventory() && bl) {
                            n3 = n4 / 3;
                            int n6 = n4 % 3;
                            ItemStack itemStack = inventoryCrafting.getStackInSlot(n4);
                            if (itemStack != null && itemStack.getItem() != Items.banner) {
                                if (itemStack.getItem() != Items.dye) {
                                    bl = false;
                                    break;
                                }
                                if (n5 != -1 && n5 != itemStack.getMetadata()) {
                                    bl = false;
                                    break;
                                }
                                if (enumBannerPattern.getCraftingLayers()[n3].charAt(n6) == ' ') {
                                    bl = false;
                                    break;
                                }
                                n5 = itemStack.getMetadata();
                            } else if (enumBannerPattern.getCraftingLayers()[n3].charAt(n6) != ' ') {
                                bl = false;
                                break;
                            }
                            ++n4;
                        }
                    } else {
                        bl = false;
                    }
                    if (bl) {
                        return enumBannerPattern;
                    }
                }
                ++n2;
            }
            return null;
        }
    }

    static class RecipeDuplicatePattern
    implements IRecipe {
        @Override
        public ItemStack getRecipeOutput() {
            return null;
        }

        @Override
        public boolean matches(InventoryCrafting inventoryCrafting, World world) {
            ItemStack itemStack = null;
            ItemStack itemStack2 = null;
            int n = 0;
            while (n < inventoryCrafting.getSizeInventory()) {
                ItemStack itemStack3 = inventoryCrafting.getStackInSlot(n);
                if (itemStack3 != null) {
                    boolean bl;
                    if (itemStack3.getItem() != Items.banner) {
                        return false;
                    }
                    if (itemStack != null && itemStack2 != null) {
                        return false;
                    }
                    int n2 = TileEntityBanner.getBaseColor(itemStack3);
                    boolean bl2 = bl = TileEntityBanner.getPatterns(itemStack3) > 0;
                    if (itemStack != null) {
                        if (bl) {
                            return false;
                        }
                        if (n2 != TileEntityBanner.getBaseColor(itemStack)) {
                            return false;
                        }
                        itemStack2 = itemStack3;
                    } else if (itemStack2 != null) {
                        if (!bl) {
                            return false;
                        }
                        if (n2 != TileEntityBanner.getBaseColor(itemStack2)) {
                            return false;
                        }
                        itemStack = itemStack3;
                    } else if (bl) {
                        itemStack = itemStack3;
                    } else {
                        itemStack2 = itemStack3;
                    }
                }
                ++n;
            }
            return itemStack != null && itemStack2 != null;
        }

        @Override
        public int getRecipeSize() {
            return 2;
        }

        private RecipeDuplicatePattern() {
        }

        @Override
        public ItemStack[] getRemainingItems(InventoryCrafting inventoryCrafting) {
            ItemStack[] itemStackArray = new ItemStack[inventoryCrafting.getSizeInventory()];
            int n = 0;
            while (n < itemStackArray.length) {
                ItemStack itemStack = inventoryCrafting.getStackInSlot(n);
                if (itemStack != null) {
                    if (itemStack.getItem().hasContainerItem()) {
                        itemStackArray[n] = new ItemStack(itemStack.getItem().getContainerItem());
                    } else if (itemStack.hasTagCompound() && TileEntityBanner.getPatterns(itemStack) > 0) {
                        itemStackArray[n] = itemStack.copy();
                        itemStackArray[n].stackSize = 1;
                    }
                }
                ++n;
            }
            return itemStackArray;
        }

        @Override
        public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
            int n = 0;
            while (n < inventoryCrafting.getSizeInventory()) {
                ItemStack itemStack = inventoryCrafting.getStackInSlot(n);
                if (itemStack != null && TileEntityBanner.getPatterns(itemStack) > 0) {
                    ItemStack itemStack2 = itemStack.copy();
                    itemStack2.stackSize = 1;
                    return itemStack2;
                }
                ++n;
            }
            return null;
        }
    }
}

