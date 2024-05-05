package optifine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBeacon;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.DataWatcher;
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

public class DynamicLights
{
  private static Map<Integer, DynamicLight> mapDynamicLights = new HashMap();
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
  
  public DynamicLights() {}
  
  public static void entityAdded(Entity entityIn, RenderGlobal renderGlobal) {}
  
  public static void entityRemoved(Entity entityIn, RenderGlobal renderGlobal)
  {
    Map var2 = mapDynamicLights;
    synchronized (mapDynamicLights)
    {
      DynamicLight dynamicLight = (DynamicLight)mapDynamicLights.remove(IntegerCache.valueOf(entityIn.getEntityId()));
      if (dynamicLight != null) {
        dynamicLight.updateLitChunks(renderGlobal);
      }
    }
  }
  
  public static void update(RenderGlobal renderGlobal)
  {
    long timeNowMs = System.currentTimeMillis();
    if (timeNowMs >= timeUpdateMs + 50L)
    {
      timeUpdateMs = timeNowMs;
      Map var3 = mapDynamicLights;
      synchronized (mapDynamicLights)
      {
        updateMapDynamicLights(renderGlobal);
        if (mapDynamicLights.size() > 0)
        {
          Collection dynamicLights = mapDynamicLights.values();
          Iterator it = dynamicLights.iterator();
          while (it.hasNext())
          {
            DynamicLight dynamicLight = (DynamicLight)it.next();
            dynamicLight.update(renderGlobal);
          }
        }
      }
    }
  }
  
  private static void updateMapDynamicLights(RenderGlobal renderGlobal)
  {
    WorldClient world = renderGlobal.getWorld();
    if (world != null)
    {
      List entities = world.getLoadedEntityList();
      Iterator it = entities.iterator();
      while (it.hasNext())
      {
        Entity entity = (Entity)it.next();
        int lightLevel = getLightLevel(entity);
        if (lightLevel > 0)
        {
          Integer key = IntegerCache.valueOf(entity.getEntityId());
          DynamicLight dynamicLight = (DynamicLight)mapDynamicLights.get(key);
          if (dynamicLight == null)
          {
            dynamicLight = new DynamicLight(entity);
            mapDynamicLights.put(key, dynamicLight);
          }
        }
        else
        {
          Integer key = IntegerCache.valueOf(entity.getEntityId());
          DynamicLight dynamicLight = (DynamicLight)mapDynamicLights.remove(key);
          if (dynamicLight != null) {
            dynamicLight.updateLitChunks(renderGlobal);
          }
        }
      }
    }
  }
  
  public static int getCombinedLight(BlockPos pos, int combinedLight)
  {
    double lightPlayer = getLightLevel(pos);
    combinedLight = getCombinedLight(lightPlayer, combinedLight);
    return combinedLight;
  }
  
  public static int getCombinedLight(Entity entity, int combinedLight)
  {
    double lightPlayer = getLightLevel(entity);
    combinedLight = getCombinedLight(lightPlayer, combinedLight);
    return combinedLight;
  }
  
  public static int getCombinedLight(double lightPlayer, int combinedLight)
  {
    if (lightPlayer > 0.0D)
    {
      int lightPlayerFF = (int)(lightPlayer * 16.0D);
      int lightBlockFF = combinedLight & 0xFF;
      if (lightPlayerFF > lightBlockFF)
      {
        combinedLight &= 0xFF00;
        combinedLight |= lightPlayerFF;
      }
    }
    return combinedLight;
  }
  
  public static double getLightLevel(BlockPos pos)
  {
    double lightLevelMax = 0.0D;
    Map lightPlayer = mapDynamicLights;
    synchronized (mapDynamicLights)
    {
      Collection dynamicLights = mapDynamicLights.values();
      Iterator it = dynamicLights.iterator();
      while (it.hasNext())
      {
        DynamicLight dynamicLight = (DynamicLight)it.next();
        int dynamicLightLevel = dynamicLight.getLastLightLevel();
        if (dynamicLightLevel > 0)
        {
          double px = dynamicLight.getLastPosX();
          double py = dynamicLight.getLastPosY();
          double pz = dynamicLight.getLastPosZ();
          double dx = pos.getX() - px;
          double dy = pos.getY() - py;
          double dz = pos.getZ() - pz;
          double distSq = dx * dx + dy * dy + dz * dz;
          if ((dynamicLight.isUnderwater()) && (!Config.isClearWater()))
          {
            dynamicLightLevel = Config.limit(dynamicLightLevel - 2, 0, 15);
            distSq *= 2.0D;
          }
          if (distSq <= 56.25D)
          {
            double dist = Math.sqrt(distSq);
            double light = 1.0D - dist / 7.5D;
            double lightLevel = light * dynamicLightLevel;
            if (lightLevel > lightLevelMax) {
              lightLevelMax = lightLevel;
            }
          }
        }
      }
    }
    double lightPlayer1 = Config.limit(lightLevelMax, 0.0D, 15.0D);
    return lightPlayer1;
  }
  
  public static int getLightLevel(ItemStack itemStack)
  {
    if (itemStack == null) {
      return 0;
    }
    Item item = itemStack.getItem();
    if ((item instanceof ItemBlock))
    {
      ItemBlock itemBlock = (ItemBlock)item;
      Block block = itemBlock.getBlock();
      if (block != null) {
        return block.getLightValue();
      }
    }
    return (item != Items.blaze_rod) && (item != Items.blaze_powder) ? 0 : item == Items.nether_star ? Blocks.beacon.getLightValue() / 2 : item == Items.magma_cream ? 8 : item == Items.prismarine_crystals ? 8 : item == Items.glowstone_dust ? 8 : item == Items.lava_bucket ? Blocks.lava.getLightValue() : 10;
  }
  
  public static int getLightLevel(Entity entity)
  {
    if ((entity == Config.getMinecraft().func_175606_aa()) && (!Config.isDynamicHandLight())) {
      return 0;
    }
    if ((entity instanceof EntityPlayer))
    {
      EntityPlayer entityItem = (EntityPlayer)entity;
      if (entityItem.func_175149_v()) {
        return 0;
      }
    }
    if (entity.isBurning()) {
      return 15;
    }
    if ((entity instanceof EntityFireball)) {
      return 15;
    }
    if ((entity instanceof EntityTNTPrimed)) {
      return 15;
    }
    if ((entity instanceof EntityBlaze))
    {
      EntityBlaze entityItem5 = (EntityBlaze)entity;
      return entityItem5.func_70845_n() ? 15 : 10;
    }
    if ((entity instanceof EntityMagmaCube))
    {
      EntityMagmaCube entityItem4 = (EntityMagmaCube)entity;
      return entityItem4.squishFactor > 0.6D ? 13 : 8;
    }
    if ((entity instanceof EntityCreeper))
    {
      EntityCreeper entityItem1 = (EntityCreeper)entity;
      if (entityItem1.getCreeperFlashIntensity(0.0F) > 0.001D) {
        return 15;
      }
    }
    if ((entity instanceof EntityLivingBase))
    {
      EntityLivingBase entityItem3 = (EntityLivingBase)entity;
      ItemStack itemStack = entityItem3.getHeldItem();
      int levelMain = getLightLevel(itemStack);
      ItemStack stackHead = entityItem3.getEquipmentInSlot(4);
      int levelHead = getLightLevel(stackHead);
      return Math.max(levelMain, levelHead);
    }
    if ((entity instanceof EntityItem))
    {
      EntityItem entityItem2 = (EntityItem)entity;
      ItemStack itemStack = getItemStack(entityItem2);
      return getLightLevel(itemStack);
    }
    return 0;
  }
  
  public static void removeLights(RenderGlobal renderGlobal)
  {
    Map var1 = mapDynamicLights;
    synchronized (mapDynamicLights)
    {
      Collection lights = mapDynamicLights.values();
      Iterator it = lights.iterator();
      while (it.hasNext())
      {
        DynamicLight dynamicLight = (DynamicLight)it.next();
        it.remove();
        dynamicLight.updateLitChunks(renderGlobal);
      }
    }
  }
  
  public static void clear()
  {
    Map var0 = mapDynamicLights;
    synchronized (mapDynamicLights)
    {
      mapDynamicLights.clear();
    }
  }
  
  /* Error */
  public static int getCount()
  {
    // Byte code:
    //   0: getstatic 2	optifine/DynamicLights:mapDynamicLights	Ljava/util/Map;
    //   3: astore_0
    //   4: getstatic 2	optifine/DynamicLights:mapDynamicLights	Ljava/util/Map;
    //   7: dup
    //   8: astore_1
    //   9: monitorenter
    //   10: getstatic 2	optifine/DynamicLights:mapDynamicLights	Ljava/util/Map;
    //   13: invokeinterface 13 1 0
    //   18: aload_1
    //   19: monitorexit
    //   20: ireturn
    //   21: astore_2
    //   22: aload_1
    //   23: monitorexit
    //   24: aload_2
    //   25: athrow
    // Line number table:
    //   Java source line #345	-> byte code offset #0
    //   Java source line #347	-> byte code offset #4
    //   Java source line #349	-> byte code offset #10
    //   Java source line #350	-> byte code offset #21
    // Local variable table:
    //   start	length	slot	name	signature
    //   3	2	0	var0	Map
    //   8	15	1	Ljava/lang/Object;	Object
    //   21	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   10	20	21	finally
    //   21	24	21	finally
  }
  
  public static ItemStack getItemStack(EntityItem entityItem)
  {
    ItemStack itemstack = entityItem.getDataWatcher().getWatchableObjectItemStack(10);
    return itemstack;
  }
}
