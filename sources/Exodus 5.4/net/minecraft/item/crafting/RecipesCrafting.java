/*
 * Decompiled with CFR 0.152.
 */
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
import net.minecraft.item.crafting.CraftingManager;

public class RecipesCrafting {
    public void addRecipes(CraftingManager craftingManager) {
        craftingManager.addRecipe(new ItemStack(Blocks.chest), "###", "# #", "###", Character.valueOf('#'), Blocks.planks);
        craftingManager.addRecipe(new ItemStack(Blocks.trapped_chest), "#-", Character.valueOf('#'), Blocks.chest, Character.valueOf('-'), Blocks.tripwire_hook);
        craftingManager.addRecipe(new ItemStack(Blocks.ender_chest), "###", "#E#", "###", Character.valueOf('#'), Blocks.obsidian, Character.valueOf('E'), Items.ender_eye);
        craftingManager.addRecipe(new ItemStack(Blocks.furnace), "###", "# #", "###", Character.valueOf('#'), Blocks.cobblestone);
        craftingManager.addRecipe(new ItemStack(Blocks.crafting_table), "##", "##", Character.valueOf('#'), Blocks.planks);
        craftingManager.addRecipe(new ItemStack(Blocks.sandstone), "##", "##", Character.valueOf('#'), new ItemStack(Blocks.sand, 1, BlockSand.EnumType.SAND.getMetadata()));
        craftingManager.addRecipe(new ItemStack(Blocks.red_sandstone), "##", "##", Character.valueOf('#'), new ItemStack(Blocks.sand, 1, BlockSand.EnumType.RED_SAND.getMetadata()));
        craftingManager.addRecipe(new ItemStack(Blocks.sandstone, 4, BlockSandStone.EnumType.SMOOTH.getMetadata()), "##", "##", Character.valueOf('#'), new ItemStack(Blocks.sandstone, 1, BlockSandStone.EnumType.DEFAULT.getMetadata()));
        craftingManager.addRecipe(new ItemStack(Blocks.red_sandstone, 4, BlockRedSandstone.EnumType.SMOOTH.getMetadata()), "##", "##", Character.valueOf('#'), new ItemStack(Blocks.red_sandstone, 1, BlockRedSandstone.EnumType.DEFAULT.getMetadata()));
        craftingManager.addRecipe(new ItemStack(Blocks.sandstone, 1, BlockSandStone.EnumType.CHISELED.getMetadata()), "#", "#", Character.valueOf('#'), new ItemStack(Blocks.stone_slab, 1, BlockStoneSlab.EnumType.SAND.getMetadata()));
        craftingManager.addRecipe(new ItemStack(Blocks.red_sandstone, 1, BlockRedSandstone.EnumType.CHISELED.getMetadata()), "#", "#", Character.valueOf('#'), new ItemStack(Blocks.stone_slab2, 1, BlockStoneSlabNew.EnumType.RED_SANDSTONE.getMetadata()));
        craftingManager.addRecipe(new ItemStack(Blocks.quartz_block, 1, BlockQuartz.EnumType.CHISELED.getMetadata()), "#", "#", Character.valueOf('#'), new ItemStack(Blocks.stone_slab, 1, BlockStoneSlab.EnumType.QUARTZ.getMetadata()));
        craftingManager.addRecipe(new ItemStack(Blocks.quartz_block, 2, BlockQuartz.EnumType.LINES_Y.getMetadata()), "#", "#", Character.valueOf('#'), new ItemStack(Blocks.quartz_block, 1, BlockQuartz.EnumType.DEFAULT.getMetadata()));
        craftingManager.addRecipe(new ItemStack(Blocks.stonebrick, 4), "##", "##", Character.valueOf('#'), new ItemStack(Blocks.stone, 1, BlockStone.EnumType.STONE.getMetadata()));
        craftingManager.addRecipe(new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.CHISELED_META), "#", "#", Character.valueOf('#'), new ItemStack(Blocks.stone_slab, 1, BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()));
        craftingManager.addShapelessRecipe(new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.MOSSY_META), Blocks.stonebrick, Blocks.vine);
        craftingManager.addShapelessRecipe(new ItemStack(Blocks.mossy_cobblestone, 1), Blocks.cobblestone, Blocks.vine);
        craftingManager.addRecipe(new ItemStack(Blocks.iron_bars, 16), "###", "###", Character.valueOf('#'), Items.iron_ingot);
        craftingManager.addRecipe(new ItemStack(Blocks.glass_pane, 16), "###", "###", Character.valueOf('#'), Blocks.glass);
        craftingManager.addRecipe(new ItemStack(Blocks.redstone_lamp, 1), " R ", "RGR", " R ", Character.valueOf('R'), Items.redstone, Character.valueOf('G'), Blocks.glowstone);
        craftingManager.addRecipe(new ItemStack(Blocks.beacon, 1), "GGG", "GSG", "OOO", Character.valueOf('G'), Blocks.glass, Character.valueOf('S'), Items.nether_star, Character.valueOf('O'), Blocks.obsidian);
        craftingManager.addRecipe(new ItemStack(Blocks.nether_brick, 1), "NN", "NN", Character.valueOf('N'), Items.netherbrick);
        craftingManager.addRecipe(new ItemStack(Blocks.stone, 2, BlockStone.EnumType.DIORITE.getMetadata()), "CQ", "QC", Character.valueOf('C'), Blocks.cobblestone, Character.valueOf('Q'), Items.quartz);
        craftingManager.addShapelessRecipe(new ItemStack(Blocks.stone, 1, BlockStone.EnumType.GRANITE.getMetadata()), new ItemStack(Blocks.stone, 1, BlockStone.EnumType.DIORITE.getMetadata()), Items.quartz);
        craftingManager.addShapelessRecipe(new ItemStack(Blocks.stone, 2, BlockStone.EnumType.ANDESITE.getMetadata()), new ItemStack(Blocks.stone, 1, BlockStone.EnumType.DIORITE.getMetadata()), Blocks.cobblestone);
        craftingManager.addRecipe(new ItemStack(Blocks.dirt, 4, BlockDirt.DirtType.COARSE_DIRT.getMetadata()), "DG", "GD", Character.valueOf('D'), new ItemStack(Blocks.dirt, 1, BlockDirt.DirtType.DIRT.getMetadata()), Character.valueOf('G'), Blocks.gravel);
        craftingManager.addRecipe(new ItemStack(Blocks.stone, 4, BlockStone.EnumType.DIORITE_SMOOTH.getMetadata()), "SS", "SS", Character.valueOf('S'), new ItemStack(Blocks.stone, 1, BlockStone.EnumType.DIORITE.getMetadata()));
        craftingManager.addRecipe(new ItemStack(Blocks.stone, 4, BlockStone.EnumType.GRANITE_SMOOTH.getMetadata()), "SS", "SS", Character.valueOf('S'), new ItemStack(Blocks.stone, 1, BlockStone.EnumType.GRANITE.getMetadata()));
        craftingManager.addRecipe(new ItemStack(Blocks.stone, 4, BlockStone.EnumType.ANDESITE_SMOOTH.getMetadata()), "SS", "SS", Character.valueOf('S'), new ItemStack(Blocks.stone, 1, BlockStone.EnumType.ANDESITE.getMetadata()));
        craftingManager.addRecipe(new ItemStack(Blocks.prismarine, 1, BlockPrismarine.ROUGH_META), "SS", "SS", Character.valueOf('S'), Items.prismarine_shard);
        craftingManager.addRecipe(new ItemStack(Blocks.prismarine, 1, BlockPrismarine.BRICKS_META), "SSS", "SSS", "SSS", Character.valueOf('S'), Items.prismarine_shard);
        craftingManager.addRecipe(new ItemStack(Blocks.prismarine, 1, BlockPrismarine.DARK_META), "SSS", "SIS", "SSS", Character.valueOf('S'), Items.prismarine_shard, Character.valueOf('I'), new ItemStack(Items.dye, 1, EnumDyeColor.BLACK.getDyeDamage()));
        craftingManager.addRecipe(new ItemStack(Blocks.sea_lantern, 1, 0), "SCS", "CCC", "SCS", Character.valueOf('S'), Items.prismarine_shard, Character.valueOf('C'), Items.prismarine_crystals);
    }
}

