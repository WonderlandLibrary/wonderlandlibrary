/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.storage;

import net.minecraft.world.storage.WorldSavedData;

public class WorldSavedDataCallableSave
implements Runnable {
    private final WorldSavedData data;

    public WorldSavedDataCallableSave(WorldSavedData dataIn) {
        this.data = dataIn;
    }

    @Override
    public void run() {
        this.data.markDirty();
    }
}

