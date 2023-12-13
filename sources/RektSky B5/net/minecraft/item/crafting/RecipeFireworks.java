/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item.crafting;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class RecipeFireworks
implements IRecipe {
    private ItemStack field_92102_a;

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        this.field_92102_a = null;
        int i2 = 0;
        int j2 = 0;
        int k2 = 0;
        int l2 = 0;
        int i1 = 0;
        int j1 = 0;
        for (int k1 = 0; k1 < inv.getSizeInventory(); ++k1) {
            ItemStack itemstack = inv.getStackInSlot(k1);
            if (itemstack == null) continue;
            if (itemstack.getItem() == Items.gunpowder) {
                ++j2;
                continue;
            }
            if (itemstack.getItem() == Items.firework_charge) {
                ++l2;
                continue;
            }
            if (itemstack.getItem() == Items.dye) {
                ++k2;
                continue;
            }
            if (itemstack.getItem() == Items.paper) {
                ++i2;
                continue;
            }
            if (itemstack.getItem() == Items.glowstone_dust) {
                ++i1;
                continue;
            }
            if (itemstack.getItem() == Items.diamond) {
                ++i1;
                continue;
            }
            if (itemstack.getItem() == Items.fire_charge) {
                ++j1;
                continue;
            }
            if (itemstack.getItem() == Items.feather) {
                ++j1;
                continue;
            }
            if (itemstack.getItem() == Items.gold_nugget) {
                ++j1;
                continue;
            }
            if (itemstack.getItem() != Items.skull) {
                return false;
            }
            ++j1;
        }
        i1 = i1 + k2 + j1;
        if (j2 <= 3 && i2 <= 1) {
            if (j2 >= 1 && i2 == 1 && i1 == 0) {
                this.field_92102_a = new ItemStack(Items.fireworks);
                if (l2 > 0) {
                    NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                    NBTTagCompound nbttagcompound3 = new NBTTagCompound();
                    NBTTagList nbttaglist = new NBTTagList();
                    for (int k22 = 0; k22 < inv.getSizeInventory(); ++k22) {
                        ItemStack itemstack3 = inv.getStackInSlot(k22);
                        if (itemstack3 == null || itemstack3.getItem() != Items.firework_charge || !itemstack3.hasTagCompound() || !itemstack3.getTagCompound().hasKey("Explosion", 10)) continue;
                        nbttaglist.appendTag(itemstack3.getTagCompound().getCompoundTag("Explosion"));
                    }
                    nbttagcompound3.setTag("Explosions", nbttaglist);
                    nbttagcompound3.setByte("Flight", (byte)j2);
                    nbttagcompound1.setTag("Fireworks", nbttagcompound3);
                    this.field_92102_a.setTagCompound(nbttagcompound1);
                }
                return true;
            }
            if (j2 == 1 && i2 == 0 && l2 == 0 && k2 > 0 && j1 <= 1) {
                this.field_92102_a = new ItemStack(Items.firework_charge);
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                int b0 = 0;
                ArrayList<Integer> list = Lists.newArrayList();
                for (int l1 = 0; l1 < inv.getSizeInventory(); ++l1) {
                    ItemStack itemstack2 = inv.getStackInSlot(l1);
                    if (itemstack2 == null) continue;
                    if (itemstack2.getItem() == Items.dye) {
                        list.add(ItemDye.dyeColors[itemstack2.getMetadata() & 0xF]);
                        continue;
                    }
                    if (itemstack2.getItem() == Items.glowstone_dust) {
                        nbttagcompound2.setBoolean("Flicker", true);
                        continue;
                    }
                    if (itemstack2.getItem() == Items.diamond) {
                        nbttagcompound2.setBoolean("Trail", true);
                        continue;
                    }
                    if (itemstack2.getItem() == Items.fire_charge) {
                        b0 = 1;
                        continue;
                    }
                    if (itemstack2.getItem() == Items.feather) {
                        b0 = 4;
                        continue;
                    }
                    if (itemstack2.getItem() == Items.gold_nugget) {
                        b0 = 2;
                        continue;
                    }
                    if (itemstack2.getItem() != Items.skull) continue;
                    b0 = 3;
                }
                int[] aint1 = new int[list.size()];
                for (int l22 = 0; l22 < aint1.length; ++l22) {
                    aint1[l22] = (Integer)list.get(l22);
                }
                nbttagcompound2.setIntArray("Colors", aint1);
                nbttagcompound2.setByte("Type", (byte)b0);
                nbttagcompound.setTag("Explosion", nbttagcompound2);
                this.field_92102_a.setTagCompound(nbttagcompound);
                return true;
            }
            if (j2 == 0 && i2 == 0 && l2 == 1 && k2 > 0 && k2 == i1) {
                ArrayList<Integer> list1 = Lists.newArrayList();
                for (int i22 = 0; i22 < inv.getSizeInventory(); ++i22) {
                    ItemStack itemstack1 = inv.getStackInSlot(i22);
                    if (itemstack1 == null) continue;
                    if (itemstack1.getItem() == Items.dye) {
                        list1.add(ItemDye.dyeColors[itemstack1.getMetadata() & 0xF]);
                        continue;
                    }
                    if (itemstack1.getItem() != Items.firework_charge) continue;
                    this.field_92102_a = itemstack1.copy();
                    this.field_92102_a.stackSize = 1;
                }
                int[] aint = new int[list1.size()];
                for (int j22 = 0; j22 < aint.length; ++j22) {
                    aint[j22] = (Integer)list1.get(j22);
                }
                if (this.field_92102_a != null && this.field_92102_a.hasTagCompound()) {
                    NBTTagCompound nbttagcompound4 = this.field_92102_a.getTagCompound().getCompoundTag("Explosion");
                    if (nbttagcompound4 == null) {
                        return false;
                    }
                    nbttagcompound4.setIntArray("FadeColors", aint);
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return this.field_92102_a.copy();
    }

    @Override
    public int getRecipeSize() {
        return 10;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.field_92102_a;
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
}

