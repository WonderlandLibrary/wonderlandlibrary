// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.stats;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.ItemBlock;
import net.minecraft.block.Block;
import java.util.Iterator;
import java.util.HashSet;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.CraftingManager;
import com.google.common.collect.Sets;
import net.minecraft.entity.EntityList;
import java.util.List;
import java.util.Map;

public class StatList
{
    protected static Map oneShotStats;
    public static List allStats;
    public static List generalStats;
    public static List itemStats;
    public static List objectMineStats;
    public static StatBase leaveGameStat;
    public static StatBase minutesPlayedStat;
    public static StatBase timeSinceDeathStat;
    public static StatBase distanceWalkedStat;
    public static StatBase distanceCrouchedStat;
    public static StatBase distanceSprintedStat;
    public static StatBase distanceSwumStat;
    public static StatBase distanceFallenStat;
    public static StatBase distanceClimbedStat;
    public static StatBase distanceFlownStat;
    public static StatBase distanceDoveStat;
    public static StatBase distanceByMinecartStat;
    public static StatBase distanceByBoatStat;
    public static StatBase distanceByPigStat;
    public static StatBase distanceByHorseStat;
    public static StatBase jumpStat;
    public static StatBase dropStat;
    public static StatBase damageDealtStat;
    public static StatBase damageTakenStat;
    public static StatBase deathsStat;
    public static StatBase mobKillsStat;
    public static StatBase animalsBredStat;
    public static StatBase playerKillsStat;
    public static StatBase fishCaughtStat;
    public static StatBase junkFishedStat;
    public static StatBase treasureFishedStat;
    public static StatBase timesTalkedToVillagerStat;
    public static StatBase timesTradedWithVillagerStat;
    public static final StatBase[] mineBlockStatArray;
    public static final StatBase[] objectCraftStats;
    public static final StatBase[] objectUseStats;
    public static final StatBase[] objectBreakStats;
    private static final String __OBFID = "CL_00001480";
    
    public static void func_151178_a() {
        func_151181_c();
        initStats();
        func_151179_e();
        initCraftableStats();
        AchievementList.init();
        EntityList.func_151514_a();
    }
    
    private static void initCraftableStats() {
        final HashSet var0 = Sets.newHashSet();
        for (final IRecipe var3 : CraftingManager.getInstance().getRecipeList()) {
            if (var3.getRecipeOutput() != null) {
                var0.add(var3.getRecipeOutput().getItem());
            }
        }
        for (final ItemStack var4 : FurnaceRecipes.instance().getSmeltingList().values()) {
            var0.add(var4.getItem());
        }
        for (final Item var5 : var0) {
            if (var5 != null) {
                final int var6 = Item.getIdFromItem(var5);
                final String var7 = func_180204_a(var5);
                if (var7 == null) {
                    continue;
                }
                StatList.objectCraftStats[var6] = new StatCrafting("stat.craftItem.", var7, new ChatComponentTranslation("stat.craftItem", new Object[] { new ItemStack(var5).getChatComponent() }), var5).registerStat();
            }
        }
        replaceAllSimilarBlocks(StatList.objectCraftStats);
    }
    
    private static void func_151181_c() {
        for (final Block var2 : Block.blockRegistry) {
            final Item var3 = Item.getItemFromBlock(var2);
            if (var3 != null) {
                final int var4 = Block.getIdFromBlock(var2);
                final String var5 = func_180204_a(var3);
                if (var5 == null || !var2.getEnableStats()) {
                    continue;
                }
                StatList.mineBlockStatArray[var4] = new StatCrafting("stat.mineBlock.", var5, new ChatComponentTranslation("stat.mineBlock", new Object[] { new ItemStack(var2).getChatComponent() }), var3).registerStat();
                StatList.objectMineStats.add(StatList.mineBlockStatArray[var4]);
            }
        }
        replaceAllSimilarBlocks(StatList.mineBlockStatArray);
    }
    
    private static void initStats() {
        for (final Item var2 : Item.itemRegistry) {
            if (var2 != null) {
                final int var3 = Item.getIdFromItem(var2);
                final String var4 = func_180204_a(var2);
                if (var4 == null) {
                    continue;
                }
                StatList.objectUseStats[var3] = new StatCrafting("stat.useItem.", var4, new ChatComponentTranslation("stat.useItem", new Object[] { new ItemStack(var2).getChatComponent() }), var2).registerStat();
                if (var2 instanceof ItemBlock) {
                    continue;
                }
                StatList.itemStats.add(StatList.objectUseStats[var3]);
            }
        }
        replaceAllSimilarBlocks(StatList.objectUseStats);
    }
    
    private static void func_151179_e() {
        for (final Item var2 : Item.itemRegistry) {
            if (var2 != null) {
                final int var3 = Item.getIdFromItem(var2);
                final String var4 = func_180204_a(var2);
                if (var4 == null || !var2.isDamageable()) {
                    continue;
                }
                StatList.objectBreakStats[var3] = new StatCrafting("stat.breakItem.", var4, new ChatComponentTranslation("stat.breakItem", new Object[] { new ItemStack(var2).getChatComponent() }), var2).registerStat();
            }
        }
        replaceAllSimilarBlocks(StatList.objectBreakStats);
    }
    
    private static String func_180204_a(final Item p_180204_0_) {
        final ResourceLocation var1 = (ResourceLocation)Item.itemRegistry.getNameForObject(p_180204_0_);
        return (var1 != null) ? var1.toString().replace(':', '.') : null;
    }
    
    private static void replaceAllSimilarBlocks(final StatBase[] p_75924_0_) {
        func_151180_a(p_75924_0_, Blocks.water, Blocks.flowing_water);
        func_151180_a(p_75924_0_, Blocks.lava, Blocks.flowing_lava);
        func_151180_a(p_75924_0_, Blocks.lit_pumpkin, Blocks.pumpkin);
        func_151180_a(p_75924_0_, Blocks.lit_furnace, Blocks.furnace);
        func_151180_a(p_75924_0_, Blocks.lit_redstone_ore, Blocks.redstone_ore);
        func_151180_a(p_75924_0_, Blocks.powered_repeater, Blocks.unpowered_repeater);
        func_151180_a(p_75924_0_, Blocks.powered_comparator, Blocks.unpowered_comparator);
        func_151180_a(p_75924_0_, Blocks.redstone_torch, Blocks.unlit_redstone_torch);
        func_151180_a(p_75924_0_, Blocks.lit_redstone_lamp, Blocks.redstone_lamp);
        func_151180_a(p_75924_0_, Blocks.double_stone_slab, Blocks.stone_slab);
        func_151180_a(p_75924_0_, Blocks.double_wooden_slab, Blocks.wooden_slab);
        func_151180_a(p_75924_0_, Blocks.double_stone_slab2, Blocks.stone_slab2);
        func_151180_a(p_75924_0_, Blocks.grass, Blocks.dirt);
        func_151180_a(p_75924_0_, Blocks.farmland, Blocks.dirt);
    }
    
    private static void func_151180_a(final StatBase[] p_151180_0_, final Block p_151180_1_, final Block p_151180_2_) {
        final int var3 = Block.getIdFromBlock(p_151180_1_);
        final int var4 = Block.getIdFromBlock(p_151180_2_);
        if (p_151180_0_[var3] != null && p_151180_0_[var4] == null) {
            p_151180_0_[var4] = p_151180_0_[var3];
        }
        else {
            StatList.allStats.remove(p_151180_0_[var3]);
            StatList.objectMineStats.remove(p_151180_0_[var3]);
            StatList.generalStats.remove(p_151180_0_[var3]);
            p_151180_0_[var3] = p_151180_0_[var4];
        }
    }
    
    public static StatBase func_151182_a(final EntityList.EntityEggInfo p_151182_0_) {
        final String var1 = EntityList.getStringFromID(p_151182_0_.spawnedID);
        return (var1 == null) ? null : new StatBase("stat.killEntity." + var1, new ChatComponentTranslation("stat.entityKill", new Object[] { new ChatComponentTranslation("entity." + var1 + ".name", new Object[0]) })).registerStat();
    }
    
    public static StatBase func_151176_b(final EntityList.EntityEggInfo p_151176_0_) {
        final String var1 = EntityList.getStringFromID(p_151176_0_.spawnedID);
        return (var1 == null) ? null : new StatBase("stat.entityKilledBy." + var1, new ChatComponentTranslation("stat.entityKilledBy", new Object[] { new ChatComponentTranslation("entity." + var1 + ".name", new Object[0]) })).registerStat();
    }
    
    public static StatBase getOneShotStat(final String p_151177_0_) {
        return StatList.oneShotStats.get(p_151177_0_);
    }
    
    static {
        StatList.oneShotStats = Maps.newHashMap();
        StatList.allStats = Lists.newArrayList();
        StatList.generalStats = Lists.newArrayList();
        StatList.itemStats = Lists.newArrayList();
        StatList.objectMineStats = Lists.newArrayList();
        StatList.leaveGameStat = new StatBasic("stat.leaveGame", new ChatComponentTranslation("stat.leaveGame", new Object[0])).initIndependentStat().registerStat();
        StatList.minutesPlayedStat = new StatBasic("stat.playOneMinute", new ChatComponentTranslation("stat.playOneMinute", new Object[0]), StatBase.timeStatType).initIndependentStat().registerStat();
        StatList.timeSinceDeathStat = new StatBasic("stat.timeSinceDeath", new ChatComponentTranslation("stat.timeSinceDeath", new Object[0]), StatBase.timeStatType).initIndependentStat().registerStat();
        StatList.distanceWalkedStat = new StatBasic("stat.walkOneCm", new ChatComponentTranslation("stat.walkOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
        StatList.distanceCrouchedStat = new StatBasic("stat.crouchOneCm", new ChatComponentTranslation("stat.crouchOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
        StatList.distanceSprintedStat = new StatBasic("stat.sprintOneCm", new ChatComponentTranslation("stat.sprintOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
        StatList.distanceSwumStat = new StatBasic("stat.swimOneCm", new ChatComponentTranslation("stat.swimOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
        StatList.distanceFallenStat = new StatBasic("stat.fallOneCm", new ChatComponentTranslation("stat.fallOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
        StatList.distanceClimbedStat = new StatBasic("stat.climbOneCm", new ChatComponentTranslation("stat.climbOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
        StatList.distanceFlownStat = new StatBasic("stat.flyOneCm", new ChatComponentTranslation("stat.flyOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
        StatList.distanceDoveStat = new StatBasic("stat.diveOneCm", new ChatComponentTranslation("stat.diveOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
        StatList.distanceByMinecartStat = new StatBasic("stat.minecartOneCm", new ChatComponentTranslation("stat.minecartOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
        StatList.distanceByBoatStat = new StatBasic("stat.boatOneCm", new ChatComponentTranslation("stat.boatOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
        StatList.distanceByPigStat = new StatBasic("stat.pigOneCm", new ChatComponentTranslation("stat.pigOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
        StatList.distanceByHorseStat = new StatBasic("stat.horseOneCm", new ChatComponentTranslation("stat.horseOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
        StatList.jumpStat = new StatBasic("stat.jump", new ChatComponentTranslation("stat.jump", new Object[0])).initIndependentStat().registerStat();
        StatList.dropStat = new StatBasic("stat.drop", new ChatComponentTranslation("stat.drop", new Object[0])).initIndependentStat().registerStat();
        StatList.damageDealtStat = new StatBasic("stat.damageDealt", new ChatComponentTranslation("stat.damageDealt", new Object[0]), StatBase.field_111202_k).registerStat();
        StatList.damageTakenStat = new StatBasic("stat.damageTaken", new ChatComponentTranslation("stat.damageTaken", new Object[0]), StatBase.field_111202_k).registerStat();
        StatList.deathsStat = new StatBasic("stat.deaths", new ChatComponentTranslation("stat.deaths", new Object[0])).registerStat();
        StatList.mobKillsStat = new StatBasic("stat.mobKills", new ChatComponentTranslation("stat.mobKills", new Object[0])).registerStat();
        StatList.animalsBredStat = new StatBasic("stat.animalsBred", new ChatComponentTranslation("stat.animalsBred", new Object[0])).registerStat();
        StatList.playerKillsStat = new StatBasic("stat.playerKills", new ChatComponentTranslation("stat.playerKills", new Object[0])).registerStat();
        StatList.fishCaughtStat = new StatBasic("stat.fishCaught", new ChatComponentTranslation("stat.fishCaught", new Object[0])).registerStat();
        StatList.junkFishedStat = new StatBasic("stat.junkFished", new ChatComponentTranslation("stat.junkFished", new Object[0])).registerStat();
        StatList.treasureFishedStat = new StatBasic("stat.treasureFished", new ChatComponentTranslation("stat.treasureFished", new Object[0])).registerStat();
        StatList.timesTalkedToVillagerStat = new StatBasic("stat.talkedToVillager", new ChatComponentTranslation("stat.talkedToVillager", new Object[0])).registerStat();
        StatList.timesTradedWithVillagerStat = new StatBasic("stat.tradedWithVillager", new ChatComponentTranslation("stat.tradedWithVillager", new Object[0])).registerStat();
        mineBlockStatArray = new StatBase[4096];
        objectCraftStats = new StatBase[32000];
        objectUseStats = new StatBase[32000];
        objectBreakStats = new StatBase[32000];
    }
}
