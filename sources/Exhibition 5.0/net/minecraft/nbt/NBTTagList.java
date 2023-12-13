// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.nbt;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import java.io.DataInput;
import java.io.IOException;
import java.io.DataOutput;
import com.google.common.collect.Lists;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class NBTTagList extends NBTBase
{
    private static final Logger LOGGER;
    private List tagList;
    private byte tagType;
    private static final String __OBFID = "CL_00001224";
    
    public NBTTagList() {
        this.tagList = Lists.newArrayList();
        this.tagType = 0;
    }
    
    @Override
    void write(final DataOutput output) throws IOException {
        if (!this.tagList.isEmpty()) {
            this.tagType = this.tagList.get(0).getId();
        }
        else {
            this.tagType = 0;
        }
        output.writeByte(this.tagType);
        output.writeInt(this.tagList.size());
        for (int var2 = 0; var2 < this.tagList.size(); ++var2) {
            this.tagList.get(var2).write(output);
        }
    }
    
    @Override
    void read(final DataInput input, final int depth, final NBTSizeTracker sizeTracker) throws IOException {
        if (depth > 512) {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        }
        sizeTracker.read(8L);
        this.tagType = input.readByte();
        final int var4 = input.readInt();
        this.tagList = Lists.newArrayList();
        for (int var5 = 0; var5 < var4; ++var5) {
            final NBTBase var6 = NBTBase.createNewByType(this.tagType);
            var6.read(input, depth + 1, sizeTracker);
            this.tagList.add(var6);
        }
    }
    
    @Override
    public byte getId() {
        return 9;
    }
    
    @Override
    public String toString() {
        String var1 = "[";
        int var2 = 0;
        for (final NBTBase var4 : this.tagList) {
            var1 = var1 + "" + var2 + ':' + var4 + ',';
            ++var2;
        }
        return var1 + "]";
    }
    
    public void appendTag(final NBTBase nbt) {
        if (this.tagType == 0) {
            this.tagType = nbt.getId();
        }
        else if (this.tagType != nbt.getId()) {
            NBTTagList.LOGGER.warn("Adding mismatching tag types to tag list");
            return;
        }
        this.tagList.add(nbt);
    }
    
    public void set(final int idx, final NBTBase nbt) {
        if (idx >= 0 && idx < this.tagList.size()) {
            if (this.tagType == 0) {
                this.tagType = nbt.getId();
            }
            else if (this.tagType != nbt.getId()) {
                NBTTagList.LOGGER.warn("Adding mismatching tag types to tag list");
                return;
            }
            this.tagList.set(idx, nbt);
        }
        else {
            NBTTagList.LOGGER.warn("index out of bounds to set tag in tag list");
        }
    }
    
    public NBTBase removeTag(final int i) {
        return this.tagList.remove(i);
    }
    
    @Override
    public boolean hasNoTags() {
        return this.tagList.isEmpty();
    }
    
    public NBTTagCompound getCompoundTagAt(final int i) {
        if (i >= 0 && i < this.tagList.size()) {
            final NBTBase var2 = this.tagList.get(i);
            return (NBTTagCompound)((var2.getId() == 10) ? var2 : new NBTTagCompound());
        }
        return new NBTTagCompound();
    }
    
    public int[] getIntArray(final int i) {
        if (i >= 0 && i < this.tagList.size()) {
            final NBTBase var2 = this.tagList.get(i);
            return (var2.getId() == 11) ? ((NBTTagIntArray)var2).getIntArray() : new int[0];
        }
        return new int[0];
    }
    
    public double getDouble(final int i) {
        if (i >= 0 && i < this.tagList.size()) {
            final NBTBase var2 = this.tagList.get(i);
            return (var2.getId() == 6) ? ((NBTTagDouble)var2).getDouble() : 0.0;
        }
        return 0.0;
    }
    
    public float getFloat(final int i) {
        if (i >= 0 && i < this.tagList.size()) {
            final NBTBase var2 = this.tagList.get(i);
            return (var2.getId() == 5) ? ((NBTTagFloat)var2).getFloat() : 0.0f;
        }
        return 0.0f;
    }
    
    public String getStringTagAt(final int i) {
        if (i >= 0 && i < this.tagList.size()) {
            final NBTBase var2 = this.tagList.get(i);
            return (var2.getId() == 8) ? var2.getString() : var2.toString();
        }
        return "";
    }
    
    public NBTBase get(final int idx) {
        return (idx >= 0 && idx < this.tagList.size()) ? this.tagList.get(idx) : new NBTTagEnd();
    }
    
    public int tagCount() {
        return this.tagList.size();
    }
    
    @Override
    public NBTBase copy() {
        final NBTTagList var1 = new NBTTagList();
        var1.tagType = this.tagType;
        for (final NBTBase var3 : this.tagList) {
            final NBTBase var4 = var3.copy();
            var1.tagList.add(var4);
        }
        return var1;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (super.equals(p_equals_1_)) {
            final NBTTagList var2 = (NBTTagList)p_equals_1_;
            if (this.tagType == var2.tagType) {
                return this.tagList.equals(var2.tagList);
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return super.hashCode() ^ this.tagList.hashCode();
    }
    
    public int getTagType() {
        return this.tagType;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
