// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item.crafting;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;

public class RecipeBookCloning implements IRecipe
{
    private static final String __OBFID = "CL_00000081";
    
    @Override
    public boolean matches(final InventoryCrafting p_77569_1_, final World worldIn) {
        int var3 = 0;
        ItemStack var4 = null;
        for (int var5 = 0; var5 < p_77569_1_.getSizeInventory(); ++var5) {
            final ItemStack var6 = p_77569_1_.getStackInSlot(var5);
            if (var6 != null) {
                if (var6.getItem() == Items.written_book) {
                    if (var4 != null) {
                        return false;
                    }
                    var4 = var6;
                }
                else {
                    if (var6.getItem() != Items.writable_book) {
                        return false;
                    }
                    ++var3;
                }
            }
        }
        return var4 != null && var3 > 0;
    }
    
    @Override
    public ItemStack getCraftingResult(final InventoryCrafting p_77572_1_) {
        int var2 = 0;
        ItemStack var3 = null;
        for (int var4 = 0; var4 < p_77572_1_.getSizeInventory(); ++var4) {
            final ItemStack var5 = p_77572_1_.getStackInSlot(var4);
            if (var5 != null) {
                if (var5.getItem() == Items.written_book) {
                    if (var3 != null) {
                        return null;
                    }
                    var3 = var5;
                }
                else {
                    if (var5.getItem() != Items.writable_book) {
                        return null;
                    }
                    ++var2;
                }
            }
        }
        if (var3 != null && var2 >= 1 && ItemEditableBook.func_179230_h(var3) < 2) {
            final ItemStack var6 = new ItemStack(Items.written_book, var2);
            var6.setTagCompound((NBTTagCompound)var3.getTagCompound().copy());
            var6.getTagCompound().setInteger("generation", ItemEditableBook.func_179230_h(var3) + 1);
            if (var3.hasDisplayName()) {
                var6.setStackDisplayName(var3.getDisplayName());
            }
            return var6;
        }
        return null;
    }
    
    @Override
    public int getRecipeSize() {
        return 9;
    }
    
    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }
    
    @Override
    public ItemStack[] func_179532_b(final InventoryCrafting p_179532_1_) {
        final ItemStack[] var2 = new ItemStack[p_179532_1_.getSizeInventory()];
        for (int var3 = 0; var3 < var2.length; ++var3) {
            final ItemStack var4 = p_179532_1_.getStackInSlot(var3);
            if (var4 != null && var4.getItem() instanceof ItemEditableBook) {
                var2[var3] = var4;
                break;
            }
        }
        return var2;
    }
}
