package net.minecraft.item.crafting;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.BlockStone.EnumType;
import net.minecraft.block.BlockStoneSlab.EnumType;
import net.minecraft.block.BlockStoneSlabNew.EnumType;
import net.minecraft.block.BlockWall.EnumType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CraftingManager
{
  private static final CraftingManager instance = new CraftingManager();
  private final List recipes = Lists.newArrayList();
  private static final String __OBFID = "CL_00000090";
  
  public static CraftingManager getInstance()
  {
    return instance;
  }
  
  private CraftingManager()
  {
    new RecipesTools().addRecipes(this);
    new RecipesWeapons().addRecipes(this);
    new RecipesIngots().addRecipes(this);
    new RecipesFood().addRecipes(this);
    new RecipesCrafting().addRecipes(this);
    new RecipesArmor().addRecipes(this);
    new RecipesDyes().addRecipes(this);
    this.recipes.add(new RecipesArmorDyes());
    this.recipes.add(new RecipeBookCloning());
    this.recipes.add(new RecipesMapCloning());
    this.recipes.add(new RecipesMapExtending());
    this.recipes.add(new RecipeFireworks());
    this.recipes.add(new RecipeRepairItem());
    new RecipesBanners().func_179534_a(this);
    addRecipe(new ItemStack(Items.paper, 3), new Object[] { "###", Character.valueOf('#'), Items.reeds });
    addShapelessRecipe(new ItemStack(Items.book, 1), new Object[] { Items.paper, Items.paper, Items.paper, Items.leather });
    addShapelessRecipe(new ItemStack(Items.writable_book, 1), new Object[] { Items.book, new ItemStack(Items.dye, 1, EnumDyeColor.BLACK.getDyeColorDamage()), Items.feather });
    addRecipe(new ItemStack(Blocks.oak_fence, 3), new Object[] { "W#W", "W#W", Character.valueOf('#'), Items.stick, Character.valueOf('W'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.OAK.func_176839_a()) });
    addRecipe(new ItemStack(Blocks.birch_fence, 3), new Object[] { "W#W", "W#W", Character.valueOf('#'), Items.stick, Character.valueOf('W'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.BIRCH.func_176839_a()) });
    addRecipe(new ItemStack(Blocks.spruce_fence, 3), new Object[] { "W#W", "W#W", Character.valueOf('#'), Items.stick, Character.valueOf('W'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.SPRUCE.func_176839_a()) });
    addRecipe(new ItemStack(Blocks.jungle_fence, 3), new Object[] { "W#W", "W#W", Character.valueOf('#'), Items.stick, Character.valueOf('W'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.JUNGLE.func_176839_a()) });
    addRecipe(new ItemStack(Blocks.acacia_fence, 3), new Object[] { "W#W", "W#W", Character.valueOf('#'), Items.stick, Character.valueOf('W'), new ItemStack(Blocks.planks, 1, 4 + BlockPlanks.EnumType.ACACIA.func_176839_a() - 4) });
    addRecipe(new ItemStack(Blocks.dark_oak_fence, 3), new Object[] { "W#W", "W#W", Character.valueOf('#'), Items.stick, Character.valueOf('W'), new ItemStack(Blocks.planks, 1, 4 + BlockPlanks.EnumType.DARK_OAK.func_176839_a() - 4) });
    addRecipe(new ItemStack(Blocks.cobblestone_wall, 6, BlockWall.EnumType.NORMAL.func_176657_a()), new Object[] { "###", "###", Character.valueOf('#'), Blocks.cobblestone });
    addRecipe(new ItemStack(Blocks.cobblestone_wall, 6, BlockWall.EnumType.MOSSY.func_176657_a()), new Object[] { "###", "###", Character.valueOf('#'), Blocks.mossy_cobblestone });
    addRecipe(new ItemStack(Blocks.nether_brick_fence, 6), new Object[] { "###", "###", Character.valueOf('#'), Blocks.nether_brick });
    addRecipe(new ItemStack(Blocks.oak_fence_gate, 1), new Object[] { "#W#", "#W#", Character.valueOf('#'), Items.stick, Character.valueOf('W'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.OAK.func_176839_a()) });
    addRecipe(new ItemStack(Blocks.birch_fence_gate, 1), new Object[] { "#W#", "#W#", Character.valueOf('#'), Items.stick, Character.valueOf('W'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.BIRCH.func_176839_a()) });
    addRecipe(new ItemStack(Blocks.spruce_fence_gate, 1), new Object[] { "#W#", "#W#", Character.valueOf('#'), Items.stick, Character.valueOf('W'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.SPRUCE.func_176839_a()) });
    addRecipe(new ItemStack(Blocks.jungle_fence_gate, 1), new Object[] { "#W#", "#W#", Character.valueOf('#'), Items.stick, Character.valueOf('W'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.JUNGLE.func_176839_a()) });
    addRecipe(new ItemStack(Blocks.acacia_fence_gate, 1), new Object[] { "#W#", "#W#", Character.valueOf('#'), Items.stick, Character.valueOf('W'), new ItemStack(Blocks.planks, 1, 4 + BlockPlanks.EnumType.ACACIA.func_176839_a() - 4) });
    addRecipe(new ItemStack(Blocks.dark_oak_fence_gate, 1), new Object[] { "#W#", "#W#", Character.valueOf('#'), Items.stick, Character.valueOf('W'), new ItemStack(Blocks.planks, 1, 4 + BlockPlanks.EnumType.DARK_OAK.func_176839_a() - 4) });
    addRecipe(new ItemStack(Blocks.jukebox, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Blocks.planks, Character.valueOf('X'), Items.diamond });
    addRecipe(new ItemStack(Items.lead, 2), new Object[] { "~~ ", "~O ", "  ~", Character.valueOf('~'), Items.string, Character.valueOf('O'), Items.slime_ball });
    addRecipe(new ItemStack(Blocks.noteblock, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Blocks.planks, Character.valueOf('X'), Items.redstone });
    addRecipe(new ItemStack(Blocks.bookshelf, 1), new Object[] { "###", "XXX", "###", Character.valueOf('#'), Blocks.planks, Character.valueOf('X'), Items.book });
    addRecipe(new ItemStack(Blocks.snow, 1), new Object[] { "##", "##", Character.valueOf('#'), Items.snowball });
    addRecipe(new ItemStack(Blocks.snow_layer, 6), new Object[] { "###", Character.valueOf('#'), Blocks.snow });
    addRecipe(new ItemStack(Blocks.clay, 1), new Object[] { "##", "##", Character.valueOf('#'), Items.clay_ball });
    addRecipe(new ItemStack(Blocks.brick_block, 1), new Object[] { "##", "##", Character.valueOf('#'), Items.brick });
    addRecipe(new ItemStack(Blocks.glowstone, 1), new Object[] { "##", "##", Character.valueOf('#'), Items.glowstone_dust });
    addRecipe(new ItemStack(Blocks.quartz_block, 1), new Object[] { "##", "##", Character.valueOf('#'), Items.quartz });
    addRecipe(new ItemStack(Blocks.wool, 1), new Object[] { "##", "##", Character.valueOf('#'), Items.string });
    addRecipe(new ItemStack(Blocks.tnt, 1), new Object[] { "X#X", "#X#", "X#X", Character.valueOf('X'), Items.gunpowder, Character.valueOf('#'), Blocks.sand });
    addRecipe(new ItemStack(Blocks.stone_slab, 6, BlockStoneSlab.EnumType.COBBLESTONE.func_176624_a()), new Object[] { "###", Character.valueOf('#'), Blocks.cobblestone });
    addRecipe(new ItemStack(Blocks.stone_slab, 6, BlockStoneSlab.EnumType.STONE.func_176624_a()), new Object[] { "###", Character.valueOf('#'), new ItemStack(Blocks.stone, BlockStone.EnumType.STONE.getMetaFromState()) });
    addRecipe(new ItemStack(Blocks.stone_slab, 6, BlockStoneSlab.EnumType.SAND.func_176624_a()), new Object[] { "###", Character.valueOf('#'), Blocks.sandstone });
    addRecipe(new ItemStack(Blocks.stone_slab, 6, BlockStoneSlab.EnumType.BRICK.func_176624_a()), new Object[] { "###", Character.valueOf('#'), Blocks.brick_block });
    addRecipe(new ItemStack(Blocks.stone_slab, 6, BlockStoneSlab.EnumType.SMOOTHBRICK.func_176624_a()), new Object[] { "###", Character.valueOf('#'), Blocks.stonebrick });
    addRecipe(new ItemStack(Blocks.stone_slab, 6, BlockStoneSlab.EnumType.NETHERBRICK.func_176624_a()), new Object[] { "###", Character.valueOf('#'), Blocks.nether_brick });
    addRecipe(new ItemStack(Blocks.stone_slab, 6, BlockStoneSlab.EnumType.QUARTZ.func_176624_a()), new Object[] { "###", Character.valueOf('#'), Blocks.quartz_block });
    addRecipe(new ItemStack(Blocks.stone_slab2, 6, BlockStoneSlabNew.EnumType.RED_SANDSTONE.func_176915_a()), new Object[] { "###", Character.valueOf('#'), Blocks.red_sandstone });
    addRecipe(new ItemStack(Blocks.wooden_slab, 6, 0), new Object[] { "###", Character.valueOf('#'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.OAK.func_176839_a()) });
    addRecipe(new ItemStack(Blocks.wooden_slab, 6, BlockPlanks.EnumType.BIRCH.func_176839_a()), new Object[] { "###", Character.valueOf('#'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.BIRCH.func_176839_a()) });
    addRecipe(new ItemStack(Blocks.wooden_slab, 6, BlockPlanks.EnumType.SPRUCE.func_176839_a()), new Object[] { "###", Character.valueOf('#'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.SPRUCE.func_176839_a()) });
    addRecipe(new ItemStack(Blocks.wooden_slab, 6, BlockPlanks.EnumType.JUNGLE.func_176839_a()), new Object[] { "###", Character.valueOf('#'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.JUNGLE.func_176839_a()) });
    addRecipe(new ItemStack(Blocks.wooden_slab, 6, 4 + BlockPlanks.EnumType.ACACIA.func_176839_a() - 4), new Object[] { "###", Character.valueOf('#'), new ItemStack(Blocks.planks, 1, 4 + BlockPlanks.EnumType.ACACIA.func_176839_a() - 4) });
    addRecipe(new ItemStack(Blocks.wooden_slab, 6, 4 + BlockPlanks.EnumType.DARK_OAK.func_176839_a() - 4), new Object[] { "###", Character.valueOf('#'), new ItemStack(Blocks.planks, 1, 4 + BlockPlanks.EnumType.DARK_OAK.func_176839_a() - 4) });
    addRecipe(new ItemStack(Blocks.ladder, 3), new Object[] { "# #", "###", "# #", Character.valueOf('#'), Items.stick });
    addRecipe(new ItemStack(Items.oak_door, 3), new Object[] { "##", "##", "##", Character.valueOf('#'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.OAK.func_176839_a()) });
    addRecipe(new ItemStack(Items.spruce_door, 3), new Object[] { "##", "##", "##", Character.valueOf('#'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.SPRUCE.func_176839_a()) });
    addRecipe(new ItemStack(Items.birch_door, 3), new Object[] { "##", "##", "##", Character.valueOf('#'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.BIRCH.func_176839_a()) });
    addRecipe(new ItemStack(Items.jungle_door, 3), new Object[] { "##", "##", "##", Character.valueOf('#'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.JUNGLE.func_176839_a()) });
    addRecipe(new ItemStack(Items.acacia_door, 3), new Object[] { "##", "##", "##", Character.valueOf('#'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.ACACIA.func_176839_a()) });
    addRecipe(new ItemStack(Items.dark_oak_door, 3), new Object[] { "##", "##", "##", Character.valueOf('#'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.DARK_OAK.func_176839_a()) });
    addRecipe(new ItemStack(Blocks.trapdoor, 2), new Object[] { "###", "###", Character.valueOf('#'), Blocks.planks });
    addRecipe(new ItemStack(Items.iron_door, 3), new Object[] { "##", "##", "##", Character.valueOf('#'), Items.iron_ingot });
    addRecipe(new ItemStack(Blocks.iron_trapdoor, 1), new Object[] { "##", "##", Character.valueOf('#'), Items.iron_ingot });
    addRecipe(new ItemStack(Items.sign, 3), new Object[] { "###", "###", " X ", Character.valueOf('#'), Blocks.planks, Character.valueOf('X'), Items.stick });
    addRecipe(new ItemStack(Items.cake, 1), new Object[] { "AAA", "BEB", "CCC", Character.valueOf('A'), Items.milk_bucket, Character.valueOf('B'), Items.sugar, Character.valueOf('C'), Items.wheat, Character.valueOf('E'), Items.egg });
    addRecipe(new ItemStack(Items.sugar, 1), new Object[] { "#", Character.valueOf('#'), Items.reeds });
    addRecipe(new ItemStack(Blocks.planks, 4, BlockPlanks.EnumType.OAK.func_176839_a()), new Object[] { "#", Character.valueOf('#'), new ItemStack(Blocks.log, 1, BlockPlanks.EnumType.OAK.func_176839_a()) });
    addRecipe(new ItemStack(Blocks.planks, 4, BlockPlanks.EnumType.SPRUCE.func_176839_a()), new Object[] { "#", Character.valueOf('#'), new ItemStack(Blocks.log, 1, BlockPlanks.EnumType.SPRUCE.func_176839_a()) });
    addRecipe(new ItemStack(Blocks.planks, 4, BlockPlanks.EnumType.BIRCH.func_176839_a()), new Object[] { "#", Character.valueOf('#'), new ItemStack(Blocks.log, 1, BlockPlanks.EnumType.BIRCH.func_176839_a()) });
    addRecipe(new ItemStack(Blocks.planks, 4, BlockPlanks.EnumType.JUNGLE.func_176839_a()), new Object[] { "#", Character.valueOf('#'), new ItemStack(Blocks.log, 1, BlockPlanks.EnumType.JUNGLE.func_176839_a()) });
    addRecipe(new ItemStack(Blocks.planks, 4, 4 + BlockPlanks.EnumType.ACACIA.func_176839_a() - 4), new Object[] { "#", Character.valueOf('#'), new ItemStack(Blocks.log2, 1, BlockPlanks.EnumType.ACACIA.func_176839_a() - 4) });
    addRecipe(new ItemStack(Blocks.planks, 4, 4 + BlockPlanks.EnumType.DARK_OAK.func_176839_a() - 4), new Object[] { "#", Character.valueOf('#'), new ItemStack(Blocks.log2, 1, BlockPlanks.EnumType.DARK_OAK.func_176839_a() - 4) });
    addRecipe(new ItemStack(Items.stick, 4), new Object[] { "#", "#", Character.valueOf('#'), Blocks.planks });
    addRecipe(new ItemStack(Blocks.torch, 4), new Object[] { "X", "#", Character.valueOf('X'), Items.coal, Character.valueOf('#'), Items.stick });
    addRecipe(new ItemStack(Blocks.torch, 4), new Object[] { "X", "#", Character.valueOf('X'), new ItemStack(Items.coal, 1, 1), Character.valueOf('#'), Items.stick });
    addRecipe(new ItemStack(Items.bowl, 4), new Object[] { "# #", " # ", Character.valueOf('#'), Blocks.planks });
    addRecipe(new ItemStack(Items.glass_bottle, 3), new Object[] { "# #", " # ", Character.valueOf('#'), Blocks.glass });
    addRecipe(new ItemStack(Blocks.rail, 16), new Object[] { "X X", "X#X", "X X", Character.valueOf('X'), Items.iron_ingot, Character.valueOf('#'), Items.stick });
    addRecipe(new ItemStack(Blocks.golden_rail, 6), new Object[] { "X X", "X#X", "XRX", Character.valueOf('X'), Items.gold_ingot, Character.valueOf('R'), Items.redstone, Character.valueOf('#'), Items.stick });
    addRecipe(new ItemStack(Blocks.activator_rail, 6), new Object[] { "XSX", "X#X", "XSX", Character.valueOf('X'), Items.iron_ingot, Character.valueOf('#'), Blocks.redstone_torch, Character.valueOf('S'), Items.stick });
    addRecipe(new ItemStack(Blocks.detector_rail, 6), new Object[] { "X X", "X#X", "XRX", Character.valueOf('X'), Items.iron_ingot, Character.valueOf('R'), Items.redstone, Character.valueOf('#'), Blocks.stone_pressure_plate });
    addRecipe(new ItemStack(Items.minecart, 1), new Object[] { "# #", "###", Character.valueOf('#'), Items.iron_ingot });
    addRecipe(new ItemStack(Items.cauldron, 1), new Object[] { "# #", "# #", "###", Character.valueOf('#'), Items.iron_ingot });
    addRecipe(new ItemStack(Items.brewing_stand, 1), new Object[] { " B ", "###", Character.valueOf('#'), Blocks.cobblestone, Character.valueOf('B'), Items.blaze_rod });
    addRecipe(new ItemStack(Blocks.lit_pumpkin, 1), new Object[] { "A", "B", Character.valueOf('A'), Blocks.pumpkin, Character.valueOf('B'), Blocks.torch });
    addRecipe(new ItemStack(Items.chest_minecart, 1), new Object[] { "A", "B", Character.valueOf('A'), Blocks.chest, Character.valueOf('B'), Items.minecart });
    addRecipe(new ItemStack(Items.furnace_minecart, 1), new Object[] { "A", "B", Character.valueOf('A'), Blocks.furnace, Character.valueOf('B'), Items.minecart });
    addRecipe(new ItemStack(Items.tnt_minecart, 1), new Object[] { "A", "B", Character.valueOf('A'), Blocks.tnt, Character.valueOf('B'), Items.minecart });
    addRecipe(new ItemStack(Items.hopper_minecart, 1), new Object[] { "A", "B", Character.valueOf('A'), Blocks.hopper, Character.valueOf('B'), Items.minecart });
    addRecipe(new ItemStack(Items.boat, 1), new Object[] { "# #", "###", Character.valueOf('#'), Blocks.planks });
    addRecipe(new ItemStack(Items.bucket, 1), new Object[] { "# #", " # ", Character.valueOf('#'), Items.iron_ingot });
    addRecipe(new ItemStack(Items.flower_pot, 1), new Object[] { "# #", " # ", Character.valueOf('#'), Items.brick });
    addShapelessRecipe(new ItemStack(Items.flint_and_steel, 1), new Object[] { new ItemStack(Items.iron_ingot, 1), new ItemStack(Items.flint, 1) });
    addRecipe(new ItemStack(Items.bread, 1), new Object[] { "###", Character.valueOf('#'), Items.wheat });
    addRecipe(new ItemStack(Blocks.oak_stairs, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.OAK.func_176839_a()) });
    addRecipe(new ItemStack(Blocks.birch_stairs, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.BIRCH.func_176839_a()) });
    addRecipe(new ItemStack(Blocks.spruce_stairs, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.SPRUCE.func_176839_a()) });
    addRecipe(new ItemStack(Blocks.jungle_stairs, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.JUNGLE.func_176839_a()) });
    addRecipe(new ItemStack(Blocks.acacia_stairs, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), new ItemStack(Blocks.planks, 1, 4 + BlockPlanks.EnumType.ACACIA.func_176839_a() - 4) });
    addRecipe(new ItemStack(Blocks.dark_oak_stairs, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), new ItemStack(Blocks.planks, 1, 4 + BlockPlanks.EnumType.DARK_OAK.func_176839_a() - 4) });
    addRecipe(new ItemStack(Items.fishing_rod, 1), new Object[] { "  #", " #X", "# X", Character.valueOf('#'), Items.stick, Character.valueOf('X'), Items.string });
    addRecipe(new ItemStack(Items.carrot_on_a_stick, 1), new Object[] { "# ", " X", Character.valueOf('#'), Items.fishing_rod, Character.valueOf('X'), Items.carrot }).func_92100_c();
    addRecipe(new ItemStack(Blocks.stone_stairs, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Blocks.cobblestone });
    addRecipe(new ItemStack(Blocks.brick_stairs, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Blocks.brick_block });
    addRecipe(new ItemStack(Blocks.stone_brick_stairs, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Blocks.stonebrick });
    addRecipe(new ItemStack(Blocks.nether_brick_stairs, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Blocks.nether_brick });
    addRecipe(new ItemStack(Blocks.sandstone_stairs, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Blocks.sandstone });
    addRecipe(new ItemStack(Blocks.red_sandstone_stairs, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Blocks.red_sandstone });
    addRecipe(new ItemStack(Blocks.quartz_stairs, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Blocks.quartz_block });
    addRecipe(new ItemStack(Items.painting, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Items.stick, Character.valueOf('X'), Blocks.wool });
    addRecipe(new ItemStack(Items.item_frame, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Items.stick, Character.valueOf('X'), Items.leather });
    addRecipe(new ItemStack(Items.golden_apple, 1, 0), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Items.gold_ingot, Character.valueOf('X'), Items.apple });
    addRecipe(new ItemStack(Items.golden_apple, 1, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Blocks.gold_block, Character.valueOf('X'), Items.apple });
    addRecipe(new ItemStack(Items.golden_carrot, 1, 0), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Items.gold_nugget, Character.valueOf('X'), Items.carrot });
    addRecipe(new ItemStack(Items.speckled_melon, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Items.gold_nugget, Character.valueOf('X'), Items.melon });
    addRecipe(new ItemStack(Blocks.lever, 1), new Object[] { "X", "#", Character.valueOf('#'), Blocks.cobblestone, Character.valueOf('X'), Items.stick });
    addRecipe(new ItemStack(Blocks.tripwire_hook, 2), new Object[] { "I", "S", "#", Character.valueOf('#'), Blocks.planks, Character.valueOf('S'), Items.stick, Character.valueOf('I'), Items.iron_ingot });
    addRecipe(new ItemStack(Blocks.redstone_torch, 1), new Object[] { "X", "#", Character.valueOf('#'), Items.stick, Character.valueOf('X'), Items.redstone });
    addRecipe(new ItemStack(Items.repeater, 1), new Object[] { "#X#", "III", Character.valueOf('#'), Blocks.redstone_torch, Character.valueOf('X'), Items.redstone, Character.valueOf('I'), new ItemStack(Blocks.stone, 1, BlockStone.EnumType.STONE.getMetaFromState()) });
    addRecipe(new ItemStack(Items.comparator, 1), new Object[] { " # ", "#X#", "III", Character.valueOf('#'), Blocks.redstone_torch, Character.valueOf('X'), Items.quartz, Character.valueOf('I'), new ItemStack(Blocks.stone, 1, BlockStone.EnumType.STONE.getMetaFromState()) });
    addRecipe(new ItemStack(Items.clock, 1), new Object[] { " # ", "#X#", " # ", Character.valueOf('#'), Items.gold_ingot, Character.valueOf('X'), Items.redstone });
    addRecipe(new ItemStack(Items.compass, 1), new Object[] { " # ", "#X#", " # ", Character.valueOf('#'), Items.iron_ingot, Character.valueOf('X'), Items.redstone });
    addRecipe(new ItemStack(Items.map, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Items.paper, Character.valueOf('X'), Items.compass });
    addRecipe(new ItemStack(Blocks.stone_button, 1), new Object[] { "#", Character.valueOf('#'), new ItemStack(Blocks.stone, 1, BlockStone.EnumType.STONE.getMetaFromState()) });
    addRecipe(new ItemStack(Blocks.wooden_button, 1), new Object[] { "#", Character.valueOf('#'), Blocks.planks });
    addRecipe(new ItemStack(Blocks.stone_pressure_plate, 1), new Object[] { "##", Character.valueOf('#'), new ItemStack(Blocks.stone, 1, BlockStone.EnumType.STONE.getMetaFromState()) });
    addRecipe(new ItemStack(Blocks.wooden_pressure_plate, 1), new Object[] { "##", Character.valueOf('#'), Blocks.planks });
    addRecipe(new ItemStack(Blocks.heavy_weighted_pressure_plate, 1), new Object[] { "##", Character.valueOf('#'), Items.iron_ingot });
    addRecipe(new ItemStack(Blocks.light_weighted_pressure_plate, 1), new Object[] { "##", Character.valueOf('#'), Items.gold_ingot });
    addRecipe(new ItemStack(Blocks.dispenser, 1), new Object[] { "###", "#X#", "#R#", Character.valueOf('#'), Blocks.cobblestone, Character.valueOf('X'), Items.bow, Character.valueOf('R'), Items.redstone });
    addRecipe(new ItemStack(Blocks.dropper, 1), new Object[] { "###", "# #", "#R#", Character.valueOf('#'), Blocks.cobblestone, Character.valueOf('R'), Items.redstone });
    addRecipe(new ItemStack(Blocks.piston, 1), new Object[] { "TTT", "#X#", "#R#", Character.valueOf('#'), Blocks.cobblestone, Character.valueOf('X'), Items.iron_ingot, Character.valueOf('R'), Items.redstone, Character.valueOf('T'), Blocks.planks });
    addRecipe(new ItemStack(Blocks.sticky_piston, 1), new Object[] { "S", "P", Character.valueOf('S'), Items.slime_ball, Character.valueOf('P'), Blocks.piston });
    addRecipe(new ItemStack(Items.bed, 1), new Object[] { "###", "XXX", Character.valueOf('#'), Blocks.wool, Character.valueOf('X'), Blocks.planks });
    addRecipe(new ItemStack(Blocks.enchanting_table, 1), new Object[] { " B ", "D#D", "###", Character.valueOf('#'), Blocks.obsidian, Character.valueOf('B'), Items.book, Character.valueOf('D'), Items.diamond });
    addRecipe(new ItemStack(Blocks.anvil, 1), new Object[] { "III", " i ", "iii", Character.valueOf('I'), Blocks.iron_block, Character.valueOf('i'), Items.iron_ingot });
    addRecipe(new ItemStack(Items.leather), new Object[] { "##", "##", Character.valueOf('#'), Items.rabbit_hide });
    addShapelessRecipe(new ItemStack(Items.ender_eye, 1), new Object[] { Items.ender_pearl, Items.blaze_powder });
    addShapelessRecipe(new ItemStack(Items.fire_charge, 3), new Object[] { Items.gunpowder, Items.blaze_powder, Items.coal });
    addShapelessRecipe(new ItemStack(Items.fire_charge, 3), new Object[] { Items.gunpowder, Items.blaze_powder, new ItemStack(Items.coal, 1, 1) });
    addRecipe(new ItemStack(Blocks.daylight_detector), new Object[] { "GGG", "QQQ", "WWW", Character.valueOf('G'), Blocks.glass, Character.valueOf('Q'), Items.quartz, Character.valueOf('W'), Blocks.wooden_slab });
    addRecipe(new ItemStack(Blocks.hopper), new Object[] { "I I", "ICI", " I ", Character.valueOf('I'), Items.iron_ingot, Character.valueOf('C'), Blocks.chest });
    addRecipe(new ItemStack(Items.armor_stand, 1), new Object[] { "///", " / ", "/_/", Character.valueOf('/'), Items.stick, Character.valueOf('_'), new ItemStack(Blocks.stone_slab, 1, BlockStoneSlab.EnumType.STONE.func_176624_a()) });
    Collections.sort(this.recipes, new Comparator()
    {
      private static final String __OBFID = "CL_00000091";
      
      public int compare(IRecipe p_compare_1_, IRecipe p_compare_2_)
      {
        return p_compare_2_.getRecipeSize() > p_compare_1_.getRecipeSize() ? 1 : p_compare_2_.getRecipeSize() < p_compare_1_.getRecipeSize() ? -1 : ((p_compare_2_ instanceof ShapelessRecipes)) && ((p_compare_1_ instanceof ShapedRecipes)) ? -1 : ((p_compare_1_ instanceof ShapelessRecipes)) && ((p_compare_2_ instanceof ShapedRecipes)) ? 1 : 0;
      }
      
      public int compare(Object p_compare_1_, Object p_compare_2_)
      {
        return compare((IRecipe)p_compare_1_, (IRecipe)p_compare_2_);
      }
    });
  }
  
  public ShapedRecipes addRecipe(ItemStack p_92103_1_, Object... p_92103_2_)
  {
    String var3 = "";
    int var4 = 0;
    int var5 = 0;
    int var6 = 0;
    if ((p_92103_2_[var4] instanceof String[]))
    {
      String[] var7 = (String[])(String[])p_92103_2_[(var4++)];
      for (int var8 = 0; var8 < var7.length; var8++)
      {
        String var9 = var7[var8];
        var6++;
        var5 = var9.length();
        var3 = var3 + var9;
      }
    }
    else
    {
      while ((p_92103_2_[var4] instanceof String))
      {
        String var11 = (String)p_92103_2_[(var4++)];
        var6++;
        var5 = var11.length();
        var3 = var3 + var11;
      }
    }
    for (HashMap var12 = Maps.newHashMap(); var4 < p_92103_2_.length; var4 += 2)
    {
      Character var13 = (Character)p_92103_2_[var4];
      ItemStack var15 = null;
      if ((p_92103_2_[(var4 + 1)] instanceof Item)) {
        var15 = new ItemStack((Item)p_92103_2_[(var4 + 1)]);
      } else if ((p_92103_2_[(var4 + 1)] instanceof Block)) {
        var15 = new ItemStack((Block)p_92103_2_[(var4 + 1)], 1, 32767);
      } else if ((p_92103_2_[(var4 + 1)] instanceof ItemStack)) {
        var15 = (ItemStack)p_92103_2_[(var4 + 1)];
      }
      var12.put(var13, var15);
    }
    ItemStack[] var14 = new ItemStack[var5 * var6];
    for (int var16 = 0; var16 < var5 * var6; var16++)
    {
      char var10 = var3.charAt(var16);
      if (var12.containsKey(Character.valueOf(var10))) {
        var14[var16] = ((ItemStack)var12.get(Character.valueOf(var10))).copy();
      } else {
        var14[var16] = null;
      }
    }
    ShapedRecipes var17 = new ShapedRecipes(var5, var6, var14, p_92103_1_);
    this.recipes.add(var17);
    return var17;
  }
  
  public void addShapelessRecipe(ItemStack p_77596_1_, Object... p_77596_2_)
  {
    ArrayList var3 = Lists.newArrayList();
    Object[] var4 = p_77596_2_;
    int var5 = p_77596_2_.length;
    for (int var6 = 0; var6 < var5; var6++)
    {
      Object var7 = var4[var6];
      if ((var7 instanceof ItemStack))
      {
        var3.add(((ItemStack)var7).copy());
      }
      else if ((var7 instanceof Item))
      {
        var3.add(new ItemStack((Item)var7));
      }
      else
      {
        if (!(var7 instanceof Block)) {
          throw new IllegalArgumentException("Invalid shapeless recipe: unknown type " + var7.getClass().getName() + "!");
        }
        var3.add(new ItemStack((Block)var7));
      }
    }
    this.recipes.add(new ShapelessRecipes(p_77596_1_, var3));
  }
  
  public void func_180302_a(IRecipe p_180302_1_)
  {
    this.recipes.add(p_180302_1_);
  }
  
  public ItemStack findMatchingRecipe(InventoryCrafting p_82787_1_, World worldIn)
  {
    Iterator var3 = this.recipes.iterator();
    IRecipe var4;
    do
    {
      if (!var3.hasNext()) {
        return null;
      }
      var4 = (IRecipe)var3.next();
    } while (!var4.matches(p_82787_1_, worldIn));
    return var4.getCraftingResult(p_82787_1_);
  }
  
  public ItemStack[] func_180303_b(InventoryCrafting p_180303_1_, World worldIn)
  {
    Iterator var3 = this.recipes.iterator();
    while (var3.hasNext())
    {
      IRecipe var4 = (IRecipe)var3.next();
      if (var4.matches(p_180303_1_, worldIn)) {
        return var4.func_179532_b(p_180303_1_);
      }
    }
    ItemStack[] var5 = new ItemStack[p_180303_1_.getSizeInventory()];
    for (int var6 = 0; var6 < var5.length; var6++) {
      var5[var6] = p_180303_1_.getStackInSlot(var6);
    }
    return var5;
  }
  
  public List getRecipeList()
  {
    return this.recipes;
  }
}
