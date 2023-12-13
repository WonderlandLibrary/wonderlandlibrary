package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemColored extends ItemBlock {
   private final Block coloredBlock;
   private String[] subtypeNames;

   public int getColorFromItemStack(ItemStack var1, int var2) {
      return this.coloredBlock.getRenderColor(this.coloredBlock.getStateFromMeta(var1.getMetadata()));
   }

   public String getUnlocalizedName(ItemStack var1) {
      if (this.subtypeNames == null) {
         return super.getUnlocalizedName(var1);
      } else {
         int var2 = var1.getMetadata();
         return var2 >= 0 && var2 < this.subtypeNames.length ? super.getUnlocalizedName(var1) + "." + this.subtypeNames[var2] : super.getUnlocalizedName(var1);
      }
   }

   public ItemColored(Block var1, boolean var2) {
      super(var1);
      this.coloredBlock = var1;
      if (var2) {
         this.setMaxDamage(0);
         this.setHasSubtypes(true);
      }

   }

   public int getMetadata(int var1) {
      return var1;
   }

   public ItemColored setSubtypeNames(String[] var1) {
      this.subtypeNames = var1;
      return this;
   }
}
