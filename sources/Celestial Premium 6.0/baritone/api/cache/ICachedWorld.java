/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.cache;

import baritone.api.cache.ICachedRegion;
import java.util.ArrayList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

public interface ICachedWorld {
    public ICachedRegion getRegion(int var1, int var2);

    public void queueForPacking(Chunk var1);

    public boolean isCached(int var1, int var2);

    public ArrayList<BlockPos> getLocationsOf(String var1, int var2, int var3, int var4, int var5);

    public void reloadAllFromDisk();

    public void save();
}

