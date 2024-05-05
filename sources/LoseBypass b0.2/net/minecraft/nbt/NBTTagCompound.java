/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.nbt;

import com.google.common.collect.Maps;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;
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

    @Override
    void write(DataOutput output) throws IOException {
        Iterator<String> iterator = this.tagMap.keySet().iterator();
        while (true) {
            if (!iterator.hasNext()) {
                output.writeByte(0);
                return;
            }
            String s = iterator.next();
            NBTBase nbtbase = this.tagMap.get(s);
            NBTTagCompound.writeEntry(s, nbtbase, output);
        }
    }

    @Override
    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        byte b0;
        sizeTracker.read(384L);
        if (depth > 512) {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        }
        this.tagMap.clear();
        while ((b0 = NBTTagCompound.readType(input, sizeTracker)) != 0) {
            String s = NBTTagCompound.readKey(input, sizeTracker);
            sizeTracker.read(224 + 16 * s.length());
            NBTBase nbtbase = NBTTagCompound.readNBT(b0, s, input, depth + 1, sizeTracker);
            if (this.tagMap.put(s, nbtbase) == null) continue;
            sizeTracker.read(288L);
        }
    }

    public Set<String> getKeySet() {
        return this.tagMap.keySet();
    }

    @Override
    public byte getId() {
        return 10;
    }

    public void setTag(String key, NBTBase value) {
        this.tagMap.put(key, value);
    }

    public void setByte(String key, byte value) {
        this.tagMap.put(key, new NBTTagByte(value));
    }

    public void setShort(String key, short value) {
        this.tagMap.put(key, new NBTTagShort(value));
    }

    public void setInteger(String key, int value) {
        this.tagMap.put(key, new NBTTagInt(value));
    }

    public void setLong(String key, long value) {
        this.tagMap.put(key, new NBTTagLong(value));
    }

    public void setFloat(String key, float value) {
        this.tagMap.put(key, new NBTTagFloat(value));
    }

    public void setDouble(String key, double value) {
        this.tagMap.put(key, new NBTTagDouble(value));
    }

    public void setString(String key, String value) {
        this.tagMap.put(key, new NBTTagString(value));
    }

    public void setByteArray(String key, byte[] value) {
        this.tagMap.put(key, new NBTTagByteArray(value));
    }

    public void setIntArray(String key, int[] value) {
        this.tagMap.put(key, new NBTTagIntArray(value));
    }

    public void setBoolean(String key, boolean value) {
        this.setByte(key, (byte)(value ? 1 : 0));
    }

    public NBTBase getTag(String key) {
        return this.tagMap.get(key);
    }

    public byte getTagId(String key) {
        NBTBase nbtbase = this.tagMap.get(key);
        if (nbtbase == null) return 0;
        byte by = nbtbase.getId();
        return by;
    }

    public boolean hasKey(String key) {
        return this.tagMap.containsKey(key);
    }

    public boolean hasKey(String key, int type) {
        byte i = this.getTagId(key);
        if (i == type) {
            return true;
        }
        if (type != 99) {
            if (i <= 0) return false;
            return false;
        }
        if (i == 1) return true;
        if (i == 2) return true;
        if (i == 3) return true;
        if (i == 4) return true;
        if (i == 5) return true;
        if (i == 6) return true;
        return false;
    }

    public byte getByte(String key) {
        try {
            if (!this.hasKey(key, 99)) {
                return 0;
            }
            byte by = ((NBTBase.NBTPrimitive)this.tagMap.get(key)).getByte();
            return by;
        }
        catch (ClassCastException var3) {
            return 0;
        }
    }

    public short getShort(String key) {
        try {
            if (!this.hasKey(key, 99)) {
                return 0;
            }
            short s = ((NBTBase.NBTPrimitive)this.tagMap.get(key)).getShort();
            return s;
        }
        catch (ClassCastException var3) {
            return 0;
        }
    }

    public int getInteger(String key) {
        try {
            if (!this.hasKey(key, 99)) {
                return 0;
            }
            int n = ((NBTBase.NBTPrimitive)this.tagMap.get(key)).getInt();
            return n;
        }
        catch (ClassCastException var3) {
            return 0;
        }
    }

    public long getLong(String key) {
        try {
            if (!this.hasKey(key, 99)) {
                return 0L;
            }
            long l = ((NBTBase.NBTPrimitive)this.tagMap.get(key)).getLong();
            return l;
        }
        catch (ClassCastException var3) {
            return 0L;
        }
    }

    public float getFloat(String key) {
        try {
            if (!this.hasKey(key, 99)) {
                return 0.0f;
            }
            float f = ((NBTBase.NBTPrimitive)this.tagMap.get(key)).getFloat();
            return f;
        }
        catch (ClassCastException var3) {
            return 0.0f;
        }
    }

    public double getDouble(String key) {
        try {
            if (!this.hasKey(key, 99)) {
                return 0.0;
            }
            double d = ((NBTBase.NBTPrimitive)this.tagMap.get(key)).getDouble();
            return d;
        }
        catch (ClassCastException var3) {
            return 0.0;
        }
    }

    public String getString(String key) {
        try {
            if (!this.hasKey(key, 8)) {
                return "";
            }
            String string = this.tagMap.get(key).getString();
            return string;
        }
        catch (ClassCastException var3) {
            return "";
        }
    }

    public byte[] getByteArray(String key) {
        try {
            byte[] byArray;
            if (!this.hasKey(key, 7)) {
                byArray = new byte[]{};
                return byArray;
            }
            byArray = ((NBTTagByteArray)this.tagMap.get(key)).getByteArray();
            return byArray;
        }
        catch (ClassCastException classcastexception) {
            throw new ReportedException(this.createCrashReport(key, 7, classcastexception));
        }
    }

    public int[] getIntArray(String key) {
        try {
            int[] nArray;
            if (!this.hasKey(key, 11)) {
                nArray = new int[]{};
                return nArray;
            }
            nArray = ((NBTTagIntArray)this.tagMap.get(key)).getIntArray();
            return nArray;
        }
        catch (ClassCastException classcastexception) {
            throw new ReportedException(this.createCrashReport(key, 11, classcastexception));
        }
    }

    public NBTTagCompound getCompoundTag(String key) {
        try {
            NBTTagCompound nBTTagCompound;
            if (!this.hasKey(key, 10)) {
                nBTTagCompound = new NBTTagCompound();
                return nBTTagCompound;
            }
            nBTTagCompound = (NBTTagCompound)this.tagMap.get(key);
            return nBTTagCompound;
        }
        catch (ClassCastException classcastexception) {
            throw new ReportedException(this.createCrashReport(key, 10, classcastexception));
        }
    }

    public NBTTagList getTagList(String key, int type) {
        try {
            NBTTagList nBTTagList;
            if (this.getTagId(key) != 9) {
                return new NBTTagList();
            }
            NBTTagList nbttaglist = (NBTTagList)this.tagMap.get(key);
            if (nbttaglist.tagCount() > 0 && nbttaglist.getTagType() != type) {
                nBTTagList = new NBTTagList();
                return nBTTagList;
            }
            nBTTagList = nbttaglist;
            return nBTTagList;
        }
        catch (ClassCastException classcastexception) {
            throw new ReportedException(this.createCrashReport(key, 9, classcastexception));
        }
    }

    public boolean getBoolean(String key) {
        if (this.getByte(key) == 0) return false;
        return true;
    }

    public void removeTag(String key) {
        this.tagMap.remove(key);
    }

    @Override
    public String toString() {
        StringBuilder stringbuilder = new StringBuilder("{");
        Iterator<Map.Entry<String, NBTBase>> iterator = this.tagMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, NBTBase> entry = iterator.next();
            if (stringbuilder.length() != 1) {
                stringbuilder.append(',');
            }
            stringbuilder.append(entry.getKey()).append(':').append(entry.getValue());
        }
        return stringbuilder.append('}').toString();
    }

    @Override
    public boolean hasNoTags() {
        return this.tagMap.isEmpty();
    }

    private CrashReport createCrashReport(final String key, final int expectedType, ClassCastException ex) {
        CrashReport crashreport = CrashReport.makeCrashReport(ex, "Reading NBT data");
        CrashReportCategory crashreportcategory = crashreport.makeCategoryDepth("Corrupt NBT tag", 1);
        crashreportcategory.addCrashSectionCallable("Tag type found", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return NBTBase.NBT_TYPES[((NBTBase)NBTTagCompound.this.tagMap.get(key)).getId()];
            }
        });
        crashreportcategory.addCrashSectionCallable("Tag type expected", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return NBTBase.NBT_TYPES[expectedType];
            }
        });
        crashreportcategory.addCrashSection("Tag name", key);
        return crashreport;
    }

    @Override
    public NBTBase copy() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        Iterator<String> iterator = this.tagMap.keySet().iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            nbttagcompound.setTag(s, this.tagMap.get(s).copy());
        }
        return nbttagcompound;
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        if (!super.equals(p_equals_1_)) return false;
        NBTTagCompound nbttagcompound = (NBTTagCompound)p_equals_1_;
        return this.tagMap.entrySet().equals(nbttagcompound.tagMap.entrySet());
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ this.tagMap.hashCode();
    }

    private static void writeEntry(String name, NBTBase data, DataOutput output) throws IOException {
        output.writeByte(data.getId());
        if (data.getId() == 0) return;
        output.writeUTF(name);
        data.write(output);
    }

    private static byte readType(DataInput input, NBTSizeTracker sizeTracker) throws IOException {
        return input.readByte();
    }

    private static String readKey(DataInput input, NBTSizeTracker sizeTracker) throws IOException {
        return input.readUTF();
    }

    static NBTBase readNBT(byte id, String key, DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        NBTBase nbtbase = NBTBase.createNewByType(id);
        try {
            nbtbase.read(input, depth, sizeTracker);
            return nbtbase;
        }
        catch (IOException ioexception) {
            CrashReport crashreport = CrashReport.makeCrashReport(ioexception, "Loading NBT data");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("NBT Tag");
            crashreportcategory.addCrashSection("Tag name", key);
            crashreportcategory.addCrashSection("Tag type", id);
            throw new ReportedException(crashreport);
        }
    }

    public void merge(NBTTagCompound other) {
        Iterator<String> iterator = other.tagMap.keySet().iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            NBTBase nbtbase = other.tagMap.get(s);
            if (nbtbase.getId() == 10) {
                if (this.hasKey(s, 10)) {
                    NBTTagCompound nbttagcompound = this.getCompoundTag(s);
                    nbttagcompound.merge((NBTTagCompound)nbtbase);
                    continue;
                }
                this.setTag(s, nbtbase.copy());
                continue;
            }
            this.setTag(s, nbtbase.copy());
        }
    }
}

