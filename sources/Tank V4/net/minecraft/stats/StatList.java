package net.minecraft.stats;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ResourceLocation;

public class StatList {
   public static final StatBase[] objectUseStats;
   public static StatBase damageDealtStat;
   public static StatBase field_181737_U;
   public static StatBase timesTradedWithVillagerStat;
   public static StatBase field_181742_Z;
   public static StatBase field_181730_N;
   public static StatBase field_181724_H;
   protected static Map oneShotStats = Maps.newHashMap();
   public static StatBase distanceByHorseStat;
   public static List allStats = Lists.newArrayList();
   public static StatBase damageTakenStat;
   public static StatBase animalsBredStat;
   public static StatBase playerKillsStat;
   public static StatBase field_181735_S;
   public static StatBase distanceCrouchedStat;
   public static StatBase distanceByPigStat;
   public static final StatBase[] objectBreakStats;
   public static StatBase distanceSwumStat;
   public static StatBase field_181741_Y;
   public static StatBase distanceFallenStat;
   public static StatBase field_181732_P;
   public static StatBase distanceFlownStat;
   public static StatBase distanceDoveStat;
   public static StatBase timesTalkedToVillagerStat;
   public static StatBase distanceWalkedStat;
   public static StatBase field_181740_X;
   public static StatBase field_181728_L;
   public static StatBase mobKillsStat;
   public static List generalStats = Lists.newArrayList();
   public static StatBase field_181726_J;
   public static List itemStats = Lists.newArrayList();
   public static final StatBase[] mineBlockStatArray;
   public static StatBase field_181731_O;
   public static List objectMineStats = Lists.newArrayList();
   public static StatBase field_181723_aa;
   public static StatBase field_181734_R;
   public static final StatBase[] objectCraftStats;
   public static StatBase distanceByBoatStat;
   public static StatBase distanceSprintedStat;
   public static StatBase field_181729_M;
   public static StatBase junkFishedStat;
   public static StatBase leaveGameStat = (new StatBasic("stat.leaveGame", new ChatComponentTranslation("stat.leaveGame", new Object[0]))).initIndependentStat().registerStat();
   public static StatBase treasureFishedStat;
   public static StatBase field_181727_K;
   public static StatBase field_181733_Q;
   public static StatBase minutesPlayedStat;
   public static StatBase jumpStat;
   public static StatBase distanceClimbedStat;
   public static StatBase fishCaughtStat;
   public static StatBase field_181736_T;
   public static StatBase field_181739_W;
   public static StatBase field_181725_I;
   public static StatBase deathsStat;
   public static StatBase distanceByMinecartStat;
   public static StatBase dropStat;
   public static StatBase field_181738_V;
   public static StatBase timeSinceDeathStat;

   private static void initCraftableStats() {
      HashSet var0 = Sets.newHashSet();
      Iterator var2 = CraftingManager.getInstance().getRecipeList().iterator();

      while(var2.hasNext()) {
         IRecipe var1 = (IRecipe)var2.next();
         if (var1.getRecipeOutput() != null) {
            var0.add(var1.getRecipeOutput().getItem());
         }
      }

      var2 = FurnaceRecipes.instance().getSmeltingList().values().iterator();

      while(var2.hasNext()) {
         ItemStack var5 = (ItemStack)var2.next();
         var0.add(var5.getItem());
      }

      var2 = var0.iterator();

      while(var2.hasNext()) {
         Item var6 = (Item)var2.next();
         if (var6 != null) {
            int var3 = Item.getIdFromItem(var6);
            String var4 = func_180204_a(var6);
            if (var4 != null) {
               objectCraftStats[var3] = (new StatCrafting("stat.craftItem.", var4, new ChatComponentTranslation("stat.craftItem", new Object[]{(new ItemStack(var6)).getChatComponent()}), var6)).registerStat();
            }
         }
      }

      replaceAllSimilarBlocks(objectCraftStats);
   }

   private static void mergeStatBases(StatBase[] var0, Block var1, Block var2) {
      int var3 = Block.getIdFromBlock(var1);
      int var4 = Block.getIdFromBlock(var2);
      if (var0[var3] != null && var0[var4] == null) {
         var0[var4] = var0[var3];
      } else {
         allStats.remove(var0[var3]);
         objectMineStats.remove(var0[var3]);
         generalStats.remove(var0[var3]);
         var0[var3] = var0[var4];
      }

   }

   private static void replaceAllSimilarBlocks(StatBase[] var0) {
      mergeStatBases(var0, Blocks.water, Blocks.flowing_water);
      mergeStatBases(var0, Blocks.lava, Blocks.flowing_lava);
      mergeStatBases(var0, Blocks.lit_pumpkin, Blocks.pumpkin);
      mergeStatBases(var0, Blocks.lit_furnace, Blocks.furnace);
      mergeStatBases(var0, Blocks.lit_redstone_ore, Blocks.redstone_ore);
      mergeStatBases(var0, Blocks.powered_repeater, Blocks.unpowered_repeater);
      mergeStatBases(var0, Blocks.powered_comparator, Blocks.unpowered_comparator);
      mergeStatBases(var0, Blocks.redstone_torch, Blocks.unlit_redstone_torch);
      mergeStatBases(var0, Blocks.lit_redstone_lamp, Blocks.redstone_lamp);
      mergeStatBases(var0, Blocks.double_stone_slab, Blocks.stone_slab);
      mergeStatBases(var0, Blocks.double_wooden_slab, Blocks.wooden_slab);
      mergeStatBases(var0, Blocks.double_stone_slab2, Blocks.stone_slab2);
      mergeStatBases(var0, Blocks.grass, Blocks.dirt);
      mergeStatBases(var0, Blocks.farmland, Blocks.dirt);
   }

   static {
      minutesPlayedStat = (new StatBasic("stat.playOneMinute", new ChatComponentTranslation("stat.playOneMinute", new Object[0]), StatBase.timeStatType)).initIndependentStat().registerStat();
      timeSinceDeathStat = (new StatBasic("stat.timeSinceDeath", new ChatComponentTranslation("stat.timeSinceDeath", new Object[0]), StatBase.timeStatType)).initIndependentStat().registerStat();
      distanceWalkedStat = (new StatBasic("stat.walkOneCm", new ChatComponentTranslation("stat.walkOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      distanceCrouchedStat = (new StatBasic("stat.crouchOneCm", new ChatComponentTranslation("stat.crouchOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      distanceSprintedStat = (new StatBasic("stat.sprintOneCm", new ChatComponentTranslation("stat.sprintOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      distanceSwumStat = (new StatBasic("stat.swimOneCm", new ChatComponentTranslation("stat.swimOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      distanceFallenStat = (new StatBasic("stat.fallOneCm", new ChatComponentTranslation("stat.fallOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      distanceClimbedStat = (new StatBasic("stat.climbOneCm", new ChatComponentTranslation("stat.climbOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      distanceFlownStat = (new StatBasic("stat.flyOneCm", new ChatComponentTranslation("stat.flyOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      distanceDoveStat = (new StatBasic("stat.diveOneCm", new ChatComponentTranslation("stat.diveOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      distanceByMinecartStat = (new StatBasic("stat.minecartOneCm", new ChatComponentTranslation("stat.minecartOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      distanceByBoatStat = (new StatBasic("stat.boatOneCm", new ChatComponentTranslation("stat.boatOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      distanceByPigStat = (new StatBasic("stat.pigOneCm", new ChatComponentTranslation("stat.pigOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      distanceByHorseStat = (new StatBasic("stat.horseOneCm", new ChatComponentTranslation("stat.horseOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      jumpStat = (new StatBasic("stat.jump", new ChatComponentTranslation("stat.jump", new Object[0]))).initIndependentStat().registerStat();
      dropStat = (new StatBasic("stat.drop", new ChatComponentTranslation("stat.drop", new Object[0]))).initIndependentStat().registerStat();
      damageDealtStat = (new StatBasic("stat.damageDealt", new ChatComponentTranslation("stat.damageDealt", new Object[0]), StatBase.field_111202_k)).registerStat();
      damageTakenStat = (new StatBasic("stat.damageTaken", new ChatComponentTranslation("stat.damageTaken", new Object[0]), StatBase.field_111202_k)).registerStat();
      deathsStat = (new StatBasic("stat.deaths", new ChatComponentTranslation("stat.deaths", new Object[0]))).registerStat();
      mobKillsStat = (new StatBasic("stat.mobKills", new ChatComponentTranslation("stat.mobKills", new Object[0]))).registerStat();
      animalsBredStat = (new StatBasic("stat.animalsBred", new ChatComponentTranslation("stat.animalsBred", new Object[0]))).registerStat();
      playerKillsStat = (new StatBasic("stat.playerKills", new ChatComponentTranslation("stat.playerKills", new Object[0]))).registerStat();
      fishCaughtStat = (new StatBasic("stat.fishCaught", new ChatComponentTranslation("stat.fishCaught", new Object[0]))).registerStat();
      junkFishedStat = (new StatBasic("stat.junkFished", new ChatComponentTranslation("stat.junkFished", new Object[0]))).registerStat();
      treasureFishedStat = (new StatBasic("stat.treasureFished", new ChatComponentTranslation("stat.treasureFished", new Object[0]))).registerStat();
      timesTalkedToVillagerStat = (new StatBasic("stat.talkedToVillager", new ChatComponentTranslation("stat.talkedToVillager", new Object[0]))).registerStat();
      timesTradedWithVillagerStat = (new StatBasic("stat.tradedWithVillager", new ChatComponentTranslation("stat.tradedWithVillager", new Object[0]))).registerStat();
      field_181724_H = (new StatBasic("stat.cakeSlicesEaten", new ChatComponentTranslation("stat.cakeSlicesEaten", new Object[0]))).registerStat();
      field_181725_I = (new StatBasic("stat.cauldronFilled", new ChatComponentTranslation("stat.cauldronFilled", new Object[0]))).registerStat();
      field_181726_J = (new StatBasic("stat.cauldronUsed", new ChatComponentTranslation("stat.cauldronUsed", new Object[0]))).registerStat();
      field_181727_K = (new StatBasic("stat.armorCleaned", new ChatComponentTranslation("stat.armorCleaned", new Object[0]))).registerStat();
      field_181728_L = (new StatBasic("stat.bannerCleaned", new ChatComponentTranslation("stat.bannerCleaned", new Object[0]))).registerStat();
      field_181729_M = (new StatBasic("stat.brewingstandInteraction", new ChatComponentTranslation("stat.brewingstandInteraction", new Object[0]))).registerStat();
      field_181730_N = (new StatBasic("stat.beaconInteraction", new ChatComponentTranslation("stat.beaconInteraction", new Object[0]))).registerStat();
      field_181731_O = (new StatBasic("stat.dropperInspected", new ChatComponentTranslation("stat.dropperInspected", new Object[0]))).registerStat();
      field_181732_P = (new StatBasic("stat.hopperInspected", new ChatComponentTranslation("stat.hopperInspected", new Object[0]))).registerStat();
      field_181733_Q = (new StatBasic("stat.dispenserInspected", new ChatComponentTranslation("stat.dispenserInspected", new Object[0]))).registerStat();
      field_181734_R = (new StatBasic("stat.noteblockPlayed", new ChatComponentTranslation("stat.noteblockPlayed", new Object[0]))).registerStat();
      field_181735_S = (new StatBasic("stat.noteblockTuned", new ChatComponentTranslation("stat.noteblockTuned", new Object[0]))).registerStat();
      field_181736_T = (new StatBasic("stat.flowerPotted", new ChatComponentTranslation("stat.flowerPotted", new Object[0]))).registerStat();
      field_181737_U = (new StatBasic("stat.trappedChestTriggered", new ChatComponentTranslation("stat.trappedChestTriggered", new Object[0]))).registerStat();
      field_181738_V = (new StatBasic("stat.enderchestOpened", new ChatComponentTranslation("stat.enderchestOpened", new Object[0]))).registerStat();
      field_181739_W = (new StatBasic("stat.itemEnchanted", new ChatComponentTranslation("stat.itemEnchanted", new Object[0]))).registerStat();
      field_181740_X = (new StatBasic("stat.recordPlayed", new ChatComponentTranslation("stat.recordPlayed", new Object[0]))).registerStat();
      field_181741_Y = (new StatBasic("stat.furnaceInteraction", new ChatComponentTranslation("stat.furnaceInteraction", new Object[0]))).registerStat();
      field_181742_Z = (new StatBasic("stat.craftingTableInteraction", new ChatComponentTranslation("stat.workbenchInteraction", new Object[0]))).registerStat();
      field_181723_aa = (new StatBasic("stat.chestOpened", new ChatComponentTranslation("stat.chestOpened", new Object[0]))).registerStat();
      mineBlockStatArray = new StatBase[4096];
      objectCraftStats = new StatBase[32000];
      objectUseStats = new StatBase[32000];
      objectBreakStats = new StatBase[32000];
   }

   private static void initMiningStats() {
      Iterator var1 = Block.blockRegistry.iterator();

      while(var1.hasNext()) {
         Block var0 = (Block)var1.next();
         Item var2 = Item.getItemFromBlock(var0);
         if (var2 != null) {
            int var3 = Block.getIdFromBlock(var0);
            String var4 = func_180204_a(var2);
            if (var4 != null && var0.getEnableStats()) {
               mineBlockStatArray[var3] = (new StatCrafting("stat.mineBlock.", var4, new ChatComponentTranslation("stat.mineBlock", new Object[]{(new ItemStack(var0)).getChatComponent()}), var2)).registerStat();
               objectMineStats.add((StatCrafting)mineBlockStatArray[var3]);
            }
         }
      }

      replaceAllSimilarBlocks(mineBlockStatArray);
   }

   private static void initStats() {
      Iterator var1 = Item.itemRegistry.iterator();

      while(var1.hasNext()) {
         Item var0 = (Item)var1.next();
         if (var0 != null) {
            int var2 = Item.getIdFromItem(var0);
            String var3 = func_180204_a(var0);
            if (var3 != null) {
               objectUseStats[var2] = (new StatCrafting("stat.useItem.", var3, new ChatComponentTranslation("stat.useItem", new Object[]{(new ItemStack(var0)).getChatComponent()}), var0)).registerStat();
               if (!(var0 instanceof ItemBlock)) {
                  itemStats.add((StatCrafting)objectUseStats[var2]);
               }
            }
         }
      }

      replaceAllSimilarBlocks(objectUseStats);
   }

   public static void init() {
      initMiningStats();
      initStats();
      initItemDepleteStats();
      initCraftableStats();
      AchievementList.init();
      EntityList.func_151514_a();
   }

   private static String func_180204_a(Item var0) {
      ResourceLocation var1 = (ResourceLocation)Item.itemRegistry.getNameForObject(var0);
      return var1 != null ? var1.toString().replace(':', '.') : null;
   }

   public static StatBase getStatEntityKilledBy(EntityList.EntityEggInfo var0) {
      String var1 = EntityList.getStringFromID(var0.spawnedID);
      return var1 == null ? null : (new StatBase("stat.entityKilledBy." + var1, new ChatComponentTranslation("stat.entityKilledBy", new Object[]{new ChatComponentTranslation("entity." + var1 + ".name", new Object[0])}))).registerStat();
   }

   public static StatBase getOneShotStat(String var0) {
      return (StatBase)oneShotStats.get(var0);
   }

   private static void initItemDepleteStats() {
      Iterator var1 = Item.itemRegistry.iterator();

      while(var1.hasNext()) {
         Item var0 = (Item)var1.next();
         if (var0 != null) {
            int var2 = Item.getIdFromItem(var0);
            String var3 = func_180204_a(var0);
            if (var3 != null && var0.isDamageable()) {
               objectBreakStats[var2] = (new StatCrafting("stat.breakItem.", var3, new ChatComponentTranslation("stat.breakItem", new Object[]{(new ItemStack(var0)).getChatComponent()}), var0)).registerStat();
            }
         }
      }

      replaceAllSimilarBlocks(objectBreakStats);
   }

   public static StatBase getStatKillEntity(EntityList.EntityEggInfo var0) {
      String var1 = EntityList.getStringFromID(var0.spawnedID);
      return var1 == null ? null : (new StatBase("stat.killEntity." + var1, new ChatComponentTranslation("stat.entityKill", new Object[]{new ChatComponentTranslation("entity." + var1 + ".name", new Object[0])}))).registerStat();
   }
}
