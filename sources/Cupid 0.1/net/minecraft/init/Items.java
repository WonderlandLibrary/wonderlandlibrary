package net.minecraft.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmorStand;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEmptyMap;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemShears;
import net.minecraft.util.ResourceLocation;

public class Items {
  private static Item getRegisteredItem(String name) {
    return (Item)Item.itemRegistry.getObject(new ResourceLocation(name));
  }
  
  static {
    if (!Bootstrap.isRegistered())
      throw new RuntimeException("Accessed Items before Bootstrap!"); 
  }
  
  public static final Item iron_shovel = getRegisteredItem("iron_shovel");
  
  public static final Item iron_pickaxe = getRegisteredItem("iron_pickaxe");
  
  public static final Item iron_axe = getRegisteredItem("iron_axe");
  
  public static final Item flint_and_steel = getRegisteredItem("flint_and_steel");
  
  public static final Item apple = getRegisteredItem("apple");
  
  public static final ItemBow bow = (ItemBow)getRegisteredItem("bow");
  
  public static final Item arrow = getRegisteredItem("arrow");
  
  public static final Item coal = getRegisteredItem("coal");
  
  public static final Item diamond = getRegisteredItem("diamond");
  
  public static final Item iron_ingot = getRegisteredItem("iron_ingot");
  
  public static final Item gold_ingot = getRegisteredItem("gold_ingot");
  
  public static final Item iron_sword = getRegisteredItem("iron_sword");
  
  public static final Item wooden_sword = getRegisteredItem("wooden_sword");
  
  public static final Item wooden_shovel = getRegisteredItem("wooden_shovel");
  
  public static final Item wooden_pickaxe = getRegisteredItem("wooden_pickaxe");
  
  public static final Item wooden_axe = getRegisteredItem("wooden_axe");
  
  public static final Item stone_sword = getRegisteredItem("stone_sword");
  
  public static final Item stone_shovel = getRegisteredItem("stone_shovel");
  
  public static final Item stone_pickaxe = getRegisteredItem("stone_pickaxe");
  
  public static final Item stone_axe = getRegisteredItem("stone_axe");
  
  public static final Item diamond_sword = getRegisteredItem("diamond_sword");
  
  public static final Item diamond_shovel = getRegisteredItem("diamond_shovel");
  
  public static final Item diamond_pickaxe = getRegisteredItem("diamond_pickaxe");
  
  public static final Item diamond_axe = getRegisteredItem("diamond_axe");
  
  public static final Item stick = getRegisteredItem("stick");
  
  public static final Item bowl = getRegisteredItem("bowl");
  
  public static final Item mushroom_stew = getRegisteredItem("mushroom_stew");
  
  public static final Item golden_sword = getRegisteredItem("golden_sword");
  
  public static final Item golden_shovel = getRegisteredItem("golden_shovel");
  
  public static final Item golden_pickaxe = getRegisteredItem("golden_pickaxe");
  
  public static final Item golden_axe = getRegisteredItem("golden_axe");
  
  public static final Item string = getRegisteredItem("string");
  
  public static final Item feather = getRegisteredItem("feather");
  
  public static final Item gunpowder = getRegisteredItem("gunpowder");
  
  public static final Item wooden_hoe = getRegisteredItem("wooden_hoe");
  
  public static final Item stone_hoe = getRegisteredItem("stone_hoe");
  
  public static final Item iron_hoe = getRegisteredItem("iron_hoe");
  
  public static final Item diamond_hoe = getRegisteredItem("diamond_hoe");
  
  public static final Item golden_hoe = getRegisteredItem("golden_hoe");
  
  public static final Item wheat_seeds = getRegisteredItem("wheat_seeds");
  
  public static final Item wheat = getRegisteredItem("wheat");
  
  public static final Item bread = getRegisteredItem("bread");
  
  public static final ItemArmor leather_helmet = (ItemArmor)getRegisteredItem("leather_helmet");
  
  public static final ItemArmor leather_chestplate = (ItemArmor)getRegisteredItem("leather_chestplate");
  
  public static final ItemArmor leather_leggings = (ItemArmor)getRegisteredItem("leather_leggings");
  
  public static final ItemArmor leather_boots = (ItemArmor)getRegisteredItem("leather_boots");
  
  public static final ItemArmor chainmail_helmet = (ItemArmor)getRegisteredItem("chainmail_helmet");
  
  public static final ItemArmor chainmail_chestplate = (ItemArmor)getRegisteredItem("chainmail_chestplate");
  
  public static final ItemArmor chainmail_leggings = (ItemArmor)getRegisteredItem("chainmail_leggings");
  
  public static final ItemArmor chainmail_boots = (ItemArmor)getRegisteredItem("chainmail_boots");
  
  public static final ItemArmor iron_helmet = (ItemArmor)getRegisteredItem("iron_helmet");
  
  public static final ItemArmor iron_chestplate = (ItemArmor)getRegisteredItem("iron_chestplate");
  
  public static final ItemArmor iron_leggings = (ItemArmor)getRegisteredItem("iron_leggings");
  
  public static final ItemArmor iron_boots = (ItemArmor)getRegisteredItem("iron_boots");
  
  public static final ItemArmor diamond_helmet = (ItemArmor)getRegisteredItem("diamond_helmet");
  
  public static final ItemArmor diamond_chestplate = (ItemArmor)getRegisteredItem("diamond_chestplate");
  
  public static final ItemArmor diamond_leggings = (ItemArmor)getRegisteredItem("diamond_leggings");
  
  public static final ItemArmor diamond_boots = (ItemArmor)getRegisteredItem("diamond_boots");
  
  public static final ItemArmor golden_helmet = (ItemArmor)getRegisteredItem("golden_helmet");
  
  public static final ItemArmor golden_chestplate = (ItemArmor)getRegisteredItem("golden_chestplate");
  
  public static final ItemArmor golden_leggings = (ItemArmor)getRegisteredItem("golden_leggings");
  
  public static final ItemArmor golden_boots = (ItemArmor)getRegisteredItem("golden_boots");
  
  public static final Item flint = getRegisteredItem("flint");
  
  public static final Item porkchop = getRegisteredItem("porkchop");
  
  public static final Item cooked_porkchop = getRegisteredItem("cooked_porkchop");
  
  public static final Item painting = getRegisteredItem("painting");
  
  public static final Item golden_apple = getRegisteredItem("golden_apple");
  
  public static final Item sign = getRegisteredItem("sign");
  
  public static final Item oak_door = getRegisteredItem("wooden_door");
  
  public static final Item spruce_door = getRegisteredItem("spruce_door");
  
  public static final Item birch_door = getRegisteredItem("birch_door");
  
  public static final Item jungle_door = getRegisteredItem("jungle_door");
  
  public static final Item acacia_door = getRegisteredItem("acacia_door");
  
  public static final Item dark_oak_door = getRegisteredItem("dark_oak_door");
  
  public static final Item bucket = getRegisteredItem("bucket");
  
  public static final Item water_bucket = getRegisteredItem("water_bucket");
  
  public static final Item lava_bucket = getRegisteredItem("lava_bucket");
  
  public static final Item minecart = getRegisteredItem("minecart");
  
  public static final Item saddle = getRegisteredItem("saddle");
  
  public static final Item iron_door = getRegisteredItem("iron_door");
  
  public static final Item redstone = getRegisteredItem("redstone");
  
  public static final Item snowball = getRegisteredItem("snowball");
  
  public static final Item boat = getRegisteredItem("boat");
  
  public static final Item leather = getRegisteredItem("leather");
  
  public static final Item milk_bucket = getRegisteredItem("milk_bucket");
  
  public static final Item brick = getRegisteredItem("brick");
  
  public static final Item clay_ball = getRegisteredItem("clay_ball");
  
  public static final Item reeds = getRegisteredItem("reeds");
  
  public static final Item paper = getRegisteredItem("paper");
  
  public static final Item book = getRegisteredItem("book");
  
  public static final Item slime_ball = getRegisteredItem("slime_ball");
  
  public static final Item chest_minecart = getRegisteredItem("chest_minecart");
  
  public static final Item furnace_minecart = getRegisteredItem("furnace_minecart");
  
  public static final Item egg = getRegisteredItem("egg");
  
  public static final Item compass = getRegisteredItem("compass");
  
  public static final ItemFishingRod fishing_rod = (ItemFishingRod)getRegisteredItem("fishing_rod");
  
  public static final Item clock = getRegisteredItem("clock");
  
  public static final Item glowstone_dust = getRegisteredItem("glowstone_dust");
  
  public static final Item fish = getRegisteredItem("fish");
  
  public static final Item cooked_fish = getRegisteredItem("cooked_fish");
  
  public static final Item dye = getRegisteredItem("dye");
  
  public static final Item bone = getRegisteredItem("bone");
  
  public static final Item sugar = getRegisteredItem("sugar");
  
  public static final Item cake = getRegisteredItem("cake");
  
  public static final Item bed = getRegisteredItem("bed");
  
  public static final Item repeater = getRegisteredItem("repeater");
  
  public static final Item cookie = getRegisteredItem("cookie");
  
  public static final ItemMap filled_map = (ItemMap)getRegisteredItem("filled_map");
  
  public static final ItemShears shears = (ItemShears)getRegisteredItem("shears");
  
  public static final Item melon = getRegisteredItem("melon");
  
  public static final Item pumpkin_seeds = getRegisteredItem("pumpkin_seeds");
  
  public static final Item melon_seeds = getRegisteredItem("melon_seeds");
  
  public static final Item beef = getRegisteredItem("beef");
  
  public static final Item cooked_beef = getRegisteredItem("cooked_beef");
  
  public static final Item chicken = getRegisteredItem("chicken");
  
  public static final Item cooked_chicken = getRegisteredItem("cooked_chicken");
  
  public static final Item mutton = getRegisteredItem("mutton");
  
  public static final Item cooked_mutton = getRegisteredItem("cooked_mutton");
  
  public static final Item rabbit = getRegisteredItem("rabbit");
  
  public static final Item cooked_rabbit = getRegisteredItem("cooked_rabbit");
  
  public static final Item rabbit_stew = getRegisteredItem("rabbit_stew");
  
  public static final Item rabbit_foot = getRegisteredItem("rabbit_foot");
  
  public static final Item rabbit_hide = getRegisteredItem("rabbit_hide");
  
  public static final Item rotten_flesh = getRegisteredItem("rotten_flesh");
  
  public static final Item ender_pearl = getRegisteredItem("ender_pearl");
  
  public static final Item blaze_rod = getRegisteredItem("blaze_rod");
  
  public static final Item ghast_tear = getRegisteredItem("ghast_tear");
  
  public static final Item gold_nugget = getRegisteredItem("gold_nugget");
  
  public static final Item nether_wart = getRegisteredItem("nether_wart");
  
  public static final ItemPotion potionitem = (ItemPotion)getRegisteredItem("potion");
  
  public static final Item glass_bottle = getRegisteredItem("glass_bottle");
  
  public static final Item spider_eye = getRegisteredItem("spider_eye");
  
  public static final Item fermented_spider_eye = getRegisteredItem("fermented_spider_eye");
  
  public static final Item blaze_powder = getRegisteredItem("blaze_powder");
  
  public static final Item magma_cream = getRegisteredItem("magma_cream");
  
  public static final Item brewing_stand = getRegisteredItem("brewing_stand");
  
  public static final Item cauldron = getRegisteredItem("cauldron");
  
  public static final Item ender_eye = getRegisteredItem("ender_eye");
  
  public static final Item speckled_melon = getRegisteredItem("speckled_melon");
  
  public static final Item spawn_egg = getRegisteredItem("spawn_egg");
  
  public static final Item experience_bottle = getRegisteredItem("experience_bottle");
  
  public static final Item fire_charge = getRegisteredItem("fire_charge");
  
  public static final Item writable_book = getRegisteredItem("writable_book");
  
  public static final Item written_book = getRegisteredItem("written_book");
  
  public static final Item emerald = getRegisteredItem("emerald");
  
  public static final Item item_frame = getRegisteredItem("item_frame");
  
  public static final Item flower_pot = getRegisteredItem("flower_pot");
  
  public static final Item carrot = getRegisteredItem("carrot");
  
  public static final Item potato = getRegisteredItem("potato");
  
  public static final Item baked_potato = getRegisteredItem("baked_potato");
  
  public static final Item poisonous_potato = getRegisteredItem("poisonous_potato");
  
  public static final ItemEmptyMap map = (ItemEmptyMap)getRegisteredItem("map");
  
  public static final Item golden_carrot = getRegisteredItem("golden_carrot");
  
  public static final Item skull = getRegisteredItem("skull");
  
  public static final Item carrot_on_a_stick = getRegisteredItem("carrot_on_a_stick");
  
  public static final Item nether_star = getRegisteredItem("nether_star");
  
  public static final Item pumpkin_pie = getRegisteredItem("pumpkin_pie");
  
  public static final Item fireworks = getRegisteredItem("fireworks");
  
  public static final Item firework_charge = getRegisteredItem("firework_charge");
  
  public static final ItemEnchantedBook enchanted_book = (ItemEnchantedBook)getRegisteredItem("enchanted_book");
  
  public static final Item comparator = getRegisteredItem("comparator");
  
  public static final Item netherbrick = getRegisteredItem("netherbrick");
  
  public static final Item quartz = getRegisteredItem("quartz");
  
  public static final Item tnt_minecart = getRegisteredItem("tnt_minecart");
  
  public static final Item hopper_minecart = getRegisteredItem("hopper_minecart");
  
  public static final ItemArmorStand armor_stand = (ItemArmorStand)getRegisteredItem("armor_stand");
  
  public static final Item iron_horse_armor = getRegisteredItem("iron_horse_armor");
  
  public static final Item golden_horse_armor = getRegisteredItem("golden_horse_armor");
  
  public static final Item diamond_horse_armor = getRegisteredItem("diamond_horse_armor");
  
  public static final Item lead = getRegisteredItem("lead");
  
  public static final Item name_tag = getRegisteredItem("name_tag");
  
  public static final Item command_block_minecart = getRegisteredItem("command_block_minecart");
  
  public static final Item record_13 = getRegisteredItem("record_13");
  
  public static final Item record_cat = getRegisteredItem("record_cat");
  
  public static final Item record_blocks = getRegisteredItem("record_blocks");
  
  public static final Item record_chirp = getRegisteredItem("record_chirp");
  
  public static final Item record_far = getRegisteredItem("record_far");
  
  public static final Item record_mall = getRegisteredItem("record_mall");
  
  public static final Item record_mellohi = getRegisteredItem("record_mellohi");
  
  public static final Item record_stal = getRegisteredItem("record_stal");
  
  public static final Item record_strad = getRegisteredItem("record_strad");
  
  public static final Item record_ward = getRegisteredItem("record_ward");
  
  public static final Item record_11 = getRegisteredItem("record_11");
  
  public static final Item record_wait = getRegisteredItem("record_wait");
  
  public static final Item prismarine_shard = getRegisteredItem("prismarine_shard");
  
  public static final Item prismarine_crystals = getRegisteredItem("prismarine_crystals");
  
  public static final Item banner = getRegisteredItem("banner");
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\init\Items.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */