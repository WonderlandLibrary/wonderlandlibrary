package net.minecraft.item.crafting;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockStoneSlabNew;
import net.minecraft.block.BlockWall;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeBookCloning;
import net.minecraft.item.crafting.RecipeFireworks;
import net.minecraft.item.crafting.RecipeRepairItem;
import net.minecraft.item.crafting.RecipesArmor;
import net.minecraft.item.crafting.RecipesArmorDyes;
import net.minecraft.item.crafting.RecipesBanners;
import net.minecraft.item.crafting.RecipesCrafting;
import net.minecraft.item.crafting.RecipesDyes;
import net.minecraft.item.crafting.RecipesFood;
import net.minecraft.item.crafting.RecipesIngots;
import net.minecraft.item.crafting.RecipesMapCloning;
import net.minecraft.item.crafting.RecipesMapExtending;
import net.minecraft.item.crafting.RecipesTools;
import net.minecraft.item.crafting.RecipesWeapons;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;

public class CraftingManager {
    private static final CraftingManager instance = new CraftingManager();
    private final List<IRecipe> recipes = Lists.newArrayList();

    public static CraftingManager getInstance() {
        return instance;
    }

    private CraftingManager() {
        new RecipesTools().addRecipes(this);
        new RecipesWeapons().addRecipes(this);
        new RecipesIngots().addRecipes(this);
        new RecipesFood().addRecipes(this);
        new RecipesCrafting().addRecipes(this);
        new RecipesArmor().addRecipes(this);
        new RecipesDyes().addRecipes(this);
        this.recipes.add((Object)new RecipesArmorDyes());
        this.recipes.add((Object)new RecipeBookCloning());
        this.recipes.add((Object)new RecipesMapCloning());
        this.recipes.add((Object)new RecipesMapExtending());
        this.recipes.add((Object)new RecipeFireworks());
        this.recipes.add((Object)new RecipeRepairItem());
        new RecipesBanners().addRecipes(this);
        this.addRecipe(new ItemStack(Items.paper, 3), "###", Character.valueOf((char)'#'), Items.reeds);
        this.addShapelessRecipe(new ItemStack(Items.book, 1), Items.paper, Items.paper, Items.paper, Items.leather);
        this.addShapelessRecipe(new ItemStack(Items.writable_book, 1), Items.book, new ItemStack(Items.dye, 1, EnumDyeColor.BLACK.getDyeDamage()), Items.feather);
        this.addRecipe(new ItemStack(Blocks.oak_fence, 3), "W#W", "W#W", Character.valueOf((char)'#'), Items.stick, Character.valueOf((char)'W'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.OAK.getMetadata()));
        this.addRecipe(new ItemStack(Blocks.birch_fence, 3), "W#W", "W#W", Character.valueOf((char)'#'), Items.stick, Character.valueOf((char)'W'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.BIRCH.getMetadata()));
        this.addRecipe(new ItemStack(Blocks.spruce_fence, 3), "W#W", "W#W", Character.valueOf((char)'#'), Items.stick, Character.valueOf((char)'W'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()));
        this.addRecipe(new ItemStack(Blocks.jungle_fence, 3), "W#W", "W#W", Character.valueOf((char)'#'), Items.stick, Character.valueOf((char)'W'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()));
        this.addRecipe(new ItemStack(Blocks.acacia_fence, 3), "W#W", "W#W", Character.valueOf((char)'#'), Items.stick, Character.valueOf((char)'W'), new ItemStack(Blocks.planks, 1, 4 + BlockPlanks.EnumType.ACACIA.getMetadata() - 4));
        this.addRecipe(new ItemStack(Blocks.dark_oak_fence, 3), "W#W", "W#W", Character.valueOf((char)'#'), Items.stick, Character.valueOf((char)'W'), new ItemStack(Blocks.planks, 1, 4 + BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4));
        this.addRecipe(new ItemStack(Blocks.cobblestone_wall, 6, BlockWall.EnumType.NORMAL.getMetadata()), "###", "###", Character.valueOf((char)'#'), Blocks.cobblestone);
        this.addRecipe(new ItemStack(Blocks.cobblestone_wall, 6, BlockWall.EnumType.MOSSY.getMetadata()), "###", "###", Character.valueOf((char)'#'), Blocks.mossy_cobblestone);
        this.addRecipe(new ItemStack(Blocks.nether_brick_fence, 6), "###", "###", Character.valueOf((char)'#'), Blocks.nether_brick);
        this.addRecipe(new ItemStack(Blocks.oak_fence_gate, 1), "#W#", "#W#", Character.valueOf((char)'#'), Items.stick, Character.valueOf((char)'W'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.OAK.getMetadata()));
        this.addRecipe(new ItemStack(Blocks.birch_fence_gate, 1), "#W#", "#W#", Character.valueOf((char)'#'), Items.stick, Character.valueOf((char)'W'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.BIRCH.getMetadata()));
        this.addRecipe(new ItemStack(Blocks.spruce_fence_gate, 1), "#W#", "#W#", Character.valueOf((char)'#'), Items.stick, Character.valueOf((char)'W'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()));
        this.addRecipe(new ItemStack(Blocks.jungle_fence_gate, 1), "#W#", "#W#", Character.valueOf((char)'#'), Items.stick, Character.valueOf((char)'W'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()));
        this.addRecipe(new ItemStack(Blocks.acacia_fence_gate, 1), "#W#", "#W#", Character.valueOf((char)'#'), Items.stick, Character.valueOf((char)'W'), new ItemStack(Blocks.planks, 1, 4 + BlockPlanks.EnumType.ACACIA.getMetadata() - 4));
        this.addRecipe(new ItemStack(Blocks.dark_oak_fence_gate, 1), "#W#", "#W#", Character.valueOf((char)'#'), Items.stick, Character.valueOf((char)'W'), new ItemStack(Blocks.planks, 1, 4 + BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4));
        this.addRecipe(new ItemStack(Blocks.jukebox, 1), "###", "#X#", "###", Character.valueOf((char)'#'), Blocks.planks, Character.valueOf((char)'X'), Items.diamond);
        this.addRecipe(new ItemStack(Items.lead, 2), "~~ ", "~O ", "  ~", Character.valueOf((char)'~'), Items.string, Character.valueOf((char)'O'), Items.slime_ball);
        this.addRecipe(new ItemStack(Blocks.noteblock, 1), "###", "#X#", "###", Character.valueOf((char)'#'), Blocks.planks, Character.valueOf((char)'X'), Items.redstone);
        this.addRecipe(new ItemStack(Blocks.bookshelf, 1), "###", "XXX", "###", Character.valueOf((char)'#'), Blocks.planks, Character.valueOf((char)'X'), Items.book);
        this.addRecipe(new ItemStack(Blocks.snow, 1), "##", "##", Character.valueOf((char)'#'), Items.snowball);
        this.addRecipe(new ItemStack(Blocks.snow_layer, 6), "###", Character.valueOf((char)'#'), Blocks.snow);
        this.addRecipe(new ItemStack(Blocks.clay, 1), "##", "##", Character.valueOf((char)'#'), Items.clay_ball);
        this.addRecipe(new ItemStack(Blocks.brick_block, 1), "##", "##", Character.valueOf((char)'#'), Items.brick);
        this.addRecipe(new ItemStack(Blocks.glowstone, 1), "##", "##", Character.valueOf((char)'#'), Items.glowstone_dust);
        this.addRecipe(new ItemStack(Blocks.quartz_block, 1), "##", "##", Character.valueOf((char)'#'), Items.quartz);
        this.addRecipe(new ItemStack(Blocks.wool, 1), "##", "##", Character.valueOf((char)'#'), Items.string);
        this.addRecipe(new ItemStack(Blocks.tnt, 1), "X#X", "#X#", "X#X", Character.valueOf((char)'X'), Items.gunpowder, Character.valueOf((char)'#'), Blocks.sand);
        this.addRecipe(new ItemStack((Block)Blocks.stone_slab, 6, BlockStoneSlab.EnumType.COBBLESTONE.getMetadata()), "###", Character.valueOf((char)'#'), Blocks.cobblestone);
        this.addRecipe(new ItemStack((Block)Blocks.stone_slab, 6, BlockStoneSlab.EnumType.STONE.getMetadata()), "###", Character.valueOf((char)'#'), new ItemStack(Blocks.stone, BlockStone.EnumType.STONE.getMetadata()));
        this.addRecipe(new ItemStack((Block)Blocks.stone_slab, 6, BlockStoneSlab.EnumType.SAND.getMetadata()), "###", Character.valueOf((char)'#'), Blocks.sandstone);
        this.addRecipe(new ItemStack((Block)Blocks.stone_slab, 6, BlockStoneSlab.EnumType.BRICK.getMetadata()), "###", Character.valueOf((char)'#'), Blocks.brick_block);
        this.addRecipe(new ItemStack((Block)Blocks.stone_slab, 6, BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), "###", Character.valueOf((char)'#'), Blocks.stonebrick);
        this.addRecipe(new ItemStack((Block)Blocks.stone_slab, 6, BlockStoneSlab.EnumType.NETHERBRICK.getMetadata()), "###", Character.valueOf((char)'#'), Blocks.nether_brick);
        this.addRecipe(new ItemStack((Block)Blocks.stone_slab, 6, BlockStoneSlab.EnumType.QUARTZ.getMetadata()), "###", Character.valueOf((char)'#'), Blocks.quartz_block);
        this.addRecipe(new ItemStack((Block)Blocks.stone_slab2, 6, BlockStoneSlabNew.EnumType.RED_SANDSTONE.getMetadata()), "###", Character.valueOf((char)'#'), Blocks.red_sandstone);
        this.addRecipe(new ItemStack((Block)Blocks.wooden_slab, 6, 0), "###", Character.valueOf((char)'#'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.OAK.getMetadata()));
        this.addRecipe(new ItemStack((Block)Blocks.wooden_slab, 6, BlockPlanks.EnumType.BIRCH.getMetadata()), "###", Character.valueOf((char)'#'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.BIRCH.getMetadata()));
        this.addRecipe(new ItemStack((Block)Blocks.wooden_slab, 6, BlockPlanks.EnumType.SPRUCE.getMetadata()), "###", Character.valueOf((char)'#'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()));
        this.addRecipe(new ItemStack((Block)Blocks.wooden_slab, 6, BlockPlanks.EnumType.JUNGLE.getMetadata()), "###", Character.valueOf((char)'#'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()));
        this.addRecipe(new ItemStack((Block)Blocks.wooden_slab, 6, 4 + BlockPlanks.EnumType.ACACIA.getMetadata() - 4), "###", Character.valueOf((char)'#'), new ItemStack(Blocks.planks, 1, 4 + BlockPlanks.EnumType.ACACIA.getMetadata() - 4));
        this.addRecipe(new ItemStack((Block)Blocks.wooden_slab, 6, 4 + BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4), "###", Character.valueOf((char)'#'), new ItemStack(Blocks.planks, 1, 4 + BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4));
        this.addRecipe(new ItemStack(Blocks.ladder, 3), "# #", "###", "# #", Character.valueOf((char)'#'), Items.stick);
        this.addRecipe(new ItemStack(Items.oak_door, 3), "##", "##", "##", Character.valueOf((char)'#'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.OAK.getMetadata()));
        this.addRecipe(new ItemStack(Items.spruce_door, 3), "##", "##", "##", Character.valueOf((char)'#'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()));
        this.addRecipe(new ItemStack(Items.birch_door, 3), "##", "##", "##", Character.valueOf((char)'#'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.BIRCH.getMetadata()));
        this.addRecipe(new ItemStack(Items.jungle_door, 3), "##", "##", "##", Character.valueOf((char)'#'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()));
        this.addRecipe(new ItemStack(Items.acacia_door, 3), "##", "##", "##", Character.valueOf((char)'#'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.ACACIA.getMetadata()));
        this.addRecipe(new ItemStack(Items.dark_oak_door, 3), "##", "##", "##", Character.valueOf((char)'#'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.DARK_OAK.getMetadata()));
        this.addRecipe(new ItemStack(Blocks.trapdoor, 2), "###", "###", Character.valueOf((char)'#'), Blocks.planks);
        this.addRecipe(new ItemStack(Items.iron_door, 3), "##", "##", "##", Character.valueOf((char)'#'), Items.iron_ingot);
        this.addRecipe(new ItemStack(Blocks.iron_trapdoor, 1), "##", "##", Character.valueOf((char)'#'), Items.iron_ingot);
        this.addRecipe(new ItemStack(Items.sign, 3), "###", "###", " X ", Character.valueOf((char)'#'), Blocks.planks, Character.valueOf((char)'X'), Items.stick);
        this.addRecipe(new ItemStack(Items.cake, 1), "AAA", "BEB", "CCC", Character.valueOf((char)'A'), Items.milk_bucket, Character.valueOf((char)'B'), Items.sugar, Character.valueOf((char)'C'), Items.wheat, Character.valueOf((char)'E'), Items.egg);
        this.addRecipe(new ItemStack(Items.sugar, 1), "#", Character.valueOf((char)'#'), Items.reeds);
        this.addRecipe(new ItemStack(Blocks.planks, 4, BlockPlanks.EnumType.OAK.getMetadata()), "#", Character.valueOf((char)'#'), new ItemStack(Blocks.log, 1, BlockPlanks.EnumType.OAK.getMetadata()));
        this.addRecipe(new ItemStack(Blocks.planks, 4, BlockPlanks.EnumType.SPRUCE.getMetadata()), "#", Character.valueOf((char)'#'), new ItemStack(Blocks.log, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()));
        this.addRecipe(new ItemStack(Blocks.planks, 4, BlockPlanks.EnumType.BIRCH.getMetadata()), "#", Character.valueOf((char)'#'), new ItemStack(Blocks.log, 1, BlockPlanks.EnumType.BIRCH.getMetadata()));
        this.addRecipe(new ItemStack(Blocks.planks, 4, BlockPlanks.EnumType.JUNGLE.getMetadata()), "#", Character.valueOf((char)'#'), new ItemStack(Blocks.log, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()));
        this.addRecipe(new ItemStack(Blocks.planks, 4, 4 + BlockPlanks.EnumType.ACACIA.getMetadata() - 4), "#", Character.valueOf((char)'#'), new ItemStack(Blocks.log2, 1, BlockPlanks.EnumType.ACACIA.getMetadata() - 4));
        this.addRecipe(new ItemStack(Blocks.planks, 4, 4 + BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4), "#", Character.valueOf((char)'#'), new ItemStack(Blocks.log2, 1, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4));
        this.addRecipe(new ItemStack(Items.stick, 4), "#", "#", Character.valueOf((char)'#'), Blocks.planks);
        this.addRecipe(new ItemStack(Blocks.torch, 4), "X", "#", Character.valueOf((char)'X'), Items.coal, Character.valueOf((char)'#'), Items.stick);
        this.addRecipe(new ItemStack(Blocks.torch, 4), "X", "#", Character.valueOf((char)'X'), new ItemStack(Items.coal, 1, 1), Character.valueOf((char)'#'), Items.stick);
        this.addRecipe(new ItemStack(Items.bowl, 4), "# #", " # ", Character.valueOf((char)'#'), Blocks.planks);
        this.addRecipe(new ItemStack(Items.glass_bottle, 3), "# #", " # ", Character.valueOf((char)'#'), Blocks.glass);
        this.addRecipe(new ItemStack(Blocks.rail, 16), "X X", "X#X", "X X", Character.valueOf((char)'X'), Items.iron_ingot, Character.valueOf((char)'#'), Items.stick);
        this.addRecipe(new ItemStack(Blocks.golden_rail, 6), "X X", "X#X", "XRX", Character.valueOf((char)'X'), Items.gold_ingot, Character.valueOf((char)'R'), Items.redstone, Character.valueOf((char)'#'), Items.stick);
        this.addRecipe(new ItemStack(Blocks.activator_rail, 6), "XSX", "X#X", "XSX", Character.valueOf((char)'X'), Items.iron_ingot, Character.valueOf((char)'#'), Blocks.redstone_torch, Character.valueOf((char)'S'), Items.stick);
        this.addRecipe(new ItemStack(Blocks.detector_rail, 6), "X X", "X#X", "XRX", Character.valueOf((char)'X'), Items.iron_ingot, Character.valueOf((char)'R'), Items.redstone, Character.valueOf((char)'#'), Blocks.stone_pressure_plate);
        this.addRecipe(new ItemStack(Items.minecart, 1), "# #", "###", Character.valueOf((char)'#'), Items.iron_ingot);
        this.addRecipe(new ItemStack(Items.cauldron, 1), "# #", "# #", "###", Character.valueOf((char)'#'), Items.iron_ingot);
        this.addRecipe(new ItemStack(Items.brewing_stand, 1), " B ", "###", Character.valueOf((char)'#'), Blocks.cobblestone, Character.valueOf((char)'B'), Items.blaze_rod);
        this.addRecipe(new ItemStack(Blocks.lit_pumpkin, 1), "A", "B", Character.valueOf((char)'A'), Blocks.pumpkin, Character.valueOf((char)'B'), Blocks.torch);
        this.addRecipe(new ItemStack(Items.chest_minecart, 1), "A", "B", Character.valueOf((char)'A'), Blocks.chest, Character.valueOf((char)'B'), Items.minecart);
        this.addRecipe(new ItemStack(Items.furnace_minecart, 1), "A", "B", Character.valueOf((char)'A'), Blocks.furnace, Character.valueOf((char)'B'), Items.minecart);
        this.addRecipe(new ItemStack(Items.tnt_minecart, 1), "A", "B", Character.valueOf((char)'A'), Blocks.tnt, Character.valueOf((char)'B'), Items.minecart);
        this.addRecipe(new ItemStack(Items.hopper_minecart, 1), "A", "B", Character.valueOf((char)'A'), Blocks.hopper, Character.valueOf((char)'B'), Items.minecart);
        this.addRecipe(new ItemStack(Items.boat, 1), "# #", "###", Character.valueOf((char)'#'), Blocks.planks);
        this.addRecipe(new ItemStack(Items.bucket, 1), "# #", " # ", Character.valueOf((char)'#'), Items.iron_ingot);
        this.addRecipe(new ItemStack(Items.flower_pot, 1), "# #", " # ", Character.valueOf((char)'#'), Items.brick);
        this.addShapelessRecipe(new ItemStack(Items.flint_and_steel, 1), new ItemStack(Items.iron_ingot, 1), new ItemStack(Items.flint, 1));
        this.addRecipe(new ItemStack(Items.bread, 1), "###", Character.valueOf((char)'#'), Items.wheat);
        this.addRecipe(new ItemStack(Blocks.oak_stairs, 4), "#  ", "## ", "###", Character.valueOf((char)'#'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.OAK.getMetadata()));
        this.addRecipe(new ItemStack(Blocks.birch_stairs, 4), "#  ", "## ", "###", Character.valueOf((char)'#'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.BIRCH.getMetadata()));
        this.addRecipe(new ItemStack(Blocks.spruce_stairs, 4), "#  ", "## ", "###", Character.valueOf((char)'#'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()));
        this.addRecipe(new ItemStack(Blocks.jungle_stairs, 4), "#  ", "## ", "###", Character.valueOf((char)'#'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()));
        this.addRecipe(new ItemStack(Blocks.acacia_stairs, 4), "#  ", "## ", "###", Character.valueOf((char)'#'), new ItemStack(Blocks.planks, 1, 4 + BlockPlanks.EnumType.ACACIA.getMetadata() - 4));
        this.addRecipe(new ItemStack(Blocks.dark_oak_stairs, 4), "#  ", "## ", "###", Character.valueOf((char)'#'), new ItemStack(Blocks.planks, 1, 4 + BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4));
        this.addRecipe(new ItemStack((Item)Items.fishing_rod, 1), "  #", " #X", "# X", Character.valueOf((char)'#'), Items.stick, Character.valueOf((char)'X'), Items.string);
        this.addRecipe(new ItemStack(Items.carrot_on_a_stick, 1), "# ", " X", Character.valueOf((char)'#'), Items.fishing_rod, Character.valueOf((char)'X'), Items.carrot);
        this.addRecipe(new ItemStack(Blocks.stone_stairs, 4), "#  ", "## ", "###", Character.valueOf((char)'#'), Blocks.cobblestone);
        this.addRecipe(new ItemStack(Blocks.brick_stairs, 4), "#  ", "## ", "###", Character.valueOf((char)'#'), Blocks.brick_block);
        this.addRecipe(new ItemStack(Blocks.stone_brick_stairs, 4), "#  ", "## ", "###", Character.valueOf((char)'#'), Blocks.stonebrick);
        this.addRecipe(new ItemStack(Blocks.nether_brick_stairs, 4), "#  ", "## ", "###", Character.valueOf((char)'#'), Blocks.nether_brick);
        this.addRecipe(new ItemStack(Blocks.sandstone_stairs, 4), "#  ", "## ", "###", Character.valueOf((char)'#'), Blocks.sandstone);
        this.addRecipe(new ItemStack(Blocks.red_sandstone_stairs, 4), "#  ", "## ", "###", Character.valueOf((char)'#'), Blocks.red_sandstone);
        this.addRecipe(new ItemStack(Blocks.quartz_stairs, 4), "#  ", "## ", "###", Character.valueOf((char)'#'), Blocks.quartz_block);
        this.addRecipe(new ItemStack(Items.painting, 1), "###", "#X#", "###", Character.valueOf((char)'#'), Items.stick, Character.valueOf((char)'X'), Blocks.wool);
        this.addRecipe(new ItemStack(Items.item_frame, 1), "###", "#X#", "###", Character.valueOf((char)'#'), Items.stick, Character.valueOf((char)'X'), Items.leather);
        this.addRecipe(new ItemStack(Items.golden_apple, 1, 0), "###", "#X#", "###", Character.valueOf((char)'#'), Items.gold_ingot, Character.valueOf((char)'X'), Items.apple);
        this.addRecipe(new ItemStack(Items.golden_apple, 1, 1), "###", "#X#", "###", Character.valueOf((char)'#'), Blocks.gold_block, Character.valueOf((char)'X'), Items.apple);
        this.addRecipe(new ItemStack(Items.golden_carrot, 1, 0), "###", "#X#", "###", Character.valueOf((char)'#'), Items.gold_nugget, Character.valueOf((char)'X'), Items.carrot);
        this.addRecipe(new ItemStack(Items.speckled_melon, 1), "###", "#X#", "###", Character.valueOf((char)'#'), Items.gold_nugget, Character.valueOf((char)'X'), Items.melon);
        this.addRecipe(new ItemStack(Blocks.lever, 1), "X", "#", Character.valueOf((char)'#'), Blocks.cobblestone, Character.valueOf((char)'X'), Items.stick);
        this.addRecipe(new ItemStack((Block)Blocks.tripwire_hook, 2), "I", "S", "#", Character.valueOf((char)'#'), Blocks.planks, Character.valueOf((char)'S'), Items.stick, Character.valueOf((char)'I'), Items.iron_ingot);
        this.addRecipe(new ItemStack(Blocks.redstone_torch, 1), "X", "#", Character.valueOf((char)'#'), Items.stick, Character.valueOf((char)'X'), Items.redstone);
        this.addRecipe(new ItemStack(Items.repeater, 1), "#X#", "III", Character.valueOf((char)'#'), Blocks.redstone_torch, Character.valueOf((char)'X'), Items.redstone, Character.valueOf((char)'I'), new ItemStack(Blocks.stone, 1, BlockStone.EnumType.STONE.getMetadata()));
        this.addRecipe(new ItemStack(Items.comparator, 1), " # ", "#X#", "III", Character.valueOf((char)'#'), Blocks.redstone_torch, Character.valueOf((char)'X'), Items.quartz, Character.valueOf((char)'I'), new ItemStack(Blocks.stone, 1, BlockStone.EnumType.STONE.getMetadata()));
        this.addRecipe(new ItemStack(Items.clock, 1), " # ", "#X#", " # ", Character.valueOf((char)'#'), Items.gold_ingot, Character.valueOf((char)'X'), Items.redstone);
        this.addRecipe(new ItemStack(Items.compass, 1), " # ", "#X#", " # ", Character.valueOf((char)'#'), Items.iron_ingot, Character.valueOf((char)'X'), Items.redstone);
        this.addRecipe(new ItemStack((Item)Items.map, 1), "###", "#X#", "###", Character.valueOf((char)'#'), Items.paper, Character.valueOf((char)'X'), Items.compass);
        this.addRecipe(new ItemStack(Blocks.stone_button, 1), "#", Character.valueOf((char)'#'), new ItemStack(Blocks.stone, 1, BlockStone.EnumType.STONE.getMetadata()));
        this.addRecipe(new ItemStack(Blocks.wooden_button, 1), "#", Character.valueOf((char)'#'), Blocks.planks);
        this.addRecipe(new ItemStack(Blocks.stone_pressure_plate, 1), "##", Character.valueOf((char)'#'), new ItemStack(Blocks.stone, 1, BlockStone.EnumType.STONE.getMetadata()));
        this.addRecipe(new ItemStack(Blocks.wooden_pressure_plate, 1), "##", Character.valueOf((char)'#'), Blocks.planks);
        this.addRecipe(new ItemStack(Blocks.heavy_weighted_pressure_plate, 1), "##", Character.valueOf((char)'#'), Items.iron_ingot);
        this.addRecipe(new ItemStack(Blocks.light_weighted_pressure_plate, 1), "##", Character.valueOf((char)'#'), Items.gold_ingot);
        this.addRecipe(new ItemStack(Blocks.dispenser, 1), "###", "#X#", "#R#", Character.valueOf((char)'#'), Blocks.cobblestone, Character.valueOf((char)'X'), Items.bow, Character.valueOf((char)'R'), Items.redstone);
        this.addRecipe(new ItemStack(Blocks.dropper, 1), "###", "# #", "#R#", Character.valueOf((char)'#'), Blocks.cobblestone, Character.valueOf((char)'R'), Items.redstone);
        this.addRecipe(new ItemStack((Block)Blocks.piston, 1), "TTT", "#X#", "#R#", Character.valueOf((char)'#'), Blocks.cobblestone, Character.valueOf((char)'X'), Items.iron_ingot, Character.valueOf((char)'R'), Items.redstone, Character.valueOf((char)'T'), Blocks.planks);
        this.addRecipe(new ItemStack((Block)Blocks.sticky_piston, 1), "S", "P", Character.valueOf((char)'S'), Items.slime_ball, Character.valueOf((char)'P'), Blocks.piston);
        this.addRecipe(new ItemStack(Items.bed, 1), "###", "XXX", Character.valueOf((char)'#'), Blocks.wool, Character.valueOf((char)'X'), Blocks.planks);
        this.addRecipe(new ItemStack(Blocks.enchanting_table, 1), " B ", "D#D", "###", Character.valueOf((char)'#'), Blocks.obsidian, Character.valueOf((char)'B'), Items.book, Character.valueOf((char)'D'), Items.diamond);
        this.addRecipe(new ItemStack(Blocks.anvil, 1), "III", " i ", "iii", Character.valueOf((char)'I'), Blocks.iron_block, Character.valueOf((char)'i'), Items.iron_ingot);
        this.addRecipe(new ItemStack(Items.leather), "##", "##", Character.valueOf((char)'#'), Items.rabbit_hide);
        this.addShapelessRecipe(new ItemStack(Items.ender_eye, 1), Items.ender_pearl, Items.blaze_powder);
        this.addShapelessRecipe(new ItemStack(Items.fire_charge, 3), Items.gunpowder, Items.blaze_powder, Items.coal);
        this.addShapelessRecipe(new ItemStack(Items.fire_charge, 3), Items.gunpowder, Items.blaze_powder, new ItemStack(Items.coal, 1, 1));
        this.addRecipe(new ItemStack((Block)Blocks.daylight_detector), "GGG", "QQQ", "WWW", Character.valueOf((char)'G'), Blocks.glass, Character.valueOf((char)'Q'), Items.quartz, Character.valueOf((char)'W'), Blocks.wooden_slab);
        this.addRecipe(new ItemStack((Block)Blocks.hopper), "I I", "ICI", " I ", Character.valueOf((char)'I'), Items.iron_ingot, Character.valueOf((char)'C'), Blocks.chest);
        this.addRecipe(new ItemStack((Item)Items.armor_stand, 1), "///", " / ", "/_/", Character.valueOf((char)'/'), Items.stick, Character.valueOf((char)'_'), new ItemStack((Block)Blocks.stone_slab, 1, BlockStoneSlab.EnumType.STONE.getMetadata()));
        Collections.sort(this.recipes, (Comparator)new /* Unavailable Anonymous Inner Class!! */);
    }

    public ShapedRecipes addRecipe(ItemStack stack, Object ... recipeComponents) {
        String s = "";
        int i = 0;
        int j = 0;
        int k = 0;
        if (recipeComponents[i] instanceof String[]) {
            String[] astring = (String[])recipeComponents[i++];
            for (int l = 0; l < astring.length; ++l) {
                String s2 = astring[l];
                ++k;
                j = s2.length();
                s = s + s2;
            }
        } else {
            while (recipeComponents[i] instanceof String) {
                String s1 = (String)recipeComponents[i++];
                ++k;
                j = s1.length();
                s = s + s1;
            }
        }
        HashMap map = Maps.newHashMap();
        while (i < recipeComponents.length) {
            Character character = (Character)recipeComponents[i];
            ItemStack itemstack = null;
            if (recipeComponents[i + 1] instanceof Item) {
                itemstack = new ItemStack((Item)recipeComponents[i + 1]);
            } else if (recipeComponents[i + 1] instanceof Block) {
                itemstack = new ItemStack((Block)recipeComponents[i + 1], 1, Short.MAX_VALUE);
            } else if (recipeComponents[i + 1] instanceof ItemStack) {
                itemstack = (ItemStack)recipeComponents[i + 1];
            }
            map.put((Object)character, (Object)itemstack);
            i += 2;
        }
        ItemStack[] aitemstack = new ItemStack[j * k];
        for (int i1 = 0; i1 < j * k; ++i1) {
            char c0 = s.charAt(i1);
            aitemstack[i1] = map.containsKey((Object)Character.valueOf((char)c0)) ? ((ItemStack)map.get((Object)Character.valueOf((char)c0))).copy() : null;
        }
        ShapedRecipes shapedrecipes = new ShapedRecipes(j, k, aitemstack, stack);
        this.recipes.add((Object)shapedrecipes);
        return shapedrecipes;
    }

    public void addShapelessRecipe(ItemStack stack, Object ... recipeComponents) {
        ArrayList list = Lists.newArrayList();
        for (Object object : recipeComponents) {
            if (object instanceof ItemStack) {
                list.add((Object)((ItemStack)object).copy());
                continue;
            }
            if (object instanceof Item) {
                list.add((Object)new ItemStack((Item)object));
                continue;
            }
            if (!(object instanceof Block)) {
                throw new IllegalArgumentException("Invalid shapeless recipe: unknown type " + object.getClass().getName() + "!");
            }
            list.add((Object)new ItemStack((Block)object));
        }
        this.recipes.add((Object)new ShapelessRecipes(stack, (List)list));
    }

    public void addRecipe(IRecipe recipe) {
        this.recipes.add((Object)recipe);
    }

    public ItemStack findMatchingRecipe(InventoryCrafting p_82787_1_, World worldIn) {
        for (IRecipe irecipe : this.recipes) {
            if (!irecipe.matches(p_82787_1_, worldIn)) continue;
            return irecipe.getCraftingResult(p_82787_1_);
        }
        return null;
    }

    public ItemStack[] func_180303_b(InventoryCrafting p_180303_1_, World worldIn) {
        for (IRecipe irecipe : this.recipes) {
            if (!irecipe.matches(p_180303_1_, worldIn)) continue;
            return irecipe.getRemainingItems(p_180303_1_);
        }
        ItemStack[] aitemstack = new ItemStack[p_180303_1_.getSizeInventory()];
        for (int i = 0; i < aitemstack.length; ++i) {
            aitemstack[i] = p_180303_1_.getStackInSlot(i);
        }
        return aitemstack;
    }

    public List<IRecipe> getRecipeList() {
        return this.recipes;
    }
}
