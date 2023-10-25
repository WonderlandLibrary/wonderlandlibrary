/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.storage;

import javax.annotation.Nullable;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class SaveDataMemoryStorage
extends MapStorage {
    public SaveDataMemoryStorage() {
        super(null);
    }

    @Override
    @Nullable
    public WorldSavedData getOrLoadData(Class<? extends WorldSavedData> clazz, String dataIdentifier) {
        return (WorldSavedData)this.loadedDataMap.get(dataIdentifier);
    }

    @Override
    public void setData(String dataIdentifier, WorldSavedData data) {
        this.loadedDataMap.put(dataIdentifier, data);
    }

    @Override
    public void saveAllData() {
    }

    @Override
    public int getUniqueDataId(String key) {
        return 0;
    }
}

