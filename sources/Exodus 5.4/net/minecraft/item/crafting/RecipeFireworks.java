/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.item.crafting;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class RecipeFireworks
implements IRecipe {
    private ItemStack field_92102_a;

    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        Object object;
        this.field_92102_a = null;
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        while (n7 < inventoryCrafting.getSizeInventory()) {
            object = inventoryCrafting.getStackInSlot(n7);
            if (object != null) {
                if (((ItemStack)object).getItem() == Items.gunpowder) {
                    ++n2;
                } else if (((ItemStack)object).getItem() == Items.firework_charge) {
                    ++n4;
                } else if (((ItemStack)object).getItem() == Items.dye) {
                    ++n3;
                } else if (((ItemStack)object).getItem() == Items.paper) {
                    ++n;
                } else if (((ItemStack)object).getItem() == Items.glowstone_dust) {
                    ++n5;
                } else if (((ItemStack)object).getItem() == Items.diamond) {
                    ++n5;
                } else if (((ItemStack)object).getItem() == Items.fire_charge) {
                    ++n6;
                } else if (((ItemStack)object).getItem() == Items.feather) {
                    ++n6;
                } else if (((ItemStack)object).getItem() == Items.gold_nugget) {
                    ++n6;
                } else {
                    if (((ItemStack)object).getItem() != Items.skull) {
                        return false;
                    }
                    ++n6;
                }
            }
            ++n7;
        }
        n5 = n5 + n3 + n6;
        if (n2 <= 3 && n <= 1) {
            if (n2 >= 1 && n == 1 && n5 == 0) {
                this.field_92102_a = new ItemStack(Items.fireworks);
                if (n4 > 0) {
                    NBTTagCompound nBTTagCompound = new NBTTagCompound();
                    object = new NBTTagCompound();
                    NBTTagList nBTTagList = new NBTTagList();
                    int n8 = 0;
                    while (n8 < inventoryCrafting.getSizeInventory()) {
                        ItemStack itemStack = inventoryCrafting.getStackInSlot(n8);
                        if (itemStack != null && itemStack.getItem() == Items.firework_charge && itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("Explosion", 10)) {
                            nBTTagList.appendTag(itemStack.getTagCompound().getCompoundTag("Explosion"));
                        }
                        ++n8;
                    }
                    ((NBTTagCompound)object).setTag("Explosions", nBTTagList);
                    ((NBTTagCompound)object).setByte("Flight", (byte)n2);
                    nBTTagCompound.setTag("Fireworks", (NBTBase)object);
                    this.field_92102_a.setTagCompound(nBTTagCompound);
                }
                return true;
            }
            if (n2 == 1 && n == 0 && n4 == 0 && n3 > 0 && n6 <= 1) {
                this.field_92102_a = new ItemStack(Items.firework_charge);
                NBTTagCompound nBTTagCompound = new NBTTagCompound();
                object = new NBTTagCompound();
                int n9 = 0;
                ArrayList arrayList = Lists.newArrayList();
                int n10 = 0;
                while (n10 < inventoryCrafting.getSizeInventory()) {
                    ItemStack itemStack = inventoryCrafting.getStackInSlot(n10);
                    if (itemStack != null) {
                        if (itemStack.getItem() == Items.dye) {
                            arrayList.add(ItemDye.dyeColors[itemStack.getMetadata() & 0xF]);
                        } else if (itemStack.getItem() == Items.glowstone_dust) {
                            ((NBTTagCompound)object).setBoolean("Flicker", true);
                        } else if (itemStack.getItem() == Items.diamond) {
                            ((NBTTagCompound)object).setBoolean("Trail", true);
                        } else if (itemStack.getItem() == Items.fire_charge) {
                            n9 = 1;
                        } else if (itemStack.getItem() == Items.feather) {
                            n9 = 4;
                        } else if (itemStack.getItem() == Items.gold_nugget) {
                            n9 = 2;
                        } else if (itemStack.getItem() == Items.skull) {
                            n9 = 3;
                        }
                    }
                    ++n10;
                }
                int[] nArray = new int[arrayList.size()];
                int n11 = 0;
                while (n11 < nArray.length) {
                    nArray[n11] = (Integer)arrayList.get(n11);
                    ++n11;
                }
                ((NBTTagCompound)object).setIntArray("Colors", nArray);
                ((NBTTagCompound)object).setByte("Type", (byte)n9);
                nBTTagCompound.setTag("Explosion", (NBTBase)object);
                this.field_92102_a.setTagCompound(nBTTagCompound);
                return true;
            }
            if (n2 == 0 && n == 0 && n4 == 1 && n3 > 0 && n3 == n5) {
                ArrayList arrayList = Lists.newArrayList();
                int n12 = 0;
                while (n12 < inventoryCrafting.getSizeInventory()) {
                    ItemStack itemStack = inventoryCrafting.getStackInSlot(n12);
                    if (itemStack != null) {
                        if (itemStack.getItem() == Items.dye) {
                            arrayList.add(ItemDye.dyeColors[itemStack.getMetadata() & 0xF]);
                        } else if (itemStack.getItem() == Items.firework_charge) {
                            this.field_92102_a = itemStack.copy();
                            this.field_92102_a.stackSize = 1;
                        }
                    }
                    ++n12;
                }
                int[] nArray = new int[arrayList.size()];
                int n13 = 0;
                while (n13 < nArray.length) {
                    nArray[n13] = (Integer)arrayList.get(n13);
                    ++n13;
                }
                if (this.field_92102_a != null && this.field_92102_a.hasTagCompound()) {
                    NBTTagCompound nBTTagCompound = this.field_92102_a.getTagCompound().getCompoundTag("Explosion");
                    if (nBTTagCompound == null) {
                        return false;
                    }
                    nBTTagCompound.setIntArray("FadeColors", nArray);
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
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

    @Override
    public ItemStack getRecipeOutput() {
        return this.field_92102_a;
    }

    @Override
    public int getRecipeSize() {
        return 10;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
        return this.field_92102_a.copy();
    }
}

