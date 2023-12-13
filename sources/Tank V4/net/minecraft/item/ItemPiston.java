package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemPiston extends ItemBlock {
   public ItemPiston(Block var1) {
      super(var1);
   }

   public int getMetadata(int var1) {
      return 7;
   }
}
