package net.minecraft.entity;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.entity.monster.EndermiteEntity;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.monster.GuardianEntity;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.entity.monster.HuskEntity;
import net.minecraft.entity.monster.MagmaCubeEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.PatrollerEntity;
import net.minecraft.entity.monster.SilverfishEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.monster.StrayEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.passive.DolphinEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.Heightmap;

public class EntitySpawnPlacementRegistry
{
    private static final Map < EntityType<?>, Entry > REGISTRY = Maps.newHashMap();

    private static <T extends MobEntity> void register(EntityType<T> entityTypeIn, PlacementType placementType, Heightmap.Type heightMapType, IPlacementPredicate<T> placementPredicate)
    {
        Entry entityspawnplacementregistry$entry = REGISTRY.put(entityTypeIn, new Entry(heightMapType, placementType, placementPredicate));

        if (entityspawnplacementregistry$entry != null)
        {
            throw new IllegalStateException("Duplicate registration for type " + Registry.ENTITY_TYPE.getKey(entityTypeIn));
        }
    }

    public static PlacementType getPlacementType(EntityType<?> entityTypeIn)
    {
        Entry entityspawnplacementregistry$entry = REGISTRY.get(entityTypeIn);
        return entityspawnplacementregistry$entry == null ? PlacementType.NO_RESTRICTIONS : entityspawnplacementregistry$entry.placementType;
    }

    public static Heightmap.Type func_209342_b(@Nullable EntityType<?> entityTypeIn)
    {
        Entry entityspawnplacementregistry$entry = REGISTRY.get(entityTypeIn);
        return entityspawnplacementregistry$entry == null ? Heightmap.Type.MOTION_BLOCKING_NO_LEAVES : entityspawnplacementregistry$entry.type;
    }

    public static <T extends Entity> boolean canSpawnEntity(EntityType<T> entityType, IServerWorld world, SpawnReason reason, BlockPos pos, Random rand)
    {
        Entry entityspawnplacementregistry$entry = REGISTRY.get(entityType);
        return entityspawnplacementregistry$entry == null || entityspawnplacementregistry$entry.placementPredicate.test((EntityType)entityType, world, reason, pos, rand);
    }

    static
    {
        register(EntityType.COD, PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AbstractFishEntity::func_223363_b);
        register(EntityType.DOLPHIN, PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, DolphinEntity::func_223364_b);
        register(EntityType.DROWNED, PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, DrownedEntity::func_223332_b);
        register(EntityType.GUARDIAN, PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GuardianEntity::func_223329_b);
        register(EntityType.PUFFERFISH, PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AbstractFishEntity::func_223363_b);
        register(EntityType.SALMON, PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AbstractFishEntity::func_223363_b);
        register(EntityType.SQUID, PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SquidEntity::func_223365_b);
        register(EntityType.TROPICAL_FISH, PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AbstractFishEntity::func_223363_b);
        register(EntityType.BAT, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, BatEntity::canSpawn);
        register(EntityType.BLAZE, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawn);
        register(EntityType.CAVE_SPIDER, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.CHICKEN, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        register(EntityType.COW, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        register(EntityType.CREEPER, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.DONKEY, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        register(EntityType.ENDERMAN, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.ENDERMITE, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EndermiteEntity::func_223328_b);
        register(EntityType.ENDER_DRAGON, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
        register(EntityType.GHAST, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GhastEntity::func_223368_b);
        register(EntityType.GIANT, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.HORSE, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        register(EntityType.HUSK, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HuskEntity::func_223334_b);
        register(EntityType.IRON_GOLEM, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
        register(EntityType.LLAMA, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        register(EntityType.MAGMA_CUBE, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MagmaCubeEntity::func_223367_b);
        register(EntityType.MOOSHROOM, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MooshroomEntity::func_223318_c);
        register(EntityType.MULE, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        register(EntityType.OCELOT, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, OcelotEntity::func_223319_c);
        register(EntityType.PARROT, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, ParrotEntity::func_223317_c);
        register(EntityType.PIG, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        register(EntityType.HOGLIN, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HoglinEntity::func_234361_c_);
        register(EntityType.PIGLIN, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, PiglinEntity::func_234418_b_);
        register(EntityType.PILLAGER, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, PatrollerEntity::func_223330_b);
        register(EntityType.POLAR_BEAR, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, PolarBearEntity::func_223320_c);
        register(EntityType.RABBIT, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, RabbitEntity::func_223321_c);
        register(EntityType.SHEEP, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        register(EntityType.SILVERFISH, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SilverfishEntity::func_223331_b);
        register(EntityType.SKELETON, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.SKELETON_HORSE, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        register(EntityType.SLIME, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SlimeEntity::func_223366_c);
        register(EntityType.SNOW_GOLEM, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
        register(EntityType.SPIDER, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.STRAY, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, StrayEntity::func_223327_b);
        register(EntityType.STRIDER, PlacementType.IN_LAVA, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, StriderEntity::func_234314_c_);
        register(EntityType.TURTLE, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, TurtleEntity::func_223322_c);
        register(EntityType.VILLAGER, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
        register(EntityType.WITCH, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.WITHER, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.WITHER_SKELETON, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.WOLF, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        register(EntityType.ZOMBIE, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.ZOMBIE_HORSE, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        register(EntityType.ZOMBIFIED_PIGLIN, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ZombifiedPiglinEntity::func_234351_b_);
        register(EntityType.ZOMBIE_VILLAGER, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.CAT, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        register(EntityType.ELDER_GUARDIAN, PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GuardianEntity::func_223329_b);
        register(EntityType.EVOKER, PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.FOX, PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        register(EntityType.ILLUSIONER, PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.PANDA, PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        register(EntityType.PHANTOM, PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
        register(EntityType.RAVAGER, PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.SHULKER, PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
        register(EntityType.TRADER_LLAMA, PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        register(EntityType.VEX, PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.VINDICATOR, PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.WANDERING_TRADER, PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
    }

    static class Entry
    {
        private final Heightmap.Type type;
        private final PlacementType placementType;
        private final IPlacementPredicate<?> placementPredicate;

        public Entry(Heightmap.Type typeIn, PlacementType placementTypeIn, IPlacementPredicate<?> placementPredicateIn)
        {
            this.type = typeIn;
            this.placementType = placementTypeIn;
            this.placementPredicate = placementPredicateIn;
        }
    }

    @FunctionalInterface
    public interface IPlacementPredicate<T extends Entity>
    {
        boolean test(EntityType<T> p_test_1_, IServerWorld p_test_2_, SpawnReason p_test_3_, BlockPos p_test_4_, Random p_test_5_);
    }

    public static enum PlacementType
    {
        ON_GROUND,
        IN_WATER,
        NO_RESTRICTIONS,
        IN_LAVA;
    }
}
