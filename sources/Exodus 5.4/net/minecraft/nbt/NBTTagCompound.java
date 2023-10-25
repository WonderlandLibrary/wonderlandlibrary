/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.minecraft.nbt;

import com.google.common.collect.Maps;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ReportedException;

public class NBTTagCompound
extends NBTBase {
    private Map<String, NBTBase> tagMap = Maps.newHashMap();

    private static void writeEntry(String string, NBTBase nBTBase, DataOutput dataOutput) throws IOException {
        dataOutput.writeByte(nBTBase.getId());
        if (nBTBase.getId() != 0) {
            dataOutput.writeUTF(string);
            nBTBase.write(dataOutput);
        }
    }

    public short getShort(String string) {
        try {
            return !this.hasKey(string, 99) ? (short)0 : ((NBTBase.NBTPrimitive)this.tagMap.get(string)).getShort();
        }
        catch (ClassCastException classCastException) {
            return 0;
        }
    }

    @Override
    public boolean hasNoTags() {
        return this.tagMap.isEmpty();
    }

    @Override
    public NBTBase copy() {
        NBTTagCompound nBTTagCompound = new NBTTagCompound();
        for (String string : this.tagMap.keySet()) {
            nBTTagCompound.setTag(string, this.tagMap.get(string).copy());
        }
        return nBTTagCompound;
    }

    public void setByteArray(String string, byte[] byArray) {
        this.tagMap.put(string, new NBTTagByteArray(byArray));
    }

    public byte getByte(String string) {
        try {
            return !this.hasKey(string, 99) ? (byte)0 : ((NBTBase.NBTPrimitive)this.tagMap.get(string)).getByte();
        }
        catch (ClassCastException classCastException) {
            return 0;
        }
    }

    public byte getTagId(String string) {
        NBTBase nBTBase = this.tagMap.get(string);
        return nBTBase != null ? nBTBase.getId() : (byte)0;
    }

    public double getDouble(String string) {
        try {
            return !this.hasKey(string, 99) ? 0.0 : ((NBTBase.NBTPrimitive)this.tagMap.get(string)).getDouble();
        }
        catch (ClassCastException classCastException) {
            return 0.0;
        }
    }

    public void setByte(String string, byte by) {
        this.tagMap.put(string, new NBTTagByte(by));
    }

    public long getLong(String string) {
        try {
            return !this.hasKey(string, 99) ? 0L : ((NBTBase.NBTPrimitive)this.tagMap.get(string)).getLong();
        }
        catch (ClassCastException classCastException) {
            return 0L;
        }
    }

    public NBTBase getTag(String string) {
        return this.tagMap.get(string);
    }

    public void setString(String string, String string2) {
        this.tagMap.put(string, new NBTTagString(string2));
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("{");
        for (Map.Entry<String, NBTBase> entry : this.tagMap.entrySet()) {
            if (stringBuilder.length() != 1) {
                stringBuilder.append(',');
            }
            stringBuilder.append(entry.getKey()).append(':').append(entry.getValue());
        }
        return stringBuilder.append('}').toString();
    }

    public NBTTagList getTagList(String string, int n) {
        try {
            if (this.getTagId(string) != 9) {
                return new NBTTagList();
            }
            NBTTagList nBTTagList = (NBTTagList)this.tagMap.get(string);
            return nBTTagList.tagCount() > 0 && nBTTagList.getTagType() != n ? new NBTTagList() : nBTTagList;
        }
        catch (ClassCastException classCastException) {
            throw new ReportedException(this.createCrashReport(string, 9, classCastException));
        }
    }

    @Override
    void read(DataInput dataInput, int n, NBTSizeTracker nBTSizeTracker) throws IOException {
        byte by;
        nBTSizeTracker.read(384L);
        if (n > 512) {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        }
        this.tagMap.clear();
        while ((by = NBTTagCompound.readType(dataInput, nBTSizeTracker)) != 0) {
            String string = NBTTagCompound.readKey(dataInput, nBTSizeTracker);
            nBTSizeTracker.read(224 + 16 * string.length());
            NBTBase nBTBase = NBTTagCompound.readNBT(by, string, dataInput, n + 1, nBTSizeTracker);
            if (this.tagMap.put(string, nBTBase) == null) continue;
            nBTSizeTracker.read(288L);
        }
    }

    public void removeTag(String string) {
        this.tagMap.remove(string);
    }

    public void setBoolean(String string, boolean bl) {
        this.setByte(string, (byte)(bl ? (char)'\u0001' : '\u0000'));
    }

    @Override
    public boolean equals(Object object) {
        if (super.equals(object)) {
            NBTTagCompound nBTTagCompound = (NBTTagCompound)object;
            return this.tagMap.entrySet().equals(nBTTagCompound.tagMap.entrySet());
        }
        return false;
    }

    @Override
    void write(DataOutput dataOutput) throws IOException {
        for (String string : this.tagMap.keySet()) {
            NBTBase nBTBase = this.tagMap.get(string);
            NBTTagCompound.writeEntry(string, nBTBase, dataOutput);
        }
        dataOutput.writeByte(0);
    }

    public void setShort(String string, short s) {
        this.tagMap.put(string, new NBTTagShort(s));
    }

    public void setTag(String string, NBTBase nBTBase) {
        this.tagMap.put(string, nBTBase);
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ this.tagMap.hashCode();
    }

    private static String readKey(DataInput dataInput, NBTSizeTracker nBTSizeTracker) throws IOException {
        return dataInput.readUTF();
    }

    public Set<String> getKeySet() {
        return this.tagMap.keySet();
    }

    public float getFloat(String string) {
        try {
            return !this.hasKey(string, 99) ? 0.0f : ((NBTBase.NBTPrimitive)this.tagMap.get(string)).getFloat();
        }
        catch (ClassCastException classCastException) {
            return 0.0f;
        }
    }

    public int[] getIntArray(String string) {
        try {
            return !this.hasKey(string, 11) ? new int[]{} : ((NBTTagIntArray)this.tagMap.get(string)).getIntArray();
        }
        catch (ClassCastException classCastException) {
            throw new ReportedException(this.createCrashReport(string, 11, classCastException));
        }
    }

    private static byte readType(DataInput dataInput, NBTSizeTracker nBTSizeTracker) throws IOException {
        return dataInput.readByte();
    }

    public boolean hasKey(String string, int n) {
        byte by = this.getTagId(string);
        if (by == n) {
            return true;
        }
        if (n != 99) {
            if (by > 0) {
                // empty if block
            }
            return false;
        }
        return by == 1 || by == 2 || by == 3 || by == 4 || by == 5 || by == 6;
    }

    public void setLong(String string, long l) {
        this.tagMap.put(string, new NBTTagLong(l));
    }

    public void setIntArray(String string, int[] nArray) {
        this.tagMap.put(string, new NBTTagIntArray(nArray));
    }

    public boolean hasKey(String string) {
        return this.tagMap.containsKey(string);
    }

    public void merge(NBTTagCompound nBTTagCompound) {
        for (String string : nBTTagCompound.tagMap.keySet()) {
            NBTBase nBTBase = nBTTagCompound.tagMap.get(string);
            if (nBTBase.getId() == 10) {
                if (this.hasKey(string, 10)) {
                    NBTTagCompound nBTTagCompound2 = this.getCompoundTag(string);
                    nBTTagCompound2.merge((NBTTagCompound)nBTBase);
                    continue;
                }
                this.setTag(string, nBTBase.copy());
                continue;
            }
            this.setTag(string, nBTBase.copy());
        }
    }

    public void setDouble(String string, double d) {
        this.tagMap.put(string, new NBTTagDouble(d));
    }

    public byte[] getByteArray(String string) {
        try {
            return !this.hasKey(string, 7) ? new byte[]{} : ((NBTTagByteArray)this.tagMap.get(string)).getByteArray();
        }
        catch (ClassCastException classCastException) {
            throw new ReportedException(this.createCrashReport(string, 7, classCastException));
        }
    }

    static NBTBase readNBT(byte by, String string, DataInput dataInput, int n, NBTSizeTracker nBTSizeTracker) throws IOException {
        NBTBase nBTBase = NBTBase.createNewByType(by);
        try {
            nBTBase.read(dataInput, n, nBTSizeTracker);
            return nBTBase;
        }
        catch (IOException iOException) {
            CrashReport crashReport = CrashReport.makeCrashReport(iOException, "Loading NBT data");
            CrashReportCategory crashReportCategory = crashReport.makeCategory("NBT Tag");
            crashReportCategory.addCrashSection("Tag name", string);
            crashReportCategory.addCrashSection("Tag type", by);
            throw new ReportedException(crashReport);
        }
    }

    private CrashReport createCrashReport(final String string, final int n, ClassCastException classCastException) {
        CrashReport crashReport = CrashReport.makeCrashReport(classCastException, "Reading NBT data");
        CrashReportCategory crashReportCategory = crashReport.makeCategoryDepth("Corrupt NBT tag", 1);
        crashReportCategory.addCrashSectionCallable("Tag type found", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return NBTBase.NBT_TYPES[((NBTBase)NBTTagCompound.this.tagMap.get(string)).getId()];
            }
        });
        crashReportCategory.addCrashSectionCallable("Tag type expected", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return NBTBase.NBT_TYPES[n];
            }
        });
        crashReportCategory.addCrashSection("Tag name", string);
        return crashReport;
    }

    public NBTTagCompound getCompoundTag(String string) {
        try {
            return !this.hasKey(string, 10) ? new NBTTagCompound() : (NBTTagCompound)this.tagMap.get(string);
        }
        catch (ClassCastException classCastException) {
            throw new ReportedException(this.createCrashReport(string, 10, classCastException));
        }
    }

    public void setFloat(String string, float f) {
        this.tagMap.put(string, new NBTTagFloat(f));
    }

    public String getString(String string) {
        try {
            return !this.hasKey(string, 8) ? "" : this.tagMap.get(string).getString();
        }
        catch (ClassCastException classCastException) {
            return "";
        }
    }

    @Override
    public byte getId() {
        return 10;
    }

    public void setInteger(String string, int n) {
        this.tagMap.put(string, new NBTTagInt(n));
    }

    public int getInteger(String string) {
        try {
            return !this.hasKey(string, 99) ? 0 : ((NBTBase.NBTPrimitive)this.tagMap.get(string)).getInt();
        }
        catch (ClassCastException classCastException) {
            return 0;
        }
    }

    public boolean getBoolean(String string) {
        return this.getByte(string) != 0;
    }
}

