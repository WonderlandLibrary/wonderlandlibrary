package net.minecraft.profiler;

import net.minecraft.profiler.PlayerUsageSnooper;

public interface IPlayerUsage {
    public void addServerStatsToSnooper(PlayerUsageSnooper var1);

    public void addServerTypeToSnooper(PlayerUsageSnooper var1);

    public boolean isSnooperEnabled();
}
