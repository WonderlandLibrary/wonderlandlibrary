package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemAnvilBlock extends ItemMultiTexture {
   public ItemAnvilBlock(Block var1) {
      super(var1, var1, new String[]{"intact", "slightlyDamaged", "veryDamaged"});
   }

   public int getMetadata(int var1) {
      return var1 << 2;
   }
}
