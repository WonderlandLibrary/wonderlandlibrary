/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 */
package net.minecraft.world.storage;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.ISaveHandler;

public class MapStorage {
    private Map<String, Short> idCounts;
    private List<WorldSavedData> loadedDataList;
    protected Map<String, WorldSavedData> loadedDataMap = Maps.newHashMap();
    private ISaveHandler saveHandler;

    public WorldSavedData loadData(Class<? extends WorldSavedData> clazz, String string) {
        WorldSavedData worldSavedData;
        block7: {
            worldSavedData = this.loadedDataMap.get(string);
            if (worldSavedData != null) {
                return worldSavedData;
            }
            if (this.saveHandler != null) {
                try {
                    File file = this.saveHandler.getMapFileFromName(string);
                    if (file == null || !file.exists()) break block7;
                    try {
                        worldSavedData = clazz.getConstructor(String.class).newInstance(string);
                    }
                    catch (Exception exception) {
                        throw new RuntimeException("Failed to instantiate " + clazz.toString(), exception);
                    }
                    FileInputStream fileInputStream = new FileInputStream(file);
                    NBTTagCompound nBTTagCompound = CompressedStreamTools.readCompressed(fileInputStream);
                    fileInputStream.close();
                    worldSavedData.readFromNBT(nBTTagCompound.getCompoundTag("data"));
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
        if (worldSavedData != null) {
            this.loadedDataMap.put(string, worldSavedData);
            this.loadedDataList.add(worldSavedData);
        }
        return worldSavedData;
    }

    private void saveData(WorldSavedData worldSavedData) {
        if (this.saveHandler != null) {
            try {
                File file = this.saveHandler.getMapFileFromName(worldSavedData.mapName);
                if (file != null) {
                    NBTTagCompound nBTTagCompound = new NBTTagCompound();
                    worldSavedData.writeToNBT(nBTTagCompound);
                    NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
                    nBTTagCompound2.setTag("data", nBTTagCompound);
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    CompressedStreamTools.writeCompressed(nBTTagCompound2, fileOutputStream);
                    fileOutputStream.close();
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public void setData(String string, WorldSavedData worldSavedData) {
        if (this.loadedDataMap.containsKey(string)) {
            this.loadedDataList.remove(this.loadedDataMap.remove(string));
        }
        this.loadedDataMap.put(string, worldSavedData);
        this.loadedDataList.add(worldSavedData);
    }

    public int getUniqueDataId(String string) {
        Short s = this.idCounts.get(string);
        s = s == null ? Short.valueOf((short)0) : Short.valueOf((short)(s + 1));
        this.idCounts.put(string, s);
        if (this.saveHandler == null) {
            return s.shortValue();
        }
        try {
            File file = this.saveHandler.getMapFileFromName("idcounts");
            if (file != null) {
                NBTTagCompound nBTTagCompound = new NBTTagCompound();
                for (String object2 : this.idCounts.keySet()) {
                    short s2 = this.idCounts.get(object2);
                    nBTTagCompound.setShort(object2, s2);
                }
                DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file));
                CompressedStreamTools.write(nBTTagCompound, dataOutputStream);
                dataOutputStream.close();
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return s.shortValue();
    }

    public MapStorage(ISaveHandler iSaveHandler) {
        this.loadedDataList = Lists.newArrayList();
        this.idCounts = Maps.newHashMap();
        this.saveHandler = iSaveHandler;
        this.loadIdCounts();
    }

    public void saveAllData() {
        int n = 0;
        while (n < this.loadedDataList.size()) {
            WorldSavedData worldSavedData = this.loadedDataList.get(n);
            if (worldSavedData.isDirty()) {
                this.saveData(worldSavedData);
                worldSavedData.setDirty(false);
            }
            ++n;
        }
    }

    private void loadIdCounts() {
        try {
            this.idCounts.clear();
            if (this.saveHandler == null) {
                return;
            }
            File file = this.saveHandler.getMapFileFromName("idcounts");
            if (file != null && file.exists()) {
                DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));
                NBTTagCompound nBTTagCompound = CompressedStreamTools.read(dataInputStream);
                dataInputStream.close();
                for (String string : nBTTagCompound.getKeySet()) {
                    NBTBase nBTBase = nBTTagCompound.getTag(string);
                    if (!(nBTBase instanceof NBTTagShort)) continue;
                    NBTTagShort nBTTagShort = (NBTTagShort)nBTBase;
                    short s = nBTTagShort.getShort();
                    this.idCounts.put(string, s);
                }
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

