// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage;

import javax.annotation.Nullable;

public class SaveDataMemoryStorage extends MapStorage
{
    public SaveDataMemoryStorage() {
        super(null);
    }
    
    @Nullable
    @Override
    public WorldSavedData getOrLoadData(final Class<? extends WorldSavedData> clazz, final String dataIdentifier) {
        return this.loadedDataMap.get(dataIdentifier);
    }
    
    @Override
    public void setData(final String dataIdentifier, final WorldSavedData data) {
        this.loadedDataMap.put(dataIdentifier, data);
    }
    
    @Override
    public void saveAllData() {
    }
    
    @Override
    public int getUniqueDataId(final String key) {
        return 0;
    }
}
