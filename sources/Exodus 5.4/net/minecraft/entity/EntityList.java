/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.entity.ai.EntityMinecartMobSpawner;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
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
import net.minecraft.entity.monster.EntityMob;
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityList {
    private static final Map<Class<? extends Entity>, Integer> classToIDMapping;
    public static final Map<Integer, EntityEggInfo> entityEggs;
    private static final Map<Integer, Class<? extends Entity>> idToClassMapping;
    private static final Map<String, Integer> stringToIDMapping;
    private static final Logger logger;
    private static final Map<Class<? extends Entity>, String> classToStringMapping;
    private static final Map<String, Class<? extends Entity>> stringToClassMapping;

    static {
        logger = LogManager.getLogger();
        stringToClassMapping = Maps.newHashMap();
        classToStringMapping = Maps.newHashMap();
        idToClassMapping = Maps.newHashMap();
        classToIDMapping = Maps.newHashMap();
        stringToIDMapping = Maps.newHashMap();
        entityEggs = Maps.newLinkedHashMap();
        EntityList.addMapping(EntityItem.class, "Item", 1);
        EntityList.addMapping(EntityXPOrb.class, "XPOrb", 2);
        EntityList.addMapping(EntityEgg.class, "ThrownEgg", 7);
        EntityList.addMapping(EntityLeashKnot.class, "LeashKnot", 8);
        EntityList.addMapping(EntityPainting.class, "Painting", 9);
        EntityList.addMapping(EntityArrow.class, "Arrow", 10);
        EntityList.addMapping(EntitySnowball.class, "Snowball", 11);
        EntityList.addMapping(EntityLargeFireball.class, "Fireball", 12);
        EntityList.addMapping(EntitySmallFireball.class, "SmallFireball", 13);
        EntityList.addMapping(EntityEnderPearl.class, "ThrownEnderpearl", 14);
        EntityList.addMapping(EntityEnderEye.class, "EyeOfEnderSignal", 15);
        EntityList.addMapping(EntityPotion.class, "ThrownPotion", 16);
        EntityList.addMapping(EntityExpBottle.class, "ThrownExpBottle", 17);
        EntityList.addMapping(EntityItemFrame.class, "ItemFrame", 18);
        EntityList.addMapping(EntityWitherSkull.class, "WitherSkull", 19);
        EntityList.addMapping(EntityTNTPrimed.class, "PrimedTnt", 20);
        EntityList.addMapping(EntityFallingBlock.class, "FallingSand", 21);
        EntityList.addMapping(EntityFireworkRocket.class, "FireworksRocketEntity", 22);
        EntityList.addMapping(EntityArmorStand.class, "ArmorStand", 30);
        EntityList.addMapping(EntityBoat.class, "Boat", 41);
        EntityList.addMapping(EntityMinecartEmpty.class, EntityMinecart.EnumMinecartType.RIDEABLE.getName(), 42);
        EntityList.addMapping(EntityMinecartChest.class, EntityMinecart.EnumMinecartType.CHEST.getName(), 43);
        EntityList.addMapping(EntityMinecartFurnace.class, EntityMinecart.EnumMinecartType.FURNACE.getName(), 44);
        EntityList.addMapping(EntityMinecartTNT.class, EntityMinecart.EnumMinecartType.TNT.getName(), 45);
        EntityList.addMapping(EntityMinecartHopper.class, EntityMinecart.EnumMinecartType.HOPPER.getName(), 46);
        EntityList.addMapping(EntityMinecartMobSpawner.class, EntityMinecart.EnumMinecartType.SPAWNER.getName(), 47);
        EntityList.addMapping(EntityMinecartCommandBlock.class, EntityMinecart.EnumMinecartType.COMMAND_BLOCK.getName(), 40);
        EntityList.addMapping(EntityLiving.class, "Mob", 48);
        EntityList.addMapping(EntityMob.class, "Monster", 49);
        EntityList.addMapping(EntityCreeper.class, "Creeper", 50, 894731, 0);
        EntityList.addMapping(EntitySkeleton.class, "Skeleton", 51, 0xC1C1C1, 0x494949);
        EntityList.addMapping(EntitySpider.class, "Spider", 52, 3419431, 11013646);
        EntityList.addMapping(EntityGiantZombie.class, "Giant", 53);
        EntityList.addMapping(EntityZombie.class, "Zombie", 54, 44975, 7969893);
        EntityList.addMapping(EntitySlime.class, "Slime", 55, 5349438, 8306542);
        EntityList.addMapping(EntityGhast.class, "Ghast", 56, 0xF9F9F9, 0xBCBCBC);
        EntityList.addMapping(EntityPigZombie.class, "PigZombie", 57, 15373203, 5009705);
        EntityList.addMapping(EntityEnderman.class, "Enderman", 58, 0x161616, 0);
        EntityList.addMapping(EntityCaveSpider.class, "CaveSpider", 59, 803406, 11013646);
        EntityList.addMapping(EntitySilverfish.class, "Silverfish", 60, 0x6E6E6E, 0x303030);
        EntityList.addMapping(EntityBlaze.class, "Blaze", 61, 16167425, 16775294);
        EntityList.addMapping(EntityMagmaCube.class, "LavaSlime", 62, 0x340000, 0xFCFC00);
        EntityList.addMapping(EntityDragon.class, "EnderDragon", 63);
        EntityList.addMapping(EntityWither.class, "WitherBoss", 64);
        EntityList.addMapping(EntityBat.class, "Bat", 65, 4996656, 986895);
        EntityList.addMapping(EntityWitch.class, "Witch", 66, 0x340000, 5349438);
        EntityList.addMapping(EntityEndermite.class, "Endermite", 67, 0x161616, 0x6E6E6E);
        EntityList.addMapping(EntityGuardian.class, "Guardian", 68, 5931634, 15826224);
        EntityList.addMapping(EntityPig.class, "Pig", 90, 15771042, 14377823);
        EntityList.addMapping(EntitySheep.class, "Sheep", 91, 0xE7E7E7, 0xFFB5B5);
        EntityList.addMapping(EntityCow.class, "Cow", 92, 4470310, 0xA1A1A1);
        EntityList.addMapping(EntityChicken.class, "Chicken", 93, 0xA1A1A1, 0xFF0000);
        EntityList.addMapping(EntitySquid.class, "Squid", 94, 2243405, 7375001);
        EntityList.addMapping(EntityWolf.class, "Wolf", 95, 0xD7D3D3, 13545366);
        EntityList.addMapping(EntityMooshroom.class, "MushroomCow", 96, 10489616, 0xB7B7B7);
        EntityList.addMapping(EntitySnowman.class, "SnowMan", 97);
        EntityList.addMapping(EntityOcelot.class, "Ozelot", 98, 15720061, 5653556);
        EntityList.addMapping(EntityIronGolem.class, "VillagerGolem", 99);
        EntityList.addMapping(EntityHorse.class, "EntityHorse", 100, 12623485, 0xEEE500);
        EntityList.addMapping(EntityRabbit.class, "Rabbit", 101, 10051392, 7555121);
        EntityList.addMapping(EntityVillager.class, "Villager", 120, 5651507, 12422002);
        EntityList.addMapping(EntityEnderCrystal.class, "EnderCrystal", 200);
    }

    public static String getStringFromID(int n) {
        return classToStringMapping.get(EntityList.getClassFromID(n));
    }

    public static void func_151514_a() {
    }

    public static String getEntityString(Entity entity) {
        return classToStringMapping.get(entity.getClass());
    }

    public static Class<? extends Entity> getClassFromID(int n) {
        return idToClassMapping.get(n);
    }

    private static void addMapping(Class<? extends Entity> clazz, String string, int n, int n2, int n3) {
        EntityList.addMapping(clazz, string, n);
        entityEggs.put(n, new EntityEggInfo(n, n2, n3));
    }

    public static int getIDFromString(String string) {
        Integer n = stringToIDMapping.get(string);
        return n == null ? 90 : n;
    }

    public static int getEntityID(Entity entity) {
        Integer n = classToIDMapping.get(entity.getClass());
        return n == null ? 0 : n;
    }

    public static Entity createEntityByID(int n, World world) {
        Entity entity = null;
        try {
            Class<? extends Entity> clazz = EntityList.getClassFromID(n);
            if (clazz != null) {
                entity = clazz.getConstructor(World.class).newInstance(world);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        if (entity == null) {
            logger.warn("Skipping Entity with id " + n);
        }
        return entity;
    }

    public static Entity createEntityFromNBT(NBTTagCompound nBTTagCompound, World world) {
        Entity entity = null;
        if ("Minecart".equals(nBTTagCompound.getString("id"))) {
            nBTTagCompound.setString("id", EntityMinecart.EnumMinecartType.byNetworkID(nBTTagCompound.getInteger("Type")).getName());
            nBTTagCompound.removeTag("Type");
        }
        try {
            Class<? extends Entity> clazz = stringToClassMapping.get(nBTTagCompound.getString("id"));
            if (clazz != null) {
                entity = clazz.getConstructor(World.class).newInstance(world);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        if (entity != null) {
            entity.readFromNBT(nBTTagCompound);
        } else {
            logger.warn("Skipping Entity with id " + nBTTagCompound.getString("id"));
        }
        return entity;
    }

    public static boolean isStringEntityName(Entity entity, String string) {
        String string2 = EntityList.getEntityString(entity);
        if (string2 == null && entity instanceof EntityPlayer) {
            string2 = "Player";
        } else if (string2 == null && entity instanceof EntityLightningBolt) {
            string2 = "LightningBolt";
        }
        return string.equals(string2);
    }

    public static List<String> getEntityNameList() {
        Set<String> set = stringToClassMapping.keySet();
        ArrayList arrayList = Lists.newArrayList();
        for (String string : set) {
            Class<? extends Entity> clazz = stringToClassMapping.get(string);
            if ((clazz.getModifiers() & 0x400) == 1024) continue;
            arrayList.add(string);
        }
        arrayList.add("LightningBolt");
        return arrayList;
    }

    private static void addMapping(Class<? extends Entity> clazz, String string, int n) {
        if (stringToClassMapping.containsKey(string)) {
            throw new IllegalArgumentException("ID is already registered: " + string);
        }
        if (idToClassMapping.containsKey(n)) {
            throw new IllegalArgumentException("ID is already registered: " + n);
        }
        if (n == 0) {
            throw new IllegalArgumentException("Cannot register to reserved id: " + n);
        }
        if (clazz == null) {
            throw new IllegalArgumentException("Cannot register null clazz for id: " + n);
        }
        stringToClassMapping.put(string, clazz);
        classToStringMapping.put(clazz, string);
        idToClassMapping.put(n, clazz);
        classToIDMapping.put(clazz, n);
        stringToIDMapping.put(string, n);
    }

    public static Entity createEntityByName(String string, World world) {
        Entity entity = null;
        try {
            Class<? extends Entity> clazz = stringToClassMapping.get(string);
            if (clazz != null) {
                entity = clazz.getConstructor(World.class).newInstance(world);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return entity;
    }

    public static boolean isStringValidEntityName(String string) {
        return "Player".equals(string) || EntityList.getEntityNameList().contains(string);
    }

    public static class EntityEggInfo {
        public final int secondaryColor;
        public final int primaryColor;
        public final StatBase field_151512_d;
        public final int spawnedID;
        public final StatBase field_151513_e;

        public EntityEggInfo(int n, int n2, int n3) {
            this.spawnedID = n;
            this.primaryColor = n2;
            this.secondaryColor = n3;
            this.field_151512_d = StatList.getStatKillEntity(this);
            this.field_151513_e = StatList.getStatEntityKilledBy(this);
        }
    }
}

