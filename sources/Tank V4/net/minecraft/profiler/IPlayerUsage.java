package net.minecraft.profiler;

public interface IPlayerUsage {
   void addServerTypeToSnooper(PlayerUsageSnooper var1);

   boolean isSnooperEnabled();

   void addServerStatsToSnooper(PlayerUsageSnooper var1);
}
