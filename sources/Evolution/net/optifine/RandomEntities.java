package net.optifine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityShoulderRiding;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.src.Config;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorRaw;
import net.optifine.util.IntegratedServerUtils;
import net.optifine.util.PropertiesOrdered;
import net.optifine.util.ResUtils;
import net.optifine.util.StrUtils;

public class RandomEntities
{
    private static Map<String, RandomEntityProperties> mapProperties = new HashMap<String, RandomEntityProperties>();
    private static boolean active = false;
    private static RenderGlobal renderGlobal;
    private static RandomEntity randomEntity = new RandomEntity();
    private static TileEntityRendererDispatcher tileEntityRendererDispatcher;
    private static RandomTileEntity randomTileEntity = new RandomTileEntity();
    private static boolean working = false;
    public static final String SUFFIX_PNG = ".png";
    public static final String SUFFIX_PROPERTIES = ".properties";
    public static final String PREFIX_TEXTURES_ENTITY = "textures/entity/";
    public static final String PREFIX_TEXTURES_PAINTING = "textures/painting/";
    public static final String PREFIX_TEXTURES = "textures/";
    public static final String PREFIX_OPTIFINE_RANDOM = "optifine/random/";
    public static final String PREFIX_MCPATCHER_MOB = "mcpatcher/mob/";
    private static final String[] DEPENDANT_SUFFIXES = new String[] {"_armor", "_eyes", "_exploding", "_shooting", "_fur", "_eyes", "_invulnerable", "_angry", "_tame", "_collar"};
    private static final String PREFIX_DYNAMIC_TEXTURE_HORSE = "horse/";
    private static final String[] HORSE_TEXTURES = (String[])ReflectorRaw.getFieldValue((Object)null, EntityHorse.class, String[].class, 0);
    private static final String[] HORSE_TEXTURES_ABBR = (String[])ReflectorRaw.getFieldValue((Object)null, EntityHorse.class, String[].class, 1);

    public static void entityLoaded(Entity entity, World world)
    {
        if (world != null)
        {
            EntityDataManager entitydatamanager = entity.getDataManager();
            entitydatamanager.spawnPosition = entity.getPosition();
            entitydatamanager.spawnBiome = world.getBiome(entitydatamanager.spawnPosition);

            if (entity instanceof EntityShoulderRiding)
            {
                EntityShoulderRiding entityshoulderriding = (EntityShoulderRiding)entity;
                checkEntityShoulder(entityshoulderriding, false);
            }

            UUID uuid = entity.getUniqueID();

            if (entity instanceof EntityVillager)
            {
                updateEntityVillager(uuid, (EntityVillager)entity);
            }
        }
    }

    public static void entityUnloaded(Entity entity, World world)
    {
        if (entity instanceof EntityShoulderRiding)
        {
            EntityShoulderRiding entityshoulderriding = (EntityShoulderRiding)entity;
            checkEntityShoulder(entityshoulderriding, true);
        }
    }

    private static void checkEntityShoulder(EntityShoulderRiding entity, boolean attach)
    {
        EntityLivingBase entitylivingbase = entity.getOwner();

        if (entitylivingbase == null)
        {
            entitylivingbase = Config.getMinecraft().player;
        }

        if (entitylivingbase instanceof AbstractClientPlayer)
        {
            AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)entitylivingbase;
            UUID uuid = entity.getUniqueID();

            if (attach)
            {
                NBTTagCompound nbttagcompound = abstractclientplayer.getLeftShoulderEntity();

                if (nbttagcompound != null && nbttagcompound.hasKey("UUID") && Config.equals(nbttagcompound.getUniqueId("UUID"), uuid))
                {
                    abstractclientplayer.entityShoulderLeft = entity;
                }

                NBTTagCompound nbttagcompound1 = abstractclientplayer.getRightShoulderEntity();

                if (nbttagcompound1 != null && nbttagcompound1.hasKey("UUID") && Config.equals(nbttagcompound1.getUniqueId("UUID"), uuid))
                {
                    abstractclientplayer.entityShoulderRight = entity;
                }
            }
            else
            {
                EntityDataManager entitydatamanager = entity.getDataManager();

                if (abstractclientplayer.entityShoulderLeft != null && Config.equals(abstractclientplayer.entityShoulderLeft.getUniqueID(), uuid))
                {
                    EntityDataManager entitydatamanager1 = abstractclientplayer.entityShoulderLeft.getDataManager();
                    entitydatamanager.spawnPosition = entitydatamanager1.spawnPosition;
                    entitydatamanager.spawnBiome = entitydatamanager1.spawnBiome;
                    abstractclientplayer.entityShoulderLeft = null;
                }

                if (abstractclientplayer.entityShoulderRight != null && Config.equals(abstractclientplayer.entityShoulderRight.getUniqueID(), uuid))
                {
                    EntityDataManager entitydatamanager2 = abstractclientplayer.entityShoulderRight.getDataManager();
                    entitydatamanager.spawnPosition = entitydatamanager2.spawnPosition;
                    entitydatamanager.spawnBiome = entitydatamanager2.spawnBiome;
                    abstractclientplayer.entityShoulderRight = null;
                }
            }
        }
    }

    private static void updateEntityVillager(UUID uuid, EntityVillager ev)
    {
        Entity entity = IntegratedServerUtils.getEntity(uuid);

        if (entity instanceof EntityVillager)
        {
            EntityVillager entityvillager = (EntityVillager)entity;
            int i = entityvillager.getProfession();
            ev.setProfession(i);
            int j = Reflector.getFieldValueInt(entityvillager, Reflector.EntityVillager_careerId, 0);
            Reflector.setFieldValueInt(ev, Reflector.EntityVillager_careerId, j);
            int k = Reflector.getFieldValueInt(entityvillager, Reflector.EntityVillager_careerLevel, 0);
            Reflector.setFieldValueInt(ev, Reflector.EntityVillager_careerLevel, k);
        }
    }

    public static void worldChanged(World oldWorld, World newWorld)
    {
        if (newWorld != null)
        {
            List list = newWorld.getLoadedEntityList();

            for (int i = 0; i < list.size(); ++i)
            {
                Entity entity = (Entity)list.get(i);
                entityLoaded(entity, newWorld);
            }
        }

        randomEntity.setEntity((Entity)null);
        randomTileEntity.setTileEntity((TileEntity)null);
    }

    public static ResourceLocation getTextureLocation(ResourceLocation loc)
    {
        if (!active)
        {
            return loc;
        }
        else if (working)
        {
            return loc;
        }
        else
        {
            ResourceLocation name;

            try
            {
                working = true;
                IRandomEntity irandomentity = getRandomEntityRendered();

                if (irandomentity != null)
                {
                    String s = loc.getPath();

                    if (s.startsWith("horse/"))
                    {
                        s = getHorseTexturePath(s, "horse/".length());
                    }

                    if (!s.startsWith("textures/entity/") && !s.startsWith("textures/painting/"))
                    {
                        ResourceLocation resourcelocation2 = loc;
                        return resourcelocation2;
                    }

                    RandomEntityProperties randomentityproperties = mapProperties.get(s);

                    if (randomentityproperties == null)
                    {
                        ResourceLocation resourcelocation3 = loc;
                        return resourcelocation3;
                    }

                    ResourceLocation resourcelocation1 = randomentityproperties.getTextureLocation(loc, irandomentity);
                    return resourcelocation1;
                }

                name = loc;
            }
            finally
            {
                working = false;
            }

            return name;
        }
    }

    private static String getHorseTexturePath(String path, int pos)
    {
        if (HORSE_TEXTURES != null && HORSE_TEXTURES_ABBR != null)
        {
            for (int i = 0; i < HORSE_TEXTURES_ABBR.length; ++i)
            {
                String s = HORSE_TEXTURES_ABBR[i];

                if (path.startsWith(s, pos))
                {
                    return HORSE_TEXTURES[i];
                }
            }

            return path;
        }
        else
        {
            return path;
        }
    }

    private static IRandomEntity getRandomEntityRendered()
    {
        if (renderGlobal.renderedEntity != null)
        {
            randomEntity.setEntity(renderGlobal.renderedEntity);
            return randomEntity;
        }
        else
        {
            if (tileEntityRendererDispatcher.tileEntityRendered != null)
            {
                TileEntity tileentity = tileEntityRendererDispatcher.tileEntityRendered;

                if (tileentity.getWorld() != null)
                {
                    randomTileEntity.setTileEntity(tileentity);
                    return randomTileEntity;
                }
            }

            return null;
        }
    }

    private static RandomEntityProperties makeProperties(ResourceLocation loc, boolean mcpatcher)
    {
        String s = loc.getPath();
        ResourceLocation resourcelocation = getLocationProperties(loc, mcpatcher);

        if (resourcelocation != null)
        {
            RandomEntityProperties randomentityproperties = parseProperties(resourcelocation, loc);

            if (randomentityproperties != null)
            {
                return randomentityproperties;
            }
        }

        ResourceLocation[] aresourcelocation = getLocationsVariants(loc, mcpatcher);
        return aresourcelocation == null ? null : new RandomEntityProperties(s, aresourcelocation);
    }

    private static RandomEntityProperties parseProperties(ResourceLocation propLoc, ResourceLocation resLoc)
    {
        try
        {
            String s = propLoc.getPath();
            dbg(resLoc.getPath() + ", properties: " + s);
            InputStream inputstream = Config.getResourceStream(propLoc);

            if (inputstream == null)
            {
                warn("Properties not found: " + s);
                return null;
            }
            else
            {
                Properties properties = new PropertiesOrdered();
                properties.load(inputstream);
                inputstream.close();
                RandomEntityProperties randomentityproperties = new RandomEntityProperties(properties, s, resLoc);
                return !randomentityproperties.isValid(s) ? null : randomentityproperties;
            }
        }
        catch (FileNotFoundException var6)
        {
            warn("File not found: " + resLoc.getPath());
            return null;
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
            return null;
        }
    }

    private static ResourceLocation getLocationProperties(ResourceLocation loc, boolean mcpatcher)
    {
        ResourceLocation resourcelocation = getLocationRandom(loc, mcpatcher);

        if (resourcelocation == null)
        {
            return null;
        }
        else
        {
            String s = resourcelocation.getNamespace();
            String s1 = resourcelocation.getPath();
            String s2 = StrUtils.removeSuffix(s1, ".png");
            String s3 = s2 + ".properties";
            ResourceLocation resourcelocation1 = new ResourceLocation(s, s3);

            if (Config.hasResource(resourcelocation1))
            {
                return resourcelocation1;
            }
            else
            {
                String s4 = getParentTexturePath(s2);

                if (s4 == null)
                {
                    return null;
                }
                else
                {
                    ResourceLocation resourcelocation2 = new ResourceLocation(s, s4 + ".properties");
                    return Config.hasResource(resourcelocation2) ? resourcelocation2 : null;
                }
            }
        }
    }

    protected static ResourceLocation getLocationRandom(ResourceLocation loc, boolean mcpatcher)
    {
        String s = loc.getNamespace();
        String s1 = loc.getPath();
        String s2 = "textures/";
        String s3 = "optifine/random/";

        if (mcpatcher)
        {
            s2 = "textures/entity/";
            s3 = "mcpatcher/mob/";
        }

        if (!s1.startsWith(s2))
        {
            return null;
        }
        else
        {
            String s4 = StrUtils.replacePrefix(s1, s2, s3);
            return new ResourceLocation(s, s4);
        }
    }

    private static String getPathBase(String pathRandom)
    {
        if (pathRandom.startsWith("optifine/random/"))
        {
            return StrUtils.replacePrefix(pathRandom, "optifine/random/", "textures/");
        }
        else
        {
            return pathRandom.startsWith("mcpatcher/mob/") ? StrUtils.replacePrefix(pathRandom, "mcpatcher/mob/", "textures/entity/") : null;
        }
    }

    protected static ResourceLocation getLocationIndexed(ResourceLocation loc, int index)
    {
        if (loc == null)
        {
            return null;
        }
        else
        {
            String s = loc.getPath();
            int i = s.lastIndexOf(46);

            if (i < 0)
            {
                return null;
            }
            else
            {
                String s1 = s.substring(0, i);
                String s2 = s.substring(i);
                String s3 = s1 + index + s2;
                ResourceLocation resourcelocation = new ResourceLocation(loc.getNamespace(), s3);
                return resourcelocation;
            }
        }
    }

    private static String getParentTexturePath(String path)
    {
        for (int i = 0; i < DEPENDANT_SUFFIXES.length; ++i)
        {
            String s = DEPENDANT_SUFFIXES[i];

            if (path.endsWith(s))
            {
                String s1 = StrUtils.removeSuffix(path, s);
                return s1;
            }
        }

        return null;
    }

    private static ResourceLocation[] getLocationsVariants(ResourceLocation loc, boolean mcpatcher)
    {
        List list = new ArrayList();
        list.add(loc);
        ResourceLocation resourcelocation = getLocationRandom(loc, mcpatcher);

        if (resourcelocation == null)
        {
            return null;
        }
        else
        {
            for (int i = 1; i < list.size() + 10; ++i)
            {
                int j = i + 1;
                ResourceLocation resourcelocation1 = getLocationIndexed(resourcelocation, j);

                if (Config.hasResource(resourcelocation1))
                {
                    list.add(resourcelocation1);
                }
            }

            if (list.size() <= 1)
            {
                return null;
            }
            else
            {
                ResourceLocation[] aresourcelocation = (ResourceLocation[])list.toArray(new ResourceLocation[list.size()]);
                dbg(loc.getPath() + ", variants: " + aresourcelocation.length);
                return aresourcelocation;
            }
        }
    }

    public static void update()
    {
        mapProperties.clear();
        active = false;

        if (Config.isRandomEntities())
        {
            initialize();
        }
    }

    private static void initialize()
    {
        renderGlobal = Config.getRenderGlobal();
        tileEntityRendererDispatcher = TileEntityRendererDispatcher.instance;
        String[] astring = new String[] {"optifine/random/", "mcpatcher/mob/"};
        String[] astring1 = new String[] {".png", ".properties"};
        String[] astring2 = ResUtils.collectFiles(astring, astring1);
        Set set = new HashSet();

        for (int i = 0; i < astring2.length; ++i)
        {
            String s = astring2[i];
            s = StrUtils.removeSuffix(s, astring1);
            s = StrUtils.trimTrailing(s, "0123456789");
            s = s + ".png";
            String s1 = getPathBase(s);

            if (!set.contains(s1))
            {
                set.add(s1);
                ResourceLocation resourcelocation = new ResourceLocation(s1);

                if (Config.hasResource(resourcelocation))
                {
                    RandomEntityProperties randomentityproperties = mapProperties.get(s1);

                    if (randomentityproperties == null)
                    {
                        randomentityproperties = makeProperties(resourcelocation, false);

                        if (randomentityproperties == null)
                        {
                            randomentityproperties = makeProperties(resourcelocation, true);
                        }

                        if (randomentityproperties != null)
                        {
                            mapProperties.put(s1, randomentityproperties);
                        }
                    }
                }
            }
        }

        active = !mapProperties.isEmpty();
    }

    public static void dbg(String str)
    {
        Config.dbg("RandomEntities: " + str);
    }

    public static void warn(String str)
    {
        Config.warn("RandomEntities: " + str);
    }
}
