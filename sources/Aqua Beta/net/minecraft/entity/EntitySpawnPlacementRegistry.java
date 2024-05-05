package net.minecraft.entity;

import com.google.common.collect.Maps;
import java.util.HashMap;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;

public class EntitySpawnPlacementRegistry {
    private static final HashMap<Class, EntityLiving.SpawnPlacementType> ENTITY_PLACEMENTS = Maps.newHashMap();

    public static EntityLiving.SpawnPlacementType getPlacementForEntity(Class entityClass) {
        return (EntityLiving.SpawnPlacementType)ENTITY_PLACEMENTS.get((Object)entityClass);
    }

    static {
        ENTITY_PLACEMENTS.put(EntityBat.class, (Object)EntityLiving.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(EntityChicken.class, (Object)EntityLiving.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(EntityCow.class, (Object)EntityLiving.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(EntityHorse.class, (Object)EntityLiving.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(EntityMooshroom.class, (Object)EntityLiving.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(EntityOcelot.class, (Object)EntityLiving.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(EntityPig.class, (Object)EntityLiving.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(EntityRabbit.class, (Object)EntityLiving.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(EntitySheep.class, (Object)EntityLiving.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(EntitySnowman.class, (Object)EntityLiving.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(EntitySquid.class, (Object)EntityLiving.SpawnPlacementType.IN_WATER);
        ENTITY_PLACEMENTS.put(EntityIronGolem.class, (Object)EntityLiving.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(EntityWolf.class, (Object)EntityLiving.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(EntityVillager.class, (Object)EntityLiving.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(EntityDragon.class, (Object)EntityLiving.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(EntityWither.class, (Object)EntityLiving.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(EntityBlaze.class, (Object)EntityLiving.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(EntityCaveSpider.class, (Object)EntityLiving.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(EntityCreeper.class, (Object)EntityLiving.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(EntityEnderman.class, (Object)EntityLiving.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(EntityEndermite.class, (Object)EntityLiving.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(EntityGhast.class, (Object)EntityLiving.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(EntityGiantZombie.class, (Object)EntityLiving.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(EntityGuardian.class, (Object)EntityLiving.SpawnPlacementType.IN_WATER);
        ENTITY_PLACEMENTS.put(EntityMagmaCube.class, (Object)EntityLiving.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(EntityPigZombie.class, (Object)EntityLiving.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(EntitySilverfish.class, (Object)EntityLiving.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(EntitySkeleton.class, (Object)EntityLiving.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(EntitySlime.class, (Object)EntityLiving.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(EntitySpider.class, (Object)EntityLiving.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(EntityWitch.class, (Object)EntityLiving.SpawnPlacementType.ON_GROUND);
        ENTITY_PLACEMENTS.put(EntityZombie.class, (Object)EntityLiving.SpawnPlacementType.ON_GROUND);
    }
}
