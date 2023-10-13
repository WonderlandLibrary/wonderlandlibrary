/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.storage;

import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

public class SaveDataMemoryStorage
extends MapStorage {
    private static final String __OBFID = "CL_00001963";

    public SaveDataMemoryStorage() {
        super(null);
    }

    @Override
    public WorldSavedData loadData(Class p_75742_1_, String p_75742_2_) {
        return (WorldSavedData)this.loadedDataMap.get(p_75742_2_);
    }

    @Override
    public void setData(String p_75745_1_, WorldSavedData p_75745_2_) {
        this.loadedDataMap.put(p_75745_1_, p_75745_2_);
    }

    @Override
    public void saveAllData() {
    }

    @Override
    public int getUniqueDataId(String p_75743_1_) {
        return 0;
    }
}

