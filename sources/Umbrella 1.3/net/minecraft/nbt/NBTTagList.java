/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.nbt;

import com.google.common.collect.Lists;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagEnd;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagIntArray;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NBTTagList
extends NBTBase {
    private static final Logger LOGGER = LogManager.getLogger();
    private List tagList = Lists.newArrayList();
    private byte tagType = 0;
    private static final String __OBFID = "CL_00001224";

    @Override
    void write(DataOutput output) throws IOException {
        this.tagType = !this.tagList.isEmpty() ? ((NBTBase)this.tagList.get(0)).getId() : (byte)0;
        output.writeByte(this.tagType);
        output.writeInt(this.tagList.size());
        for (int var2 = 0; var2 < this.tagList.size(); ++var2) {
            ((NBTBase)this.tagList.get(var2)).write(output);
        }
    }

    @Override
    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        if (depth > 512) {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        }
        sizeTracker.read(8L);
        this.tagType = input.readByte();
        int var4 = input.readInt();
        this.tagList = Lists.newArrayList();
        for (int var5 = 0; var5 < var4; ++var5) {
            NBTBase var6 = NBTBase.createNewByType(this.tagType);
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
        for (NBTBase var4 : this.tagList) {
            var1 = String.valueOf(var1) + var2 + ':' + var4 + ',';
            ++var2;
        }
        return String.valueOf(var1) + "]";
    }

    public void appendTag(NBTBase nbt) {
        if (this.tagType == 0) {
            this.tagType = nbt.getId();
        } else if (this.tagType != nbt.getId()) {
            LOGGER.warn("Adding mismatching tag types to tag list");
            return;
        }
        this.tagList.add(nbt);
    }

    public void set(int idx, NBTBase nbt) {
        if (idx >= 0 && idx < this.tagList.size()) {
            if (this.tagType == 0) {
                this.tagType = nbt.getId();
            } else if (this.tagType != nbt.getId()) {
                LOGGER.warn("Adding mismatching tag types to tag list");
                return;
            }
            this.tagList.set(idx, nbt);
        } else {
            LOGGER.warn("index out of bounds to set tag in tag list");
        }
    }

    public NBTBase removeTag(int i) {
        return (NBTBase)this.tagList.remove(i);
    }

    @Override
    public boolean hasNoTags() {
        return this.tagList.isEmpty();
    }

    public NBTTagCompound getCompoundTagAt(int i) {
        if (i >= 0 && i < this.tagList.size()) {
            NBTBase var2 = (NBTBase)this.tagList.get(i);
            return var2.getId() == 10 ? (NBTTagCompound)var2 : new NBTTagCompound();
        }
        return new NBTTagCompound();
    }

    public int[] getIntArray(int i) {
        if (i >= 0 && i < this.tagList.size()) {
            NBTBase var2 = (NBTBase)this.tagList.get(i);
            return var2.getId() == 11 ? ((NBTTagIntArray)var2).getIntArray() : new int[0];
        }
        return new int[0];
    }

    public double getDouble(int i) {
        if (i >= 0 && i < this.tagList.size()) {
            NBTBase var2 = (NBTBase)this.tagList.get(i);
            return var2.getId() == 6 ? ((NBTTagDouble)var2).getDouble() : 0.0;
        }
        return 0.0;
    }

    public float getFloat(int i) {
        if (i >= 0 && i < this.tagList.size()) {
            NBTBase var2 = (NBTBase)this.tagList.get(i);
            return var2.getId() == 5 ? ((NBTTagFloat)var2).getFloat() : 0.0f;
        }
        return 0.0f;
    }

    public String getStringTagAt(int i) {
        if (i >= 0 && i < this.tagList.size()) {
            NBTBase var2 = (NBTBase)this.tagList.get(i);
            return var2.getId() == 8 ? var2.getString() : var2.toString();
        }
        return "";
    }

    public NBTBase get(int idx) {
        return idx >= 0 && idx < this.tagList.size() ? (NBTBase)this.tagList.get(idx) : new NBTTagEnd();
    }

    public int tagCount() {
        return this.tagList.size();
    }

    @Override
    public NBTBase copy() {
        NBTTagList var1 = new NBTTagList();
        var1.tagType = this.tagType;
        for (NBTBase var3 : this.tagList) {
            NBTBase var4 = var3.copy();
            var1.tagList.add(var4);
        }
        return var1;
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        if (super.equals(p_equals_1_)) {
            NBTTagList var2 = (NBTTagList)p_equals_1_;
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
}

