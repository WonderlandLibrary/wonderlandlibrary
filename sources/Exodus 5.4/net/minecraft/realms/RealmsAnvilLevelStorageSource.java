/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.realms;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.realms.RealmsLevelSummary;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.SaveFormatComparator;

public class RealmsAnvilLevelStorageSource {
    private ISaveFormat levelStorageSource;

    public boolean isConvertible(String string) {
        return this.levelStorageSource.func_154334_a(string);
    }

    public RealmsAnvilLevelStorageSource(ISaveFormat iSaveFormat) {
        this.levelStorageSource = iSaveFormat;
    }

    public String getName() {
        return this.levelStorageSource.getName();
    }

    public boolean requiresConversion(String string) {
        return this.levelStorageSource.isOldMapFormat(string);
    }

    public void renameLevel(String string, String string2) {
        this.levelStorageSource.renameWorld(string, string2);
    }

    public boolean convertLevel(String string, IProgressUpdate iProgressUpdate) {
        return this.levelStorageSource.convertMapFormat(string, iProgressUpdate);
    }

    public List<RealmsLevelSummary> getLevelList() throws AnvilConverterException {
        ArrayList arrayList = Lists.newArrayList();
        for (SaveFormatComparator saveFormatComparator : this.levelStorageSource.getSaveList()) {
            arrayList.add(new RealmsLevelSummary(saveFormatComparator));
        }
        return arrayList;
    }

    public boolean isNewLevelIdAcceptable(String string) {
        return this.levelStorageSource.func_154335_d(string);
    }

    public boolean levelExists(String string) {
        return this.levelStorageSource.canLoadWorld(string);
    }

    public boolean deleteLevel(String string) {
        return this.levelStorageSource.deleteWorldDirectory(string);
    }

    public void clearAll() {
        this.levelStorageSource.flushCache();
    }
}

