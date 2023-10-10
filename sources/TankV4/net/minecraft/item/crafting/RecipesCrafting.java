package net.minecraft.item.crafting;

import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockStoneSlabNew;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;

public class RecipesCrafting {
   public void addRecipes(CraftingManager var1) {
      var1.addRecipe(new ItemStack(Blocks.chest), "###", "# #", "###", '#', Blocks.planks);
      var1.addRecipe(new ItemStack(Blocks.trapped_chest), "#-", '#', Blocks.chest, '-', Blocks.tripwire_hook);
      var1.addRecipe(new ItemStack(Blocks.ender_chest), "###", "#E#", "###", '#', Blocks.obsidian, 'E', Items.ender_eye);
      var1.addRecipe(new ItemStack(Blocks.furnace), "###", "# #", "###", '#', Blocks.cobblestone);
      var1.addRecipe(new ItemStack(Blocks.crafting_table), "##", "##", '#', Blocks.planks);
      var1.addRecipe(new ItemStack(Blocks.sandstone), "##", "##", '#', new ItemStack(Blocks.sand, 1, BlockSand.EnumType.SAND.getMetadata()));
      var1.addRecipe(new ItemStack(Blocks.red_sandstone), "##", "##", '#', new ItemStack(Blocks.sand, 1, BlockSand.EnumType.RED_SAND.getMetadata()));
      var1.addRecipe(new ItemStack(Blocks.sandstone, 4, BlockSandStone.EnumType.SMOOTH.getMetadata()), "##", "##", '#', new ItemStack(Blocks.sandstone, 1, BlockSandStone.EnumType.DEFAULT.getMetadata()));
      var1.addRecipe(new ItemStack(Blocks.red_sandstone, 4, BlockRedSandstone.EnumType.SMOOTH.getMetadata()), "##", "##", '#', new ItemStack(Blocks.red_sandstone, 1, BlockRedSandstone.EnumType.DEFAULT.getMetadata()));
      var1.addRecipe(new ItemStack(Blocks.sandstone, 1, BlockSandStone.EnumType.CHISELED.getMetadata()), "#", "#", '#', new ItemStack(Blocks.stone_slab, 1, BlockStoneSlab.EnumType.SAND.getMetadata()));
      var1.addRecipe(new ItemStack(Blocks.red_sandstone, 1, BlockRedSandstone.EnumType.CHISELED.getMetadata()), "#", "#", '#', new ItemStack(Blocks.stone_slab2, 1, BlockStoneSlabNew.EnumType.RED_SANDSTONE.getMetadata()));
      var1.addRecipe(new ItemStack(Blocks.quartz_block, 1, BlockQuartz.EnumType.CHISELED.getMetadata()), "#", "#", '#', new ItemStack(Blocks.stone_slab, 1, BlockStoneSlab.EnumType.QUARTZ.getMetadata()));
      var1.addRecipe(new ItemStack(Blocks.quartz_block, 2, BlockQuartz.EnumType.LINES_Y.getMetadata()), "#", "#", '#', new ItemStack(Blocks.quartz_block, 1, BlockQuartz.EnumType.DEFAULT.getMetadata()));
      var1.addRecipe(new ItemStack(Blocks.stonebrick, 4), "##", "##", '#', new ItemStack(Blocks.stone, 1, BlockStone.EnumType.STONE.getMetadata()));
      var1.addRecipe(new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.CHISELED_META), "#", "#", '#', new ItemStack(Blocks.stone_slab, 1, BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()));
      var1.addShapelessRecipe(new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.MOSSY_META), Blocks.stonebrick, Blocks.vine);
      var1.addShapelessRecipe(new ItemStack(Blocks.mossy_cobblestone, 1), Blocks.cobblestone, Blocks.vine);
      var1.addRecipe(new ItemStack(Blocks.iron_bars, 16), "###", "###", '#', Items.iron_ingot);
      var1.addRecipe(new ItemStack(Blocks.glass_pane, 16), "###", "###", '#', Blocks.glass);
      var1.addRecipe(new ItemStack(Blocks.redstone_lamp, 1), " R ", "RGR", " R ", 'R', Items.redstone, 'G', Blocks.glowstone);
      var1.addRecipe(new ItemStack(Blocks.beacon, 1), "GGG", "GSG", "OOO", 'G', Blocks.glass, 'S', Items.nether_star, 'O', Blocks.obsidian);
      var1.addRecipe(new ItemStack(Blocks.nether_brick, 1), "NN", "NN", 'N', Items.netherbrick);
      var1.addRecipe(new ItemStack(Blocks.stone, 2, BlockStone.EnumType.DIORITE.getMetadata()), "CQ", "QC", 'C', Blocks.cobblestone, 'Q', Items.quartz);
      var1.addShapelessRecipe(new ItemStack(Blocks.stone, 1, BlockStone.EnumType.GRANITE.getMetadata()), new ItemStack(Blocks.stone, 1, BlockStone.EnumType.DIORITE.getMetadata()), Items.quartz);
      var1.addShapelessRecipe(new ItemStack(Blocks.stone, 2, BlockStone.EnumType.ANDESITE.getMetadata()), new ItemStack(Blocks.stone, 1, BlockStone.EnumType.DIORITE.getMetadata()), Blocks.cobblestone);
      var1.addRecipe(new ItemStack(Blocks.dirt, 4, BlockDirt.DirtType.COARSE_DIRT.getMetadata()), "DG", "GD", 'D', new ItemStack(Blocks.dirt, 1, BlockDirt.DirtType.DIRT.getMetadata()), 'G', Blocks.gravel);
      var1.addRecipe(new ItemStack(Blocks.stone, 4, BlockStone.EnumType.DIORITE_SMOOTH.getMetadata()), "SS", "SS", 'S', new ItemStack(Blocks.stone, 1, BlockStone.EnumType.DIORITE.getMetadata()));
      var1.addRecipe(new ItemStack(Blocks.stone, 4, BlockStone.EnumType.GRANITE_SMOOTH.getMetadata()), "SS", "SS", 'S', new ItemStack(Blocks.stone, 1, BlockStone.EnumType.GRANITE.getMetadata()));
      var1.addRecipe(new ItemStack(Blocks.stone, 4, BlockStone.EnumType.ANDESITE_SMOOTH.getMetadata()), "SS", "SS", 'S', new ItemStack(Blocks.stone, 1, BlockStone.EnumType.ANDESITE.getMetadata()));
      var1.addRecipe(new ItemStack(Blocks.prismarine, 1, BlockPrismarine.ROUGH_META), "SS", "SS", 'S', Items.prismarine_shard);
      var1.addRecipe(new ItemStack(Blocks.prismarine, 1, BlockPrismarine.BRICKS_META), "SSS", "SSS", "SSS", 'S', Items.prismarine_shard);
      var1.addRecipe(new ItemStack(Blocks.prismarine, 1, BlockPrismarine.DARK_META), "SSS", "SIS", "SSS", 'S', Items.prismarine_shard, 'I', new ItemStack(Items.dye, 1, EnumDyeColor.BLACK.getDyeDamage()));
      var1.addRecipe(new ItemStack(Blocks.sea_lantern, 1, 0), "SCS", "CCC", "SCS", 'S', Items.prismarine_shard, 'C', Items.prismarine_crystals);
   }
}
