package net.minecraft.item;

import com.google.common.base.Function;
import net.minecraft.block.Block;

public class ItemMultiTexture extends ItemBlock {
   protected final Function nameFunction;
   protected final Block theBlock;

   public String getUnlocalizedName(ItemStack var1) {
      return super.getUnlocalizedName() + "." + (String)this.nameFunction.apply(var1);
   }

   public ItemMultiTexture(Block var1, Block var2, Function var3) {
      super(var1);
      this.theBlock = var2;
      this.nameFunction = var3;
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   public int getMetadata(int var1) {
      return var1;
   }

   public ItemMultiTexture(Block var1, Block var2, String[] var3) {
      this(var1, var2, new Function(var3) {
         private final String[] val$namesByMeta;

         public String apply(ItemStack var1) {
            int var2 = var1.getMetadata();
            if (var2 < 0 || var2 >= this.val$namesByMeta.length) {
               var2 = 0;
            }

            return this.val$namesByMeta[var2];
         }

         {
            this.val$namesByMeta = var1;
         }

         public Object apply(Object var1) {
            return this.apply((ItemStack)var1);
         }
      });
   }
}
