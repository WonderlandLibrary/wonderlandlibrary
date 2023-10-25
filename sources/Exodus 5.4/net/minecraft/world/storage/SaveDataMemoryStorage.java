/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.storage;

import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

public class SaveDataMemoryStorage
extends MapStorage {
    @Override
    public void saveAllData() {
    }

    @Override
    public void setData(String string, WorldSavedData worldSavedData) {
        this.loadedDataMap.put(string, worldSavedData);
    }

    @Override
    public WorldSavedData loadData(Class<? extends WorldSavedData> clazz, String string) {
        return (WorldSavedData)this.loadedDataMap.get(string);
    }

    @Override
    public int getUniqueDataId(String string) {
        return 0;
    }

    public SaveDataMemoryStorage() {
        super(null);
    }
}

