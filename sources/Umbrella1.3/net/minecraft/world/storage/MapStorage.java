/*
 * Decompiled with CFR 0.150.
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
    private ISaveHandler saveHandler;
    protected Map loadedDataMap = Maps.newHashMap();
    private List loadedDataList = Lists.newArrayList();
    private Map idCounts = Maps.newHashMap();
    private static final String __OBFID = "CL_00000604";

    public MapStorage(ISaveHandler p_i2162_1_) {
        this.saveHandler = p_i2162_1_;
        this.loadIdCounts();
    }

    public WorldSavedData loadData(Class p_75742_1_, String p_75742_2_) {
        WorldSavedData var3;
        block7: {
            var3 = (WorldSavedData)this.loadedDataMap.get(p_75742_2_);
            if (var3 != null) {
                return var3;
            }
            if (this.saveHandler != null) {
                try {
                    File var4 = this.saveHandler.getMapFileFromName(p_75742_2_);
                    if (var4 == null || !var4.exists()) break block7;
                    try {
                        var3 = (WorldSavedData)p_75742_1_.getConstructor(String.class).newInstance(p_75742_2_);
                    }
                    catch (Exception var7) {
                        throw new RuntimeException("Failed to instantiate " + p_75742_1_.toString(), var7);
                    }
                    FileInputStream var5 = new FileInputStream(var4);
                    NBTTagCompound var6 = CompressedStreamTools.readCompressed(var5);
                    var5.close();
                    var3.readFromNBT(var6.getCompoundTag("data"));
                }
                catch (Exception var8) {
                    var8.printStackTrace();
                }
            }
        }
        if (var3 != null) {
            this.loadedDataMap.put(p_75742_2_, var3);
            this.loadedDataList.add(var3);
        }
        return var3;
    }

    public void setData(String p_75745_1_, WorldSavedData p_75745_2_) {
        if (this.loadedDataMap.containsKey(p_75745_1_)) {
            this.loadedDataList.remove(this.loadedDataMap.remove(p_75745_1_));
        }
        this.loadedDataMap.put(p_75745_1_, p_75745_2_);
        this.loadedDataList.add(p_75745_2_);
    }

    public void saveAllData() {
        for (int var1 = 0; var1 < this.loadedDataList.size(); ++var1) {
            WorldSavedData var2 = (WorldSavedData)this.loadedDataList.get(var1);
            if (!var2.isDirty()) continue;
            this.saveData(var2);
            var2.setDirty(false);
        }
    }

    private void saveData(WorldSavedData p_75747_1_) {
        if (this.saveHandler != null) {
            try {
                File var2 = this.saveHandler.getMapFileFromName(p_75747_1_.mapName);
                if (var2 != null) {
                    NBTTagCompound var3 = new NBTTagCompound();
                    p_75747_1_.writeToNBT(var3);
                    NBTTagCompound var4 = new NBTTagCompound();
                    var4.setTag("data", var3);
                    FileOutputStream var5 = new FileOutputStream(var2);
                    CompressedStreamTools.writeCompressed(var4, var5);
                    var5.close();
                }
            }
            catch (Exception var6) {
                var6.printStackTrace();
            }
        }
    }

    private void loadIdCounts() {
        try {
            this.idCounts.clear();
            if (this.saveHandler == null) {
                return;
            }
            File var1 = this.saveHandler.getMapFileFromName("idcounts");
            if (var1 != null && var1.exists()) {
                DataInputStream var2 = new DataInputStream(new FileInputStream(var1));
                NBTTagCompound var3 = CompressedStreamTools.read(var2);
                var2.close();
                for (String var5 : var3.getKeySet()) {
                    NBTBase var6 = var3.getTag(var5);
                    if (!(var6 instanceof NBTTagShort)) continue;
                    NBTTagShort var7 = (NBTTagShort)var6;
                    short var9 = var7.getShort();
                    this.idCounts.put(var5, var9);
                }
            }
        }
        catch (Exception var10) {
            var10.printStackTrace();
        }
    }

    public int getUniqueDataId(String p_75743_1_) {
        Short var2 = (Short)this.idCounts.get(p_75743_1_);
        var2 = var2 == null ? Short.valueOf((short)0) : Short.valueOf((short)(var2 + 1));
        this.idCounts.put(p_75743_1_, var2);
        if (this.saveHandler == null) {
            return var2.shortValue();
        }
        try {
            File var3 = this.saveHandler.getMapFileFromName("idcounts");
            if (var3 != null) {
                NBTTagCompound var4 = new NBTTagCompound();
                for (String var6 : this.idCounts.keySet()) {
                    short var7 = (Short)this.idCounts.get(var6);
                    var4.setShort(var6, var7);
                }
                DataOutputStream var9 = new DataOutputStream(new FileOutputStream(var3));
                CompressedStreamTools.write(var4, var9);
                var9.close();
            }
        }
        catch (Exception var8) {
            var8.printStackTrace();
        }
        return var2.shortValue();
    }
}

