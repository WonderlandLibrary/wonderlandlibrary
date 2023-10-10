package net.minecraft.item;

import com.google.common.base.Function;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.world.ColorizerGrass;

public class ItemDoublePlant extends ItemMultiTexture {
   public int getColorFromItemStack(ItemStack var1, int var2) {
      BlockDoublePlant.EnumPlantType var3 = BlockDoublePlant.EnumPlantType.byMetadata(var1.getMetadata());
      return var3 != BlockDoublePlant.EnumPlantType.GRASS && var3 != BlockDoublePlant.EnumPlantType.FERN ? super.getColorFromItemStack(var1, var2) : ColorizerGrass.getGrassColor(0.5D, 1.0D);
   }

   public ItemDoublePlant(Block var1, Block var2, Function var3) {
      super(var1, var2, var3);
   }
}
