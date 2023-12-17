// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.stats;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.ItemBlock;
import net.minecraft.init.Items;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.CraftingManager;
import com.google.common.collect.Sets;
import net.minecraft.item.Item;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import java.util.List;
import java.util.Map;

public class StatList
{
    protected static final Map<String, StatBase> ID_TO_STAT_MAP;
    public static final List<StatBase> ALL_STATS;
    public static final List<StatBase> BASIC_STATS;
    public static final List<StatCrafting> USE_ITEM_STATS;
    public static final List<StatCrafting> MINE_BLOCK_STATS;
    public static final StatBase LEAVE_GAME;
    public static final StatBase PLAY_ONE_MINUTE;
    public static final StatBase TIME_SINCE_DEATH;
    public static final StatBase SNEAK_TIME;
    public static final StatBase WALK_ONE_CM;
    public static final StatBase CROUCH_ONE_CM;
    public static final StatBase SPRINT_ONE_CM;
    public static final StatBase SWIM_ONE_CM;
    public static final StatBase FALL_ONE_CM;
    public static final StatBase CLIMB_ONE_CM;
    public static final StatBase FLY_ONE_CM;
    public static final StatBase DIVE_ONE_CM;
    public static final StatBase MINECART_ONE_CM;
    public static final StatBase BOAT_ONE_CM;
    public static final StatBase PIG_ONE_CM;
    public static final StatBase HORSE_ONE_CM;
    public static final StatBase AVIATE_ONE_CM;
    public static final StatBase JUMP;
    public static final StatBase DROP;
    public static final StatBase DAMAGE_DEALT;
    public static final StatBase DAMAGE_TAKEN;
    public static final StatBase DEATHS;
    public static final StatBase MOB_KILLS;
    public static final StatBase ANIMALS_BRED;
    public static final StatBase PLAYER_KILLS;
    public static final StatBase FISH_CAUGHT;
    public static final StatBase TALKED_TO_VILLAGER;
    public static final StatBase TRADED_WITH_VILLAGER;
    public static final StatBase CAKE_SLICES_EATEN;
    public static final StatBase CAULDRON_FILLED;
    public static final StatBase CAULDRON_USED;
    public static final StatBase ARMOR_CLEANED;
    public static final StatBase BANNER_CLEANED;
    public static final StatBase BREWINGSTAND_INTERACTION;
    public static final StatBase BEACON_INTERACTION;
    public static final StatBase DROPPER_INSPECTED;
    public static final StatBase HOPPER_INSPECTED;
    public static final StatBase DISPENSER_INSPECTED;
    public static final StatBase NOTEBLOCK_PLAYED;
    public static final StatBase NOTEBLOCK_TUNED;
    public static final StatBase FLOWER_POTTED;
    public static final StatBase TRAPPED_CHEST_TRIGGERED;
    public static final StatBase ENDERCHEST_OPENED;
    public static final StatBase ITEM_ENCHANTED;
    public static final StatBase RECORD_PLAYED;
    public static final StatBase FURNACE_INTERACTION;
    public static final StatBase CRAFTING_TABLE_INTERACTION;
    public static final StatBase CHEST_OPENED;
    public static final StatBase SLEEP_IN_BED;
    public static final StatBase OPEN_SHULKER_BOX;
    private static final StatBase[] BLOCKS_STATS;
    private static final StatBase[] CRAFTS_STATS;
    private static final StatBase[] OBJECT_USE_STATS;
    private static final StatBase[] OBJECT_BREAK_STATS;
    private static final StatBase[] OBJECTS_PICKED_UP_STATS;
    private static final StatBase[] OBJECTS_DROPPED_STATS;
    
    @Nullable
    public static StatBase getBlockStats(final Block blockIn) {
        return StatList.BLOCKS_STATS[Block.getIdFromBlock(blockIn)];
    }
    
    @Nullable
    public static StatBase getCraftStats(final Item itemIn) {
        return StatList.CRAFTS_STATS[Item.getIdFromItem(itemIn)];
    }
    
    @Nullable
    public static StatBase getObjectUseStats(final Item itemIn) {
        return StatList.OBJECT_USE_STATS[Item.getIdFromItem(itemIn)];
    }
    
    @Nullable
    public static StatBase getObjectBreakStats(final Item itemIn) {
        return StatList.OBJECT_BREAK_STATS[Item.getIdFromItem(itemIn)];
    }
    
    @Nullable
    public static StatBase getObjectsPickedUpStats(final Item itemIn) {
        return StatList.OBJECTS_PICKED_UP_STATS[Item.getIdFromItem(itemIn)];
    }
    
    @Nullable
    public static StatBase getDroppedObjectStats(final Item itemIn) {
        return StatList.OBJECTS_DROPPED_STATS[Item.getIdFromItem(itemIn)];
    }
    
    public static void init() {
        initMiningStats();
        initStats();
        initItemDepleteStats();
        initCraftableStats();
        initPickedUpAndDroppedStats();
    }
    
    private static void initCraftableStats() {
        final Set<Item> set = (Set<Item>)Sets.newHashSet();
        for (final IRecipe irecipe : CraftingManager.REGISTRY) {
            final ItemStack itemstack = irecipe.getRecipeOutput();
            if (!itemstack.isEmpty()) {
                set.add(irecipe.getRecipeOutput().getItem());
            }
        }
        for (final ItemStack itemstack2 : FurnaceRecipes.instance().getSmeltingList().values()) {
            set.add(itemstack2.getItem());
        }
        for (final Item item : set) {
            if (item != null) {
                final int i = Item.getIdFromItem(item);
                final String s = getItemName(item);
                if (s == null) {
                    continue;
                }
                StatList.CRAFTS_STATS[i] = new StatCrafting("stat.craftItem.", s, new TextComponentTranslation("stat.craftItem", new Object[] { new ItemStack(item).getTextComponent() }), item).registerStat();
            }
        }
        replaceAllSimilarBlocks(StatList.CRAFTS_STATS);
    }
    
    private static void initMiningStats() {
        for (final Block block : Block.REGISTRY) {
            final Item item = Item.getItemFromBlock(block);
            if (item != Items.AIR) {
                final int i = Block.getIdFromBlock(block);
                final String s = getItemName(item);
                if (s == null || !block.getEnableStats()) {
                    continue;
                }
                StatList.BLOCKS_STATS[i] = new StatCrafting("stat.mineBlock.", s, new TextComponentTranslation("stat.mineBlock", new Object[] { new ItemStack(block).getTextComponent() }), item).registerStat();
                StatList.MINE_BLOCK_STATS.add((StatCrafting)StatList.BLOCKS_STATS[i]);
            }
        }
        replaceAllSimilarBlocks(StatList.BLOCKS_STATS);
    }
    
    private static void initStats() {
        for (final Item item : Item.REGISTRY) {
            if (item != null) {
                final int i = Item.getIdFromItem(item);
                final String s = getItemName(item);
                if (s == null) {
                    continue;
                }
                StatList.OBJECT_USE_STATS[i] = new StatCrafting("stat.useItem.", s, new TextComponentTranslation("stat.useItem", new Object[] { new ItemStack(item).getTextComponent() }), item).registerStat();
                if (item instanceof ItemBlock) {
                    continue;
                }
                StatList.USE_ITEM_STATS.add((StatCrafting)StatList.OBJECT_USE_STATS[i]);
            }
        }
        replaceAllSimilarBlocks(StatList.OBJECT_USE_STATS);
    }
    
    private static void initItemDepleteStats() {
        for (final Item item : Item.REGISTRY) {
            if (item != null) {
                final int i = Item.getIdFromItem(item);
                final String s = getItemName(item);
                if (s == null || !item.isDamageable()) {
                    continue;
                }
                StatList.OBJECT_BREAK_STATS[i] = new StatCrafting("stat.breakItem.", s, new TextComponentTranslation("stat.breakItem", new Object[] { new ItemStack(item).getTextComponent() }), item).registerStat();
            }
        }
        replaceAllSimilarBlocks(StatList.OBJECT_BREAK_STATS);
    }
    
    private static void initPickedUpAndDroppedStats() {
        for (final Item item : Item.REGISTRY) {
            if (item != null) {
                final int i = Item.getIdFromItem(item);
                final String s = getItemName(item);
                if (s == null) {
                    continue;
                }
                StatList.OBJECTS_PICKED_UP_STATS[i] = new StatCrafting("stat.pickup.", s, new TextComponentTranslation("stat.pickup", new Object[] { new ItemStack(item).getTextComponent() }), item).registerStat();
                StatList.OBJECTS_DROPPED_STATS[i] = new StatCrafting("stat.drop.", s, new TextComponentTranslation("stat.drop", new Object[] { new ItemStack(item).getTextComponent() }), item).registerStat();
            }
        }
        replaceAllSimilarBlocks(StatList.OBJECT_BREAK_STATS);
    }
    
    private static String getItemName(final Item itemIn) {
        final ResourceLocation resourcelocation = Item.REGISTRY.getNameForObject(itemIn);
        return (resourcelocation != null) ? resourcelocation.toString().replace(':', '.') : null;
    }
    
    private static void replaceAllSimilarBlocks(final StatBase[] stat) {
        mergeStatBases(stat, Blocks.WATER, Blocks.FLOWING_WATER);
        mergeStatBases(stat, Blocks.LAVA, Blocks.FLOWING_LAVA);
        mergeStatBases(stat, Blocks.LIT_PUMPKIN, Blocks.PUMPKIN);
        mergeStatBases(stat, Blocks.LIT_FURNACE, Blocks.FURNACE);
        mergeStatBases(stat, Blocks.LIT_REDSTONE_ORE, Blocks.REDSTONE_ORE);
        mergeStatBases(stat, Blocks.POWERED_REPEATER, Blocks.UNPOWERED_REPEATER);
        mergeStatBases(stat, Blocks.POWERED_COMPARATOR, Blocks.UNPOWERED_COMPARATOR);
        mergeStatBases(stat, Blocks.REDSTONE_TORCH, Blocks.UNLIT_REDSTONE_TORCH);
        mergeStatBases(stat, Blocks.LIT_REDSTONE_LAMP, Blocks.REDSTONE_LAMP);
        mergeStatBases(stat, Blocks.DOUBLE_STONE_SLAB, Blocks.STONE_SLAB);
        mergeStatBases(stat, Blocks.DOUBLE_WOODEN_SLAB, Blocks.WOODEN_SLAB);
        mergeStatBases(stat, Blocks.DOUBLE_STONE_SLAB2, Blocks.STONE_SLAB2);
        mergeStatBases(stat, Blocks.GRASS, Blocks.DIRT);
        mergeStatBases(stat, Blocks.FARMLAND, Blocks.DIRT);
    }
    
    private static void mergeStatBases(final StatBase[] statBaseIn, final Block block1, final Block block2) {
        final int i = Block.getIdFromBlock(block1);
        final int j = Block.getIdFromBlock(block2);
        if (statBaseIn[i] != null && statBaseIn[j] == null) {
            statBaseIn[j] = statBaseIn[i];
        }
        else {
            StatList.ALL_STATS.remove(statBaseIn[i]);
            StatList.MINE_BLOCK_STATS.remove(statBaseIn[i]);
            StatList.BASIC_STATS.remove(statBaseIn[i]);
            statBaseIn[i] = statBaseIn[j];
        }
    }
    
    public static StatBase getStatKillEntity(final EntityList.EntityEggInfo eggInfo) {
        final String s = EntityList.getTranslationName(eggInfo.spawnedID);
        return (s == null) ? null : new StatBase("stat.killEntity." + s, new TextComponentTranslation("stat.entityKill", new Object[] { new TextComponentTranslation("entity." + s + ".name", new Object[0]) })).registerStat();
    }
    
    public static StatBase getStatEntityKilledBy(final EntityList.EntityEggInfo eggInfo) {
        final String s = EntityList.getTranslationName(eggInfo.spawnedID);
        return (s == null) ? null : new StatBase("stat.entityKilledBy." + s, new TextComponentTranslation("stat.entityKilledBy", new Object[] { new TextComponentTranslation("entity." + s + ".name", new Object[0]) })).registerStat();
    }
    
    @Nullable
    public static StatBase getOneShotStat(final String statName) {
        return StatList.ID_TO_STAT_MAP.get(statName);
    }
    
    static {
        ID_TO_STAT_MAP = Maps.newHashMap();
        ALL_STATS = Lists.newArrayList();
        BASIC_STATS = Lists.newArrayList();
        USE_ITEM_STATS = Lists.newArrayList();
        MINE_BLOCK_STATS = Lists.newArrayList();
        LEAVE_GAME = new StatBasic("stat.leaveGame", new TextComponentTranslation("stat.leaveGame", new Object[0])).initIndependentStat().registerStat();
        PLAY_ONE_MINUTE = new StatBasic("stat.playOneMinute", new TextComponentTranslation("stat.playOneMinute", new Object[0]), StatBase.timeStatType).initIndependentStat().registerStat();
        TIME_SINCE_DEATH = new StatBasic("stat.timeSinceDeath", new TextComponentTranslation("stat.timeSinceDeath", new Object[0]), StatBase.timeStatType).initIndependentStat().registerStat();
        SNEAK_TIME = new StatBasic("stat.sneakTime", new TextComponentTranslation("stat.sneakTime", new Object[0]), StatBase.timeStatType).initIndependentStat().registerStat();
        WALK_ONE_CM = new StatBasic("stat.walkOneCm", new TextComponentTranslation("stat.walkOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
        CROUCH_ONE_CM = new StatBasic("stat.crouchOneCm", new TextComponentTranslation("stat.crouchOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
        SPRINT_ONE_CM = new StatBasic("stat.sprintOneCm", new TextComponentTranslation("stat.sprintOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
        SWIM_ONE_CM = new StatBasic("stat.swimOneCm", new TextComponentTranslation("stat.swimOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
        FALL_ONE_CM = new StatBasic("stat.fallOneCm", new TextComponentTranslation("stat.fallOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
        CLIMB_ONE_CM = new StatBasic("stat.climbOneCm", new TextComponentTranslation("stat.climbOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
        FLY_ONE_CM = new StatBasic("stat.flyOneCm", new TextComponentTranslation("stat.flyOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
        DIVE_ONE_CM = new StatBasic("stat.diveOneCm", new TextComponentTranslation("stat.diveOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
        MINECART_ONE_CM = new StatBasic("stat.minecartOneCm", new TextComponentTranslation("stat.minecartOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
        BOAT_ONE_CM = new StatBasic("stat.boatOneCm", new TextComponentTranslation("stat.boatOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
        PIG_ONE_CM = new StatBasic("stat.pigOneCm", new TextComponentTranslation("stat.pigOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
        HORSE_ONE_CM = new StatBasic("stat.horseOneCm", new TextComponentTranslation("stat.horseOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
        AVIATE_ONE_CM = new StatBasic("stat.aviateOneCm", new TextComponentTranslation("stat.aviateOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
        JUMP = new StatBasic("stat.jump", new TextComponentTranslation("stat.jump", new Object[0])).initIndependentStat().registerStat();
        DROP = new StatBasic("stat.drop", new TextComponentTranslation("stat.drop", new Object[0])).initIndependentStat().registerStat();
        DAMAGE_DEALT = new StatBasic("stat.damageDealt", new TextComponentTranslation("stat.damageDealt", new Object[0]), StatBase.divideByTen).registerStat();
        DAMAGE_TAKEN = new StatBasic("stat.damageTaken", new TextComponentTranslation("stat.damageTaken", new Object[0]), StatBase.divideByTen).registerStat();
        DEATHS = new StatBasic("stat.deaths", new TextComponentTranslation("stat.deaths", new Object[0])).registerStat();
        MOB_KILLS = new StatBasic("stat.mobKills", new TextComponentTranslation("stat.mobKills", new Object[0])).registerStat();
        ANIMALS_BRED = new StatBasic("stat.animalsBred", new TextComponentTranslation("stat.animalsBred", new Object[0])).registerStat();
        PLAYER_KILLS = new StatBasic("stat.playerKills", new TextComponentTranslation("stat.playerKills", new Object[0])).registerStat();
        FISH_CAUGHT = new StatBasic("stat.fishCaught", new TextComponentTranslation("stat.fishCaught", new Object[0])).registerStat();
        TALKED_TO_VILLAGER = new StatBasic("stat.talkedToVillager", new TextComponentTranslation("stat.talkedToVillager", new Object[0])).registerStat();
        TRADED_WITH_VILLAGER = new StatBasic("stat.tradedWithVillager", new TextComponentTranslation("stat.tradedWithVillager", new Object[0])).registerStat();
        CAKE_SLICES_EATEN = new StatBasic("stat.cakeSlicesEaten", new TextComponentTranslation("stat.cakeSlicesEaten", new Object[0])).registerStat();
        CAULDRON_FILLED = new StatBasic("stat.cauldronFilled", new TextComponentTranslation("stat.cauldronFilled", new Object[0])).registerStat();
        CAULDRON_USED = new StatBasic("stat.cauldronUsed", new TextComponentTranslation("stat.cauldronUsed", new Object[0])).registerStat();
        ARMOR_CLEANED = new StatBasic("stat.armorCleaned", new TextComponentTranslation("stat.armorCleaned", new Object[0])).registerStat();
        BANNER_CLEANED = new StatBasic("stat.bannerCleaned", new TextComponentTranslation("stat.bannerCleaned", new Object[0])).registerStat();
        BREWINGSTAND_INTERACTION = new StatBasic("stat.brewingstandInteraction", new TextComponentTranslation("stat.brewingstandInteraction", new Object[0])).registerStat();
        BEACON_INTERACTION = new StatBasic("stat.beaconInteraction", new TextComponentTranslation("stat.beaconInteraction", new Object[0])).registerStat();
        DROPPER_INSPECTED = new StatBasic("stat.dropperInspected", new TextComponentTranslation("stat.dropperInspected", new Object[0])).registerStat();
        HOPPER_INSPECTED = new StatBasic("stat.hopperInspected", new TextComponentTranslation("stat.hopperInspected", new Object[0])).registerStat();
        DISPENSER_INSPECTED = new StatBasic("stat.dispenserInspected", new TextComponentTranslation("stat.dispenserInspected", new Object[0])).registerStat();
        NOTEBLOCK_PLAYED = new StatBasic("stat.noteblockPlayed", new TextComponentTranslation("stat.noteblockPlayed", new Object[0])).registerStat();
        NOTEBLOCK_TUNED = new StatBasic("stat.noteblockTuned", new TextComponentTranslation("stat.noteblockTuned", new Object[0])).registerStat();
        FLOWER_POTTED = new StatBasic("stat.flowerPotted", new TextComponentTranslation("stat.flowerPotted", new Object[0])).registerStat();
        TRAPPED_CHEST_TRIGGERED = new StatBasic("stat.trappedChestTriggered", new TextComponentTranslation("stat.trappedChestTriggered", new Object[0])).registerStat();
        ENDERCHEST_OPENED = new StatBasic("stat.enderchestOpened", new TextComponentTranslation("stat.enderchestOpened", new Object[0])).registerStat();
        ITEM_ENCHANTED = new StatBasic("stat.itemEnchanted", new TextComponentTranslation("stat.itemEnchanted", new Object[0])).registerStat();
        RECORD_PLAYED = new StatBasic("stat.recordPlayed", new TextComponentTranslation("stat.recordPlayed", new Object[0])).registerStat();
        FURNACE_INTERACTION = new StatBasic("stat.furnaceInteraction", new TextComponentTranslation("stat.furnaceInteraction", new Object[0])).registerStat();
        CRAFTING_TABLE_INTERACTION = new StatBasic("stat.craftingTableInteraction", new TextComponentTranslation("stat.workbenchInteraction", new Object[0])).registerStat();
        CHEST_OPENED = new StatBasic("stat.chestOpened", new TextComponentTranslation("stat.chestOpened", new Object[0])).registerStat();
        SLEEP_IN_BED = new StatBasic("stat.sleepInBed", new TextComponentTranslation("stat.sleepInBed", new Object[0])).registerStat();
        OPEN_SHULKER_BOX = new StatBasic("stat.shulkerBoxOpened", new TextComponentTranslation("stat.shulkerBoxOpened", new Object[0])).registerStat();
        BLOCKS_STATS = new StatBase[4096];
        CRAFTS_STATS = new StatBase[32000];
        OBJECT_USE_STATS = new StatBase[32000];
        OBJECT_BREAK_STATS = new StatBase[32000];
        OBJECTS_PICKED_UP_STATS = new StatBase[32000];
        OBJECTS_DROPPED_STATS = new StatBase[32000];
    }
}
