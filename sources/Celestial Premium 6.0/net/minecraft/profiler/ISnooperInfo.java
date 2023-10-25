/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.profiler;

import net.minecraft.profiler.Snooper;

public interface ISnooperInfo {
    public void addServerStatsToSnooper(Snooper var1);

    public void addServerTypeToSnooper(Snooper var1);

    public boolean isSnooperEnabled();
}

