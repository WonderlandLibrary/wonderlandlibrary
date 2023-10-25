package net.minecraft.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.entity.ai.EntityMinecartMobSpawner;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
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
  private static final Logger logger = LogManager.getLogger();
  
  private static final Map<String, Class<? extends Entity>> stringToClassMapping = Maps.newHashMap();
  
  private static final Map<Class<? extends Entity>, String> classToStringMapping = Maps.newHashMap();
  
  private static final Map<Integer, Class<? extends Entity>> idToClassMapping = Maps.newHashMap();
  
  private static final Map<Class<? extends Entity>, Integer> classToIDMapping = Maps.newHashMap();
  
  private static final Map<String, Integer> stringToIDMapping = Maps.newHashMap();
  
  public static final Map<Integer, EntityEggInfo> entityEggs = Maps.newLinkedHashMap();
  
  private static void addMapping(Class<? extends Entity> entityClass, String entityName, int id) {
    if (stringToClassMapping.containsKey(entityName))
      throw new IllegalArgumentException("ID is already registered: " + entityName); 
    if (idToClassMapping.containsKey(Integer.valueOf(id)))
      throw new IllegalArgumentException("ID is already registered: " + id); 
    if (id == 0)
      throw new IllegalArgumentException("Cannot register to reserved id: " + id); 
    if (entityClass == null)
      throw new IllegalArgumentException("Cannot register null clazz for id: " + id); 
    stringToClassMapping.put(entityName, entityClass);
    classToStringMapping.put(entityClass, entityName);
    idToClassMapping.put(Integer.valueOf(id), entityClass);
    classToIDMapping.put(entityClass, Integer.valueOf(id));
    stringToIDMapping.put(entityName, Integer.valueOf(id));
  }
  
  private static void addMapping(Class<? extends Entity> entityClass, String entityName, int entityID, int baseColor, int spotColor) {
    addMapping(entityClass, entityName, entityID);
    entityEggs.put(Integer.valueOf(entityID), new EntityEggInfo(entityID, baseColor, spotColor));
  }
  
  public static Entity createEntityByName(String entityName, World worldIn) {
    Entity entity = null;
    try {
      Class<? extends Entity> oclass = stringToClassMapping.get(entityName);
      if (oclass != null)
        entity = oclass.getConstructor(new Class[] { World.class }).newInstance(new Object[] { worldIn }); 
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
    return entity;
  }
  
  public static Entity createEntityFromNBT(NBTTagCompound nbt, World worldIn) {
    Entity entity = null;
    if ("Minecart".equals(nbt.getString("id"))) {
      nbt.setString("id", EntityMinecart.EnumMinecartType.byNetworkID(nbt.getInteger("Type")).getName());
      nbt.removeTag("Type");
    } 
    try {
      Class<? extends Entity> oclass = stringToClassMapping.get(nbt.getString("id"));
      if (oclass != null)
        entity = oclass.getConstructor(new Class[] { World.class }).newInstance(new Object[] { worldIn }); 
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
    if (entity != null) {
      entity.readFromNBT(nbt);
    } else {
      logger.warn("Skipping Entity with id " + nbt.getString("id"));
    } 
    return entity;
  }
  
  public static Entity createEntityByID(int entityID, World worldIn) {
    Entity entity = null;
    try {
      Class<? extends Entity> oclass = getClassFromID(entityID);
      if (oclass != null)
        entity = oclass.getConstructor(new Class[] { World.class }).newInstance(new Object[] { worldIn }); 
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
    if (entity == null)
      logger.warn("Skipping Entity with id " + entityID); 
    return entity;
  }
  
  public static int getEntityID(Entity entityIn) {
    Integer integer = classToIDMapping.get(entityIn.getClass());
    return (integer == null) ? 0 : integer.intValue();
  }
  
  public static Class<? extends Entity> getClassFromID(int entityID) {
    return idToClassMapping.get(Integer.valueOf(entityID));
  }
  
  public static String getEntityString(Entity entityIn) {
    return classToStringMapping.get(entityIn.getClass());
  }
  
  public static int getIDFromString(String entityName) {
    Integer integer = stringToIDMapping.get(entityName);
    return (integer == null) ? 90 : integer.intValue();
  }
  
  public static String getStringFromID(int entityID) {
    return classToStringMapping.get(getClassFromID(entityID));
  }
  
  public static void func_151514_a() {}
  
  public static List<String> getEntityNameList() {
    Set<String> set = stringToClassMapping.keySet();
    List<String> list = Lists.newArrayList();
    for (String s : set) {
      Class<? extends Entity> oclass = stringToClassMapping.get(s);
      if ((oclass.getModifiers() & 0x400) != 1024)
        list.add(s); 
    } 
    list.add("LightningBolt");
    return list;
  }
  
  public static boolean isStringEntityName(Entity entityIn, String entityName) {
    String s = getEntityString(entityIn);
    if (s == null && entityIn instanceof net.minecraft.entity.player.EntityPlayer) {
      s = "Player";
    } else if (s == null && entityIn instanceof net.minecraft.entity.effect.EntityLightningBolt) {
      s = "LightningBolt";
    } 
    return entityName.equals(s);
  }
  
  public static boolean isStringValidEntityName(String entityName) {
    return ("Player".equals(entityName) || getEntityNameList().contains(entityName));
  }
  
  static {
    addMapping((Class)EntityItem.class, "Item", 1);
    addMapping((Class)EntityXPOrb.class, "XPOrb", 2);
    addMapping((Class)EntityEgg.class, "ThrownEgg", 7);
    addMapping((Class)EntityLeashKnot.class, "LeashKnot", 8);
    addMapping((Class)EntityPainting.class, "Painting", 9);
    addMapping((Class)EntityArrow.class, "Arrow", 10);
    addMapping((Class)EntitySnowball.class, "Snowball", 11);
    addMapping((Class)EntityLargeFireball.class, "Fireball", 12);
    addMapping((Class)EntitySmallFireball.class, "SmallFireball", 13);
    addMapping((Class)EntityEnderPearl.class, "ThrownEnderpearl", 14);
    addMapping((Class)EntityEnderEye.class, "EyeOfEnderSignal", 15);
    addMapping((Class)EntityPotion.class, "ThrownPotion", 16);
    addMapping((Class)EntityExpBottle.class, "ThrownExpBottle", 17);
    addMapping((Class)EntityItemFrame.class, "ItemFrame", 18);
    addMapping((Class)EntityWitherSkull.class, "WitherSkull", 19);
    addMapping((Class)EntityTNTPrimed.class, "PrimedTnt", 20);
    addMapping((Class)EntityFallingBlock.class, "FallingSand", 21);
    addMapping((Class)EntityFireworkRocket.class, "FireworksRocketEntity", 22);
    addMapping((Class)EntityArmorStand.class, "ArmorStand", 30);
    addMapping((Class)EntityBoat.class, "Boat", 41);
    addMapping((Class)EntityMinecartEmpty.class, EntityMinecart.EnumMinecartType.RIDEABLE.getName(), 42);
    addMapping((Class)EntityMinecartChest.class, EntityMinecart.EnumMinecartType.CHEST.getName(), 43);
    addMapping((Class)EntityMinecartFurnace.class, EntityMinecart.EnumMinecartType.FURNACE.getName(), 44);
    addMapping((Class)EntityMinecartTNT.class, EntityMinecart.EnumMinecartType.TNT.getName(), 45);
    addMapping((Class)EntityMinecartHopper.class, EntityMinecart.EnumMinecartType.HOPPER.getName(), 46);
    addMapping((Class)EntityMinecartMobSpawner.class, EntityMinecart.EnumMinecartType.SPAWNER.getName(), 47);
    addMapping((Class)EntityMinecartCommandBlock.class, EntityMinecart.EnumMinecartType.COMMAND_BLOCK.getName(), 40);
    addMapping((Class)EntityLiving.class, "Mob", 48);
    addMapping((Class)EntityMob.class, "Monster", 49);
    addMapping((Class)EntityCreeper.class, "Creeper", 50, 894731, 0);
    addMapping((Class)EntitySkeleton.class, "Skeleton", 51, 12698049, 4802889);
    addMapping((Class)EntitySpider.class, "Spider", 52, 3419431, 11013646);
    addMapping((Class)EntityGiantZombie.class, "Giant", 53);
    addMapping((Class)EntityZombie.class, "Zombie", 54, 44975, 7969893);
    addMapping((Class)EntitySlime.class, "Slime", 55, 5349438, 8306542);
    addMapping((Class)EntityGhast.class, "Ghast", 56, 16382457, 12369084);
    addMapping((Class)EntityPigZombie.class, "PigZombie", 57, 15373203, 5009705);
    addMapping((Class)EntityEnderman.class, "Enderman", 58, 1447446, 0);
    addMapping((Class)EntityCaveSpider.class, "CaveSpider", 59, 803406, 11013646);
    addMapping((Class)EntitySilverfish.class, "Silverfish", 60, 7237230, 3158064);
    addMapping((Class)EntityBlaze.class, "Blaze", 61, 16167425, 16775294);
    addMapping((Class)EntityMagmaCube.class, "LavaSlime", 62, 3407872, 16579584);
    addMapping((Class)EntityDragon.class, "EnderDragon", 63);
    addMapping((Class)EntityWither.class, "WitherBoss", 64);
    addMapping((Class)EntityBat.class, "Bat", 65, 4996656, 986895);
    addMapping((Class)EntityWitch.class, "Witch", 66, 3407872, 5349438);
    addMapping((Class)EntityEndermite.class, "Endermite", 67, 1447446, 7237230);
    addMapping((Class)EntityGuardian.class, "Guardian", 68, 5931634, 15826224);
    addMapping((Class)EntityPig.class, "Pig", 90, 15771042, 14377823);
    addMapping((Class)EntitySheep.class, "Sheep", 91, 15198183, 16758197);
    addMapping((Class)EntityCow.class, "Cow", 92, 4470310, 10592673);
    addMapping((Class)EntityChicken.class, "Chicken", 93, 10592673, 16711680);
    addMapping((Class)EntitySquid.class, "Squid", 94, 2243405, 7375001);
    addMapping((Class)EntityWolf.class, "Wolf", 95, 14144467, 13545366);
    addMapping((Class)EntityMooshroom.class, "MushroomCow", 96, 10489616, 12040119);
    addMapping((Class)EntitySnowman.class, "SnowMan", 97);
    addMapping((Class)EntityOcelot.class, "Ozelot", 98, 15720061, 5653556);
    addMapping((Class)EntityIronGolem.class, "VillagerGolem", 99);
    addMapping((Class)EntityHorse.class, "EntityHorse", 100, 12623485, 15656192);
    addMapping((Class)EntityRabbit.class, "Rabbit", 101, 10051392, 7555121);
    addMapping((Class)EntityVillager.class, "Villager", 120, 5651507, 12422002);
    addMapping((Class)EntityEnderCrystal.class, "EnderCrystal", 200);
  }
  
  public static class EntityEggInfo {
    public final int spawnedID;
    
    public final int primaryColor;
    
    public final int secondaryColor;
    
    public final StatBase field_151512_d;
    
    public final StatBase field_151513_e;
    
    public EntityEggInfo(int id, int baseColor, int spotColor) {
      this.spawnedID = id;
      this.primaryColor = baseColor;
      this.secondaryColor = spotColor;
      this.field_151512_d = StatList.getStatKillEntity(this);
      this.field_151513_e = StatList.getStatEntityKilledBy(this);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\EntityList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */