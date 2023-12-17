// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.nbt;

import org.apache.logging.log4j.LogManager;
import java.util.Objects;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.crash.CrashReport;
import java.util.List;
import java.util.Collection;
import java.util.Collections;
import com.google.common.collect.Lists;
import net.minecraft.util.ReportedException;
import javax.annotation.Nullable;
import java.util.UUID;
import java.util.Set;
import java.io.DataInput;
import java.io.IOException;
import java.util.Iterator;
import java.io.DataOutput;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Logger;

public class NBTTagCompound extends NBTBase
{
    private static final Logger LOGGER;
    private static final Pattern SIMPLE_VALUE;
    private final Map<String, NBTBase> tagMap;
    
    public NBTTagCompound() {
        this.tagMap = (Map<String, NBTBase>)Maps.newHashMap();
    }
    
    @Override
    void write(final DataOutput output) throws IOException {
        for (final String s : this.tagMap.keySet()) {
            final NBTBase nbtbase = this.tagMap.get(s);
            writeEntry(s, nbtbase, output);
        }
        output.writeByte(0);
    }
    
    @Override
    void read(final DataInput input, final int depth, final NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(384L);
        if (depth > 512) {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        }
        this.tagMap.clear();
        byte b0;
        while ((b0 = readType(input, sizeTracker)) != 0) {
            final String s = readKey(input, sizeTracker);
            sizeTracker.read(224 + 16 * s.length());
            final NBTBase nbtbase = readNBT(b0, s, input, depth + 1, sizeTracker);
            if (this.tagMap.put(s, nbtbase) != null) {
                sizeTracker.read(288L);
            }
        }
    }
    
    public Set<String> getKeySet() {
        return this.tagMap.keySet();
    }
    
    @Override
    public byte getId() {
        return 10;
    }
    
    public int getSize() {
        return this.tagMap.size();
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
    
    public void setUniqueId(final String key, final UUID value) {
        this.setLong(key + "Most", value.getMostSignificantBits());
        this.setLong(key + "Least", value.getLeastSignificantBits());
    }
    
    @Nullable
    public UUID getUniqueId(final String key) {
        return new UUID(this.getLong(key + "Most"), this.getLong(key + "Least"));
    }
    
    public boolean hasUniqueId(final String key) {
        return this.hasKey(key + "Most", 99) && this.hasKey(key + "Least", 99);
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
    
    public byte getTagId(final String key) {
        final NBTBase nbtbase = this.tagMap.get(key);
        return (byte)((nbtbase == null) ? 0 : nbtbase.getId());
    }
    
    public boolean hasKey(final String key) {
        return this.tagMap.containsKey(key);
    }
    
    public boolean hasKey(final String key, final int type) {
        final int i = this.getTagId(key);
        return i == type || (type == 99 && (i == 1 || i == 2 || i == 3 || i == 4 || i == 5 || i == 6));
    }
    
    public byte getByte(final String key) {
        try {
            if (this.hasKey(key, 99)) {
                return this.tagMap.get(key).getByte();
            }
        }
        catch (ClassCastException ex) {}
        return 0;
    }
    
    public short getShort(final String key) {
        try {
            if (this.hasKey(key, 99)) {
                return this.tagMap.get(key).getShort();
            }
        }
        catch (ClassCastException ex) {}
        return 0;
    }
    
    public int getInteger(final String key) {
        try {
            if (this.hasKey(key, 99)) {
                return this.tagMap.get(key).getInt();
            }
        }
        catch (ClassCastException ex) {}
        return 0;
    }
    
    public long getLong(final String key) {
        try {
            if (this.hasKey(key, 99)) {
                return this.tagMap.get(key).getLong();
            }
        }
        catch (ClassCastException ex) {}
        return 0L;
    }
    
    public float getFloat(final String key) {
        try {
            if (this.hasKey(key, 99)) {
                return this.tagMap.get(key).getFloat();
            }
        }
        catch (ClassCastException ex) {}
        return 0.0f;
    }
    
    public double getDouble(final String key) {
        try {
            if (this.hasKey(key, 99)) {
                return this.tagMap.get(key).getDouble();
            }
        }
        catch (ClassCastException ex) {}
        return 0.0;
    }
    
    public String getString(final String key) {
        try {
            if (this.hasKey(key, 8)) {
                return this.tagMap.get(key).getString();
            }
        }
        catch (ClassCastException ex) {}
        return "";
    }
    
    public byte[] getByteArray(final String key) {
        try {
            if (this.hasKey(key, 7)) {
                return this.tagMap.get(key).getByteArray();
            }
        }
        catch (ClassCastException classcastexception) {
            throw new ReportedException(this.createCrashReport(key, 7, classcastexception));
        }
        return new byte[0];
    }
    
    public int[] getIntArray(final String key) {
        try {
            if (this.hasKey(key, 11)) {
                return this.tagMap.get(key).getIntArray();
            }
        }
        catch (ClassCastException classcastexception) {
            throw new ReportedException(this.createCrashReport(key, 11, classcastexception));
        }
        return new int[0];
    }
    
    public NBTTagCompound getCompoundTag(final String key) {
        try {
            if (this.hasKey(key, 10)) {
                return this.tagMap.get(key);
            }
        }
        catch (ClassCastException classcastexception) {
            throw new ReportedException(this.createCrashReport(key, 10, classcastexception));
        }
        return new NBTTagCompound();
    }
    
    public NBTTagList getTagList(final String key, final int type) {
        try {
            if (this.getTagId(key) == 9) {
                final NBTTagList nbttaglist = this.tagMap.get(key);
                if (!nbttaglist.isEmpty() && nbttaglist.getTagType() != type) {
                    return new NBTTagList();
                }
                return nbttaglist;
            }
        }
        catch (ClassCastException classcastexception) {
            throw new ReportedException(this.createCrashReport(key, 9, classcastexception));
        }
        return new NBTTagList();
    }
    
    public boolean getBoolean(final String key) {
        return this.getByte(key) != 0;
    }
    
    public void removeTag(final String key) {
        this.tagMap.remove(key);
    }
    
    @Override
    public String toString() {
        final StringBuilder stringbuilder = new StringBuilder("{");
        Collection<String> collection = this.tagMap.keySet();
        if (NBTTagCompound.LOGGER.isDebugEnabled()) {
            final List<String> list = (List<String>)Lists.newArrayList((Iterable)this.tagMap.keySet());
            Collections.sort(list);
            collection = list;
        }
        for (final String s : collection) {
            if (stringbuilder.length() != 1) {
                stringbuilder.append(',');
            }
            stringbuilder.append(handleEscape(s)).append(':').append(this.tagMap.get(s));
        }
        return stringbuilder.append('}').toString();
    }
    
    @Override
    public boolean isEmpty() {
        return this.tagMap.isEmpty();
    }
    
    private CrashReport createCrashReport(final String key, final int expectedType, final ClassCastException ex) {
        final CrashReport crashreport = CrashReport.makeCrashReport(ex, "Reading NBT data");
        final CrashReportCategory crashreportcategory = crashreport.makeCategoryDepth("Corrupt NBT tag", 1);
        crashreportcategory.addDetail("Tag type found", new ICrashReportDetail<String>() {
            @Override
            public String call() throws Exception {
                return NBTBase.NBT_TYPES[NBTTagCompound.this.tagMap.get(key).getId()];
            }
        });
        crashreportcategory.addDetail("Tag type expected", new ICrashReportDetail<String>() {
            @Override
            public String call() throws Exception {
                return NBTBase.NBT_TYPES[expectedType];
            }
        });
        crashreportcategory.addCrashSection("Tag name", key);
        return crashreport;
    }
    
    @Override
    public NBTTagCompound copy() {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        for (final String s : this.tagMap.keySet()) {
            nbttagcompound.setTag(s, this.tagMap.get(s).copy());
        }
        return nbttagcompound;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        return super.equals(p_equals_1_) && Objects.equals(this.tagMap.entrySet(), ((NBTTagCompound)p_equals_1_).tagMap.entrySet());
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
    
    static NBTBase readNBT(final byte id, final String key, final DataInput input, final int depth, final NBTSizeTracker sizeTracker) throws IOException {
        final NBTBase nbtbase = NBTBase.create(id);
        try {
            nbtbase.read(input, depth, sizeTracker);
            return nbtbase;
        }
        catch (IOException ioexception) {
            final CrashReport crashreport = CrashReport.makeCrashReport(ioexception, "Loading NBT data");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("NBT Tag");
            crashreportcategory.addCrashSection("Tag name", key);
            crashreportcategory.addCrashSection("Tag type", id);
            throw new ReportedException(crashreport);
        }
    }
    
    public void merge(final NBTTagCompound other) {
        for (final String s : other.tagMap.keySet()) {
            final NBTBase nbtbase = other.tagMap.get(s);
            if (nbtbase.getId() == 10) {
                if (this.hasKey(s, 10)) {
                    final NBTTagCompound nbttagcompound = this.getCompoundTag(s);
                    nbttagcompound.merge((NBTTagCompound)nbtbase);
                }
                else {
                    this.setTag(s, nbtbase.copy());
                }
            }
            else {
                this.setTag(s, nbtbase.copy());
            }
        }
    }
    
    protected static String handleEscape(final String p_193582_0_) {
        return NBTTagCompound.SIMPLE_VALUE.matcher(p_193582_0_).matches() ? p_193582_0_ : NBTTagString.quoteAndEscape(p_193582_0_);
    }
    
    static {
        LOGGER = LogManager.getLogger();
        SIMPLE_VALUE = Pattern.compile("[A-Za-z0-9._+-]+");
    }
}
