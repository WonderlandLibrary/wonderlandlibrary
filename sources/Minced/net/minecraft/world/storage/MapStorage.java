// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.util.Iterator;
import net.minecraft.nbt.NBTTagShort;
import java.io.DataInputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import net.minecraft.nbt.NBTBase;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;
import java.io.File;
import java.io.InputStream;
import net.minecraft.nbt.CompressedStreamTools;
import java.io.FileInputStream;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;

public class MapStorage
{
    private final ISaveHandler saveHandler;
    protected Map<String, WorldSavedData> loadedDataMap;
    private final List<WorldSavedData> loadedDataList;
    private final Map<String, Short> idCounts;
    
    public MapStorage(final ISaveHandler saveHandlerIn) {
        this.loadedDataMap = (Map<String, WorldSavedData>)Maps.newHashMap();
        this.loadedDataList = (List<WorldSavedData>)Lists.newArrayList();
        this.idCounts = (Map<String, Short>)Maps.newHashMap();
        this.saveHandler = saveHandlerIn;
        this.loadIdCounts();
    }
    
    @Nullable
    public WorldSavedData getOrLoadData(final Class<? extends WorldSavedData> clazz, final String dataIdentifier) {
        WorldSavedData worldsaveddata = this.loadedDataMap.get(dataIdentifier);
        if (worldsaveddata != null) {
            return worldsaveddata;
        }
        if (this.saveHandler != null) {
            try {
                final File file1 = this.saveHandler.getMapFileFromName(dataIdentifier);
                if (file1 != null && file1.exists()) {
                    try {
                        worldsaveddata = (WorldSavedData)clazz.getConstructor(String.class).newInstance(dataIdentifier);
                    }
                    catch (Exception exception) {
                        throw new RuntimeException("Failed to instantiate " + clazz, exception);
                    }
                    final FileInputStream fileinputstream = new FileInputStream(file1);
                    final NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(fileinputstream);
                    fileinputstream.close();
                    worldsaveddata.readFromNBT(nbttagcompound.getCompoundTag("data"));
                }
            }
            catch (Exception exception2) {
                exception2.printStackTrace();
            }
        }
        if (worldsaveddata != null) {
            this.loadedDataMap.put(dataIdentifier, worldsaveddata);
            this.loadedDataList.add(worldsaveddata);
        }
        return worldsaveddata;
    }
    
    public void setData(final String dataIdentifier, final WorldSavedData data) {
        if (this.loadedDataMap.containsKey(dataIdentifier)) {
            this.loadedDataList.remove(this.loadedDataMap.remove(dataIdentifier));
        }
        this.loadedDataMap.put(dataIdentifier, data);
        this.loadedDataList.add(data);
    }
    
    public void saveAllData() {
        for (int i = 0; i < this.loadedDataList.size(); ++i) {
            final WorldSavedData worldsaveddata = this.loadedDataList.get(i);
            if (worldsaveddata.isDirty()) {
                this.saveData(worldsaveddata);
                worldsaveddata.setDirty(false);
            }
        }
    }
    
    private void saveData(final WorldSavedData data) {
        if (this.saveHandler != null) {
            try {
                final File file1 = this.saveHandler.getMapFileFromName(data.mapName);
                if (file1 != null) {
                    final NBTTagCompound nbttagcompound = new NBTTagCompound();
                    nbttagcompound.setTag("data", data.writeToNBT(new NBTTagCompound()));
                    final FileOutputStream fileoutputstream = new FileOutputStream(file1);
                    CompressedStreamTools.writeCompressed(nbttagcompound, fileoutputstream);
                    fileoutputstream.close();
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
    
    private void loadIdCounts() {
        try {
            this.idCounts.clear();
            if (this.saveHandler == null) {
                return;
            }
            final File file1 = this.saveHandler.getMapFileFromName("idcounts");
            if (file1 != null && file1.exists()) {
                final DataInputStream datainputstream = new DataInputStream(new FileInputStream(file1));
                final NBTTagCompound nbttagcompound = CompressedStreamTools.read(datainputstream);
                datainputstream.close();
                for (final String s : nbttagcompound.getKeySet()) {
                    final NBTBase nbtbase = nbttagcompound.getTag(s);
                    if (nbtbase instanceof NBTTagShort) {
                        final NBTTagShort nbttagshort = (NBTTagShort)nbtbase;
                        final short short1 = nbttagshort.getShort();
                        this.idCounts.put(s, short1);
                    }
                }
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    public int getUniqueDataId(final String key) {
        Short oshort = this.idCounts.get(key);
        if (oshort == null) {
            oshort = 0;
        }
        else {
            oshort = (short)(oshort + 1);
        }
        this.idCounts.put(key, oshort);
        if (this.saveHandler == null) {
            return oshort;
        }
        try {
            final File file1 = this.saveHandler.getMapFileFromName("idcounts");
            if (file1 != null) {
                final NBTTagCompound nbttagcompound = new NBTTagCompound();
                for (final String s : this.idCounts.keySet()) {
                    nbttagcompound.setShort(s, this.idCounts.get(s));
                }
                final DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file1));
                CompressedStreamTools.write(nbttagcompound, dataoutputstream);
                dataoutputstream.close();
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return oshort;
    }
}
