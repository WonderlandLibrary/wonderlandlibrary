package net.minecraft.init;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBeacon;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDaylightDetector;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockMycelium;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockPistonMoving;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.BlockRedstoneComparator;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockReed;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockTripWireHook;
import net.minecraft.util.ResourceLocation;

public class Blocks {
  private static Block getRegisteredBlock(String p_180383_0_) {
    return (Block)Block.blockRegistry.getObject(new ResourceLocation(p_180383_0_));
  }
  
  static {
    if (!Bootstrap.isRegistered())
      throw new RuntimeException("Accessed Blocks before Bootstrap!"); 
  }
  
  public static final Block air = getRegisteredBlock("air");
  
  public static final Block stone = getRegisteredBlock("stone");
  
  public static final BlockGrass grass = (BlockGrass)getRegisteredBlock("grass");
  
  public static final Block dirt = getRegisteredBlock("dirt");
  
  public static final Block cobblestone = getRegisteredBlock("cobblestone");
  
  public static final Block planks = getRegisteredBlock("planks");
  
  public static final Block sapling = getRegisteredBlock("sapling");
  
  public static final Block bedrock = getRegisteredBlock("bedrock");
  
  public static final BlockDynamicLiquid flowing_water = (BlockDynamicLiquid)getRegisteredBlock("flowing_water");
  
  public static final BlockStaticLiquid water = (BlockStaticLiquid)getRegisteredBlock("water");
  
  public static final BlockDynamicLiquid flowing_lava = (BlockDynamicLiquid)getRegisteredBlock("flowing_lava");
  
  public static final BlockStaticLiquid lava = (BlockStaticLiquid)getRegisteredBlock("lava");
  
  public static final BlockSand sand = (BlockSand)getRegisteredBlock("sand");
  
  public static final Block gravel = getRegisteredBlock("gravel");
  
  public static final Block gold_ore = getRegisteredBlock("gold_ore");
  
  public static final Block iron_ore = getRegisteredBlock("iron_ore");
  
  public static final Block coal_ore = getRegisteredBlock("coal_ore");
  
  public static final Block log = getRegisteredBlock("log");
  
  public static final Block log2 = getRegisteredBlock("log2");
  
  public static final BlockLeaves leaves = (BlockLeaves)getRegisteredBlock("leaves");
  
  public static final BlockLeaves leaves2 = (BlockLeaves)getRegisteredBlock("leaves2");
  
  public static final Block sponge = getRegisteredBlock("sponge");
  
  public static final Block glass = getRegisteredBlock("glass");
  
  public static final Block lapis_ore = getRegisteredBlock("lapis_ore");
  
  public static final Block lapis_block = getRegisteredBlock("lapis_block");
  
  public static final Block dispenser = getRegisteredBlock("dispenser");
  
  public static final Block sandstone = getRegisteredBlock("sandstone");
  
  public static final Block noteblock = getRegisteredBlock("noteblock");
  
  public static final Block bed = getRegisteredBlock("bed");
  
  public static final Block golden_rail = getRegisteredBlock("golden_rail");
  
  public static final Block detector_rail = getRegisteredBlock("detector_rail");
  
  public static final BlockPistonBase sticky_piston = (BlockPistonBase)getRegisteredBlock("sticky_piston");
  
  public static final Block web = getRegisteredBlock("web");
  
  public static final BlockTallGrass tallgrass = (BlockTallGrass)getRegisteredBlock("tallgrass");
  
  public static final BlockDeadBush deadbush = (BlockDeadBush)getRegisteredBlock("deadbush");
  
  public static final BlockPistonBase piston = (BlockPistonBase)getRegisteredBlock("piston");
  
  public static final BlockPistonExtension piston_head = (BlockPistonExtension)getRegisteredBlock("piston_head");
  
  public static final Block wool = getRegisteredBlock("wool");
  
  public static final BlockPistonMoving piston_extension = (BlockPistonMoving)getRegisteredBlock("piston_extension");
  
  public static final BlockFlower yellow_flower = (BlockFlower)getRegisteredBlock("yellow_flower");
  
  public static final BlockFlower red_flower = (BlockFlower)getRegisteredBlock("red_flower");
  
  public static final BlockBush brown_mushroom = (BlockBush)getRegisteredBlock("brown_mushroom");
  
  public static final BlockBush red_mushroom = (BlockBush)getRegisteredBlock("red_mushroom");
  
  public static final Block gold_block = getRegisteredBlock("gold_block");
  
  public static final Block iron_block = getRegisteredBlock("iron_block");
  
  public static final BlockSlab double_stone_slab = (BlockSlab)getRegisteredBlock("double_stone_slab");
  
  public static final BlockSlab stone_slab = (BlockSlab)getRegisteredBlock("stone_slab");
  
  public static final Block brick_block = getRegisteredBlock("brick_block");
  
  public static final Block tnt = getRegisteredBlock("tnt");
  
  public static final Block bookshelf = getRegisteredBlock("bookshelf");
  
  public static final Block mossy_cobblestone = getRegisteredBlock("mossy_cobblestone");
  
  public static final Block obsidian = getRegisteredBlock("obsidian");
  
  public static final Block torch = getRegisteredBlock("torch");
  
  public static final BlockFire fire = (BlockFire)getRegisteredBlock("fire");
  
  public static final Block mob_spawner = getRegisteredBlock("mob_spawner");
  
  public static final Block oak_stairs = getRegisteredBlock("oak_stairs");
  
  public static final BlockChest chest = (BlockChest)getRegisteredBlock("chest");
  
  public static final BlockRedstoneWire redstone_wire = (BlockRedstoneWire)getRegisteredBlock("redstone_wire");
  
  public static final Block diamond_ore = getRegisteredBlock("diamond_ore");
  
  public static final Block diamond_block = getRegisteredBlock("diamond_block");
  
  public static final Block crafting_table = getRegisteredBlock("crafting_table");
  
  public static final Block wheat = getRegisteredBlock("wheat");
  
  public static final Block farmland = getRegisteredBlock("farmland");
  
  public static final Block furnace = getRegisteredBlock("furnace");
  
  public static final Block lit_furnace = getRegisteredBlock("lit_furnace");
  
  public static final Block standing_sign = getRegisteredBlock("standing_sign");
  
  public static final Block oak_door = getRegisteredBlock("wooden_door");
  
  public static final Block spruce_door = getRegisteredBlock("spruce_door");
  
  public static final Block birch_door = getRegisteredBlock("birch_door");
  
  public static final Block jungle_door = getRegisteredBlock("jungle_door");
  
  public static final Block acacia_door = getRegisteredBlock("acacia_door");
  
  public static final Block dark_oak_door = getRegisteredBlock("dark_oak_door");
  
  public static final Block ladder = getRegisteredBlock("ladder");
  
  public static final Block rail = getRegisteredBlock("rail");
  
  public static final Block stone_stairs = getRegisteredBlock("stone_stairs");
  
  public static final Block wall_sign = getRegisteredBlock("wall_sign");
  
  public static final Block lever = getRegisteredBlock("lever");
  
  public static final Block stone_pressure_plate = getRegisteredBlock("stone_pressure_plate");
  
  public static final Block iron_door = getRegisteredBlock("iron_door");
  
  public static final Block wooden_pressure_plate = getRegisteredBlock("wooden_pressure_plate");
  
  public static final Block redstone_ore = getRegisteredBlock("redstone_ore");
  
  public static final Block lit_redstone_ore = getRegisteredBlock("lit_redstone_ore");
  
  public static final Block unlit_redstone_torch = getRegisteredBlock("unlit_redstone_torch");
  
  public static final Block redstone_torch = getRegisteredBlock("redstone_torch");
  
  public static final Block stone_button = getRegisteredBlock("stone_button");
  
  public static final Block snow_layer = getRegisteredBlock("snow_layer");
  
  public static final Block ice = getRegisteredBlock("ice");
  
  public static final Block snow = getRegisteredBlock("snow");
  
  public static final BlockCactus cactus = (BlockCactus)getRegisteredBlock("cactus");
  
  public static final Block clay = getRegisteredBlock("clay");
  
  public static final BlockReed reeds = (BlockReed)getRegisteredBlock("reeds");
  
  public static final Block jukebox = getRegisteredBlock("jukebox");
  
  public static final Block oak_fence = getRegisteredBlock("fence");
  
  public static final Block spruce_fence = getRegisteredBlock("spruce_fence");
  
  public static final Block birch_fence = getRegisteredBlock("birch_fence");
  
  public static final Block jungle_fence = getRegisteredBlock("jungle_fence");
  
  public static final Block dark_oak_fence = getRegisteredBlock("dark_oak_fence");
  
  public static final Block acacia_fence = getRegisteredBlock("acacia_fence");
  
  public static final Block pumpkin = getRegisteredBlock("pumpkin");
  
  public static final Block netherrack = getRegisteredBlock("netherrack");
  
  public static final Block soul_sand = getRegisteredBlock("soul_sand");
  
  public static final Block glowstone = getRegisteredBlock("glowstone");
  
  public static final BlockPortal portal = (BlockPortal)getRegisteredBlock("portal");
  
  public static final Block lit_pumpkin = getRegisteredBlock("lit_pumpkin");
  
  public static final Block cake = getRegisteredBlock("cake");
  
  public static final BlockRedstoneRepeater unpowered_repeater = (BlockRedstoneRepeater)getRegisteredBlock("unpowered_repeater");
  
  public static final BlockRedstoneRepeater powered_repeater = (BlockRedstoneRepeater)getRegisteredBlock("powered_repeater");
  
  public static final Block trapdoor = getRegisteredBlock("trapdoor");
  
  public static final Block monster_egg = getRegisteredBlock("monster_egg");
  
  public static final Block stonebrick = getRegisteredBlock("stonebrick");
  
  public static final Block brown_mushroom_block = getRegisteredBlock("brown_mushroom_block");
  
  public static final Block red_mushroom_block = getRegisteredBlock("red_mushroom_block");
  
  public static final Block iron_bars = getRegisteredBlock("iron_bars");
  
  public static final Block glass_pane = getRegisteredBlock("glass_pane");
  
  public static final Block melon_block = getRegisteredBlock("melon_block");
  
  public static final Block pumpkin_stem = getRegisteredBlock("pumpkin_stem");
  
  public static final Block melon_stem = getRegisteredBlock("melon_stem");
  
  public static final Block vine = getRegisteredBlock("vine");
  
  public static final Block oak_fence_gate = getRegisteredBlock("fence_gate");
  
  public static final Block spruce_fence_gate = getRegisteredBlock("spruce_fence_gate");
  
  public static final Block birch_fence_gate = getRegisteredBlock("birch_fence_gate");
  
  public static final Block jungle_fence_gate = getRegisteredBlock("jungle_fence_gate");
  
  public static final Block dark_oak_fence_gate = getRegisteredBlock("dark_oak_fence_gate");
  
  public static final Block acacia_fence_gate = getRegisteredBlock("acacia_fence_gate");
  
  public static final Block brick_stairs = getRegisteredBlock("brick_stairs");
  
  public static final Block stone_brick_stairs = getRegisteredBlock("stone_brick_stairs");
  
  public static final BlockMycelium mycelium = (BlockMycelium)getRegisteredBlock("mycelium");
  
  public static final Block waterlily = getRegisteredBlock("waterlily");
  
  public static final Block nether_brick = getRegisteredBlock("nether_brick");
  
  public static final Block nether_brick_fence = getRegisteredBlock("nether_brick_fence");
  
  public static final Block nether_brick_stairs = getRegisteredBlock("nether_brick_stairs");
  
  public static final Block nether_wart = getRegisteredBlock("nether_wart");
  
  public static final Block enchanting_table = getRegisteredBlock("enchanting_table");
  
  public static final Block brewing_stand = getRegisteredBlock("brewing_stand");
  
  public static final BlockCauldron cauldron = (BlockCauldron)getRegisteredBlock("cauldron");
  
  public static final Block end_portal = getRegisteredBlock("end_portal");
  
  public static final Block end_portal_frame = getRegisteredBlock("end_portal_frame");
  
  public static final Block end_stone = getRegisteredBlock("end_stone");
  
  public static final Block dragon_egg = getRegisteredBlock("dragon_egg");
  
  public static final Block redstone_lamp = getRegisteredBlock("redstone_lamp");
  
  public static final Block lit_redstone_lamp = getRegisteredBlock("lit_redstone_lamp");
  
  public static final BlockSlab double_wooden_slab = (BlockSlab)getRegisteredBlock("double_wooden_slab");
  
  public static final BlockSlab wooden_slab = (BlockSlab)getRegisteredBlock("wooden_slab");
  
  public static final Block cocoa = getRegisteredBlock("cocoa");
  
  public static final Block sandstone_stairs = getRegisteredBlock("sandstone_stairs");
  
  public static final Block emerald_ore = getRegisteredBlock("emerald_ore");
  
  public static final Block ender_chest = getRegisteredBlock("ender_chest");
  
  public static final BlockTripWireHook tripwire_hook = (BlockTripWireHook)getRegisteredBlock("tripwire_hook");
  
  public static final Block tripwire = getRegisteredBlock("tripwire");
  
  public static final Block emerald_block = getRegisteredBlock("emerald_block");
  
  public static final Block spruce_stairs = getRegisteredBlock("spruce_stairs");
  
  public static final Block birch_stairs = getRegisteredBlock("birch_stairs");
  
  public static final Block jungle_stairs = getRegisteredBlock("jungle_stairs");
  
  public static final Block command_block = getRegisteredBlock("command_block");
  
  public static final BlockBeacon beacon = (BlockBeacon)getRegisteredBlock("beacon");
  
  public static final Block cobblestone_wall = getRegisteredBlock("cobblestone_wall");
  
  public static final Block flower_pot = getRegisteredBlock("flower_pot");
  
  public static final Block carrots = getRegisteredBlock("carrots");
  
  public static final Block potatoes = getRegisteredBlock("potatoes");
  
  public static final Block wooden_button = getRegisteredBlock("wooden_button");
  
  public static final BlockSkull skull = (BlockSkull)getRegisteredBlock("skull");
  
  public static final Block anvil = getRegisteredBlock("anvil");
  
  public static final Block trapped_chest = getRegisteredBlock("trapped_chest");
  
  public static final Block light_weighted_pressure_plate = getRegisteredBlock("light_weighted_pressure_plate");
  
  public static final Block heavy_weighted_pressure_plate = getRegisteredBlock("heavy_weighted_pressure_plate");
  
  public static final BlockRedstoneComparator unpowered_comparator = (BlockRedstoneComparator)getRegisteredBlock("unpowered_comparator");
  
  public static final BlockRedstoneComparator powered_comparator = (BlockRedstoneComparator)getRegisteredBlock("powered_comparator");
  
  public static final BlockDaylightDetector daylight_detector = (BlockDaylightDetector)getRegisteredBlock("daylight_detector");
  
  public static final BlockDaylightDetector daylight_detector_inverted = (BlockDaylightDetector)getRegisteredBlock("daylight_detector_inverted");
  
  public static final Block redstone_block = getRegisteredBlock("redstone_block");
  
  public static final Block quartz_ore = getRegisteredBlock("quartz_ore");
  
  public static final BlockHopper hopper = (BlockHopper)getRegisteredBlock("hopper");
  
  public static final Block quartz_block = getRegisteredBlock("quartz_block");
  
  public static final Block quartz_stairs = getRegisteredBlock("quartz_stairs");
  
  public static final Block activator_rail = getRegisteredBlock("activator_rail");
  
  public static final Block dropper = getRegisteredBlock("dropper");
  
  public static final Block stained_hardened_clay = getRegisteredBlock("stained_hardened_clay");
  
  public static final Block barrier = getRegisteredBlock("barrier");
  
  public static final Block iron_trapdoor = getRegisteredBlock("iron_trapdoor");
  
  public static final Block hay_block = getRegisteredBlock("hay_block");
  
  public static final Block carpet = getRegisteredBlock("carpet");
  
  public static final Block hardened_clay = getRegisteredBlock("hardened_clay");
  
  public static final Block coal_block = getRegisteredBlock("coal_block");
  
  public static final Block packed_ice = getRegisteredBlock("packed_ice");
  
  public static final Block acacia_stairs = getRegisteredBlock("acacia_stairs");
  
  public static final Block dark_oak_stairs = getRegisteredBlock("dark_oak_stairs");
  
  public static final Block slime_block = getRegisteredBlock("slime");
  
  public static final BlockDoublePlant double_plant = (BlockDoublePlant)getRegisteredBlock("double_plant");
  
  public static final BlockStainedGlass stained_glass = (BlockStainedGlass)getRegisteredBlock("stained_glass");
  
  public static final BlockStainedGlassPane stained_glass_pane = (BlockStainedGlassPane)getRegisteredBlock("stained_glass_pane");
  
  public static final Block prismarine = getRegisteredBlock("prismarine");
  
  public static final Block sea_lantern = getRegisteredBlock("sea_lantern");
  
  public static final Block standing_banner = getRegisteredBlock("standing_banner");
  
  public static final Block wall_banner = getRegisteredBlock("wall_banner");
  
  public static final Block red_sandstone = getRegisteredBlock("red_sandstone");
  
  public static final Block red_sandstone_stairs = getRegisteredBlock("red_sandstone_stairs");
  
  public static final BlockSlab double_stone_slab2 = (BlockSlab)getRegisteredBlock("double_stone_slab2");
  
  public static final BlockSlab stone_slab2 = (BlockSlab)getRegisteredBlock("stone_slab2");
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\init\Blocks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */