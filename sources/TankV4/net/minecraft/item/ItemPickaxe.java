package net.minecraft.item;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

public class ItemPickaxe extends ItemTool {
   private static final Set EFFECTIVE_ON;

   public boolean canHarvestBlock(Block var1) {
      return var1 == Blocks.obsidian ? this.toolMaterial.getHarvestLevel() == 3 : (var1 != Blocks.diamond_block && var1 != Blocks.diamond_ore ? (var1 != Blocks.emerald_ore && var1 != Blocks.emerald_block ? (var1 != Blocks.gold_block && var1 != Blocks.gold_ore ? (var1 != Blocks.iron_block && var1 != Blocks.iron_ore ? (var1 != Blocks.lapis_block && var1 != Blocks.lapis_ore ? (var1 != Blocks.redstone_ore && var1 != Blocks.lit_redstone_ore ? (var1.getMaterial() == Material.rock ? true : (var1.getMaterial() == Material.iron ? true : var1.getMaterial() == Material.anvil)) : this.toolMaterial.getHarvestLevel() >= 2) : this.toolMaterial.getHarvestLevel() >= 1) : this.toolMaterial.getHarvestLevel() >= 1) : this.toolMaterial.getHarvestLevel() >= 2) : this.toolMaterial.getHarvestLevel() >= 2) : this.toolMaterial.getHarvestLevel() >= 2);
   }

   public float getStrVsBlock(ItemStack var1, Block var2) {
      return var2.getMaterial() != Material.iron && var2.getMaterial() != Material.anvil && var2.getMaterial() != Material.rock ? super.getStrVsBlock(var1, var2) : this.efficiencyOnProperMaterial;
   }

   protected ItemPickaxe(Item.ToolMaterial var1) {
      super(2.0F, var1, EFFECTIVE_ON);
   }

   static {
      EFFECTIVE_ON = Sets.newHashSet((Object[])(Blocks.activator_rail, Blocks.coal_ore, Blocks.cobblestone, Blocks.detector_rail, Blocks.diamond_block, Blocks.diamond_ore, Blocks.double_stone_slab, Blocks.golden_rail, Blocks.gold_block, Blocks.gold_ore, Blocks.ice, Blocks.iron_block, Blocks.iron_ore, Blocks.lapis_block, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.mossy_cobblestone, Blocks.netherrack, Blocks.packed_ice, Blocks.rail, Blocks.redstone_ore, Blocks.sandstone, Blocks.red_sandstone, Blocks.stone, Blocks.stone_slab));
   }
}
