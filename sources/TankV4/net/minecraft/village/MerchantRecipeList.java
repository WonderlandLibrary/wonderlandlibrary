package net.minecraft.village;

import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;

public class MerchantRecipeList extends ArrayList {
   public void readRecipiesFromTags(NBTTagCompound var1) {
      NBTTagList var2 = var1.getTagList("Recipes", 10);

      for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
         NBTTagCompound var4 = var2.getCompoundTagAt(var3);
         this.add(new MerchantRecipe(var4));
      }

   }

   public MerchantRecipeList(NBTTagCompound var1) {
      this.readRecipiesFromTags(var1);
   }

   public MerchantRecipeList() {
   }

   public void writeToBuf(PacketBuffer var1) {
      var1.writeByte((byte)(this.size() & 255));

      for(int var2 = 0; var2 < this.size(); ++var2) {
         MerchantRecipe var3 = (MerchantRecipe)this.get(var2);
         var1.writeItemStackToBuffer(var3.getItemToBuy());
         var1.writeItemStackToBuffer(var3.getItemToSell());
         ItemStack var4 = var3.getSecondItemToBuy();
         var1.writeBoolean(var4 != null);
         if (var4 != null) {
            var1.writeItemStackToBuffer(var4);
         }

         var1.writeBoolean(var3.isRecipeDisabled());
         var1.writeInt(var3.getToolUses());
         var1.writeInt(var3.getMaxTradeUses());
      }

   }

   public NBTTagCompound getRecipiesAsTags() {
      NBTTagCompound var1 = new NBTTagCompound();
      NBTTagList var2 = new NBTTagList();

      for(int var3 = 0; var3 < this.size(); ++var3) {
         MerchantRecipe var4 = (MerchantRecipe)this.get(var3);
         var2.appendTag(var4.writeToTags());
      }

      var1.setTag("Recipes", var2);
      return var1;
   }

   public MerchantRecipe canRecipeBeUsed(ItemStack var1, ItemStack var2, int var3) {
      if (var3 > 0 && var3 < this.size()) {
         MerchantRecipe var6 = (MerchantRecipe)this.get(var3);
         return var6.getItemToBuy() != false && (var2 == null && !var6.hasSecondItemToBuy() || var6.hasSecondItemToBuy() && var6.getSecondItemToBuy() != false) && var1.stackSize >= var6.getItemToBuy().stackSize && (!var6.hasSecondItemToBuy() || var2.stackSize >= var6.getSecondItemToBuy().stackSize) ? var6 : null;
      } else {
         for(int var4 = 0; var4 < this.size(); ++var4) {
            MerchantRecipe var5 = (MerchantRecipe)this.get(var4);
            if (var5.getItemToBuy() != false && var1.stackSize >= var5.getItemToBuy().stackSize && (!var5.hasSecondItemToBuy() && var2 == null || var5.hasSecondItemToBuy() && var5.getSecondItemToBuy() != false && var2.stackSize >= var5.getSecondItemToBuy().stackSize)) {
               return var5;
            }
         }

         return null;
      }
   }

   public static MerchantRecipeList readFromBuf(PacketBuffer var0) throws IOException {
      MerchantRecipeList var1 = new MerchantRecipeList();
      int var2 = var0.readByte() & 255;

      for(int var3 = 0; var3 < var2; ++var3) {
         ItemStack var4 = var0.readItemStackFromBuffer();
         ItemStack var5 = var0.readItemStackFromBuffer();
         ItemStack var6 = null;
         if (var0.readBoolean()) {
            var6 = var0.readItemStackFromBuffer();
         }

         boolean var7 = var0.readBoolean();
         int var8 = var0.readInt();
         int var9 = var0.readInt();
         MerchantRecipe var10 = new MerchantRecipe(var4, var6, var5, var8, var9);
         if (var7) {
            var10.compensateToolUses();
         }

         var1.add(var10);
      }

      return var1;
   }
}
