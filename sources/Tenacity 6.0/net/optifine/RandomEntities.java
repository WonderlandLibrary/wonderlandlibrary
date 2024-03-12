// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import net.optifine.reflect.ReflectorRaw;
import net.minecraft.entity.passive.EntityHorse;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import net.optifine.util.ResUtils;
import java.util.ArrayList;
import net.optifine.util.StrUtils;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import net.optifine.util.PropertiesOrdered;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import java.util.List;
import net.minecraft.tileentity.TileEntity;
import net.optifine.reflect.Reflector;
import net.optifine.util.IntegratedServerUtils;
import java.util.UUID;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.RenderGlobal;
import java.util.Map;

public class RandomEntities
{
    private static Map<String, RandomEntityProperties> mapProperties;
    private static boolean active;
    private static RenderGlobal renderGlobal;
    private static RandomEntity randomEntity;
    private static TileEntityRendererDispatcher tileEntityRendererDispatcher;
    private static RandomTileEntity randomTileEntity;
    private static boolean working;
    public static final String SUFFIX_PNG = ".png";
    public static final String SUFFIX_PROPERTIES = ".properties";
    public static final String PREFIX_TEXTURES_ENTITY = "textures/entity/";
    public static final String PREFIX_TEXTURES_PAINTING = "textures/painting/";
    public static final String PREFIX_TEXTURES = "textures/";
    public static final String PREFIX_OPTIFINE_RANDOM = "optifine/random/";
    public static final String PREFIX_MCPATCHER_MOB = "mcpatcher/mob/";
    private static final String[] DEPENDANT_SUFFIXES;
    private static final String PREFIX_DYNAMIC_TEXTURE_HORSE = "horse/";
    private static final String[] HORSE_TEXTURES;
    private static final String[] HORSE_TEXTURES_ABBR;
    
    public static void entityLoaded(final Entity entity, final World world) {
        if (world != null) {
            final DataWatcher datawatcher = entity.getDataWatcher();
            datawatcher.spawnPosition = entity.getPosition();
            datawatcher.spawnBiome = world.getBiomeGenForCoords(datawatcher.spawnPosition);
            final UUID uuid = entity.getUniqueID();
            if (entity instanceof EntityVillager) {
                updateEntityVillager(uuid, (EntityVillager)entity);
            }
        }
    }
    
    public static void entityUnloaded(final Entity entity, final World world) {
    }
    
    private static void updateEntityVillager(final UUID uuid, final EntityVillager ev) {
        final Entity entity = IntegratedServerUtils.getEntity(uuid);
        if (entity instanceof EntityVillager) {
            final EntityVillager entityvillager = (EntityVillager)entity;
            final int i = entityvillager.getProfession();
            ev.setProfession(i);
            final int j = Reflector.getFieldValueInt(entityvillager, Reflector.EntityVillager_careerId, 0);
            Reflector.setFieldValueInt(ev, Reflector.EntityVillager_careerId, j);
            final int k = Reflector.getFieldValueInt(entityvillager, Reflector.EntityVillager_careerLevel, 0);
            Reflector.setFieldValueInt(ev, Reflector.EntityVillager_careerLevel, k);
        }
    }
    
    public static void worldChanged(final World oldWorld, final World newWorld) {
        if (newWorld != null) {
            final List list = newWorld.getLoadedEntityList();
            for (int i = 0; i < list.size(); ++i) {
                final Entity entity = list.get(i);
                entityLoaded(entity, newWorld);
            }
        }
        RandomEntities.randomEntity.setEntity(null);
        RandomEntities.randomTileEntity.setTileEntity(null);
    }
    
    public static ResourceLocation getTextureLocation(final ResourceLocation loc) {
        if (!RandomEntities.active) {
            return loc;
        }
        if (RandomEntities.working) {
            return loc;
        }
        ResourceLocation name;
        try {
            RandomEntities.working = true;
            final IRandomEntity irandomentity = getRandomEntityRendered();
            if (irandomentity != null) {
                String s = loc.getResourcePath();
                if (s.startsWith("horse/")) {
                    s = getHorseTexturePath(s, "horse/".length());
                }
                if (!s.startsWith("textures/entity/") && !s.startsWith("textures/painting/")) {
                    final ResourceLocation resourcelocation2 = loc;
                    return resourcelocation2;
                }
                final RandomEntityProperties randomentityproperties = RandomEntities.mapProperties.get(s);
                if (randomentityproperties == null) {
                    final ResourceLocation resourcelocation3 = loc;
                    return resourcelocation3;
                }
                final ResourceLocation resourcelocation4 = randomentityproperties.getTextureLocation(loc, irandomentity);
                return resourcelocation4;
            }
            else {
                name = loc;
            }
        }
        finally {
            RandomEntities.working = false;
        }
        return name;
    }
    
    private static String getHorseTexturePath(final String path, final int pos) {
        if (RandomEntities.HORSE_TEXTURES != null && RandomEntities.HORSE_TEXTURES_ABBR != null) {
            for (int i = 0; i < RandomEntities.HORSE_TEXTURES_ABBR.length; ++i) {
                final String s = RandomEntities.HORSE_TEXTURES_ABBR[i];
                if (path.startsWith(s, pos)) {
                    return RandomEntities.HORSE_TEXTURES[i];
                }
            }
            return path;
        }
        return path;
    }
    
    private static IRandomEntity getRandomEntityRendered() {
        if (RandomEntities.renderGlobal.renderedEntity != null) {
            RandomEntities.randomEntity.setEntity(RandomEntities.renderGlobal.renderedEntity);
            return RandomEntities.randomEntity;
        }
        if (RandomEntities.tileEntityRendererDispatcher.tileEntityRendered != null) {
            final TileEntity tileentity = RandomEntities.tileEntityRendererDispatcher.tileEntityRendered;
            if (tileentity.getWorld() != null) {
                RandomEntities.randomTileEntity.setTileEntity(tileentity);
                return RandomEntities.randomTileEntity;
            }
        }
        return null;
    }
    
    private static RandomEntityProperties makeProperties(final ResourceLocation loc, final boolean mcpatcher) {
        final String s = loc.getResourcePath();
        final ResourceLocation resourcelocation = getLocationProperties(loc, mcpatcher);
        if (resourcelocation != null) {
            final RandomEntityProperties randomentityproperties = parseProperties(resourcelocation, loc);
            if (randomentityproperties != null) {
                return randomentityproperties;
            }
        }
        final ResourceLocation[] aresourcelocation = getLocationsVariants(loc, mcpatcher);
        return (aresourcelocation == null) ? null : new RandomEntityProperties(s, aresourcelocation);
    }
    
    private static RandomEntityProperties parseProperties(final ResourceLocation propLoc, final ResourceLocation resLoc) {
        try {
            final String s = propLoc.getResourcePath();
            dbg(resLoc.getResourcePath() + ", properties: " + s);
            final InputStream inputstream = Config.getResourceStream(propLoc);
            if (inputstream == null) {
                warn("Properties not found: " + s);
                return null;
            }
            final Properties properties = new PropertiesOrdered();
            properties.load(inputstream);
            inputstream.close();
            final RandomEntityProperties randomentityproperties = new RandomEntityProperties(properties, s, resLoc);
            return randomentityproperties.isValid(s) ? randomentityproperties : null;
        }
        catch (FileNotFoundException var6) {
            warn("File not found: " + resLoc.getResourcePath());
            return null;
        }
        catch (IOException ioexception) {
            ioexception.printStackTrace();
            return null;
        }
    }
    
    private static ResourceLocation getLocationProperties(final ResourceLocation loc, final boolean mcpatcher) {
        final ResourceLocation resourcelocation = getLocationRandom(loc, mcpatcher);
        if (resourcelocation == null) {
            return null;
        }
        final String s = resourcelocation.getResourceDomain();
        final String s2 = resourcelocation.getResourcePath();
        final String s3 = StrUtils.removeSuffix(s2, ".png");
        final String s4 = s3 + ".properties";
        final ResourceLocation resourcelocation2 = new ResourceLocation(s, s4);
        if (Config.hasResource(resourcelocation2)) {
            return resourcelocation2;
        }
        final String s5 = getParentTexturePath(s3);
        if (s5 == null) {
            return null;
        }
        final ResourceLocation resourcelocation3 = new ResourceLocation(s, s5 + ".properties");
        return Config.hasResource(resourcelocation3) ? resourcelocation3 : null;
    }
    
    protected static ResourceLocation getLocationRandom(final ResourceLocation loc, final boolean mcpatcher) {
        final String s = loc.getResourceDomain();
        final String s2 = loc.getResourcePath();
        String s3 = "textures/";
        String s4 = "optifine/random/";
        if (mcpatcher) {
            s3 = "textures/entity/";
            s4 = "mcpatcher/mob/";
        }
        if (!s2.startsWith(s3)) {
            return null;
        }
        final String s5 = StrUtils.replacePrefix(s2, s3, s4);
        return new ResourceLocation(s, s5);
    }
    
    private static String getPathBase(final String pathRandom) {
        return pathRandom.startsWith("optifine/random/") ? StrUtils.replacePrefix(pathRandom, "optifine/random/", "textures/") : (pathRandom.startsWith("mcpatcher/mob/") ? StrUtils.replacePrefix(pathRandom, "mcpatcher/mob/", "textures/entity/") : null);
    }
    
    protected static ResourceLocation getLocationIndexed(final ResourceLocation loc, final int index) {
        if (loc == null) {
            return null;
        }
        final String s = loc.getResourcePath();
        final int i = s.lastIndexOf(46);
        if (i < 0) {
            return null;
        }
        final String s2 = s.substring(0, i);
        final String s3 = s.substring(i);
        final String s4 = s2 + index + s3;
        final ResourceLocation resourcelocation = new ResourceLocation(loc.getResourceDomain(), s4);
        return resourcelocation;
    }
    
    private static String getParentTexturePath(final String path) {
        for (int i = 0; i < RandomEntities.DEPENDANT_SUFFIXES.length; ++i) {
            final String s = RandomEntities.DEPENDANT_SUFFIXES[i];
            if (path.endsWith(s)) {
                final String s2 = StrUtils.removeSuffix(path, s);
                return s2;
            }
        }
        return null;
    }
    
    private static ResourceLocation[] getLocationsVariants(final ResourceLocation loc, final boolean mcpatcher) {
        final List list = new ArrayList();
        list.add(loc);
        final ResourceLocation resourcelocation = getLocationRandom(loc, mcpatcher);
        if (resourcelocation == null) {
            return null;
        }
        for (int i = 1; i < list.size() + 10; ++i) {
            final int j = i + 1;
            final ResourceLocation resourcelocation2 = getLocationIndexed(resourcelocation, j);
            if (Config.hasResource(resourcelocation2)) {
                list.add(resourcelocation2);
            }
        }
        if (list.size() <= 1) {
            return null;
        }
        final ResourceLocation[] aresourcelocation = list.toArray(new ResourceLocation[list.size()]);
        dbg(loc.getResourcePath() + ", variants: " + aresourcelocation.length);
        return aresourcelocation;
    }
    
    public static void update() {
        RandomEntities.mapProperties.clear();
        RandomEntities.active = false;
        if (Config.isRandomEntities()) {
            initialize();
        }
    }
    
    private static void initialize() {
        RandomEntities.renderGlobal = Config.getRenderGlobal();
        RandomEntities.tileEntityRendererDispatcher = TileEntityRendererDispatcher.instance;
        final String[] astring = { "optifine/random/", "mcpatcher/mob/" };
        final String[] astring2 = { ".png", ".properties" };
        final String[] astring3 = ResUtils.collectFiles(astring, astring2);
        final Set set = new HashSet();
        for (int i = 0; i < astring3.length; ++i) {
            String s = astring3[i];
            s = StrUtils.removeSuffix(s, astring2);
            s = StrUtils.trimTrailing(s, "0123456789");
            s += ".png";
            final String s2 = getPathBase(s);
            if (!set.contains(s2)) {
                set.add(s2);
                final ResourceLocation resourcelocation = new ResourceLocation(s2);
                if (Config.hasResource(resourcelocation)) {
                    RandomEntityProperties randomentityproperties = RandomEntities.mapProperties.get(s2);
                    if (randomentityproperties == null) {
                        randomentityproperties = makeProperties(resourcelocation, false);
                        if (randomentityproperties == null) {
                            randomentityproperties = makeProperties(resourcelocation, true);
                        }
                        if (randomentityproperties != null) {
                            RandomEntities.mapProperties.put(s2, randomentityproperties);
                        }
                    }
                }
            }
        }
        RandomEntities.active = !RandomEntities.mapProperties.isEmpty();
    }
    
    public static void dbg(final String str) {
        Config.dbg("RandomEntities: " + str);
    }
    
    public static void warn(final String str) {
        Config.warn("RandomEntities: " + str);
    }
    
    static {
        RandomEntities.mapProperties = new HashMap<String, RandomEntityProperties>();
        RandomEntities.active = false;
        RandomEntities.randomEntity = new RandomEntity();
        RandomEntities.randomTileEntity = new RandomTileEntity();
        RandomEntities.working = false;
        DEPENDANT_SUFFIXES = new String[] { "_armor", "_eyes", "_exploding", "_shooting", "_fur", "_eyes", "_invulnerable", "_angry", "_tame", "_collar" };
        HORSE_TEXTURES = (String[])ReflectorRaw.getFieldValue(null, EntityHorse.class, String[].class, 2);
        HORSE_TEXTURES_ABBR = (String[])ReflectorRaw.getFieldValue(null, EntityHorse.class, String[].class, 3);
    }
}
