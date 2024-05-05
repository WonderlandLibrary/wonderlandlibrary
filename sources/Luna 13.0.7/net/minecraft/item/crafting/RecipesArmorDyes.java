package net.minecraft.item.crafting;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class RecipesArmorDyes
  implements IRecipe
{
  private static final String __OBFID = "CL_00000079";
  
  public RecipesArmorDyes() {}
  
  public boolean matches(InventoryCrafting p_77569_1_, World worldIn)
  {
    ItemStack var3 = null;
    ArrayList var4 = Lists.newArrayList();
    for (int var5 = 0; var5 < p_77569_1_.getSizeInventory(); var5++)
    {
      ItemStack var6 = p_77569_1_.getStackInSlot(var5);
      if (var6 != null) {
        if ((var6.getItem() instanceof ItemArmor))
        {
          ItemArmor var7 = (ItemArmor)var6.getItem();
          if ((var7.getArmorMaterial() != ItemArmor.ArmorMaterial.LEATHER) || (var3 != null)) {
            return false;
          }
          var3 = var6;
        }
        else
        {
          if (var6.getItem() != Items.dye) {
            return false;
          }
          var4.add(var6);
        }
      }
    }
    return (var3 != null) && (!var4.isEmpty());
  }
  
  public ItemStack getCraftingResult(InventoryCrafting p_77572_1_)
  {
    ItemStack var2 = null;
    int[] var3 = new int[3];
    int var4 = 0;
    int var5 = 0;
    ItemArmor var6 = null;
    for (int var7 = 0; var7 < p_77572_1_.getSizeInventory(); var7++)
    {
      ItemStack var8 = p_77572_1_.getStackInSlot(var7);
      if (var8 != null) {
        if ((var8.getItem() instanceof ItemArmor))
        {
          var6 = (ItemArmor)var8.getItem();
          if ((var6.getArmorMaterial() != ItemArmor.ArmorMaterial.LEATHER) || (var2 != null)) {
            return null;
          }
          var2 = var8.copy();
          var2.stackSize = 1;
          if (var6.hasColor(var8))
          {
            int var9 = var6.getColor(var2);
            float var10 = (var9 >> 16 & 0xFF) / 255.0F;
            float var11 = (var9 >> 8 & 0xFF) / 255.0F;
            float var12 = (var9 & 0xFF) / 255.0F;
            var4 = (int)(var4 + Math.max(var10, Math.max(var11, var12)) * 255.0F);
            var3[0] = ((int)(var3[0] + var10 * 255.0F));
            var3[1] = ((int)(var3[1] + var11 * 255.0F));
            var3[2] = ((int)(var3[2] + var12 * 255.0F));
            var5++;
          }
        }
        else
        {
          if (var8.getItem() != Items.dye) {
            return null;
          }
          float[] var14 = EntitySheep.func_175513_a(EnumDyeColor.func_176766_a(var8.getMetadata()));
          int var15 = (int)(var14[0] * 255.0F);
          int var16 = (int)(var14[1] * 255.0F);
          int var17 = (int)(var14[2] * 255.0F);
          var4 += Math.max(var15, Math.max(var16, var17));
          var3[0] += var15;
          var3[1] += var16;
          var3[2] += var17;
          var5++;
        }
      }
    }
    if (var6 == null) {
      return null;
    }
    var7 = var3[0] / var5;
    int var13 = var3[1] / var5;
    int var9 = var3[2] / var5;
    float var10 = var4 / var5;
    float var11 = Math.max(var7, Math.max(var13, var9));
    var7 = (int)(var7 * var10 / var11);
    var13 = (int)(var13 * var10 / var11);
    var9 = (int)(var9 * var10 / var11);
    int var17 = (var7 << 8) + var13;
    var17 = (var17 << 8) + var9;
    var6.func_82813_b(var2, var17);
    return var2;
  }
  
  public int getRecipeSize()
  {
    return 10;
  }
  
  public ItemStack getRecipeOutput()
  {
    return null;
  }
  
  public ItemStack[] func_179532_b(InventoryCrafting p_179532_1_)
  {
    ItemStack[] var2 = new ItemStack[p_179532_1_.getSizeInventory()];
    for (int var3 = 0; var3 < var2.length; var3++)
    {
      ItemStack var4 = p_179532_1_.getStackInSlot(var3);
      if ((var4 != null) && (var4.getItem().hasContainerItem())) {
        var2[var3] = new ItemStack(var4.getItem().getContainerItem());
      }
    }
    return var2;
  }
}
