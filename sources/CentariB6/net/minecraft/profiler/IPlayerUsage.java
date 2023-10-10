package net.minecraft.profiler;

import net.minecraft.profiler.PlayerUsageSnooper;

public interface IPlayerUsage {
   boolean isSnooperEnabled();

   void addServerStatsToSnooper(PlayerUsageSnooper var1);

   void addServerTypeToSnooper(PlayerUsageSnooper var1);
}
