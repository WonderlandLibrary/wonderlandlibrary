package optifine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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

public class DynamicLights {
   private static Map mapDynamicLights = new HashMap();
   private static long timeUpdateMs = 0L;
   private static final double MAX_DIST = 7.5D;
   private static final double MAX_DIST_SQ = 56.25D;
   private static final int LIGHT_LEVEL_MAX = 15;
   private static final int LIGHT_LEVEL_FIRE = 15;
   private static final int LIGHT_LEVEL_BLAZE = 10;
   private static final int LIGHT_LEVEL_MAGMA_CUBE = 8;
   private static final int LIGHT_LEVEL_MAGMA_CUBE_CORE = 13;
   private static final int LIGHT_LEVEL_GLOWSTONE_DUST = 8;
   private static final int LIGHT_LEVEL_PRISMARINE_CRYSTALS = 8;

   public static void entityAdded(Entity var0, RenderGlobal var1) {
   }

   public static void entityRemoved(Entity var0, RenderGlobal var1) {
      Map var2;
      synchronized(var2 = mapDynamicLights){}
      DynamicLight var3 = (DynamicLight)mapDynamicLights.remove(IntegerCache.valueOf(var0.getEntityId()));
      if (var3 != null) {
         var3.updateLitChunks(var1);
      }

   }

   public static void update(RenderGlobal var0) {
      long var1 = System.currentTimeMillis();
      if (var1 >= timeUpdateMs + 50L) {
         timeUpdateMs = var1;
         Map var3;
         synchronized(var3 = mapDynamicLights){}
         updateMapDynamicLights(var0);
         if (mapDynamicLights.size() > 0) {
            Iterator var5 = mapDynamicLights.values().iterator();

            while(var5.hasNext()) {
               DynamicLight var4 = (DynamicLight)var5.next();
               var4.update(var0);
            }
         }
      }

   }

   private static void updateMapDynamicLights(RenderGlobal var0) {
      WorldClient var1 = var0.getWorld();
      if (var1 != null) {
         Iterator var3 = var1.getLoadedEntityList().iterator();

         while(var3.hasNext()) {
            Entity var2 = (Entity)var3.next();
            int var4 = getLightLevel(var2);
            Integer var5;
            DynamicLight var6;
            if (var4 > 0) {
               var5 = IntegerCache.valueOf(var2.getEntityId());
               var6 = (DynamicLight)mapDynamicLights.get(var5);
               if (var6 == null) {
                  var6 = new DynamicLight(var2);
                  mapDynamicLights.put(var5, var6);
               }
            } else {
               var5 = IntegerCache.valueOf(var2.getEntityId());
               var6 = (DynamicLight)mapDynamicLights.remove(var5);
               if (var6 != null) {
                  var6.updateLitChunks(var0);
               }
            }
         }
      }

   }

   public static int getCombinedLight(BlockPos var0, int var1) {
      double var2 = getLightLevel(var0);
      var1 = getCombinedLight(var2, var1);
      return var1;
   }

   public static int getCombinedLight(Entity var0, int var1) {
      double var2 = (double)getLightLevel(var0);
      var1 = getCombinedLight(var2, var1);
      return var1;
   }

   public static int getCombinedLight(double var0, int var2) {
      if (var0 > 0.0D) {
         int var3 = (int)(var0 * 16.0D);
         int var4 = var2 & 255;
         if (var3 > var4) {
            var2 &= -256;
            var2 |= var3;
         }
      }

      return var2;
   }

   public static double getLightLevel(BlockPos var0) {
      double var1 = 0.0D;
      Map var3;
      synchronized(var3 = mapDynamicLights){}
      Iterator var5 = mapDynamicLights.values().iterator();

      while(var5.hasNext()) {
         DynamicLight var4 = (DynamicLight)var5.next();
         int var6 = var4.getLastLightLevel();
         if (var6 > 0) {
            double var7 = var4.getLastPosX();
            double var9 = var4.getLastPosY();
            double var11 = var4.getLastPosZ();
            double var13 = (double)var0.getX() - var7;
            double var15 = (double)var0.getY() - var9;
            double var17 = (double)var0.getZ() - var11;
            double var19 = var13 * var13 + var15 * var15 + var17 * var17;
            if (var4.isUnderwater() && !Config.isClearWater()) {
               var6 = Config.limit(var6 - 2, 0, 15);
               var19 *= 2.0D;
            }

            if (var19 <= 56.25D) {
               double var21 = Math.sqrt(var19);
               double var23 = 1.0D - var21 / 7.5D;
               double var25 = var23 * (double)var6;
               if (var25 > var1) {
                  var1 = var25;
               }
            }
         }
      }

      double var27 = Config.limit(var1, 0.0D, 15.0D);
      return var27;
   }

   public static int getLightLevel(ItemStack var0) {
      if (var0 == null) {
         return 0;
      } else {
         Item var1 = var0.getItem();
         if (var1 instanceof ItemBlock) {
            ItemBlock var2 = (ItemBlock)var1;
            Block var3 = var2.getBlock();
            if (var3 != null) {
               return var3.getLightValue();
            }
         }

         return var1 == Items.lava_bucket ? Blocks.lava.getLightValue() : (var1 != Items.blaze_rod && var1 != Items.blaze_powder ? (var1 == Items.glowstone_dust ? 8 : (var1 == Items.prismarine_crystals ? 8 : (var1 == Items.magma_cream ? 8 : (var1 == Items.nether_star ? Blocks.beacon.getLightValue() / 2 : 0)))) : 10);
      }
   }

   public static int getLightLevel(Entity var0) {
      if (var0 == Config.getMinecraft().getRenderViewEntity() && !Config.isDynamicHandLight()) {
         return 0;
      } else {
         if (var0 instanceof EntityPlayer) {
            EntityPlayer var1 = (EntityPlayer)var0;
            if (var1.isSpectator()) {
               return 0;
            }
         }

         if (var0.isBurning()) {
            return 15;
         } else if (var0 instanceof EntityFireball) {
            return 15;
         } else if (var0 instanceof EntityTNTPrimed) {
            return 15;
         } else if (var0 instanceof EntityBlaze) {
            EntityBlaze var10 = (EntityBlaze)var0;
            return var10.func_70845_n() ? 15 : 10;
         } else if (var0 instanceof EntityMagmaCube) {
            EntityMagmaCube var9 = (EntityMagmaCube)var0;
            return (double)var9.squishFactor > 0.6D ? 13 : 8;
         } else {
            if (var0 instanceof EntityCreeper) {
               EntityCreeper var6 = (EntityCreeper)var0;
               if ((double)var6.getCreeperFlashIntensity(0.0F) > 0.001D) {
                  return 15;
               }
            }

            ItemStack var2;
            if (var0 instanceof EntityLivingBase) {
               EntityLivingBase var8 = (EntityLivingBase)var0;
               var2 = var8.getHeldItem();
               int var3 = getLightLevel(var2);
               ItemStack var4 = var8.getEquipmentInSlot(4);
               int var5 = getLightLevel(var4);
               return Math.max(var3, var5);
            } else if (var0 instanceof EntityItem) {
               EntityItem var7 = (EntityItem)var0;
               var2 = getItemStack(var7);
               return getLightLevel(var2);
            } else {
               return 0;
            }
         }
      }
   }

   public static void removeLights(RenderGlobal var0) {
      Map var1;
      synchronized(var1 = mapDynamicLights){}
      Collection var2 = mapDynamicLights.values();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         DynamicLight var4 = (DynamicLight)var3.next();
         var3.remove();
         var4.updateLitChunks(var0);
      }

   }

   public static void clear() {
      Map var0;
      synchronized(var0 = mapDynamicLights){}
      mapDynamicLights.clear();
   }

   public static int getCount() {
      Map var0;
      synchronized(var0 = mapDynamicLights){}
      return mapDynamicLights.size();
   }

   public static ItemStack getItemStack(EntityItem var0) {
      ItemStack var1 = var0.getDataWatcher().getWatchableObjectItemStack(10);
      return var1;
   }
}
