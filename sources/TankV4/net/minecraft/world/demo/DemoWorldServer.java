package net.minecraft.world.demo;

import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

public class DemoWorldServer extends WorldServer {
   private static final long demoWorldSeed = (long)"North Carolina".hashCode();
   public static final WorldSettings demoWorldSettings;

   public DemoWorldServer(MinecraftServer var1, ISaveHandler var2, WorldInfo var3, int var4, Profiler var5) {
      super(var1, var2, var3, var4, var5);
      this.worldInfo.populateFromWorldSettings(demoWorldSettings);
   }

   static {
      demoWorldSettings = (new WorldSettings(demoWorldSeed, WorldSettings.GameType.SURVIVAL, true, false, WorldType.DEFAULT)).enableBonusChest();
   }
}
