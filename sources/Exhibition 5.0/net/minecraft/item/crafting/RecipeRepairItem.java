// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item.crafting;

import net.minecraft.item.Item;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import com.google.common.collect.Lists;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;

public class RecipeRepairItem implements IRecipe
{
    private static final String __OBFID = "CL_00002156";
    
    @Override
    public boolean matches(final InventoryCrafting p_77569_1_, final World worldIn) {
        final ArrayList var3 = Lists.newArrayList();
        for (int var4 = 0; var4 < p_77569_1_.getSizeInventory(); ++var4) {
            final ItemStack var5 = p_77569_1_.getStackInSlot(var4);
            if (var5 != null) {
                var3.add(var5);
                if (var3.size() > 1) {
                    final ItemStack var6 = var3.get(0);
                    if (var5.getItem() != var6.getItem() || var6.stackSize != 1 || var5.stackSize != 1 || !var6.getItem().isDamageable()) {
                        return false;
                    }
                }
            }
        }
        return var3.size() == 2;
    }
    
    @Override
    public ItemStack getCraftingResult(final InventoryCrafting p_77572_1_) {
        final ArrayList var2 = Lists.newArrayList();
        for (int var3 = 0; var3 < p_77572_1_.getSizeInventory(); ++var3) {
            final ItemStack var4 = p_77572_1_.getStackInSlot(var3);
            if (var4 != null) {
                var2.add(var4);
                if (var2.size() > 1) {
                    final ItemStack var5 = var2.get(0);
                    if (var4.getItem() != var5.getItem() || var5.stackSize != 1 || var4.stackSize != 1 || !var5.getItem().isDamageable()) {
                        return null;
                    }
                }
            }
        }
        if (var2.size() == 2) {
            final ItemStack var6 = var2.get(0);
            final ItemStack var4 = var2.get(1);
            if (var6.getItem() == var4.getItem() && var6.stackSize == 1 && var4.stackSize == 1 && var6.getItem().isDamageable()) {
                final Item var7 = var6.getItem();
                final int var8 = var7.getMaxDamage() - var6.getItemDamage();
                final int var9 = var7.getMaxDamage() - var4.getItemDamage();
                final int var10 = var8 + var9 + var7.getMaxDamage() * 5 / 100;
                int var11 = var7.getMaxDamage() - var10;
                if (var11 < 0) {
                    var11 = 0;
                }
                return new ItemStack(var6.getItem(), 1, var11);
            }
        }
        return null;
    }
    
    @Override
    public int getRecipeSize() {
        return 4;
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
            if (var4 != null && var4.getItem().hasContainerItem()) {
                var2[var3] = new ItemStack(var4.getItem().getContainerItem());
            }
        }
        return var2;
    }
}
