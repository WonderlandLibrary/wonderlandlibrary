// 
// Decompiled by Procyon v0.5.30
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
import net.minecraft.nbt.NBTTagCompound;
import java.io.File;
import java.io.InputStream;
import net.minecraft.nbt.CompressedStreamTools;
import java.io.FileInputStream;
import net.minecraft.world.WorldSavedData;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;

public class MapStorage
{
    private ISaveHandler saveHandler;
    protected Map loadedDataMap;
    private List loadedDataList;
    private Map idCounts;
    private static final String __OBFID = "CL_00000604";
    
    public MapStorage(final ISaveHandler p_i2162_1_) {
        this.loadedDataMap = Maps.newHashMap();
        this.loadedDataList = Lists.newArrayList();
        this.idCounts = Maps.newHashMap();
        this.saveHandler = p_i2162_1_;
        this.loadIdCounts();
    }
    
    public WorldSavedData loadData(final Class p_75742_1_, final String p_75742_2_) {
        WorldSavedData var3 = this.loadedDataMap.get(p_75742_2_);
        if (var3 != null) {
            return var3;
        }
        if (this.saveHandler != null) {
            try {
                final File var4 = this.saveHandler.getMapFileFromName(p_75742_2_);
                if (var4 != null && var4.exists()) {
                    try {
                        var3 = p_75742_1_.getConstructor(String.class).newInstance(p_75742_2_);
                    }
                    catch (Exception var5) {
                        throw new RuntimeException("Failed to instantiate " + p_75742_1_.toString(), var5);
                    }
                    final FileInputStream var6 = new FileInputStream(var4);
                    final NBTTagCompound var7 = CompressedStreamTools.readCompressed(var6);
                    var6.close();
                    var3.readFromNBT(var7.getCompoundTag("data"));
                }
            }
            catch (Exception var8) {
                var8.printStackTrace();
            }
        }
        if (var3 != null) {
            this.loadedDataMap.put(p_75742_2_, var3);
            this.loadedDataList.add(var3);
        }
        return var3;
    }
    
    public void setData(final String p_75745_1_, final WorldSavedData p_75745_2_) {
        if (this.loadedDataMap.containsKey(p_75745_1_)) {
            this.loadedDataList.remove(this.loadedDataMap.remove(p_75745_1_));
        }
        this.loadedDataMap.put(p_75745_1_, p_75745_2_);
        this.loadedDataList.add(p_75745_2_);
    }
    
    public void saveAllData() {
        for (int var1 = 0; var1 < this.loadedDataList.size(); ++var1) {
            final WorldSavedData var2 = this.loadedDataList.get(var1);
            if (var2.isDirty()) {
                this.saveData(var2);
                var2.setDirty(false);
            }
        }
    }
    
    private void saveData(final WorldSavedData p_75747_1_) {
        if (this.saveHandler != null) {
            try {
                final File var2 = this.saveHandler.getMapFileFromName(p_75747_1_.mapName);
                if (var2 != null) {
                    final NBTTagCompound var3 = new NBTTagCompound();
                    p_75747_1_.writeToNBT(var3);
                    final NBTTagCompound var4 = new NBTTagCompound();
                    var4.setTag("data", var3);
                    final FileOutputStream var5 = new FileOutputStream(var2);
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
            final File var1 = this.saveHandler.getMapFileFromName("idcounts");
            if (var1 != null && var1.exists()) {
                final DataInputStream var2 = new DataInputStream(new FileInputStream(var1));
                final NBTTagCompound var3 = CompressedStreamTools.read(var2);
                var2.close();
                for (final String var5 : var3.getKeySet()) {
                    final NBTBase var6 = var3.getTag(var5);
                    if (var6 instanceof NBTTagShort) {
                        final NBTTagShort var7 = (NBTTagShort)var6;
                        final short var8 = var7.getShort();
                        this.idCounts.put(var5, var8);
                    }
                }
            }
        }
        catch (Exception var9) {
            var9.printStackTrace();
        }
    }
    
    public int getUniqueDataId(final String p_75743_1_) {
        Short var2 = this.idCounts.get(p_75743_1_);
        if (var2 == null) {
            var2 = 0;
        }
        else {
            var2 = (short)(var2 + 1);
        }
        this.idCounts.put(p_75743_1_, var2);
        if (this.saveHandler == null) {
            return var2;
        }
        try {
            final File var3 = this.saveHandler.getMapFileFromName("idcounts");
            if (var3 != null) {
                final NBTTagCompound var4 = new NBTTagCompound();
                for (final String var6 : this.idCounts.keySet()) {
                    final short var7 = this.idCounts.get(var6);
                    var4.setShort(var6, var7);
                }
                final DataOutputStream var8 = new DataOutputStream(new FileOutputStream(var3));
                CompressedStreamTools.write(var4, var8);
                var8.close();
            }
        }
        catch (Exception var9) {
            var9.printStackTrace();
        }
        return var2;
    }
}
