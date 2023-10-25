/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.storage;

import java.io.File;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSummary;

public interface ISaveFormat {
    public String getName();

    public ISaveHandler getSaveLoader(String var1, boolean var2);

    public List<WorldSummary> getSaveList() throws AnvilConverterException;

    public void flushCache();

    @Nullable
    public WorldInfo getWorldInfo(String var1);

    public boolean isNewLevelIdAcceptable(String var1);

    public boolean deleteWorldDirectory(String var1);

    public void renameWorld(String var1, String var2);

    public boolean isConvertible(String var1);

    public boolean isOldMapFormat(String var1);

    public boolean convertMapFormat(String var1, IProgressUpdate var2);

    public boolean canLoadWorld(String var1);

    public File getFile(String var1, String var2);
}

