/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 */
package net.minecraft.stats;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashSet;
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
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatBasic;
import net.minecraft.stats.StatCrafting;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ResourceLocation;

public class StatList {
    protected static Map oneShotStats = Maps.newHashMap();
    public static List allStats = Lists.newArrayList();
    public static List generalStats = Lists.newArrayList();
    public static List itemStats = Lists.newArrayList();
    public static List objectMineStats = Lists.newArrayList();
    public static StatBase leaveGameStat = new StatBasic("stat.leaveGame", new ChatComponentTranslation("stat.leaveGame", new Object[0])).initIndependentStat().registerStat();
    public static StatBase minutesPlayedStat = new StatBasic("stat.playOneMinute", new ChatComponentTranslation("stat.playOneMinute", new Object[0]), StatBase.timeStatType).initIndependentStat().registerStat();
    public static StatBase timeSinceDeathStat = new StatBasic("stat.timeSinceDeath", new ChatComponentTranslation("stat.timeSinceDeath", new Object[0]), StatBase.timeStatType).initIndependentStat().registerStat();
    public static StatBase distanceWalkedStat = new StatBasic("stat.walkOneCm", new ChatComponentTranslation("stat.walkOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
    public static StatBase distanceCrouchedStat = new StatBasic("stat.crouchOneCm", new ChatComponentTranslation("stat.crouchOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
    public static StatBase distanceSprintedStat = new StatBasic("stat.sprintOneCm", new ChatComponentTranslation("stat.sprintOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
    public static StatBase distanceSwumStat = new StatBasic("stat.swimOneCm", new ChatComponentTranslation("stat.swimOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
    public static StatBase distanceFallenStat = new StatBasic("stat.fallOneCm", new ChatComponentTranslation("stat.fallOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
    public static StatBase distanceClimbedStat = new StatBasic("stat.climbOneCm", new ChatComponentTranslation("stat.climbOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
    public static StatBase distanceFlownStat = new StatBasic("stat.flyOneCm", new ChatComponentTranslation("stat.flyOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
    public static StatBase distanceDoveStat = new StatBasic("stat.diveOneCm", new ChatComponentTranslation("stat.diveOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
    public static StatBase distanceByMinecartStat = new StatBasic("stat.minecartOneCm", new ChatComponentTranslation("stat.minecartOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
    public static StatBase distanceByBoatStat = new StatBasic("stat.boatOneCm", new ChatComponentTranslation("stat.boatOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
    public static StatBase distanceByPigStat = new StatBasic("stat.pigOneCm", new ChatComponentTranslation("stat.pigOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
    public static StatBase distanceByHorseStat = new StatBasic("stat.horseOneCm", new ChatComponentTranslation("stat.horseOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
    public static StatBase jumpStat = new StatBasic("stat.jump", new ChatComponentTranslation("stat.jump", new Object[0])).initIndependentStat().registerStat();
    public static StatBase dropStat = new StatBasic("stat.drop", new ChatComponentTranslation("stat.drop", new Object[0])).initIndependentStat().registerStat();
    public static StatBase damageDealtStat = new StatBasic("stat.damageDealt", new ChatComponentTranslation("stat.damageDealt", new Object[0]), StatBase.field_111202_k).registerStat();
    public static StatBase damageTakenStat = new StatBasic("stat.damageTaken", new ChatComponentTranslation("stat.damageTaken", new Object[0]), StatBase.field_111202_k).registerStat();
    public static StatBase deathsStat = new StatBasic("stat.deaths", new ChatComponentTranslation("stat.deaths", new Object[0])).registerStat();
    public static StatBase mobKillsStat = new StatBasic("stat.mobKills", new ChatComponentTranslation("stat.mobKills", new Object[0])).registerStat();
    public static StatBase animalsBredStat = new StatBasic("stat.animalsBred", new ChatComponentTranslation("stat.animalsBred", new Object[0])).registerStat();
    public static StatBase playerKillsStat = new StatBasic("stat.playerKills", new ChatComponentTranslation("stat.playerKills", new Object[0])).registerStat();
    public static StatBase fishCaughtStat = new StatBasic("stat.fishCaught", new ChatComponentTranslation("stat.fishCaught", new Object[0])).registerStat();
    public static StatBase junkFishedStat = new StatBasic("stat.junkFished", new ChatComponentTranslation("stat.junkFished", new Object[0])).registerStat();
    public static StatBase treasureFishedStat = new StatBasic("stat.treasureFished", new ChatComponentTranslation("stat.treasureFished", new Object[0])).registerStat();
    public static StatBase timesTalkedToVillagerStat = new StatBasic("stat.talkedToVillager", new ChatComponentTranslation("stat.talkedToVillager", new Object[0])).registerStat();
    public static StatBase timesTradedWithVillagerStat = new StatBasic("stat.tradedWithVillager", new ChatComponentTranslation("stat.tradedWithVillager", new Object[0])).registerStat();
    public static final StatBase[] mineBlockStatArray = new StatBase[4096];
    public static final StatBase[] objectCraftStats = new StatBase[32000];
    public static final StatBase[] objectUseStats = new StatBase[32000];
    public static final StatBase[] objectBreakStats = new StatBase[32000];
    private static final String __OBFID = "CL_00001480";

    public static void func_151178_a() {
        StatList.func_151181_c();
        StatList.initStats();
        StatList.func_151179_e();
        StatList.initCraftableStats();
        AchievementList.init();
        EntityList.func_151514_a();
    }

    private static void initCraftableStats() {
        HashSet var0 = Sets.newHashSet();
        for (IRecipe var2 : CraftingManager.getInstance().getRecipeList()) {
            if (var2.getRecipeOutput() == null) continue;
            var0.add(var2.getRecipeOutput().getItem());
        }
        for (ItemStack var5 : FurnaceRecipes.instance().getSmeltingList().values()) {
            var0.add(var5.getItem());
        }
        for (Item var6 : var0) {
            if (var6 == null) continue;
            int var3 = Item.getIdFromItem(var6);
            String var4 = StatList.func_180204_a(var6);
            if (var4 == null) continue;
            StatList.objectCraftStats[var3] = new StatCrafting("stat.craftItem.", var4, new ChatComponentTranslation("stat.craftItem", new ItemStack(var6).getChatComponent()), var6).registerStat();
        }
        StatList.replaceAllSimilarBlocks(objectCraftStats);
    }

    private static void func_151181_c() {
        for (Block var1 : Block.blockRegistry) {
            Item var2 = Item.getItemFromBlock(var1);
            if (var2 == null) continue;
            int var3 = Block.getIdFromBlock(var1);
            String var4 = StatList.func_180204_a(var2);
            if (var4 == null || !var1.getEnableStats()) continue;
            StatList.mineBlockStatArray[var3] = new StatCrafting("stat.mineBlock.", var4, new ChatComponentTranslation("stat.mineBlock", new ItemStack(var1).getChatComponent()), var2).registerStat();
            objectMineStats.add((StatCrafting)mineBlockStatArray[var3]);
        }
        StatList.replaceAllSimilarBlocks(mineBlockStatArray);
    }

    private static void initStats() {
        for (Item var1 : Item.itemRegistry) {
            if (var1 == null) continue;
            int var2 = Item.getIdFromItem(var1);
            String var3 = StatList.func_180204_a(var1);
            if (var3 == null) continue;
            StatList.objectUseStats[var2] = new StatCrafting("stat.useItem.", var3, new ChatComponentTranslation("stat.useItem", new ItemStack(var1).getChatComponent()), var1).registerStat();
            if (var1 instanceof ItemBlock) continue;
            itemStats.add((StatCrafting)objectUseStats[var2]);
        }
        StatList.replaceAllSimilarBlocks(objectUseStats);
    }

    private static void func_151179_e() {
        for (Item var1 : Item.itemRegistry) {
            if (var1 == null) continue;
            int var2 = Item.getIdFromItem(var1);
            String var3 = StatList.func_180204_a(var1);
            if (var3 == null || !var1.isDamageable()) continue;
            StatList.objectBreakStats[var2] = new StatCrafting("stat.breakItem.", var3, new ChatComponentTranslation("stat.breakItem", new ItemStack(var1).getChatComponent()), var1).registerStat();
        }
        StatList.replaceAllSimilarBlocks(objectBreakStats);
    }

    private static String func_180204_a(Item p_180204_0_) {
        ResourceLocation var1 = (ResourceLocation)Item.itemRegistry.getNameForObject(p_180204_0_);
        return var1 != null ? var1.toString().replace(':', '.') : null;
    }

    private static void replaceAllSimilarBlocks(StatBase[] p_75924_0_) {
        StatList.func_151180_a(p_75924_0_, Blocks.water, Blocks.flowing_water);
        StatList.func_151180_a(p_75924_0_, Blocks.lava, Blocks.flowing_lava);
        StatList.func_151180_a(p_75924_0_, Blocks.lit_pumpkin, Blocks.pumpkin);
        StatList.func_151180_a(p_75924_0_, Blocks.lit_furnace, Blocks.furnace);
        StatList.func_151180_a(p_75924_0_, Blocks.lit_redstone_ore, Blocks.redstone_ore);
        StatList.func_151180_a(p_75924_0_, Blocks.powered_repeater, Blocks.unpowered_repeater);
        StatList.func_151180_a(p_75924_0_, Blocks.powered_comparator, Blocks.unpowered_comparator);
        StatList.func_151180_a(p_75924_0_, Blocks.redstone_torch, Blocks.unlit_redstone_torch);
        StatList.func_151180_a(p_75924_0_, Blocks.lit_redstone_lamp, Blocks.redstone_lamp);
        StatList.func_151180_a(p_75924_0_, Blocks.double_stone_slab, Blocks.stone_slab);
        StatList.func_151180_a(p_75924_0_, Blocks.double_wooden_slab, Blocks.wooden_slab);
        StatList.func_151180_a(p_75924_0_, Blocks.double_stone_slab2, Blocks.stone_slab2);
        StatList.func_151180_a(p_75924_0_, Blocks.grass, Blocks.dirt);
        StatList.func_151180_a(p_75924_0_, Blocks.farmland, Blocks.dirt);
    }

    private static void func_151180_a(StatBase[] p_151180_0_, Block p_151180_1_, Block p_151180_2_) {
        int var3 = Block.getIdFromBlock(p_151180_1_);
        int var4 = Block.getIdFromBlock(p_151180_2_);
        if (p_151180_0_[var3] != null && p_151180_0_[var4] == null) {
            p_151180_0_[var4] = p_151180_0_[var3];
        } else {
            allStats.remove(p_151180_0_[var3]);
            objectMineStats.remove(p_151180_0_[var3]);
            generalStats.remove(p_151180_0_[var3]);
            p_151180_0_[var3] = p_151180_0_[var4];
        }
    }

    public static StatBase func_151182_a(EntityList.EntityEggInfo p_151182_0_) {
        String var1 = EntityList.getStringFromID(p_151182_0_.spawnedID);
        return var1 == null ? null : new StatBase("stat.killEntity." + var1, new ChatComponentTranslation("stat.entityKill", new ChatComponentTranslation("entity." + var1 + ".name", new Object[0]))).registerStat();
    }

    public static StatBase func_151176_b(EntityList.EntityEggInfo p_151176_0_) {
        String var1 = EntityList.getStringFromID(p_151176_0_.spawnedID);
        return var1 == null ? null : new StatBase("stat.entityKilledBy." + var1, new ChatComponentTranslation("stat.entityKilledBy", new ChatComponentTranslation("entity." + var1 + ".name", new Object[0]))).registerStat();
    }

    public static StatBase getOneShotStat(String p_151177_0_) {
        return (StatBase)oneShotStats.get(p_151177_0_);
    }
}

