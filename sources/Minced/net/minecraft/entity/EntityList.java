// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

import net.minecraft.stats.StatList;
import net.minecraft.stats.StatBase;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.item.EntityMinecartMobSpawner;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartCommandBlock;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.entity.passive.EntityMule;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityItem;
import java.util.Iterator;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.Map;
import net.minecraft.util.registry.RegistryNamespaced;
import org.apache.logging.log4j.Logger;
import net.minecraft.util.ResourceLocation;

public class EntityList
{
    public static final ResourceLocation LIGHTNING_BOLT;
    private static final ResourceLocation PLAYER;
    private static final Logger LOGGER;
    public static final RegistryNamespaced<ResourceLocation, Class<? extends Entity>> REGISTRY;
    public static final Map<ResourceLocation, EntityEggInfo> ENTITY_EGGS;
    public static final Set<ResourceLocation> KNOWN_TYPES;
    private static final List<String> OLD_NAMES;
    
    @Nullable
    public static ResourceLocation getKey(final Entity entityIn) {
        return getKey(entityIn.getClass());
    }
    
    @Nullable
    public static ResourceLocation getKey(final Class<? extends Entity> entityIn) {
        return EntityList.REGISTRY.getNameForObject(entityIn);
    }
    
    @Nullable
    public static String getEntityString(final Entity entityIn) {
        final int i = EntityList.REGISTRY.getIDForObject(entityIn.getClass());
        return (i == -1) ? null : EntityList.OLD_NAMES.get(i);
    }
    
    @Nullable
    public static String getTranslationName(@Nullable final ResourceLocation entityType) {
        final int i = EntityList.REGISTRY.getIDForObject(EntityList.REGISTRY.getObject(entityType));
        return (i == -1) ? null : EntityList.OLD_NAMES.get(i);
    }
    
    @Nullable
    public static Class<? extends Entity> getClassFromID(final int entityID) {
        return EntityList.REGISTRY.getObjectById(entityID);
    }
    
    @Nullable
    public static Class<? extends Entity> getClassFromName(final String p_192839_0_) {
        return EntityList.REGISTRY.getObject(new ResourceLocation(p_192839_0_));
    }
    
    @Nullable
    public static Entity newEntity(@Nullable final Class<? extends Entity> clazz, final World worldIn) {
        if (clazz == null) {
            return null;
        }
        try {
            return (Entity)clazz.getConstructor(World.class).newInstance(worldIn);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
    
    @Nullable
    public static Entity createEntityByID(final int entityID, final World worldIn) {
        return newEntity(getClassFromID(entityID), worldIn);
    }
    
    @Nullable
    public static Entity createEntityByIDFromName(final ResourceLocation name, final World worldIn) {
        return newEntity(EntityList.REGISTRY.getObject(name), worldIn);
    }
    
    @Nullable
    public static Entity createEntityFromNBT(final NBTTagCompound nbt, final World worldIn) {
        final ResourceLocation resourcelocation = new ResourceLocation(nbt.getString("id"));
        final Entity entity = createEntityByIDFromName(resourcelocation, worldIn);
        if (entity == null) {
            EntityList.LOGGER.warn("Skipping Entity with id {}", (Object)resourcelocation);
        }
        else {
            entity.readFromNBT(nbt);
        }
        return entity;
    }
    
    public static Set<ResourceLocation> getEntityNameList() {
        return EntityList.KNOWN_TYPES;
    }
    
    public static boolean isMatchingName(final Entity entityIn, final ResourceLocation entityName) {
        final ResourceLocation resourcelocation = getKey(entityIn.getClass());
        if (resourcelocation != null) {
            return resourcelocation.equals(entityName);
        }
        if (entityIn instanceof EntityPlayer) {
            return EntityList.PLAYER.equals(entityName);
        }
        return entityIn instanceof EntityLightningBolt && EntityList.LIGHTNING_BOLT.equals(entityName);
    }
    
    public static boolean isRegistered(final ResourceLocation entityName) {
        return EntityList.PLAYER.equals(entityName) || getEntityNameList().contains(entityName);
    }
    
    public static String getValidTypeNames() {
        final StringBuilder stringbuilder = new StringBuilder();
        for (final ResourceLocation resourcelocation : getEntityNameList()) {
            stringbuilder.append(resourcelocation).append(", ");
        }
        stringbuilder.append(EntityList.PLAYER);
        return stringbuilder.toString();
    }
    
    public static void init() {
        register(1, "item", EntityItem.class, "Item");
        register(2, "xp_orb", EntityXPOrb.class, "XPOrb");
        register(3, "area_effect_cloud", EntityAreaEffectCloud.class, "AreaEffectCloud");
        register(4, "elder_guardian", EntityElderGuardian.class, "ElderGuardian");
        register(5, "wither_skeleton", EntityWitherSkeleton.class, "WitherSkeleton");
        register(6, "stray", EntityStray.class, "Stray");
        register(7, "egg", EntityEgg.class, "ThrownEgg");
        register(8, "leash_knot", EntityLeashKnot.class, "LeashKnot");
        register(9, "painting", EntityPainting.class, "Painting");
        register(10, "arrow", EntityTippedArrow.class, "Arrow");
        register(11, "snowball", EntitySnowball.class, "Snowball");
        register(12, "fireball", EntityLargeFireball.class, "Fireball");
        register(13, "small_fireball", EntitySmallFireball.class, "SmallFireball");
        register(14, "ender_pearl", EntityEnderPearl.class, "ThrownEnderpearl");
        register(15, "eye_of_ender_signal", EntityEnderEye.class, "EyeOfEnderSignal");
        register(16, "potion", EntityPotion.class, "ThrownPotion");
        register(17, "xp_bottle", EntityExpBottle.class, "ThrownExpBottle");
        register(18, "item_frame", EntityItemFrame.class, "ItemFrame");
        register(19, "wither_skull", EntityWitherSkull.class, "WitherSkull");
        register(20, "tnt", EntityTNTPrimed.class, "PrimedTnt");
        register(21, "falling_block", EntityFallingBlock.class, "FallingSand");
        register(22, "fireworks_rocket", EntityFireworkRocket.class, "FireworksRocketEntity");
        register(23, "husk", EntityHusk.class, "Husk");
        register(24, "spectral_arrow", EntitySpectralArrow.class, "SpectralArrow");
        register(25, "shulker_bullet", EntityShulkerBullet.class, "ShulkerBullet");
        register(26, "dragon_fireball", EntityDragonFireball.class, "DragonFireball");
        register(27, "zombie_villager", EntityZombieVillager.class, "ZombieVillager");
        register(28, "skeleton_horse", EntitySkeletonHorse.class, "SkeletonHorse");
        register(29, "zombie_horse", EntityZombieHorse.class, "ZombieHorse");
        register(30, "armor_stand", EntityArmorStand.class, "ArmorStand");
        register(31, "donkey", EntityDonkey.class, "Donkey");
        register(32, "mule", EntityMule.class, "Mule");
        register(33, "evocation_fangs", EntityEvokerFangs.class, "EvocationFangs");
        register(34, "evocation_illager", EntityEvoker.class, "EvocationIllager");
        register(35, "vex", EntityVex.class, "Vex");
        register(36, "vindication_illager", EntityVindicator.class, "VindicationIllager");
        register(37, "illusion_illager", EntityIllusionIllager.class, "IllusionIllager");
        register(40, "commandblock_minecart", EntityMinecartCommandBlock.class, EntityMinecart.Type.COMMAND_BLOCK.getName());
        register(41, "boat", EntityBoat.class, "Boat");
        register(42, "minecart", EntityMinecartEmpty.class, EntityMinecart.Type.RIDEABLE.getName());
        register(43, "chest_minecart", EntityMinecartChest.class, EntityMinecart.Type.CHEST.getName());
        register(44, "furnace_minecart", EntityMinecartFurnace.class, EntityMinecart.Type.FURNACE.getName());
        register(45, "tnt_minecart", EntityMinecartTNT.class, EntityMinecart.Type.TNT.getName());
        register(46, "hopper_minecart", EntityMinecartHopper.class, EntityMinecart.Type.HOPPER.getName());
        register(47, "spawner_minecart", EntityMinecartMobSpawner.class, EntityMinecart.Type.SPAWNER.getName());
        register(50, "creeper", EntityCreeper.class, "Creeper");
        register(51, "skeleton", EntitySkeleton.class, "Skeleton");
        register(52, "spider", EntitySpider.class, "Spider");
        register(53, "giant", EntityGiantZombie.class, "Giant");
        register(54, "zombie", EntityZombie.class, "Zombie");
        register(55, "slime", EntitySlime.class, "Slime");
        register(56, "ghast", EntityGhast.class, "Ghast");
        register(57, "zombie_pigman", EntityPigZombie.class, "PigZombie");
        register(58, "enderman", EntityEnderman.class, "Enderman");
        register(59, "cave_spider", EntityCaveSpider.class, "CaveSpider");
        register(60, "silverfish", EntitySilverfish.class, "Silverfish");
        register(61, "blaze", EntityBlaze.class, "Blaze");
        register(62, "magma_cube", EntityMagmaCube.class, "LavaSlime");
        register(63, "ender_dragon", EntityDragon.class, "EnderDragon");
        register(64, "wither", EntityWither.class, "WitherBoss");
        register(65, "bat", EntityBat.class, "Bat");
        register(66, "witch", EntityWitch.class, "Witch");
        register(67, "endermite", EntityEndermite.class, "Endermite");
        register(68, "guardian", EntityGuardian.class, "Guardian");
        register(69, "shulker", EntityShulker.class, "Shulker");
        register(90, "pig", EntityPig.class, "Pig");
        register(91, "sheep", EntitySheep.class, "Sheep");
        register(92, "cow", EntityCow.class, "Cow");
        register(93, "chicken", EntityChicken.class, "Chicken");
        register(94, "squid", EntitySquid.class, "Squid");
        register(95, "wolf", EntityWolf.class, "Wolf");
        register(96, "mooshroom", EntityMooshroom.class, "MushroomCow");
        register(97, "snowman", EntitySnowman.class, "SnowMan");
        register(98, "ocelot", EntityOcelot.class, "Ozelot");
        register(99, "villager_golem", EntityIronGolem.class, "VillagerGolem");
        register(100, "horse", EntityHorse.class, "Horse");
        register(101, "rabbit", EntityRabbit.class, "Rabbit");
        register(102, "polar_bear", EntityPolarBear.class, "PolarBear");
        register(103, "llama", EntityLlama.class, "Llama");
        register(104, "llama_spit", EntityLlamaSpit.class, "LlamaSpit");
        register(105, "parrot", EntityParrot.class, "Parrot");
        register(120, "villager", EntityVillager.class, "Villager");
        register(200, "ender_crystal", EntityEnderCrystal.class, "EnderCrystal");
        addSpawnInfo("bat", 4996656, 986895);
        addSpawnInfo("blaze", 16167425, 16775294);
        addSpawnInfo("cave_spider", 803406, 11013646);
        addSpawnInfo("chicken", 10592673, 16711680);
        addSpawnInfo("cow", 4470310, 10592673);
        addSpawnInfo("creeper", 894731, 0);
        addSpawnInfo("donkey", 5457209, 8811878);
        addSpawnInfo("elder_guardian", 13552826, 7632531);
        addSpawnInfo("enderman", 1447446, 0);
        addSpawnInfo("endermite", 1447446, 7237230);
        addSpawnInfo("evocation_illager", 9804699, 1973274);
        addSpawnInfo("ghast", 16382457, 12369084);
        addSpawnInfo("guardian", 5931634, 15826224);
        addSpawnInfo("horse", 12623485, 15656192);
        addSpawnInfo("husk", 7958625, 15125652);
        addSpawnInfo("llama", 12623485, 10051392);
        addSpawnInfo("magma_cube", 3407872, 16579584);
        addSpawnInfo("mooshroom", 10489616, 12040119);
        addSpawnInfo("mule", 1769984, 5321501);
        addSpawnInfo("ocelot", 15720061, 5653556);
        addSpawnInfo("parrot", 894731, 16711680);
        addSpawnInfo("pig", 15771042, 14377823);
        addSpawnInfo("polar_bear", 15921906, 9803152);
        addSpawnInfo("rabbit", 10051392, 7555121);
        addSpawnInfo("sheep", 15198183, 16758197);
        addSpawnInfo("shulker", 9725844, 5060690);
        addSpawnInfo("silverfish", 7237230, 3158064);
        addSpawnInfo("skeleton", 12698049, 4802889);
        addSpawnInfo("skeleton_horse", 6842447, 15066584);
        addSpawnInfo("slime", 5349438, 8306542);
        addSpawnInfo("spider", 3419431, 11013646);
        addSpawnInfo("squid", 2243405, 7375001);
        addSpawnInfo("stray", 6387319, 14543594);
        addSpawnInfo("vex", 8032420, 15265265);
        addSpawnInfo("villager", 5651507, 12422002);
        addSpawnInfo("vindication_illager", 9804699, 2580065);
        addSpawnInfo("witch", 3407872, 5349438);
        addSpawnInfo("wither_skeleton", 1315860, 4672845);
        addSpawnInfo("wolf", 14144467, 13545366);
        addSpawnInfo("zombie", 44975, 7969893);
        addSpawnInfo("zombie_horse", 3232308, 9945732);
        addSpawnInfo("zombie_pigman", 15373203, 5009705);
        addSpawnInfo("zombie_villager", 5651507, 7969893);
        EntityList.KNOWN_TYPES.add(EntityList.LIGHTNING_BOLT);
    }
    
    private static void register(final int id, final String name, final Class<? extends Entity> clazz, final String oldName) {
        try {
            clazz.getConstructor(World.class);
        }
        catch (NoSuchMethodException var5) {
            throw new RuntimeException("Invalid class " + clazz + " no constructor taking " + World.class.getName());
        }
        if ((clazz.getModifiers() & 0x400) == 0x400) {
            throw new RuntimeException("Invalid abstract class " + clazz);
        }
        final ResourceLocation resourcelocation = new ResourceLocation(name);
        EntityList.REGISTRY.register(id, resourcelocation, clazz);
        EntityList.KNOWN_TYPES.add(resourcelocation);
        while (EntityList.OLD_NAMES.size() <= id) {
            EntityList.OLD_NAMES.add(null);
        }
        EntityList.OLD_NAMES.set(id, oldName);
    }
    
    protected static EntityEggInfo addSpawnInfo(final String id, final int primaryColor, final int secondaryColor) {
        final ResourceLocation resourcelocation = new ResourceLocation(id);
        return EntityList.ENTITY_EGGS.put(resourcelocation, new EntityEggInfo(resourcelocation, primaryColor, secondaryColor));
    }
    
    static {
        LIGHTNING_BOLT = new ResourceLocation("lightning_bolt");
        PLAYER = new ResourceLocation("player");
        LOGGER = LogManager.getLogger();
        REGISTRY = new RegistryNamespaced<ResourceLocation, Class<? extends Entity>>();
        ENTITY_EGGS = Maps.newLinkedHashMap();
        KNOWN_TYPES = Sets.newHashSet();
        OLD_NAMES = Lists.newArrayList();
    }
    
    public static class EntityEggInfo
    {
        public final ResourceLocation spawnedID;
        public final int primaryColor;
        public final int secondaryColor;
        public final StatBase killEntityStat;
        public final StatBase entityKilledByStat;
        
        public EntityEggInfo(final ResourceLocation idIn, final int primaryColorIn, final int secondaryColorIn) {
            this.spawnedID = idIn;
            this.primaryColor = primaryColorIn;
            this.secondaryColor = secondaryColorIn;
            this.killEntityStat = StatList.getStatKillEntity(this);
            this.entityKilledByStat = StatList.getStatEntityKilledBy(this);
        }
    }
}
