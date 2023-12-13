// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.storage;

import net.minecraft.world.WorldSavedData;

public class SaveDataMemoryStorage extends MapStorage
{
    private static final String __OBFID = "CL_00001963";
    
    public SaveDataMemoryStorage() {
        super(null);
    }
    
    @Override
    public WorldSavedData loadData(final Class p_75742_1_, final String p_75742_2_) {
        return this.loadedDataMap.get(p_75742_2_);
    }
    
    @Override
    public void setData(final String p_75745_1_, final WorldSavedData p_75745_2_) {
        this.loadedDataMap.put(p_75745_1_, p_75745_2_);
    }
    
    @Override
    public void saveAllData() {
    }
    
    @Override
    public int getUniqueDataId(final String p_75743_1_) {
        return 0;
    }
}
