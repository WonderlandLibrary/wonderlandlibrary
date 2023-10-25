package net.minecraft.world.storage;

import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.MapStorage;

public class SaveDataMemoryStorage
extends MapStorage {
    public SaveDataMemoryStorage() {
        super((ISaveHandler)null);
    }

    public WorldSavedData loadData(Class<? extends WorldSavedData> clazz, String dataIdentifier) {
        return (WorldSavedData)this.loadedDataMap.get((Object)dataIdentifier);
    }

    public void setData(String dataIdentifier, WorldSavedData data) {
        this.loadedDataMap.put((Object)dataIdentifier, (Object)data);
    }

    public void saveAllData() {
    }

    public int getUniqueDataId(String key) {
        return 0;
    }
}
