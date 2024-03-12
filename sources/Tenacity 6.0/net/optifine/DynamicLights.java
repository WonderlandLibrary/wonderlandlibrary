// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import java.util.HashMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import java.util.Iterator;
import net.minecraft.world.World;
import java.util.Properties;
import net.optifine.config.ItemLocator;
import net.optifine.config.IObjectLocator;
import net.optifine.config.EntityClassLocator;
import net.optifine.config.ConnectedParser;
import net.optifine.util.PropertiesOrdered;
import java.io.InputStream;
import java.io.IOException;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import net.optifine.reflect.ReflectorForge;
import java.util.List;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import java.util.Map;

public class DynamicLights
{
    private static DynamicLightsMap mapDynamicLights;
    private static Map<Class, Integer> mapEntityLightLevels;
    private static Map<Item, Integer> mapItemLightLevels;
    private static long timeUpdateMs;
    private static final double MAX_DIST = 7.5;
    private static final double MAX_DIST_SQ = 56.25;
    private static final int LIGHT_LEVEL_MAX = 15;
    private static final int LIGHT_LEVEL_FIRE = 15;
    private static final int LIGHT_LEVEL_BLAZE = 10;
    private static final int LIGHT_LEVEL_MAGMA_CUBE = 8;
    private static final int LIGHT_LEVEL_MAGMA_CUBE_CORE = 13;
    private static final int LIGHT_LEVEL_GLOWSTONE_DUST = 8;
    private static final int LIGHT_LEVEL_PRISMARINE_CRYSTALS = 8;
    private static boolean initialized;
    
    public static void entityAdded(final Entity entityIn, final RenderGlobal renderGlobal) {
    }
    
    public static void entityRemoved(final Entity entityIn, final RenderGlobal renderGlobal) {
        synchronized (DynamicLights.mapDynamicLights) {
            final DynamicLight dynamiclight = DynamicLights.mapDynamicLights.remove(entityIn.getEntityId());
            if (dynamiclight != null) {
                dynamiclight.updateLitChunks(renderGlobal);
            }
        }
    }
    
    public static void update(final RenderGlobal renderGlobal) {
        final long i = System.currentTimeMillis();
        if (i >= DynamicLights.timeUpdateMs + 50L) {
            DynamicLights.timeUpdateMs = i;
            if (!DynamicLights.initialized) {
                initialize();
            }
            synchronized (DynamicLights.mapDynamicLights) {
                updateMapDynamicLights(renderGlobal);
                if (DynamicLights.mapDynamicLights.size() > 0) {
                    final List<DynamicLight> list = DynamicLights.mapDynamicLights.valueList();
                    for (int j = 0; j < list.size(); ++j) {
                        final DynamicLight dynamiclight = list.get(j);
                        dynamiclight.update(renderGlobal);
                    }
                }
            }
        }
    }
    
    private static void initialize() {
        DynamicLights.initialized = true;
        DynamicLights.mapEntityLightLevels.clear();
        DynamicLights.mapItemLightLevels.clear();
        final String[] astring = ReflectorForge.getForgeModIds();
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            try {
                final ResourceLocation resourcelocation = new ResourceLocation(s, "optifine/dynamic_lights.properties");
                final InputStream inputstream = Config.getResourceStream(resourcelocation);
                loadModConfiguration(inputstream, resourcelocation.toString(), s);
            }
            catch (IOException ex) {}
        }
        if (DynamicLights.mapEntityLightLevels.size() > 0) {
            Config.dbg("DynamicLights entities: " + DynamicLights.mapEntityLightLevels.size());
        }
        if (DynamicLights.mapItemLightLevels.size() > 0) {
            Config.dbg("DynamicLights items: " + DynamicLights.mapItemLightLevels.size());
        }
    }
    
    private static void loadModConfiguration(final InputStream in, final String path, final String modId) {
        if (in != null) {
            try {
                final Properties properties = new PropertiesOrdered();
                properties.load(in);
                in.close();
                Config.dbg("DynamicLights: Parsing " + path);
                final ConnectedParser connectedparser = new ConnectedParser("DynamicLights");
                loadModLightLevels(properties.getProperty("entities"), DynamicLights.mapEntityLightLevels, new EntityClassLocator(), connectedparser, path, modId);
                loadModLightLevels(properties.getProperty("items"), DynamicLights.mapItemLightLevels, new ItemLocator(), connectedparser, path, modId);
            }
            catch (IOException var5) {
                Config.warn("DynamicLights: Error reading " + path);
            }
        }
    }
    
    private static void loadModLightLevels(final String prop, final Map mapLightLevels, final IObjectLocator ol, final ConnectedParser cp, final String path, final String modId) {
        if (prop != null) {
            final String[] astring = Config.tokenize(prop, " ");
            for (int i = 0; i < astring.length; ++i) {
                final String s = astring[i];
                final String[] astring2 = Config.tokenize(s, ":");
                if (astring2.length != 2) {
                    cp.warn("Invalid entry: " + s + ", in:" + path);
                }
                else {
                    final String s2 = astring2[0];
                    final String s3 = astring2[1];
                    final String s4 = modId + ":" + s2;
                    final ResourceLocation resourcelocation = new ResourceLocation(s4);
                    final Object object = ol.getObject(resourcelocation);
                    if (object == null) {
                        cp.warn("Object not found: " + s4);
                    }
                    else {
                        final int j = cp.parseInt(s3, -1);
                        if (j >= 0 && j <= 15) {
                            mapLightLevels.put(object, new Integer(j));
                        }
                        else {
                            cp.warn("Invalid light level: " + s);
                        }
                    }
                }
            }
        }
    }
    
    private static void updateMapDynamicLights(final RenderGlobal renderGlobal) {
        final World world = renderGlobal.getWorld();
        if (world != null) {
            for (final Entity entity : world.getLoadedEntityList()) {
                final int i = getLightLevel(entity);
                if (i > 0) {
                    final int j = entity.getEntityId();
                    DynamicLight dynamiclight = DynamicLights.mapDynamicLights.get(j);
                    if (dynamiclight != null) {
                        continue;
                    }
                    dynamiclight = new DynamicLight(entity);
                    DynamicLights.mapDynamicLights.put(j, dynamiclight);
                }
                else {
                    final int k = entity.getEntityId();
                    final DynamicLight dynamiclight2 = DynamicLights.mapDynamicLights.remove(k);
                    if (dynamiclight2 == null) {
                        continue;
                    }
                    dynamiclight2.updateLitChunks(renderGlobal);
                }
            }
        }
    }
    
    public static int getCombinedLight(final BlockPos pos, int combinedLight) {
        final double d0 = getLightLevel(pos);
        combinedLight = getCombinedLight(d0, combinedLight);
        return combinedLight;
    }
    
    public static int getCombinedLight(final Entity entity, int combinedLight) {
        final double d0 = getLightLevel(entity);
        combinedLight = getCombinedLight(d0, combinedLight);
        return combinedLight;
    }
    
    public static int getCombinedLight(final double lightPlayer, int combinedLight) {
        if (lightPlayer > 0.0) {
            final int i = (int)(lightPlayer * 16.0);
            final int j = combinedLight & 0xFF;
            if (i > j) {
                combinedLight &= 0xFFFFFF00;
                combinedLight |= i;
            }
        }
        return combinedLight;
    }
    
    public static double getLightLevel(final BlockPos pos) {
        double d0 = 0.0;
        synchronized (DynamicLights.mapDynamicLights) {
            final List<DynamicLight> list = DynamicLights.mapDynamicLights.valueList();
            for (int i = list.size(), j = 0; j < i; ++j) {
                final DynamicLight dynamiclight = list.get(j);
                int k = dynamiclight.getLastLightLevel();
                if (k > 0) {
                    final double d2 = dynamiclight.getLastPosX();
                    final double d3 = dynamiclight.getLastPosY();
                    final double d4 = dynamiclight.getLastPosZ();
                    final double d5 = pos.getX() - d2;
                    final double d6 = pos.getY() - d3;
                    final double d7 = pos.getZ() - d4;
                    double d8 = d5 * d5 + d6 * d6 + d7 * d7;
                    if (dynamiclight.isUnderwater() && !Config.isClearWater()) {
                        k = Config.limit(k - 2, 0, 15);
                        d8 *= 2.0;
                    }
                    if (d8 <= 56.25) {
                        final double d9 = Math.sqrt(d8);
                        final double d10 = 1.0 - d9 / 7.5;
                        final double d11 = d10 * k;
                        if (d11 > d0) {
                            d0 = d11;
                        }
                    }
                }
            }
        }
        final double d12 = Config.limit(d0, 0.0, 15.0);
        return d12;
    }
    
    public static int getLightLevel(final ItemStack itemStack) {
        if (itemStack == null) {
            return 0;
        }
        final Item item = itemStack.getItem();
        if (item instanceof ItemBlock) {
            final ItemBlock itemblock = (ItemBlock)item;
            final Block block = itemblock.getBlock();
            if (block != null) {
                return block.getLightValue();
            }
        }
        if (item == Items.lava_bucket) {
            return Blocks.lava.getLightValue();
        }
        if (item == Items.blaze_rod || item == Items.blaze_powder) {
            return 10;
        }
        if (item == Items.glowstone_dust) {
            return 8;
        }
        if (item == Items.prismarine_crystals) {
            return 8;
        }
        if (item == Items.magma_cream) {
            return 8;
        }
        if (item == Items.nether_star) {
            return Blocks.beacon.getLightValue() / 2;
        }
        if (!DynamicLights.mapItemLightLevels.isEmpty()) {
            final Integer integer = DynamicLights.mapItemLightLevels.get(item);
            if (integer != null) {
                return integer;
            }
        }
        return 0;
    }
    
    public static int getLightLevel(final Entity entity) {
        if (entity == Config.getMinecraft().getRenderViewEntity() && !Config.isDynamicHandLight()) {
            return 0;
        }
        if (entity instanceof EntityPlayer) {
            final EntityPlayer entityplayer = (EntityPlayer)entity;
            if (entityplayer.isSpectator()) {
                return 0;
            }
        }
        if (entity.isBurning()) {
            return 15;
        }
        if (!DynamicLights.mapEntityLightLevels.isEmpty()) {
            final Integer integer = DynamicLights.mapEntityLightLevels.get(entity.getClass());
            if (integer != null) {
                return integer;
            }
        }
        if (entity instanceof EntityFireball) {
            return 15;
        }
        if (entity instanceof EntityTNTPrimed) {
            return 15;
        }
        if (entity instanceof EntityBlaze) {
            final EntityBlaze entityblaze = (EntityBlaze)entity;
            return entityblaze.func_70845_n() ? 15 : 10;
        }
        if (entity instanceof EntityMagmaCube) {
            final EntityMagmaCube entitymagmacube = (EntityMagmaCube)entity;
            return (entitymagmacube.squishFactor > 0.6) ? 13 : 8;
        }
        if (entity instanceof EntityCreeper) {
            final EntityCreeper entitycreeper = (EntityCreeper)entity;
            if (entitycreeper.getCreeperFlashIntensity(0.0f) > 0.001) {
                return 15;
            }
        }
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
            final ItemStack itemstack2 = entitylivingbase.getHeldItem();
            final int i = getLightLevel(itemstack2);
            final ItemStack itemstack3 = entitylivingbase.getEquipmentInSlot(4);
            final int j = getLightLevel(itemstack3);
            return Math.max(i, j);
        }
        if (entity instanceof EntityItem) {
            final EntityItem entityitem = (EntityItem)entity;
            final ItemStack itemstack4 = getItemStack(entityitem);
            return getLightLevel(itemstack4);
        }
        return 0;
    }
    
    public static void removeLights(final RenderGlobal renderGlobal) {
        synchronized (DynamicLights.mapDynamicLights) {
            final List<DynamicLight> list = DynamicLights.mapDynamicLights.valueList();
            for (int i = 0; i < list.size(); ++i) {
                final DynamicLight dynamiclight = list.get(i);
                dynamiclight.updateLitChunks(renderGlobal);
            }
            DynamicLights.mapDynamicLights.clear();
        }
    }
    
    public static void clear() {
        synchronized (DynamicLights.mapDynamicLights) {
            DynamicLights.mapDynamicLights.clear();
        }
    }
    
    public static int getCount() {
        synchronized (DynamicLights.mapDynamicLights) {
            return DynamicLights.mapDynamicLights.size();
        }
    }
    
    public static ItemStack getItemStack(final EntityItem entityItem) {
        final ItemStack itemstack = entityItem.getDataWatcher().getWatchableObjectItemStack(10);
        return itemstack;
    }
    
    static {
        DynamicLights.mapDynamicLights = new DynamicLightsMap();
        DynamicLights.mapEntityLightLevels = new HashMap<Class, Integer>();
        DynamicLights.mapItemLightLevels = new HashMap<Item, Integer>();
        DynamicLights.timeUpdateMs = 0L;
    }
}
