package net.minecraft.item;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class ItemSpade extends ItemTool {
   private static final Set EFFECTIVE_ON;

   static {
      EFFECTIVE_ON = Sets.newHashSet((Object[])(Blocks.clay, Blocks.dirt, Blocks.farmland, Blocks.grass, Blocks.gravel, Blocks.mycelium, Blocks.sand, Blocks.snow, Blocks.snow_layer, Blocks.soul_sand));
   }

   public ItemSpade(Item.ToolMaterial var1) {
      super(1.0F, var1, EFFECTIVE_ON);
   }

   public boolean canHarvestBlock(Block var1) {
      return var1 == Blocks.snow_layer ? true : var1 == Blocks.snow;
   }
}
