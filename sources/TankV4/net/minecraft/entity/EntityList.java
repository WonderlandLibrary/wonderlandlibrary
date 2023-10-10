package net.minecraft.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
   public static final Map entityEggs = Maps.newLinkedHashMap();
   private static final Logger logger = LogManager.getLogger();
   private static final Map classToIDMapping = Maps.newHashMap();
   private static final Map idToClassMapping = Maps.newHashMap();
   private static final Map classToStringMapping = Maps.newHashMap();
   private static final Map stringToIDMapping = Maps.newHashMap();
   private static final Map stringToClassMapping = Maps.newHashMap();

   private static void addMapping(Class var0, String var1, int var2, int var3, int var4) {
      addMapping(var0, var1, var2);
      entityEggs.put(var2, new EntityList.EntityEggInfo(var2, var3, var4));
   }

   public static Entity createEntityByID(int var0, World var1) {
      Entity var2 = null;

      try {
         Class var3 = getClassFromID(var0);
         if (var3 != null) {
            var2 = (Entity)var3.getConstructor(World.class).newInstance(var1);
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      if (var2 == null) {
         logger.warn("Skipping Entity with id " + var0);
      }

      return var2;
   }

   public static String getEntityString(Entity var0) {
      return (String)classToStringMapping.get(var0.getClass());
   }

   public static boolean isStringEntityName(Entity var0, String var1) {
      String var2 = getEntityString(var0);
      if (var2 == null && var0 instanceof EntityPlayer) {
         var2 = "Player";
      } else if (var2 == null && var0 instanceof EntityLightningBolt) {
         var2 = "LightningBolt";
      }

      return var1.equals(var2);
   }

   public static int getIDFromString(String var0) {
      Integer var1 = (Integer)stringToIDMapping.get(var0);
      return var1 == null ? 90 : var1;
   }

   static {
      addMapping(EntityItem.class, "Item", 1);
      addMapping(EntityXPOrb.class, "XPOrb", 2);
      addMapping(EntityEgg.class, "ThrownEgg", 7);
      addMapping(EntityLeashKnot.class, "LeashKnot", 8);
      addMapping(EntityPainting.class, "Painting", 9);
      addMapping(EntityArrow.class, "Arrow", 10);
      addMapping(EntitySnowball.class, "Snowball", 11);
      addMapping(EntityLargeFireball.class, "Fireball", 12);
      addMapping(EntitySmallFireball.class, "SmallFireball", 13);
      addMapping(EntityEnderPearl.class, "ThrownEnderpearl", 14);
      addMapping(EntityEnderEye.class, "EyeOfEnderSignal", 15);
      addMapping(EntityPotion.class, "ThrownPotion", 16);
      addMapping(EntityExpBottle.class, "ThrownExpBottle", 17);
      addMapping(EntityItemFrame.class, "ItemFrame", 18);
      addMapping(EntityWitherSkull.class, "WitherSkull", 19);
      addMapping(EntityTNTPrimed.class, "PrimedTnt", 20);
      addMapping(EntityFallingBlock.class, "FallingSand", 21);
      addMapping(EntityFireworkRocket.class, "FireworksRocketEntity", 22);
      addMapping(EntityArmorStand.class, "ArmorStand", 30);
      addMapping(EntityBoat.class, "Boat", 41);
      addMapping(EntityMinecartEmpty.class, EntityMinecart.EnumMinecartType.RIDEABLE.getName(), 42);
      addMapping(EntityMinecartChest.class, EntityMinecart.EnumMinecartType.CHEST.getName(), 43);
      addMapping(EntityMinecartFurnace.class, EntityMinecart.EnumMinecartType.FURNACE.getName(), 44);
      addMapping(EntityMinecartTNT.class, EntityMinecart.EnumMinecartType.TNT.getName(), 45);
      addMapping(EntityMinecartHopper.class, EntityMinecart.EnumMinecartType.HOPPER.getName(), 46);
      addMapping(EntityMinecartMobSpawner.class, EntityMinecart.EnumMinecartType.SPAWNER.getName(), 47);
      addMapping(EntityMinecartCommandBlock.class, EntityMinecart.EnumMinecartType.COMMAND_BLOCK.getName(), 40);
      addMapping(EntityLiving.class, "Mob", 48);
      addMapping(EntityMob.class, "Monster", 49);
      addMapping(EntityCreeper.class, "Creeper", 50, 894731, 0);
      addMapping(EntitySkeleton.class, "Skeleton", 51, 12698049, 4802889);
      addMapping(EntitySpider.class, "Spider", 52, 3419431, 11013646);
      addMapping(EntityGiantZombie.class, "Giant", 53);
      addMapping(EntityZombie.class, "Zombie", 54, 44975, 7969893);
      addMapping(EntitySlime.class, "Slime", 55, 5349438, 8306542);
      addMapping(EntityGhast.class, "Ghast", 56, 16382457, 12369084);
      addMapping(EntityPigZombie.class, "PigZombie", 57, 15373203, 5009705);
      addMapping(EntityEnderman.class, "Enderman", 58, 1447446, 0);
      addMapping(EntityCaveSpider.class, "CaveSpider", 59, 803406, 11013646);
      addMapping(EntitySilverfish.class, "Silverfish", 60, 7237230, 3158064);
      addMapping(EntityBlaze.class, "Blaze", 61, 16167425, 16775294);
      addMapping(EntityMagmaCube.class, "LavaSlime", 62, 3407872, 16579584);
      addMapping(EntityDragon.class, "EnderDragon", 63);
      addMapping(EntityWither.class, "WitherBoss", 64);
      addMapping(EntityBat.class, "Bat", 65, 4996656, 986895);
      addMapping(EntityWitch.class, "Witch", 66, 3407872, 5349438);
      addMapping(EntityEndermite.class, "Endermite", 67, 1447446, 7237230);
      addMapping(EntityGuardian.class, "Guardian", 68, 5931634, 15826224);
      addMapping(EntityPig.class, "Pig", 90, 15771042, 14377823);
      addMapping(EntitySheep.class, "Sheep", 91, 15198183, 16758197);
      addMapping(EntityCow.class, "Cow", 92, 4470310, 10592673);
      addMapping(EntityChicken.class, "Chicken", 93, 10592673, 16711680);
      addMapping(EntitySquid.class, "Squid", 94, 2243405, 7375001);
      addMapping(EntityWolf.class, "Wolf", 95, 14144467, 13545366);
      addMapping(EntityMooshroom.class, "MushroomCow", 96, 10489616, 12040119);
      addMapping(EntitySnowman.class, "SnowMan", 97);
      addMapping(EntityOcelot.class, "Ozelot", 98, 15720061, 5653556);
      addMapping(EntityIronGolem.class, "VillagerGolem", 99);
      addMapping(EntityHorse.class, "EntityHorse", 100, 12623485, 15656192);
      addMapping(EntityRabbit.class, "Rabbit", 101, 10051392, 7555121);
      addMapping(EntityVillager.class, "Villager", 120, 5651507, 12422002);
      addMapping(EntityEnderCrystal.class, "EnderCrystal", 200);
   }

   public static Entity createEntityByName(String var0, World var1) {
      Entity var2 = null;

      try {
         Class var3 = (Class)stringToClassMapping.get(var0);
         if (var3 != null) {
            var2 = (Entity)var3.getConstructor(World.class).newInstance(var1);
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return var2;
   }

   public static String getStringFromID(int var0) {
      return (String)classToStringMapping.get(getClassFromID(var0));
   }

   public static boolean isStringValidEntityName(String var0) {
      return "Player".equals(var0) || getEntityNameList().contains(var0);
   }

   public static Entity createEntityFromNBT(NBTTagCompound var0, World var1) {
      Entity var2 = null;
      if ("Minecart".equals(var0.getString("id"))) {
         var0.setString("id", EntityMinecart.EnumMinecartType.byNetworkID(var0.getInteger("Type")).getName());
         var0.removeTag("Type");
      }

      try {
         Class var3 = (Class)stringToClassMapping.get(var0.getString("id"));
         if (var3 != null) {
            var2 = (Entity)var3.getConstructor(World.class).newInstance(var1);
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      if (var2 != null) {
         var2.readFromNBT(var0);
      } else {
         logger.warn("Skipping Entity with id " + var0.getString("id"));
      }

      return var2;
   }

   public static List getEntityNameList() {
      Set var0 = stringToClassMapping.keySet();
      ArrayList var1 = Lists.newArrayList();
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         String var2 = (String)var3.next();
         Class var4 = (Class)stringToClassMapping.get(var2);
         if ((var4.getModifiers() & 1024) != 1024) {
            var1.add(var2);
         }
      }

      var1.add("LightningBolt");
      return var1;
   }

   private static void addMapping(Class var0, String var1, int var2) {
      if (stringToClassMapping.containsKey(var1)) {
         throw new IllegalArgumentException("ID is already registered: " + var1);
      } else if (idToClassMapping.containsKey(var2)) {
         throw new IllegalArgumentException("ID is already registered: " + var2);
      } else if (var2 == 0) {
         throw new IllegalArgumentException("Cannot register to reserved id: " + var2);
      } else if (var0 == null) {
         throw new IllegalArgumentException("Cannot register null clazz for id: " + var2);
      } else {
         stringToClassMapping.put(var1, var0);
         classToStringMapping.put(var0, var1);
         idToClassMapping.put(var2, var0);
         classToIDMapping.put(var0, var2);
         stringToIDMapping.put(var1, var2);
      }
   }

   public static void func_151514_a() {
   }

   public static Class getClassFromID(int var0) {
      return (Class)idToClassMapping.get(var0);
   }

   public static int getEntityID(Entity var0) {
      Integer var1 = (Integer)classToIDMapping.get(var0.getClass());
      return var1 == null ? 0 : var1;
   }

   public static class EntityEggInfo {
      public final int secondaryColor;
      public final int spawnedID;
      public final StatBase field_151512_d;
      public final int primaryColor;
      public final StatBase field_151513_e;

      public EntityEggInfo(int var1, int var2, int var3) {
         this.spawnedID = var1;
         this.primaryColor = var2;
         this.secondaryColor = var3;
         this.field_151512_d = StatList.getStatKillEntity(this);
         this.field_151513_e = StatList.getStatEntityKilledBy(this);
      }
   }
}
