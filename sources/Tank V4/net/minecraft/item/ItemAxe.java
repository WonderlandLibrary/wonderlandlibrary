package net.minecraft.item;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

public class ItemAxe extends ItemTool {
   private static final Set EFFECTIVE_ON;

   public float getStrVsBlock(ItemStack var1, Block var2) {
      return var2.getMaterial() != Material.wood && var2.getMaterial() != Material.plants && var2.getMaterial() != Material.vine ? super.getStrVsBlock(var1, var2) : this.efficiencyOnProperMaterial;
   }

   static {
      EFFECTIVE_ON = Sets.newHashSet((Object[])(Blocks.planks, Blocks.bookshelf, Blocks.log, Blocks.log2, Blocks.chest, Blocks.pumpkin, Blocks.lit_pumpkin, Blocks.melon_block, Blocks.ladder));
   }

   protected ItemAxe(Item.ToolMaterial var1) {
      super(3.0F, var1, EFFECTIVE_ON);
   }
}
