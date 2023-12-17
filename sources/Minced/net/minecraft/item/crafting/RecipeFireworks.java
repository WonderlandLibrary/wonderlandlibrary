// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item.crafting;

import net.minecraft.util.NonNullList;
import java.util.List;
import net.minecraft.item.ItemDye;
import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.init.Items;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class RecipeFireworks implements IRecipe
{
    private ItemStack resultItem;
    
    public RecipeFireworks() {
        this.resultItem = ItemStack.EMPTY;
    }
    
    @Override
    public boolean matches(final InventoryCrafting inv, final World worldIn) {
        this.resultItem = ItemStack.EMPTY;
        int i = 0;
        int j = 0;
        int k = 0;
        int l = 0;
        int i2 = 0;
        int j2 = 0;
        for (int k2 = 0; k2 < inv.getSizeInventory(); ++k2) {
            final ItemStack itemstack = inv.getStackInSlot(k2);
            if (!itemstack.isEmpty()) {
                if (itemstack.getItem() == Items.GUNPOWDER) {
                    ++j;
                }
                else if (itemstack.getItem() == Items.FIREWORK_CHARGE) {
                    ++l;
                }
                else if (itemstack.getItem() == Items.DYE) {
                    ++k;
                }
                else if (itemstack.getItem() == Items.PAPER) {
                    ++i;
                }
                else if (itemstack.getItem() == Items.GLOWSTONE_DUST) {
                    ++i2;
                }
                else if (itemstack.getItem() == Items.DIAMOND) {
                    ++i2;
                }
                else if (itemstack.getItem() == Items.FIRE_CHARGE) {
                    ++j2;
                }
                else if (itemstack.getItem() == Items.FEATHER) {
                    ++j2;
                }
                else if (itemstack.getItem() == Items.GOLD_NUGGET) {
                    ++j2;
                }
                else {
                    if (itemstack.getItem() != Items.SKULL) {
                        return false;
                    }
                    ++j2;
                }
            }
        }
        i2 = i2 + k + j2;
        if (j > 3 || i > 1) {
            return false;
        }
        if (j >= 1 && i == 1 && i2 == 0) {
            this.resultItem = new ItemStack(Items.FIREWORKS, 3);
            final NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            if (l > 0) {
                final NBTTagList nbttaglist = new NBTTagList();
                for (int k3 = 0; k3 < inv.getSizeInventory(); ++k3) {
                    final ItemStack itemstack2 = inv.getStackInSlot(k3);
                    if (itemstack2.getItem() == Items.FIREWORK_CHARGE && itemstack2.hasTagCompound() && itemstack2.getTagCompound().hasKey("Explosion", 10)) {
                        nbttaglist.appendTag(itemstack2.getTagCompound().getCompoundTag("Explosion"));
                    }
                }
                nbttagcompound1.setTag("Explosions", nbttaglist);
            }
            nbttagcompound1.setByte("Flight", (byte)j);
            final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
            nbttagcompound2.setTag("Fireworks", nbttagcompound1);
            this.resultItem.setTagCompound(nbttagcompound2);
            return true;
        }
        if (j == 1 && i == 0 && l == 0 && k > 0 && j2 <= 1) {
            this.resultItem = new ItemStack(Items.FIREWORK_CHARGE);
            final NBTTagCompound nbttagcompound3 = new NBTTagCompound();
            final NBTTagCompound nbttagcompound4 = new NBTTagCompound();
            byte b0 = 0;
            final List<Integer> list = (List<Integer>)Lists.newArrayList();
            for (int l2 = 0; l2 < inv.getSizeInventory(); ++l2) {
                final ItemStack itemstack3 = inv.getStackInSlot(l2);
                if (!itemstack3.isEmpty()) {
                    if (itemstack3.getItem() == Items.DYE) {
                        list.add(ItemDye.DYE_COLORS[itemstack3.getMetadata() & 0xF]);
                    }
                    else if (itemstack3.getItem() == Items.GLOWSTONE_DUST) {
                        nbttagcompound4.setBoolean("Flicker", true);
                    }
                    else if (itemstack3.getItem() == Items.DIAMOND) {
                        nbttagcompound4.setBoolean("Trail", true);
                    }
                    else if (itemstack3.getItem() == Items.FIRE_CHARGE) {
                        b0 = 1;
                    }
                    else if (itemstack3.getItem() == Items.FEATHER) {
                        b0 = 4;
                    }
                    else if (itemstack3.getItem() == Items.GOLD_NUGGET) {
                        b0 = 2;
                    }
                    else if (itemstack3.getItem() == Items.SKULL) {
                        b0 = 3;
                    }
                }
            }
            final int[] aint1 = new int[list.size()];
            for (int l3 = 0; l3 < aint1.length; ++l3) {
                aint1[l3] = list.get(l3);
            }
            nbttagcompound4.setIntArray("Colors", aint1);
            nbttagcompound4.setByte("Type", b0);
            nbttagcompound3.setTag("Explosion", nbttagcompound4);
            this.resultItem.setTagCompound(nbttagcompound3);
            return true;
        }
        if (j != 0 || i != 0 || l != 1 || k <= 0 || k != i2) {
            return false;
        }
        final List<Integer> list2 = (List<Integer>)Lists.newArrayList();
        for (int i3 = 0; i3 < inv.getSizeInventory(); ++i3) {
            final ItemStack itemstack4 = inv.getStackInSlot(i3);
            if (!itemstack4.isEmpty()) {
                if (itemstack4.getItem() == Items.DYE) {
                    list2.add(ItemDye.DYE_COLORS[itemstack4.getMetadata() & 0xF]);
                }
                else if (itemstack4.getItem() == Items.FIREWORK_CHARGE) {
                    (this.resultItem = itemstack4.copy()).setCount(1);
                }
            }
        }
        final int[] aint2 = new int[list2.size()];
        for (int j3 = 0; j3 < aint2.length; ++j3) {
            aint2[j3] = list2.get(j3);
        }
        if (this.resultItem.isEmpty() || !this.resultItem.hasTagCompound()) {
            return false;
        }
        final NBTTagCompound nbttagcompound5 = this.resultItem.getTagCompound().getCompoundTag("Explosion");
        if (nbttagcompound5 == null) {
            return false;
        }
        nbttagcompound5.setIntArray("FadeColors", aint2);
        return true;
    }
    
    @Override
    public ItemStack getCraftingResult(final InventoryCrafting inv) {
        return this.resultItem.copy();
    }
    
    @Override
    public ItemStack getRecipeOutput() {
        return this.resultItem;
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
    
    @Override
    public boolean isDynamic() {
        return true;
    }
    
    @Override
    public boolean canFit(final int width, final int height) {
        return width * height >= 1;
    }
}
