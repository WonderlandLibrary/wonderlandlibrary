package net.minecraft.profiler;

public abstract interface IPlayerUsage
{
  public abstract void addServerStatsToSnooper(PlayerUsageSnooper paramPlayerUsageSnooper);
  
  public abstract void addServerTypeToSnooper(PlayerUsageSnooper paramPlayerUsageSnooper);
  
  public abstract boolean isSnooperEnabled();
}
