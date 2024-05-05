package net.minecraft.item.crafting;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.world.World;

static class RecipesBanners.RecipeAddPattern
implements IRecipe {
    private RecipesBanners.RecipeAddPattern() {
    }

    public boolean matches(InventoryCrafting inv, World worldIn) {
        boolean flag = false;
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            if (itemstack == null || itemstack.getItem() != Items.banner) continue;
            if (flag) {
                return false;
            }
            if (TileEntityBanner.getPatterns((ItemStack)itemstack) >= 6) {
                return false;
            }
            flag = true;
        }
        if (!flag) {
            return false;
        }
        return this.func_179533_c(inv) != null;
    }

    public ItemStack getCraftingResult(InventoryCrafting inv) {
        TileEntityBanner.EnumBannerPattern tileentitybanner$enumbannerpattern;
        ItemStack itemstack = null;
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack itemstack1 = inv.getStackInSlot(i);
            if (itemstack1 == null || itemstack1.getItem() != Items.banner) continue;
            itemstack = itemstack1.copy();
            itemstack.stackSize = 1;
            break;
        }
        if ((tileentitybanner$enumbannerpattern = this.func_179533_c(inv)) != null) {
            int k = 0;
            for (int j = 0; j < inv.getSizeInventory(); ++j) {
                ItemStack itemstack2 = inv.getStackInSlot(j);
                if (itemstack2 == null || itemstack2.getItem() != Items.dye) continue;
                k = itemstack2.getMetadata();
                break;
            }
            NBTTagCompound nbttagcompound1 = itemstack.getSubCompound("BlockEntityTag", true);
            NBTTagList nbttaglist = null;
            if (nbttagcompound1.hasKey("Patterns", 9)) {
                nbttaglist = nbttagcompound1.getTagList("Patterns", 10);
            } else {
                nbttaglist = new NBTTagList();
                nbttagcompound1.setTag("Patterns", (NBTBase)nbttaglist);
            }
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setString("Pattern", tileentitybanner$enumbannerpattern.getPatternID());
            nbttagcompound.setInteger("Color", k);
            nbttaglist.appendTag((NBTBase)nbttagcompound);
        }
        return itemstack;
    }

    public int getRecipeSize() {
        return 10;
    }

    public ItemStack getRecipeOutput() {
        return null;
    }

    public ItemStack[] getRemainingItems(InventoryCrafting inv) {
        ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];
        for (int i = 0; i < aitemstack.length; ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            if (itemstack == null || !itemstack.getItem().hasContainerItem()) continue;
            aitemstack[i] = new ItemStack(itemstack.getItem().getContainerItem());
        }
        return aitemstack;
    }

    /*
     * Enabled aggressive block sorting
     */
    private TileEntityBanner.EnumBannerPattern func_179533_c(InventoryCrafting p_179533_1_) {
        TileEntityBanner.EnumBannerPattern[] enumBannerPatternArray = TileEntityBanner.EnumBannerPattern.values();
        int n = enumBannerPatternArray.length;
        int n2 = 0;
        while (true) {
            block12: {
                boolean flag;
                TileEntityBanner.EnumBannerPattern tileentitybanner$enumbannerpattern;
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
                                    tileentitybanner$enumbannerpattern = enumBannerPatternArray[n2];
                                    if (!tileentitybanner$enumbannerpattern.hasValidCrafting()) break block12;
                                    flag = true;
                                    if (!tileentitybanner$enumbannerpattern.hasCraftingStack()) break block13;
                                    flag1 = false;
                                    flag2 = false;
                                    break block14;
                                }
                                if (p_179533_1_.getSizeInventory() != tileentitybanner$enumbannerpattern.getCraftingLayers().length * tileentitybanner$enumbannerpattern.getCraftingLayers()[0].length()) break block15;
                                j = -1;
                                break block16;
                            }
                            flag = false;
                            break block17;
                        }
                        for (int i = 0; i < p_179533_1_.getSizeInventory() && flag; ++i) {
                            ItemStack itemstack = p_179533_1_.getStackInSlot(i);
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
                    for (int k = 0; k < p_179533_1_.getSizeInventory() && flag; ++k) {
                        int l = k / 3;
                        int i1 = k % 3;
                        ItemStack itemstack1 = p_179533_1_.getStackInSlot(k);
                        if (itemstack1 != null && itemstack1.getItem() != Items.banner) {
                            if (itemstack1.getItem() != Items.dye) {
                                flag = false;
                                break;
                            }
                            if (j != -1 && j != itemstack1.getMetadata()) {
                                flag = false;
                                break;
                            }
                            if (tileentitybanner$enumbannerpattern.getCraftingLayers()[l].charAt(i1) == ' ') {
                                flag = false;
                                break;
                            }
                            j = itemstack1.getMetadata();
                            continue;
                        }
                        if (tileentitybanner$enumbannerpattern.getCraftingLayers()[l].charAt(i1) == ' ') continue;
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    return tileentitybanner$enumbannerpattern;
                }
            }
            ++n2;
        }
    }
}
