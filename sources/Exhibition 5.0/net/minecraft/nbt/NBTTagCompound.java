// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.nbt;

import org.apache.logging.log4j.LogManager;
import net.minecraft.crash.CrashReportCategory;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;
import java.util.Set;
import java.io.DataInput;
import java.io.IOException;
import java.util.Iterator;
import java.io.DataOutput;
import com.google.common.collect.Maps;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class NBTTagCompound extends NBTBase
{
    private static final Logger logger;
    private Map tagMap;
    private static final String __OBFID = "CL_00001215";
    
    public NBTTagCompound() {
        this.tagMap = Maps.newHashMap();
    }
    
    @Override
    void write(final DataOutput output) throws IOException {
        for (final String var3 : this.tagMap.keySet()) {
            final NBTBase var4 = this.tagMap.get(var3);
            writeEntry(var3, var4, output);
        }
        output.writeByte(0);
    }
    
    @Override
    void read(final DataInput input, final int depth, final NBTSizeTracker sizeTracker) throws IOException {
        if (depth > 512) {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        }
        this.tagMap.clear();
        byte var4;
        while ((var4 = readType(input, sizeTracker)) != 0) {
            final String var5 = readKey(input, sizeTracker);
            sizeTracker.read(16 * var5.length());
            final NBTBase var6 = readNBT(var4, var5, input, depth + 1, sizeTracker);
            this.tagMap.put(var5, var6);
        }
    }
    
    public Set getKeySet() {
        return this.tagMap.keySet();
    }
    
    @Override
    public byte getId() {
        return 10;
    }
    
    public void setTag(final String key, final NBTBase value) {
        this.tagMap.put(key, value);
    }
    
    public void setByte(final String key, final byte value) {
        this.tagMap.put(key, new NBTTagByte(value));
    }
    
    public void setShort(final String key, final short value) {
        this.tagMap.put(key, new NBTTagShort(value));
    }
    
    public void setInteger(final String key, final int value) {
        this.tagMap.put(key, new NBTTagInt(value));
    }
    
    public void setLong(final String key, final long value) {
        this.tagMap.put(key, new NBTTagLong(value));
    }
    
    public void setFloat(final String key, final float value) {
        this.tagMap.put(key, new NBTTagFloat(value));
    }
    
    public void setDouble(final String key, final double value) {
        this.tagMap.put(key, new NBTTagDouble(value));
    }
    
    public void setString(final String key, final String value) {
        this.tagMap.put(key, new NBTTagString(value));
    }
    
    public void setByteArray(final String key, final byte[] value) {
        this.tagMap.put(key, new NBTTagByteArray(value));
    }
    
    public void setIntArray(final String key, final int[] value) {
        this.tagMap.put(key, new NBTTagIntArray(value));
    }
    
    public void setBoolean(final String key, final boolean value) {
        this.setByte(key, (byte)(value ? 1 : 0));
    }
    
    public NBTBase getTag(final String key) {
        return this.tagMap.get(key);
    }
    
    public byte getTagType(final String key) {
        final NBTBase var2 = this.tagMap.get(key);
        return (byte)((var2 != null) ? var2.getId() : 0);
    }
    
    public boolean hasKey(final String key) {
        return this.tagMap.containsKey(key);
    }
    
    public boolean hasKey(final String key, final int type) {
        final byte var3 = this.getTagType(key);
        if (var3 == type) {
            return true;
        }
        if (type != 99) {
            if (var3 > 0) {}
            return false;
        }
        return var3 == 1 || var3 == 2 || var3 == 3 || var3 == 4 || var3 == 5 || var3 == 6;
    }
    
    public byte getByte(final String key) {
        try {
            return (byte)(this.hasKey(key, 99) ? this.tagMap.get(key).getByte() : 0);
        }
        catch (ClassCastException var3) {
            return 0;
        }
    }
    
    public short getShort(final String key) {
        try {
            return (short)(this.hasKey(key, 99) ? this.tagMap.get(key).getShort() : 0);
        }
        catch (ClassCastException var3) {
            return 0;
        }
    }
    
    public int getInteger(final String key) {
        try {
            return this.hasKey(key, 99) ? this.tagMap.get(key).getInt() : 0;
        }
        catch (ClassCastException var3) {
            return 0;
        }
    }
    
    public long getLong(final String key) {
        try {
            return this.hasKey(key, 99) ? this.tagMap.get(key).getLong() : 0L;
        }
        catch (ClassCastException var3) {
            return 0L;
        }
    }
    
    public float getFloat(final String key) {
        try {
            return this.hasKey(key, 99) ? this.tagMap.get(key).getFloat() : 0.0f;
        }
        catch (ClassCastException var3) {
            return 0.0f;
        }
    }
    
    public double getDouble(final String key) {
        try {
            return this.hasKey(key, 99) ? this.tagMap.get(key).getDouble() : 0.0;
        }
        catch (ClassCastException var3) {
            return 0.0;
        }
    }
    
    public String getString(final String key) {
        try {
            return this.hasKey(key, 8) ? this.tagMap.get(key).getString() : "";
        }
        catch (ClassCastException var3) {
            return "";
        }
    }
    
    public byte[] getByteArray(final String key) {
        try {
            return this.hasKey(key, 7) ? this.tagMap.get(key).getByteArray() : new byte[0];
        }
        catch (ClassCastException var3) {
            throw new ReportedException(this.createCrashReport(key, 7, var3));
        }
    }
    
    public int[] getIntArray(final String key) {
        try {
            return this.hasKey(key, 11) ? this.tagMap.get(key).getIntArray() : new int[0];
        }
        catch (ClassCastException var3) {
            throw new ReportedException(this.createCrashReport(key, 11, var3));
        }
    }
    
    public NBTTagCompound getCompoundTag(final String key) {
        try {
            return this.hasKey(key, 10) ? this.tagMap.get(key) : new NBTTagCompound();
        }
        catch (ClassCastException var3) {
            throw new ReportedException(this.createCrashReport(key, 10, var3));
        }
    }
    
    public NBTTagList getTagList(final String key, final int type) {
        try {
            if (this.getTagType(key) != 9) {
                return new NBTTagList();
            }
            final NBTTagList var3 = this.tagMap.get(key);
            return (var3.tagCount() > 0 && var3.getTagType() != type) ? new NBTTagList() : var3;
        }
        catch (ClassCastException var4) {
            throw new ReportedException(this.createCrashReport(key, 9, var4));
        }
    }
    
    public boolean getBoolean(final String key) {
        return this.getByte(key) != 0;
    }
    
    public void removeTag(final String key) {
        this.tagMap.remove(key);
    }
    
    @Override
    public String toString() {
        String var1 = "{";
        for (final String var3 : this.tagMap.keySet()) {
            var1 = var1 + var3 + ':' + this.tagMap.get(var3) + ',';
        }
        return var1 + "}";
    }
    
    @Override
    public boolean hasNoTags() {
        return this.tagMap.isEmpty();
    }
    
    private CrashReport createCrashReport(final String key, final int expectedType, final ClassCastException ex) {
        final CrashReport var4 = CrashReport.makeCrashReport(ex, "Reading NBT data");
        final CrashReportCategory var5 = var4.makeCategoryDepth("Corrupt NBT tag", 1);
        var5.addCrashSectionCallable("Tag type found", new Callable() {
            private static final String __OBFID = "CL_00001216";
            
            @Override
            public String call() {
                return NBTBase.NBT_TYPES[NBTTagCompound.this.tagMap.get(key).getId()];
            }
        });
        var5.addCrashSectionCallable("Tag type expected", new Callable() {
            private static final String __OBFID = "CL_00001217";
            
            @Override
            public String call() {
                return NBTBase.NBT_TYPES[expectedType];
            }
        });
        var5.addCrashSection("Tag name", key);
        return var4;
    }
    
    @Override
    public NBTBase copy() {
        final NBTTagCompound var1 = new NBTTagCompound();
        for (final String var3 : this.tagMap.keySet()) {
            var1.setTag(var3, this.tagMap.get(var3).copy());
        }
        return var1;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (super.equals(p_equals_1_)) {
            final NBTTagCompound var2 = (NBTTagCompound)p_equals_1_;
            return this.tagMap.entrySet().equals(var2.tagMap.entrySet());
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return super.hashCode() ^ this.tagMap.hashCode();
    }
    
    private static void writeEntry(final String name, final NBTBase data, final DataOutput output) throws IOException {
        output.writeByte(data.getId());
        if (data.getId() != 0) {
            output.writeUTF(name);
            data.write(output);
        }
    }
    
    private static byte readType(final DataInput input, final NBTSizeTracker sizeTracker) throws IOException {
        return input.readByte();
    }
    
    private static String readKey(final DataInput input, final NBTSizeTracker sizeTracker) throws IOException {
        return input.readUTF();
    }
    
    static NBTBase readNBT(final byte id, final String key, final DataInput input, final int depth, final NBTSizeTracker sizeTracker) {
        final NBTBase var5 = NBTBase.createNewByType(id);
        try {
            var5.read(input, depth, sizeTracker);
            return var5;
        }
        catch (IOException var7) {
            final CrashReport var6 = CrashReport.makeCrashReport(var7, "Loading NBT data");
            final CrashReportCategory var8 = var6.makeCategory("NBT Tag");
            var8.addCrashSection("Tag name", key);
            var8.addCrashSection("Tag type", id);
            throw new ReportedException(var6);
        }
    }
    
    public void merge(final NBTTagCompound other) {
        for (final String var3 : other.tagMap.keySet()) {
            final NBTBase var4 = other.tagMap.get(var3);
            if (var4.getId() == 10) {
                if (this.hasKey(var3, 10)) {
                    final NBTTagCompound var5 = this.getCompoundTag(var3);
                    var5.merge((NBTTagCompound)var4);
                }
                else {
                    this.setTag(var3, var4.copy());
                }
            }
            else {
                this.setTag(var3, var4.copy());
            }
        }
    }
    
    static {
        logger = LogManager.getLogger();
    }
}
