// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

import java.util.UUID;
import java.util.HashMap;
import java.util.ArrayList;
import net.minecraft.util.ResourceLocation;
import java.util.List;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.Entity;
import java.lang.reflect.Field;
import java.util.Random;
import net.minecraft.client.renderer.RenderGlobal;
import java.util.Map;

public class RandomMobs
{
    private static Map textureVariantsMap;
    private static RenderGlobal renderGlobal;
    private static boolean initialized;
    private static Random random;
    private static Field fieldEntityUuid;
    private static boolean working;
    
    public static void entityLoaded(final Entity entity) {
        if (entity instanceof EntityLiving) {
            final EntityLiving el = (EntityLiving)entity;
            final WorldServer ws = Config.getWorldServer();
            if (ws != null) {
                final Entity es = ws.getEntityByID(entity.getEntityId());
                if (es instanceof EntityLiving) {
                    final EntityLiving els = (EntityLiving)es;
                    if (RandomMobs.fieldEntityUuid != null) {
                        try {
                            final Object e = RandomMobs.fieldEntityUuid.get(els);
                            RandomMobs.fieldEntityUuid.set(el, e);
                        }
                        catch (Exception var6) {
                            var6.printStackTrace();
                            RandomMobs.fieldEntityUuid = null;
                        }
                    }
                }
            }
        }
    }
    
    private static Field getField(final Class cls, final Class fieldType) {
        try {
            final Field[] declaredFields;
            final Field[] e = declaredFields = cls.getDeclaredFields();
            for (final Field field : declaredFields) {
                final Class type = field.getType();
                if (type == fieldType) {
                    field.setAccessible(true);
                    return field;
                }
            }
            return null;
        }
        catch (Exception var6) {
            var6.printStackTrace();
            return null;
        }
    }
    
    public static void worldChanged(final World oldWorld, final World newWorld) {
        if (newWorld != null) {
            final List entityList = newWorld.getLoadedEntityList();
            for (int e = 0; e < entityList.size(); ++e) {
                final Entity entity = entityList.get(e);
                entityLoaded(entity);
            }
        }
    }
    
    public static ResourceLocation getTextureLocation(final ResourceLocation loc) {
        if (RandomMobs.working) {
            return loc;
        }
        ResourceLocation var6;
        try {
            RandomMobs.working = true;
            if (!RandomMobs.initialized) {
                initialize();
            }
            if (RandomMobs.renderGlobal == null) {
                return loc;
            }
            final Entity entity = RandomMobs.renderGlobal.renderedEntity;
            if (entity == null) {
                return loc;
            }
            if (!(entity instanceof EntityLiving)) {
                return loc;
            }
            final String name = loc.getResourcePath();
            if (!name.startsWith("textures/entity/")) {
                return loc;
            }
            final long uuidLow = entity.getUniqueID().getLeastSignificantBits();
            final int id = (int)(uuidLow & 0x7FFFFFFFL);
            var6 = getTextureLocation(loc, id);
        }
        finally {
            RandomMobs.working = false;
        }
        return var6;
    }
    
    private static ResourceLocation getTextureLocation(final ResourceLocation loc, final int randomId) {
        if (randomId <= 0) {
            return loc;
        }
        final String name = loc.getResourcePath();
        ResourceLocation[] texLocs = RandomMobs.textureVariantsMap.get(name);
        if (texLocs == null) {
            texLocs = getTextureVariants(loc);
            RandomMobs.textureVariantsMap.put(name, texLocs);
        }
        if (texLocs != null && texLocs.length > 0) {
            final int index = randomId % texLocs.length;
            final ResourceLocation texLoc = texLocs[index];
            return texLoc;
        }
        return loc;
    }
    
    private static ResourceLocation[] getTextureVariants(final ResourceLocation loc) {
        TextureUtils.getTexture(loc);
        ResourceLocation[] texLocs = new ResourceLocation[0];
        final String name = loc.getResourcePath();
        final int pointPos = name.lastIndexOf(46);
        if (pointPos < 0) {
            return texLocs;
        }
        String prefix = name.substring(0, pointPos);
        final String suffix = name.substring(pointPos);
        final String texEntStr = "textures/entity/";
        if (!prefix.startsWith(texEntStr)) {
            return texLocs;
        }
        prefix = prefix.substring(texEntStr.length());
        prefix = "mcpatcher/mob/" + prefix;
        final int countVariants = getCountTextureVariants(prefix, suffix);
        if (countVariants <= 1) {
            return texLocs;
        }
        texLocs = new ResourceLocation[countVariants];
        texLocs[0] = loc;
        for (int i = 1; i < texLocs.length; ++i) {
            final int texNum = i + 1;
            final String texName = prefix + texNum + suffix;
            TextureUtils.getTexture(texLocs[i] = new ResourceLocation(loc.getResourceDomain(), texName));
        }
        Config.dbg("RandomMobs: " + loc + ", variants: " + texLocs.length);
        return texLocs;
    }
    
    private static int getCountTextureVariants(final String prefix, final String suffix) {
        final short maxNum = 1000;
        for (int num = 2; num < maxNum; ++num) {
            final String variant = prefix + num + suffix;
            final ResourceLocation loc = new ResourceLocation(variant);
            if (!Config.hasResource(loc)) {
                return num - 1;
            }
        }
        return maxNum;
    }
    
    public static void resetTextures() {
        RandomMobs.textureVariantsMap.clear();
        if (Config.isRandomMobs()) {
            initialize();
        }
    }
    
    private static void initialize() {
        RandomMobs.renderGlobal = Config.getRenderGlobal();
        if (RandomMobs.renderGlobal != null) {
            RandomMobs.initialized = true;
            final ArrayList list = new ArrayList();
            list.add("bat");
            list.add("blaze");
            list.add("cat/black");
            list.add("cat/ocelot");
            list.add("cat/red");
            list.add("cat/siamese");
            list.add("chicken");
            list.add("cow/cow");
            list.add("cow/mooshroom");
            list.add("creeper/creeper");
            list.add("enderman/enderman");
            list.add("enderman/enderman_eyes");
            list.add("ghast/ghast");
            list.add("ghast/ghast_shooting");
            list.add("iron_golem");
            list.add("pig/pig");
            list.add("sheep/sheep");
            list.add("sheep/sheep_fur");
            list.add("silverfish");
            list.add("skeleton/skeleton");
            list.add("skeleton/wither_skeleton");
            list.add("slime/slime");
            list.add("slime/magmacube");
            list.add("snowman");
            list.add("spider/cave_spider");
            list.add("spider/spider");
            list.add("spider_eyes");
            list.add("squid");
            list.add("villager/villager");
            list.add("villager/butcher");
            list.add("villager/farmer");
            list.add("villager/librarian");
            list.add("villager/priest");
            list.add("villager/smith");
            list.add("wither/wither");
            list.add("wither/wither_armor");
            list.add("wither/wither_invulnerable");
            list.add("wolf/wolf");
            list.add("wolf/wolf_angry");
            list.add("wolf/wolf_collar");
            list.add("wolf/wolf_tame");
            list.add("zombie_pigman");
            list.add("zombie/zombie");
            list.add("zombie/zombie_villager");
            for (int i = 0; i < list.size(); ++i) {
                final String name = list.get(i);
                final String tex = "textures/entity/" + name + ".png";
                final ResourceLocation texLoc = new ResourceLocation(tex);
                if (!Config.hasResource(texLoc)) {
                    Config.warn("Not found: " + texLoc);
                }
                getTextureLocation(texLoc, 100);
            }
        }
    }
    
    static {
        RandomMobs.textureVariantsMap = new HashMap();
        RandomMobs.renderGlobal = null;
        RandomMobs.initialized = false;
        RandomMobs.random = new Random();
        RandomMobs.fieldEntityUuid = getField(Entity.class, UUID.class);
        RandomMobs.working = false;
    }
}
