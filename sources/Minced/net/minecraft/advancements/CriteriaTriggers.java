// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.advancements;

import com.google.common.collect.Maps;
import javax.annotation.Nullable;
import net.minecraft.advancements.critereon.NetherTravelTrigger;
import net.minecraft.advancements.critereon.UsedTotemTrigger;
import net.minecraft.advancements.critereon.EffectsChangedTrigger;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.advancements.critereon.PlacedBlockTrigger;
import net.minecraft.advancements.critereon.TameAnimalTrigger;
import net.minecraft.advancements.critereon.TickTrigger;
import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
import net.minecraft.advancements.critereon.LevitationTrigger;
import net.minecraft.advancements.critereon.ItemDurabilityTrigger;
import net.minecraft.advancements.critereon.VillagerTradeTrigger;
import net.minecraft.advancements.critereon.CuredZombieVillagerTrigger;
import net.minecraft.advancements.critereon.PositionTrigger;
import net.minecraft.advancements.critereon.BredAnimalsTrigger;
import net.minecraft.advancements.critereon.SummonedEntityTrigger;
import net.minecraft.advancements.critereon.UsedEnderEyeTrigger;
import net.minecraft.advancements.critereon.ConstructBeaconTrigger;
import net.minecraft.advancements.critereon.BrewedPotionTrigger;
import net.minecraft.advancements.critereon.EnchantedItemTrigger;
import net.minecraft.advancements.critereon.EntityHurtPlayerTrigger;
import net.minecraft.advancements.critereon.PlayerHurtEntityTrigger;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.EnterBlockTrigger;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.util.ResourceLocation;
import java.util.Map;

public class CriteriaTriggers
{
    private static final Map<ResourceLocation, ICriterionTrigger<?>> REGISTRY;
    public static final ImpossibleTrigger IMPOSSIBLE;
    public static final KilledTrigger PLAYER_KILLED_ENTITY;
    public static final KilledTrigger ENTITY_KILLED_PLAYER;
    public static final EnterBlockTrigger ENTER_BLOCK;
    public static final InventoryChangeTrigger INVENTORY_CHANGED;
    public static final RecipeUnlockedTrigger RECIPE_UNLOCKED;
    public static final PlayerHurtEntityTrigger PLAYER_HURT_ENTITY;
    public static final EntityHurtPlayerTrigger ENTITY_HURT_PLAYER;
    public static final EnchantedItemTrigger ENCHANTED_ITEM;
    public static final BrewedPotionTrigger BREWED_POTION;
    public static final ConstructBeaconTrigger CONSTRUCT_BEACON;
    public static final UsedEnderEyeTrigger USED_ENDER_EYE;
    public static final SummonedEntityTrigger SUMMONED_ENTITY;
    public static final BredAnimalsTrigger BRED_ANIMALS;
    public static final PositionTrigger LOCATION;
    public static final PositionTrigger SLEPT_IN_BED;
    public static final CuredZombieVillagerTrigger CURED_ZOMBIE_VILLAGER;
    public static final VillagerTradeTrigger VILLAGER_TRADE;
    public static final ItemDurabilityTrigger ITEM_DURABILITY_CHANGED;
    public static final LevitationTrigger LEVITATION;
    public static final ChangeDimensionTrigger CHANGED_DIMENSION;
    public static final TickTrigger TICK;
    public static final TameAnimalTrigger TAME_ANIMAL;
    public static final PlacedBlockTrigger PLACED_BLOCK;
    public static final ConsumeItemTrigger CONSUME_ITEM;
    public static final EffectsChangedTrigger EFFECTS_CHANGED;
    public static final UsedTotemTrigger USED_TOTEM;
    public static final NetherTravelTrigger NETHER_TRAVEL;
    
    private static <T extends ICriterionTrigger> T register(final T criterion) {
        if (CriteriaTriggers.REGISTRY.containsKey(criterion.getId())) {
            throw new IllegalArgumentException("Duplicate criterion id " + criterion.getId());
        }
        CriteriaTriggers.REGISTRY.put(criterion.getId(), criterion);
        return criterion;
    }
    
    @Nullable
    public static <T extends ICriterionInstance> ICriterionTrigger<T> get(final ResourceLocation id) {
        return (ICriterionTrigger<T>)CriteriaTriggers.REGISTRY.get(id);
    }
    
    public static Iterable<? extends ICriterionTrigger<?>> getAll() {
        return CriteriaTriggers.REGISTRY.values();
    }
    
    static {
        REGISTRY = Maps.newHashMap();
        IMPOSSIBLE = register(new ImpossibleTrigger());
        PLAYER_KILLED_ENTITY = register(new KilledTrigger(new ResourceLocation("player_killed_entity")));
        ENTITY_KILLED_PLAYER = register(new KilledTrigger(new ResourceLocation("entity_killed_player")));
        ENTER_BLOCK = register(new EnterBlockTrigger());
        INVENTORY_CHANGED = register(new InventoryChangeTrigger());
        RECIPE_UNLOCKED = register(new RecipeUnlockedTrigger());
        PLAYER_HURT_ENTITY = register(new PlayerHurtEntityTrigger());
        ENTITY_HURT_PLAYER = register(new EntityHurtPlayerTrigger());
        ENCHANTED_ITEM = register(new EnchantedItemTrigger());
        BREWED_POTION = register(new BrewedPotionTrigger());
        CONSTRUCT_BEACON = register(new ConstructBeaconTrigger());
        USED_ENDER_EYE = register(new UsedEnderEyeTrigger());
        SUMMONED_ENTITY = register(new SummonedEntityTrigger());
        BRED_ANIMALS = register(new BredAnimalsTrigger());
        LOCATION = register(new PositionTrigger(new ResourceLocation("location")));
        SLEPT_IN_BED = register(new PositionTrigger(new ResourceLocation("slept_in_bed")));
        CURED_ZOMBIE_VILLAGER = register(new CuredZombieVillagerTrigger());
        VILLAGER_TRADE = register(new VillagerTradeTrigger());
        ITEM_DURABILITY_CHANGED = register(new ItemDurabilityTrigger());
        LEVITATION = register(new LevitationTrigger());
        CHANGED_DIMENSION = register(new ChangeDimensionTrigger());
        TICK = register(new TickTrigger());
        TAME_ANIMAL = register(new TameAnimalTrigger());
        PLACED_BLOCK = register(new PlacedBlockTrigger());
        CONSUME_ITEM = register(new ConsumeItemTrigger());
        EFFECTS_CHANGED = register(new EffectsChangedTrigger());
        USED_TOTEM = register(new UsedTotemTrigger());
        NETHER_TRAVEL = register(new NetherTravelTrigger());
    }
}
