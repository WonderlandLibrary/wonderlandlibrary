/*
 * Decompiled with CFR 0.150.
 */
package optifine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import optifine.Config;
import optifine.DynamicLight;
import optifine.IntegerCache;

public class DynamicLights {
    private static Map<Integer, DynamicLight> mapDynamicLights = new HashMap<Integer, DynamicLight>();
    private static long timeUpdateMs = 0L;
    private static final double MAX_DIST = 7.5;
    private static final double MAX_DIST_SQ = 56.25;
    private static final int LIGHT_LEVEL_MAX = 15;
    private static final int LIGHT_LEVEL_FIRE = 15;
    private static final int LIGHT_LEVEL_BLAZE = 10;
    private static final int LIGHT_LEVEL_MAGMA_CUBE = 8;
    private static final int LIGHT_LEVEL_MAGMA_CUBE_CORE = 13;
    private static final int LIGHT_LEVEL_GLOWSTONE_DUST = 8;
    private static final int LIGHT_LEVEL_PRISMARINE_CRYSTALS = 8;

    public static void entityAdded(Entity entityIn, RenderGlobal renderGlobal) {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void entityRemoved(Entity entityIn, RenderGlobal renderGlobal) {
        Map<Integer, DynamicLight> var2 = mapDynamicLights;
        Map<Integer, DynamicLight> map = mapDynamicLights;
        synchronized (map) {
            DynamicLight dynamicLight = mapDynamicLights.remove(IntegerCache.valueOf(entityIn.getEntityId()));
            if (dynamicLight != null) {
                dynamicLight.updateLitChunks(renderGlobal);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void update(RenderGlobal renderGlobal) {
        long timeNowMs = System.currentTimeMillis();
        if (timeNowMs >= timeUpdateMs + 50L) {
            timeUpdateMs = timeNowMs;
            Map<Integer, DynamicLight> var3 = mapDynamicLights;
            Map<Integer, DynamicLight> map = mapDynamicLights;
            synchronized (map) {
                DynamicLights.updateMapDynamicLights(renderGlobal);
                if (mapDynamicLights.size() > 0) {
                    Collection<DynamicLight> dynamicLights = mapDynamicLights.values();
                    for (DynamicLight dynamicLight : dynamicLights) {
                        dynamicLight.update(renderGlobal);
                    }
                }
            }
        }
    }

    private static void updateMapDynamicLights(RenderGlobal renderGlobal) {
        WorldClient world = renderGlobal.getWorld();
        if (world != null) {
            List entities = world.getLoadedEntityList();
            for (Entity entity : entities) {
                DynamicLight dynamicLight;
                Integer key;
                int lightLevel = DynamicLights.getLightLevel(entity);
                if (lightLevel > 0) {
                    key = IntegerCache.valueOf(entity.getEntityId());
                    dynamicLight = mapDynamicLights.get(key);
                    if (dynamicLight != null) continue;
                    dynamicLight = new DynamicLight(entity);
                    mapDynamicLights.put(key, dynamicLight);
                    continue;
                }
                key = IntegerCache.valueOf(entity.getEntityId());
                dynamicLight = mapDynamicLights.remove(key);
                if (dynamicLight == null) continue;
                dynamicLight.updateLitChunks(renderGlobal);
            }
        }
    }

    public static int getCombinedLight(BlockPos pos, int combinedLight) {
        double lightPlayer = DynamicLights.getLightLevel(pos);
        combinedLight = DynamicLights.getCombinedLight(lightPlayer, combinedLight);
        return combinedLight;
    }

    public static int getCombinedLight(Entity entity, int combinedLight) {
        double lightPlayer = DynamicLights.getLightLevel(entity);
        combinedLight = DynamicLights.getCombinedLight(lightPlayer, combinedLight);
        return combinedLight;
    }

    public static int getCombinedLight(double lightPlayer, int combinedLight) {
        int lightBlockFF;
        int lightPlayerFF;
        if (lightPlayer > 0.0 && (lightPlayerFF = (int)(lightPlayer * 16.0)) > (lightBlockFF = combinedLight & 0xFF)) {
            combinedLight &= 0xFFFFFF00;
            combinedLight |= lightPlayerFF;
        }
        return combinedLight;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static double getLightLevel(BlockPos pos) {
        double lightLevelMax = 0.0;
        Map<Integer, DynamicLight> lightPlayer = mapDynamicLights;
        Map<Integer, DynamicLight> map = mapDynamicLights;
        synchronized (map) {
            Collection<DynamicLight> dynamicLights = mapDynamicLights.values();
            for (DynamicLight dynamicLight : dynamicLights) {
                double dist;
                double light;
                double lightLevel;
                int dynamicLightLevel = dynamicLight.getLastLightLevel();
                if (dynamicLightLevel <= 0) continue;
                double px = dynamicLight.getLastPosX();
                double py = dynamicLight.getLastPosY();
                double pz = dynamicLight.getLastPosZ();
                double dx = (double)pos.getX() - px;
                double dy = (double)pos.getY() - py;
                double dz = (double)pos.getZ() - pz;
                double distSq = dx * dx + dy * dy + dz * dz;
                if (dynamicLight.isUnderwater() && !Config.isClearWater()) {
                    dynamicLightLevel = Config.limit(dynamicLightLevel - 2, 0, 15);
                    distSq *= 2.0;
                }
                if (!(distSq <= 56.25) || !((lightLevel = (light = 1.0 - (dist = Math.sqrt(distSq)) / 7.5) * (double)dynamicLightLevel) > lightLevelMax)) continue;
                lightLevelMax = lightLevel;
            }
        }
        double lightPlayer1 = Config.limit(lightLevelMax, 0.0, 15.0);
        return lightPlayer1;
    }

    public static int getLightLevel(ItemStack itemStack) {
        ItemBlock itemBlock;
        Block block;
        if (itemStack == null) {
            return 0;
        }
        Item item = itemStack.getItem();
        if (item instanceof ItemBlock && (block = (itemBlock = (ItemBlock)item).getBlock()) != null) {
            return block.getLightValue();
        }
        return item == Items.lava_bucket ? Blocks.lava.getLightValue() : (item != Items.blaze_rod && item != Items.blaze_powder ? (item == Items.glowstone_dust ? 8 : (item == Items.prismarine_crystals ? 8 : (item == Items.magma_cream ? 8 : (item == Items.nether_star ? Blocks.beacon.getLightValue() / 2 : 0)))) : 10);
    }

    public static int getLightLevel(Entity entity) {
        ItemStack itemStack;
        EntityCreeper entityItem1;
        EntityPlayer entityItem;
        if (entity == Config.getMinecraft().func_175606_aa() && !Config.isDynamicHandLight()) {
            return 0;
        }
        if (entity instanceof EntityPlayer && (entityItem = (EntityPlayer)entity).func_175149_v()) {
            return 0;
        }
        if (entity.isBurning()) {
            return 15;
        }
        if (entity instanceof EntityFireball) {
            return 15;
        }
        if (entity instanceof EntityTNTPrimed) {
            return 15;
        }
        if (entity instanceof EntityBlaze) {
            EntityBlaze entityItem5 = (EntityBlaze)entity;
            return entityItem5.func_70845_n() ? 15 : 10;
        }
        if (entity instanceof EntityMagmaCube) {
            EntityMagmaCube entityItem4 = (EntityMagmaCube)entity;
            return (double)entityItem4.squishFactor > 0.6 ? 13 : 8;
        }
        if (entity instanceof EntityCreeper && (double)(entityItem1 = (EntityCreeper)entity).getCreeperFlashIntensity(0.0f) > 0.001) {
            return 15;
        }
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityItem3 = (EntityLivingBase)entity;
            itemStack = entityItem3.getHeldItem();
            int levelMain = DynamicLights.getLightLevel(itemStack);
            ItemStack stackHead = entityItem3.getEquipmentInSlot(4);
            int levelHead = DynamicLights.getLightLevel(stackHead);
            return Math.max(levelMain, levelHead);
        }
        if (entity instanceof EntityItem) {
            EntityItem entityItem2 = (EntityItem)entity;
            itemStack = DynamicLights.getItemStack(entityItem2);
            return DynamicLights.getLightLevel(itemStack);
        }
        return 0;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void removeLights(RenderGlobal renderGlobal) {
        Map<Integer, DynamicLight> var1 = mapDynamicLights;
        Map<Integer, DynamicLight> map = mapDynamicLights;
        synchronized (map) {
            Collection<DynamicLight> lights = mapDynamicLights.values();
            Iterator<DynamicLight> it = lights.iterator();
            while (it.hasNext()) {
                DynamicLight dynamicLight = it.next();
                it.remove();
                dynamicLight.updateLitChunks(renderGlobal);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void clear() {
        Map<Integer, DynamicLight> var0 = mapDynamicLights;
        Map<Integer, DynamicLight> map = mapDynamicLights;
        synchronized (map) {
            mapDynamicLights.clear();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int getCount() {
        Map<Integer, DynamicLight> var0 = mapDynamicLights;
        Map<Integer, DynamicLight> map = mapDynamicLights;
        synchronized (map) {
            return mapDynamicLights.size();
        }
    }

    public static ItemStack getItemStack(EntityItem entityItem) {
        ItemStack itemstack = entityItem.getDataWatcher().getWatchableObjectItemStack(10);
        return itemstack;
    }
}

