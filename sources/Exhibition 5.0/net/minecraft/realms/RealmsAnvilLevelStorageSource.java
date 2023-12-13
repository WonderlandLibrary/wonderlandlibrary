// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.realms;

import net.minecraft.client.AnvilConverterException;
import java.util.Iterator;
import java.util.ArrayList;
import net.minecraft.world.storage.SaveFormatComparator;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.storage.ISaveFormat;

public class RealmsAnvilLevelStorageSource
{
    private ISaveFormat levelStorageSource;
    private static final String __OBFID = "CL_00001856";
    
    public RealmsAnvilLevelStorageSource(final ISaveFormat p_i1106_1_) {
        this.levelStorageSource = p_i1106_1_;
    }
    
    public String getName() {
        return this.levelStorageSource.func_154333_a();
    }
    
    public boolean levelExists(final String p_levelExists_1_) {
        return this.levelStorageSource.canLoadWorld(p_levelExists_1_);
    }
    
    public boolean convertLevel(final String p_convertLevel_1_, final IProgressUpdate p_convertLevel_2_) {
        return this.levelStorageSource.convertMapFormat(p_convertLevel_1_, p_convertLevel_2_);
    }
    
    public boolean requiresConversion(final String p_requiresConversion_1_) {
        return this.levelStorageSource.isOldMapFormat(p_requiresConversion_1_);
    }
    
    public boolean isNewLevelIdAcceptable(final String p_isNewLevelIdAcceptable_1_) {
        return this.levelStorageSource.func_154335_d(p_isNewLevelIdAcceptable_1_);
    }
    
    public boolean deleteLevel(final String p_deleteLevel_1_) {
        return this.levelStorageSource.deleteWorldDirectory(p_deleteLevel_1_);
    }
    
    public boolean isConvertible(final String p_isConvertible_1_) {
        return this.levelStorageSource.func_154334_a(p_isConvertible_1_);
    }
    
    public void renameLevel(final String p_renameLevel_1_, final String p_renameLevel_2_) {
        this.levelStorageSource.renameWorld(p_renameLevel_1_, p_renameLevel_2_);
    }
    
    public void clearAll() {
        this.levelStorageSource.flushCache();
    }
    
    public List getLevelList() throws AnvilConverterException {
        final ArrayList var1 = Lists.newArrayList();
        for (final SaveFormatComparator var3 : this.levelStorageSource.getSaveList()) {
            var1.add(new RealmsLevelSummary(var3));
        }
        return var1;
    }
}
