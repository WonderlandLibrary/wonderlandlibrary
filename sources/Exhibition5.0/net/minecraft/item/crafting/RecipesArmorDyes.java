// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item.crafting;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import com.google.common.collect.Lists;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;

public class RecipesArmorDyes implements IRecipe
{
    private static final String __OBFID = "CL_00000079";
    
    @Override
    public boolean matches(final InventoryCrafting p_77569_1_, final World worldIn) {
        ItemStack var3 = null;
        final ArrayList var4 = Lists.newArrayList();
        for (int var5 = 0; var5 < p_77569_1_.getSizeInventory(); ++var5) {
            final ItemStack var6 = p_77569_1_.getStackInSlot(var5);
            if (var6 != null) {
                if (var6.getItem() instanceof ItemArmor) {
                    final ItemArmor var7 = (ItemArmor)var6.getItem();
                    if (var7.getArmorMaterial() != ItemArmor.ArmorMaterial.LEATHER || var3 != null) {
                        return false;
                    }
                    var3 = var6;
                }
                else {
                    if (var6.getItem() != Items.dye) {
                        return false;
                    }
                    var4.add(var6);
                }
            }
        }
        return var3 != null && !var4.isEmpty();
    }
    
    @Override
    public ItemStack getCraftingResult(final InventoryCrafting p_77572_1_) {
        ItemStack var2 = null;
        final int[] var3 = new int[3];
        int var4 = 0;
        int var5 = 0;
        ItemArmor var6 = null;
        for (int var7 = 0; var7 < p_77572_1_.getSizeInventory(); ++var7) {
            final ItemStack var8 = p_77572_1_.getStackInSlot(var7);
            if (var8 != null) {
                if (var8.getItem() instanceof ItemArmor) {
                    var6 = (ItemArmor)var8.getItem();
                    if (var6.getArmorMaterial() != ItemArmor.ArmorMaterial.LEATHER || var2 != null) {
                        return null;
                    }
                    var2 = var8.copy();
                    var2.stackSize = 1;
                    if (var6.hasColor(var8)) {
                        final int var9 = var6.getColor(var2);
                        final float var10 = (var9 >> 16 & 0xFF) / 255.0f;
                        final float var11 = (var9 >> 8 & 0xFF) / 255.0f;
                        final float var12 = (var9 & 0xFF) / 255.0f;
                        var4 += (int)(Math.max(var10, Math.max(var11, var12)) * 255.0f);
                        var3[0] += (int)(var10 * 255.0f);
                        var3[1] += (int)(var11 * 255.0f);
                        var3[2] += (int)(var12 * 255.0f);
                        ++var5;
                    }
                }
                else {
                    if (var8.getItem() != Items.dye) {
                        return null;
                    }
                    final float[] var13 = EntitySheep.func_175513_a(EnumDyeColor.func_176766_a(var8.getMetadata()));
                    final int var14 = (int)(var13[0] * 255.0f);
                    final int var15 = (int)(var13[1] * 255.0f);
                    final int var16 = (int)(var13[2] * 255.0f);
                    var4 += Math.max(var14, Math.max(var15, var16));
                    final int[] array = var3;
                    final int n = 0;
                    array[n] += var14;
                    final int[] array2 = var3;
                    final int n2 = 1;
                    array2[n2] += var15;
                    final int[] array3 = var3;
                    final int n3 = 2;
                    array3[n3] += var16;
                    ++var5;
                }
            }
        }
        if (var6 == null) {
            return null;
        }
        int var7 = var3[0] / var5;
        int var17 = var3[1] / var5;
        int var9 = var3[2] / var5;
        final float var10 = var4 / var5;
        final float var11 = Math.max(var7, Math.max(var17, var9));
        var7 = (int)(var7 * var10 / var11);
        var17 = (int)(var17 * var10 / var11);
        var9 = (int)(var9 * var10 / var11);
        int var16 = (var7 << 8) + var17;
        var16 = (var16 << 8) + var9;
        var6.func_82813_b(var2, var16);
        return var2;
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
