/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.utils;

import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.WorldInfo;

public class FakeWorld
extends World {
    public FakeWorld(WorldInfo info, WorldProvider worldProvider) {
        super(null, info, worldProvider, null, false);
    }

    @Override
    protected IChunkProvider createChunkProvider() {
        return null;
    }

    @Override
    protected boolean isChunkLoaded(int x, int z, boolean allowEmpty) {
        return false;
    }

    @Override
    protected int getRenderDistanceChunks() {
        return 0;
    }
}

