/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.cache;

import baritone.api.cache.IBlockTypeAccess;

public interface ICachedRegion
extends IBlockTypeAccess {
    public boolean isCached(int var1, int var2);

    public int getX();

    public int getZ();
}

